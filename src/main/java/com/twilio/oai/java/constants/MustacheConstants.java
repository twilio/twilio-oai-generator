package com.twilio.oai.java.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;


// This class contains only those constants which are used in mustache templates.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MustacheConstants {
    // It is used to define the content type of the request body in mustache templates which is defined in twilio-java.
    // If content type constant changes in twilio-java then it's value should be updated as well.
    public static final String X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT = "x-request-content-type";
    public static final String X_REQUEST_HTTP_METHOD = "x-http-method";
    public static final String X_IS_LIST_OP = "x-is-list-op";
    public static final String ACTION_TYPE = "x-common-action-type";
    public static final String ACTION_METHOD = "x-common-action-method";
    
    public static final Map<String, String> serializaationMapping = Map.of(
            "application/x-www-form-urlencoded", "if ($paramName != null) { request.addPostParam($stringCapParamName, $paramName.toString())}", 
            "application/json", "Json",
            "multipart/form-data", "MultipartFormData"
    );


    // Used with "x-common-action-type"
    @Getter
    @RequiredArgsConstructor
    public enum ActionType {
        CREATOR("Creator"),
        READER("Reader"),
        UPDATER("Updater"),
        DELETER("Deleter"),
        FETCHER("Fetcher");

        private final String value;
    }

    // Used with "x-common-action-method"
    @Getter
    @RequiredArgsConstructor
    public enum ActionMethod {
        CREATE("create"),
        READ("read"),
        UPDATE("update"),
        DELETE("delete"),
        FETCH("fetch");

        private final String value;
    }
    
}
