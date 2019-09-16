package com.example.honeysucklelib.HoneysuckleLib;


import androidx.annotation.NonNull;

public class HSUtils {
    static String generatePurposesString(@NonNull String [] purposes, String separator) {
        if (purposes == null || purposes.length == 0) {
            return "Not specified by the developer";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0 ; i < purposes.length ; i++) {
            String purpose = purposes[i];
            if (purpose == null || purpose.length() == 0) {
                stringBuilder.append("- ").append("Not specified by the developer");
            } else {
                stringBuilder.append("- ").append(purpose);
            }
            if (i < purposes.length - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    public static String generateEgressInfoString(@NonNull String [] items) {
        if (items.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(items[0]);
        for (int i = 1 ; i < items.length ; ++i) {
            stringBuilder.append(", ").append(items[0]);
        }
        return stringBuilder.toString();
    }
}
