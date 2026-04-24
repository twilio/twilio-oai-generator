package com.twilio.oai.resolver.python;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Resolves the correct ordering of Python classes to avoid forward reference issues.
 *
 * In Python, classes must be defined before they are referenced. This resolver analyzes
 * class dependencies and reorders them using topological sort to ensure that:
 * 1. Parent/container classes are defined before child classes that reference them
 * 2. Classes with nested types are defined before standalone classes that use those nested types
 *
 * Example:
 *   ConversationSummaryList (contains nested class SummaryCore)
 *   must come before
 *   CreateSummariesRequest (references ConversationSummaryList.SummaryCore)
 */
public class PythonClassOrderResolver {

    // Pattern to match class references in type hints, e.g., "ConversationSummaryList.SummaryCore"
    private static final Pattern CLASS_REFERENCE_PATTERN = Pattern.compile("([A-Z][a-zA-Z0-9_]*)(?:\\.([A-Z][a-zA-Z0-9_]*))?");

    /**
     * Reorders a set of models based on their dependencies using topological sort.
     * Only applies to v1.0 APIs to avoid breaking existing implementations.
     *
     * @param models Set of CodegenModel objects to reorder
     * @param isApiV1 Whether this is a v1.0 API
     * @return List of models in dependency order (dependencies first)
     */
    public static List<CodegenModel> reorderModels(Set<CodegenModel> models, boolean isApiV1) {
        if (!isApiV1 || models == null || models.isEmpty()) {
            // For non-v1 APIs or empty sets, return original order
            return new ArrayList<>(models);
        }

        // Build dependency graph
        Map<String, Set<String>> dependencies = buildDependencyGraph(models);

        // Perform topological sort
        List<String> sortedClassNames = topologicalSort(dependencies);

        // Create a map for quick lookup
        Map<String, CodegenModel> modelMap = models.stream()
            .collect(Collectors.toMap(CodegenModel::getClassname, m -> m));

        // Return models in sorted order
        List<CodegenModel> result = new ArrayList<>();
        for (String className : sortedClassNames) {
            if (modelMap.containsKey(className)) {
                result.add(modelMap.get(className));
            }
        }

        // Add any models not in the sorted list (shouldn't happen, but safety check)
        for (CodegenModel model : models) {
            if (!result.contains(model)) {
                result.add(model);
            }
        }

        return result;
    }

    /**
     * Builds a dependency graph where each class maps to the set of classes it depends on.
     *
     * @param models Set of models to analyze
     * @return Map of class name -> set of class names it depends on
     */
    private static Map<String, Set<String>> buildDependencyGraph(Set<CodegenModel> models) {
        Map<String, Set<String>> graph = new HashMap<>();

        for (CodegenModel model : models) {
            String className = model.getClassname();
            Set<String> deps = new HashSet<>();

            // Analyze dependencies in all properties/variables
            if (model.getVars() != null) {
                for (CodegenProperty prop : model.getVars()) {
                    deps.addAll(extractClassDependencies(prop.dataType));
                    if (prop.datatypeWithEnum != null) {
                        deps.addAll(extractClassDependencies(prop.datatypeWithEnum));
                    }
                    if (prop.complexType != null) {
                        deps.addAll(extractClassDependencies(prop.complexType));
                    }
                }
            }

            // Remove self-references
            deps.remove(className);

            // Only keep dependencies that are in our model set
            Set<String> modelNames = models.stream()
                .map(CodegenModel::getClassname)
                .collect(Collectors.toSet());
            deps.retainAll(modelNames);

            graph.put(className, deps);
        }

        return graph;
    }

    /**
     * Extracts class dependencies from a type string.
     * Handles various Python type hint formats like:
     * - "ConversationSummaryList"
     * - "List[ConversationSummaryList.SummaryCore]"
     * - "Optional[ConversationSummaryList]"
     * - "Dict[str, ConversationSummaryList.SummaryCore]"
     *
     * @param dataType The type string to analyze
     * @return Set of class names referenced in the type
     */
    private static Set<String> extractClassDependencies(String dataType) {
        Set<String> deps = new HashSet<>();

        if (dataType == null || dataType.isEmpty()) {
            return deps;
        }

        // Match class references in the type string
        Matcher matcher = CLASS_REFERENCE_PATTERN.matcher(dataType);
        while (matcher.find()) {
            String className = matcher.group(1);

            // Filter out built-in types and typing module types
            if (!isBuiltInType(className)) {
                // If this is a nested class reference (e.g., ConversationSummaryList.SummaryCore)
                // we depend on the parent class (ConversationSummaryList)
                deps.add(className);
            }
        }

        return deps;
    }

    /**
     * Checks if a type name is a built-in Python type or typing module type.
     */
    private static boolean isBuiltInType(String typeName) {
        Set<String> builtInTypes = Set.of(
            "List", "Dict", "Set", "Tuple", "Optional", "Union", "Any",
            "str", "int", "float", "bool", "bytes", "datetime", "date",
            "Decimal", "Iterator", "AsyncIterator", "Protocol"
        );
        return builtInTypes.contains(typeName);
    }

    /**
     * Performs topological sort on the dependency graph.
     * Uses Kahn's algorithm to handle cycles gracefully.
     *
     * @param graph Dependency graph (class -> set of dependencies)
     * @return List of class names in topological order (dependencies first)
     */
    private static List<String> topologicalSort(Map<String, Set<String>> graph) {
        List<String> result = new ArrayList<>();

        // Calculate in-degree for each node
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : graph.keySet()) {
            inDegree.putIfAbsent(node, 0);
        }
        for (Set<String> deps : graph.values()) {
            for (String dep : deps) {
                inDegree.put(dep, inDegree.getOrDefault(dep, 0) + 1);
            }
        }

        // Queue of nodes with no dependencies
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Process nodes
        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            // Reduce in-degree for dependent nodes
            Set<String> dependents = graph.getOrDefault(node, new HashSet<>());
            for (String dependent : dependents) {
                int newDegree = inDegree.get(dependent) - 1;
                inDegree.put(dependent, newDegree);
                if (newDegree == 0) {
                    queue.add(dependent);
                }
            }
        }

        // If we haven't processed all nodes, there's a cycle
        // Add remaining nodes in arbitrary order
        for (String node : graph.keySet()) {
            if (!result.contains(node)) {
                result.add(node);
            }
        }

        // Reverse the result because we want dependencies first
        Collections.reverse(result);

        return result;
    }
}
