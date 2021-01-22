package com.twilio.oai;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.GoClientCodegen;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import static org.openapitools.codegen.utils.StringUtils.camelize;
import io.swagger.v3.oas.models.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

	public AbstractTwilioGoGenerator() {
		super();

		embeddedTemplateDir = templateDir = getName();
	}

	@Override
	public void processOpts() {
		super.processOpts();

		additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);

		supportingFiles.clear();
	}

	@Override
	public Map<String, String> createMapping(final String key, final String value) {
		// Optional dependency not needed.
		if (value.equals("github.com/antihax/optional")) {
			return new HashMap<>();
		}

		return super.createMapping(key, value);
	}

	@Override
	public void updateCodegenPropertyEnum(CodegenProperty var) {
		// make sure the inline enums have plain defaults (e.g. string, int, float)
		String enumDefault = null;
		if (var.isEnum && var.defaultValue != null) {
			enumDefault = var.defaultValue;
		}
		super.updateCodegenPropertyEnum(var);
		if (var.isEnum && enumDefault != null) {
			var.defaultValue = enumDefault;
		}
	}

	@Override
	public String toVarName(String name) {
//        System.out.println("before sanitization="+name);
		name = name.replaceAll("[-+.^:,]","");
		name = name.replace("<","lesserThan");
		name = name.replace(">","greaterThan");
		name = super.toVarName(name);
		return name;
	}


	@Override
	public CodegenProperty fromProperty(String name, Schema p) {
		name = toVarName(name);
		CodegenProperty prop = super.fromProperty(name, p);
		prop.baseName = toVarName(prop.baseName);
		return prop;
	}

	@Override
	public String toParamName(String name) {
		name = toVarName(name);
		String name_sanitized = super.toParamName(name);
		return name;
	}

	@Override
	public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {
		objs = super.postProcessOperationsWithModels(objs, allModels);
		Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
		List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
		for (CodegenOperation op : operationList) {
			for (CodegenParameter p : op.allParams) {
				p.baseName = toVarName(p.baseName);
				p.paramName = toVarName(p.paramName);
			}
		}

		return objs;
	}

}
