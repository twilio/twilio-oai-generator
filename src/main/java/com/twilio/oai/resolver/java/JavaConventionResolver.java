package com.twilio.oai.resolver.java;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.StringHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.LIST_END;
import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.REF_ENUM_EXTENSION_NAME;

public class JavaConventionResolver {
    private static final String VALUES = "values";

    private ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

}
