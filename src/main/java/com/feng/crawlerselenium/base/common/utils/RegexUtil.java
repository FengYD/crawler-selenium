package com.feng.crawlerselenium.base.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengyadong
 * @date 2023/6/19 13:56
 * @Description
 */
public class RegexUtil {

    public static String extract(String source, Pattern pattern) {
        Matcher m = pattern.matcher(source);
        String str = "";
        if (m.find()) {
            str = m.group(1);
        }
        return str;
    }

}
