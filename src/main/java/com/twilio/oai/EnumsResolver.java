package com.twilio.oai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class EnumsResolver {

    private final Map<String, Map<String, Object>> conventionMap;
    private HashSet<String> enumsDict = new HashSet<>();

    public HashSet<String> getEnumsDict() {
        return enumsDict;
    }

    public EnumsResolver(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
        extractEnums();

    }

    //Populate hash set enumsDict with all enums which are referring to Twilio.Types
    private void extractEnums() {
        Map<String, Object> usingMappings = conventionMap.get("library");
        usingMappings.forEach((key, value) -> {
            if(value != null){
                if(value instanceof String && ((String) value).equalsIgnoreCase("Twilio.Types")){
                    enumsDict.add(StringHelper.titleCase(key));
                } else if (value instanceof ArrayList) {
                    ((ArrayList<String>) value).forEach(item -> {
                        if(item.equalsIgnoreCase("Twilio.Types")){
                            enumsDict.add(StringHelper.titleCase(key));
                        }
                    });
                }
            }
        });
    }


}