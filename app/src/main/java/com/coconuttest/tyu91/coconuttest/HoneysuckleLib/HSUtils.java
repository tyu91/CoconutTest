package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

public class HSUtils {
    static String generatePurposesString(String [] purposes) {
        if (purposes == null || purposes.length == 0) {
            return "Not specified by the developer";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String purpose : purposes) {
            stringBuilder.append("- ").append(purpose).append("<br>");
        }
        return stringBuilder.toString();
    }

    public static String generateEgressInfoString(String [] destinations) {
        if (destinations == null || destinations.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(destinations[0]);
        for (int i = 1 ; i < destinations.length ; ++i) {
            stringBuilder.append(", ").append(destinations[0]);
        }
        return stringBuilder.toString();
    }
}
