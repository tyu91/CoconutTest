package com.example.honeysucklelib.HoneysuckleLib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.RequiresApi;

import com.example.honeysucklelib.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.ONE_TIME;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.RECURRING;

public class NotificationUtils {
    @RequiresApi(api = Build.VERSION_CODES.N)
    static public void pushPrivacyNotification(Context context, String ID) {
        if (context == null) {
            return;
        }

        AnnotationInfo aggregatedAnnotationInfo = HSStatus.getMyAnnotationInfoMap().getAnnotationInfoByID(ID);
        if (aggregatedAnnotationInfo == null) {
            return;
        }
        JitNoticeFrequency jitNoticeFrequency = aggregatedAnnotationInfo.jitNoticeFrequency;
        if (jitNoticeFrequency == JitNoticeFrequency.DO_NOT_SEND_NOTIFICATION) {
            return;
        }

        String dataGroup = HSUtils.getDataString(aggregatedAnnotationInfo.dataGroup, false);
        String purposesString = HSUtils.generatePurposesString(aggregatedAnnotationInfo.purposes, "<br>");
        String destinationsString = HSUtils.generateEgressInfoString(aggregatedAnnotationInfo.destinations);
        String dataTypesString = HSUtils.generateEgressInfoString(aggregatedAnnotationInfo.leakedDataTypes);
        AccessType accessType = aggregatedAnnotationInfo.accessType;

//        purposesString = "- backup data backup data backup data backup data backup data backup data backup data <br> - quality control quality control quality control quality control quality control quality control quality control quality control"
        Spanned styledText;
        if (aggregatedAnnotationInfo.dataLeaked) {
            if (destinationsString == null && dataTypesString == null) {
                styledText = Html.fromHtml(String.format("<p>This app may send the %s data or information derived from the %s data out of the phone.</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataGroup, dataGroup, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString == null && dataTypesString != null) {
                styledText = Html.fromHtml(String.format("<p>This app will send %s out of the phone</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString != null && dataTypesString == null) {
                styledText = Html.fromHtml(String.format("<p>This app will send the %s data or information derived from the %s data to %s</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataGroup, dataGroup, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else {
                styledText = Html.fromHtml(String.format("<p>This app will send %s to %s</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            }
        } else {
            styledText = Html.fromHtml(String.format("<p>This app will not send the %s data or information derived from the %s data out of the phone.</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataGroup, dataGroup, purposesString), FROM_HTML_MODE_LEGACY);
        }

        String title;
        String currentTime = new java.text.SimpleDateFormat("MM/dd HH:mm",
                context.getResources().getConfiguration().locale)
                .format(new java.util.Date (System.currentTimeMillis()));
        if (accessType == ONE_TIME || accessType == RECURRING) {
            title = String.format("%s accessed %s at %s (%d times in the last hour)", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup, currentTime, AccessHistory.getInstance().getAccessTimesInLastHour(ID));
        } else {
            title = String.format("%s is accessing %s (since %s)", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup, currentTime);
        }
        Intent intent = new Intent(HSStatus.getApplicationContext(), PrivacyCenterActivity.class);
        intent.putExtra(PrivacyPreferenceFragment.DATA_USE_KEY, ID);

        int currentID = (int) System.currentTimeMillis() & 0xffff;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, currentID, intent, 0);
        int notificationSmallIcon;
        switch (aggregatedAnnotationInfo.dataGroup) {
            case Microphone:
                notificationSmallIcon = R.drawable.baseline_mic_black_18dp;
                break;
            case Camera:
                notificationSmallIcon = R.drawable.baseline_camera_alt_black_18dp;
                break;
            case Location:
                notificationSmallIcon = R.drawable.baseline_location_on_black_18dp;
                break;
            case Sms:
                notificationSmallIcon = R.drawable.baseline_textsms_black_18dp;
                break;
            case BodySensor:
                notificationSmallIcon = R.drawable.baseline_location_on_black_18dp;
                break;
            case UserFile:
                notificationSmallIcon = R.drawable.baseline_folder_black_18dp;
                break;
            case UserInput:
                notificationSmallIcon = R.drawable.baseline_create_black_18dp;
                break;
            case CallLogs:
                notificationSmallIcon = R.drawable.baseline_phone_black_18dp;
                break;
            case Calendar:
                notificationSmallIcon = R.drawable.baseline_today_black_18dp;
                break;
            case Contacts:
                notificationSmallIcon = R.drawable.baseline_contacts_black_18dp;
                break;
            case UniqueId:
            default:
                notificationSmallIcon = R.drawable.baseline_perm_identity_black_18dp;
        }
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(styledText)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(styledText))
                .setSmallIcon(notificationSmallIcon)
                .addAction(R.drawable.ic_launcher_foreground, "View data controls", pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Don't show this again", pendingIntent);
//                .addAction(R.drawable.ic_launcher_foreground, "Privacy Notice Settings", pendingIntent) // TODO: make this optional and dependent on whether the developer embed the privacy notice settings in the app
        if (jitNoticeFrequency == JitNoticeFrequency.NOTIFICATION_ALWAYS_POP_OUT ||
                (jitNoticeFrequency == JitNoticeFrequency.NOTIFICATION_POP_OUT_FIRST_TIME_ONLY && AccessHistory.getInstance().isFirstTimeAccess(ID))) {
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setDefaults(Notification.DEFAULT_ALL);
            // TODO: test this on version >= Android O
        }
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(HSStatus.getMyAnnotationInfoMap().getNotificationIDByID(ID), notification);
    }

    static public void cancelPrivacyNotification(Context context, String ID) {
        if (context == null) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(HSStatus.getMyAnnotationInfoMap().getNotificationIDByID(ID));
    }
}
