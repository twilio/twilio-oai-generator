package com.twilio.oai;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.GoClientCodegen;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import static org.openapitools.codegen.utils.StringUtils.camelize;
import io.swagger.v3.oas.models.media.Schema;

import java.util.*;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

	public AbstractTwilioGoGenerator() {
		super();

		embeddedTemplateDir = templateDir = getName();
	}

	@Override
	public void processOpts() {
		super.processOpts();

		additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);
		additionalProperties.put(CodegenConstants.ENUM_CLASS_PREFIX, true);

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
	public String toParamName(String name) {
		name = name.replaceAll("[-+.^:,]","");
		name = name.replace("<","Before");
		name = name.replace(">","After");
		name = super.toVarName(name);
		return name;
	}

	@Override
	protected List<Map<String, Object>> buildEnumVars(List<Object> values, String dataType) {
		List<Map<String, Object>> siftedEnumVars = new ArrayList<>();
		Set<Object> uniqueNames = new HashSet<>();
		List<Map<String, Object>> rawEnumVars = super.buildEnumVars(values, dataType);

		// Filter out duplicate enum values. Example: HTTPMETHOD_HEAD = "head"/HTTPMETHOD_HEAD = "HEAD"
		for (Map<String, Object> enumVar : rawEnumVars) {
			Object enumName = enumVar.get("name");
			if (!uniqueNames.contains(enumName)) {
				uniqueNames.add(enumName);
				siftedEnumVars.add(enumVar);
			}
		}

		return siftedEnumVars;
	}
}
