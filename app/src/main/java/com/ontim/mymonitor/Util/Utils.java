package com.ontim.mymonitor.Util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static String FILENAME_FORMAT = "yyyyMMdd-HHmmss";
    private static String UNDER_CHAR = "_";

    public static String getFileName(String type) {
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + UNDER_CHAR + type;
        return name;
    }
}
