package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.common.CodegenModelContainerDataTypeResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class PythonCodegenModelContainerDataTypeResolver extends CodegenModelContainerDataTypeResolver {
    private final PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final List<? extends LanguageDataType> languageDataTypes;

    // Maximum container nesting we chain the concrete value type for. Beyond this, the
    // property is collapsed to a plain object instead of generating a deeply chained type
    // (e.g. Dict[str, Dict[str, X]]) and its leaf model.
    private static final int MAX_CONTAINER_NESTING = 1;
    private static final String OBJECT_TYPE = "object";

    public PythonCodegenModelContainerDataTypeResolver(PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver, List<? extends LanguageDataType> languageDataTypes) {
        super(codegenModelDataTypeResolver, languageDataTypes);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.languageDataTypes = languageDataTypes;
    }

    public CodegenProperty resolve(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder, PythonCodegenModelResolver codegenModelResolver) {
        Stack<String> containerTypes = new Stack<>();
        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);
        if (collapseIfDeeplyNested(codegenProperty, containerTypes)) {
            return codegenProperty;
        }
        CodegenModel nestedModel = codegenModelResolver.resolveNestedModel(codegenProperty, apiResourceBuilder);
        if (nestedModel == null) {
            codegenModelDataTypeResolver.resolve(codegenProperty, apiResourceBuilder);
        }
        rewrapContainerType(codegenProperty,containerTypes);

        return codegenProperty;
    }

    public CodegenProperty resolveResponseModel(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        Stack<String> containerTypes = new Stack<>();
        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);
        if (collapseIfDeeplyNested(codegenProperty, containerTypes)) {
            return codegenProperty;
        }
        codegenModelDataTypeResolver.resolveResponseModel(codegenProperty, apiResourceBuilder);
        rewrapContainerType(codegenProperty,containerTypes);
        return codegenProperty;
    }

    /**
     * When a property nests containers more than {@link #MAX_CONTAINER_NESTING} level deep, collapse it to a generic
     * object rather than chaining the value type (e.g. Dict[str, Dict[str, X]]). This also prevents the leaf model
     * from being resolved/emitted through the deeply nested path.
     * <p>
     * The dataType is set to {@code object}, which the Python convention mapper renders as the generator's
     * free-form object representation ({@code Dict[str, object]}) - the same form used for {@code additionalProperties: true}
     * and other free-form object properties. The container stack is intentionally not re-wrapped so no extra container
     * layers are prepended.
     *
     * @param codegenProperty the property being resolved, already unwrapped to its value type
     * @param containerTypes the stack of container prefixes produced by unwrapping
     * @return true if the property was collapsed to an object, false otherwise
     */
    private boolean collapseIfDeeplyNested(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        if (containerTypes.size() <= MAX_CONTAINER_NESTING) {
            return false;
        }
        codegenProperty.dataType = OBJECT_TYPE;
        codegenProperty.baseType = OBJECT_TYPE;
        codegenProperty.datatypeWithEnum = OBJECT_TYPE;
        codegenProperty.complexType = null;
        codegenProperty.isContainer = false;
        codegenProperty.isMap = false;
        codegenProperty.isArray = false;
        codegenProperty.items = null;
        return true;
    }

    /**
     * Unwraps the container type(s) from the underlying property datatype and adds the container type(s) to the given
     * containerTypes stack. Returns the underlying property datatype (i.e. "List<IceServer>" -> "IceServer").
     * @param codegenProperty the property whose dataType is to be unwrapped
     * @param containerTypes the stack which stores the containers used to unwrap
     * @return unwrapped continer type
     */
    @Override
    protected String unwrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        String codegenPropertyDataType = "";
        codegenPropertyDataType = codegenProperty.dataType;

        String currentContainerType = "";
        boolean isContainerType = false;

        while(codegenPropertyDataType != null && !codegenPropertyDataType.isEmpty()) {
            for (LanguageDataType dataType : languageDataTypes) {
                if (codegenPropertyDataType.startsWith(dataType.getValue())) {
                    isContainerType = true;
                    currentContainerType = dataType.getValue();
                }
            }
            if(isContainerType) {
                containerTypes.push(currentContainerType);
                codegenPropertyDataType = codegenPropertyDataType.replaceFirst(Pattern.quote(currentContainerType), "");
                codegenPropertyDataType = codegenPropertyDataType.substring(0, codegenPropertyDataType.length()-1);
                isContainerType = false;
            }
            else
                return codegenPropertyDataType;
        }
        return codegenPropertyDataType;
    }

    /**
     * Re-wraps the property dataType with the container types in the given stack. Sets the property dataType to the
     * rewrapped value (i.e. "IceServer" -> "List[IceServer]").
     * @param codegenProperty the property whose dataType is to be rewrapped
     * @param containerTypes the stack which stores the containers used to re-wrap
     */
    @Override
    public void rewrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = currentContainerType + codegenProperty.dataType + ApplicationConstants.PYTHON_LIST_END;
        }
    }
}
