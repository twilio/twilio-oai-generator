package com.twilio.oai.common;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class Utility {

    public String toFirstLetterCaps(String input) {
        return StringUtils.isBlank(input) ? input : input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
