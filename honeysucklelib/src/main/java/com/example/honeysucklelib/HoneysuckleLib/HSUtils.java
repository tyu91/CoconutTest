package com.example.honeysucklelib.HoneysuckleLib;


import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.example.honeysucklelib.R;

public class HSUtils {
    static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;
    static final long ONE_HOUR_TIME = 60 * 60 * 1000;
    static final long ONE_MINUTE_TIME = 60 * 1000;

    static final String jitNotificationConfigKeyPattern = "%s_alert_settings";
    static final String accessTrackerConfigKeyPattern = "%s_access_tracker_settings";


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

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    static String getDataString(PersonalDataGroup dataGroup, boolean lowerCase) {
        switch (dataGroup) {
            case UniqueId:
                return lowerCase ? "unique ID" : "Unique ID";
            case UserFile:
                return lowerCase ? "user files" : "User Files";
            case UserAccount:
                return lowerCase ? "user account" : "User Account";
            case Microphone:
                return lowerCase ? "microphone" : "Microphone";
            case Calendar:
                return lowerCase ? "calendar" : "Calendar";
            case Camera:
                return lowerCase ? "camera" : "Camera";
            case Sms:
                return lowerCase ? "SMS" : "SMS";
            case CallLogs:
                return lowerCase ? "call logs" : "Call Logs";
            case Contacts:
                return lowerCase ? "contacts" : "Contacts";
            case Location:
                return lowerCase ? "location" : "Location";
            case Clipboard:
                return lowerCase ? "clipboard" : "Clipboard";
            case UserInput:
                return lowerCase ? "text input" : "Text Input";
            case BodySensor:
                return lowerCase ? "body sensor" : "Body Sensor";
            case RunningApps:
                return lowerCase ? "running apps" : "Running Apps";
            case MotionSensor:
                return lowerCase ? "motion sensor" : "Motion Sensor";
            case Notification:
                return lowerCase ? "notifications" : "Notifications";
            case Accessibility:
                return lowerCase ? "accessibility events" : "Accessibility Events";
            case SystemLogs:
                return lowerCase ? "system logs" : "System Logs";
            default:
                return "";
        }
    }

    static String getRelativeOrAbsoluteTimeString(long beginTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        long timeDifference = currentTimestamp - beginTimestamp;
        String time;
        if (timeDifference < ONE_MINUTE_TIME) {
            time = "1 minute ago";
        } else if (timeDifference < ONE_HOUR_TIME) {
            time = new java.text.SimpleDateFormat("m",
                    HSStatus.getApplicationContext().getResources().getConfiguration().locale)
                    .format(new java.util.Date(timeDifference));
            time = String.format("%s minutes ago", time);
        } else if (timeDifference < ONE_DAY_TIME) {
            time = new java.text.SimpleDateFormat("H",
                    HSStatus.getApplicationContext().getResources().getConfiguration().locale)
                    .format(new java.util.Date(timeDifference));
            if (timeDifference < 2 * ONE_HOUR_TIME) {
                time = "1 hour ago";
            } else {
                time = String.format("%s hours ago", time);
            }
        } else {
            time = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm",
                    HSStatus.getApplicationContext().getResources().getConfiguration().locale)
                    .format(new java.util.Date(beginTimestamp));
            time = String.format("Last accessed at %s", time);
        }
        return time;
    }

    public static int getDataGroupIconId(PersonalDataGroup dataGroup) {
        switch (dataGroup) {
            case UniqueId:
                return R.drawable.baseline_perm_device_information_black_18dp;
            case UserFile:
                return R.drawable.baseline_folder_black_18dp;
            case UserAccount:
                return R.drawable.baseline_perm_identity_black_18dp;
            case Microphone:
                return R.drawable.baseline_mic_black_18dp;
            case Calendar:
                return R.drawable.baseline_today_black_18dp;
            case Camera:
                return R.drawable.baseline_camera_alt_black_18dp;
            case Sms:
                return R.drawable.baseline_textsms_black_18dp;
            case CallLogs:
                return R.drawable.baseline_phone_black_18dp;
            case Contacts:
                return R.drawable.baseline_contacts_black_18dp;
            case Location:
                return R.drawable.baseline_location_on_black_18dp;
            case Clipboard:
                return R.drawable.baseline_content_paste_black_18dp;
            case UserInput:
                return R.drawable.baseline_create_black_18dp;
            case BodySensor:
                return R.drawable.baseline_accessibility_black_18dp;
            case RunningApps:
                return R.drawable.baseline_apps_black_18dp;
            case MotionSensor:
                return R.drawable.baseline_accessibility_black_18dp;
            case Notification:
                return R.drawable.baseline_notifications_black_18dp;
            case Accessibility:
                return R.drawable.baseline_touch_app_black_18dp;
            case SystemLogs:
                return R.drawable.baseline_android_black_18dp;
            default:
                return R.drawable.baseline_info_black_18dp;
        }
    }

    public static String getMethodCallSignatureByStackTraceElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement != null) {
            return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
        } else {
            return "";
        }
    }
}
