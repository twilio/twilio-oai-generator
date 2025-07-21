package com.twilio.oai.java;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;


// This class contains only those constants which are used in mustache templates.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MustacheConstants {
    // It is used to define the content type of the request body in mustache templates which is defined in twilio-java.
    // If content type constant changes in twilio-java then it's value should be updated as well.
    public static final String X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT = "x-content-type";
    public static final String X_REQUEST_HTTP_METHOD = "x-http-method";
    
    public static final Map<String, String> serializaationMapping = Map.of(
            "application/x-www-form-urlencoded", "if ($paramName != null) { request.addPostParam($stringCapParamName, $paramName.toString())}", 
            "application/json", "Json",
            "multipart/form-data", "MultipartFormData"
    );
    
}
