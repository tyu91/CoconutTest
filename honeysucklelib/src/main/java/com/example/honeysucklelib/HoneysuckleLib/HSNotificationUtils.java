package com.example.honeysucklelib.HoneysuckleLib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import com.example.honeysucklelib.R;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.CONTINUOUS_COLLECTION;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.ONE_TIME_COLLECTION;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.RECURRING_COLLECTION;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.SENT_OFF_DEVICE;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.STORED_ON_CLOUD;
import static com.example.honeysucklelib.HoneysuckleLib.AccessType.STORED_ON_DEVICE;
import static com.example.honeysucklelib.HoneysuckleLib.HSUtils.jitNotificationConfigKeyPattern;

public class HSNotificationUtils {
    private static int notificationID = 1000;

    static JitNoticeFrequency readJitNoticeFrequencyFromSharedPref(JitNoticeFrequency defaultValue, String ID) {
        JitNoticeFrequency jitNoticeFrequency = defaultValue;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HSStatus.getApplicationContext());
        String userSetJitNoticeFrequencyString =
                sharedPref.getString(String.format(jitNotificationConfigKeyPattern, ID), "Not configured");
        if ("Always pop up".equals(userSetJitNoticeFrequencyString)) {
            jitNoticeFrequency = JitNoticeFrequency.NOTIFICATION_ALWAYS_POP_OUT;
        } else if ("Pop up on first collection".equals(userSetJitNoticeFrequencyString)) {
            jitNoticeFrequency = JitNoticeFrequency.NOTIFICATION_POP_OUT_FIRST_TIME_ONLY;
        } else if ("Show data icon on stats bar".equals(userSetJitNoticeFrequencyString)) {
            jitNoticeFrequency = JitNoticeFrequency.SEND_NOTIFICATION_SILENTLY;
        } else if ("No alert".equals(userSetJitNoticeFrequencyString)) {
            jitNoticeFrequency = JitNoticeFrequency.DO_NOT_SEND_NOTIFICATION;
        }
        return jitNoticeFrequency;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static public void pushPrivacyNotification(Context context, String ID) {
        if (context == null) {
            return;
        }
        JitNoticeFrequency jitNoticeFrequency;
        Spanned styledText;
        String title;
        String currentTime = new java.text.SimpleDateFormat("MM/dd HH:mm",
                context.getResources().getConfiguration().locale)
                .format(new java.util.Date(System.currentTimeMillis()));
        Intent intent;

        if (HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID) instanceof SourcePrivacyInfo) {
            SourcePrivacyInfo aggregatedPrivacyInfo = (SourcePrivacyInfo) HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID);
            if (aggregatedPrivacyInfo == null) {
                return;
            }
            jitNoticeFrequency = readJitNoticeFrequencyFromSharedPref(aggregatedPrivacyInfo.jitNoticeFrequency, ID);
            if (jitNoticeFrequency == JitNoticeFrequency.DO_NOT_SEND_NOTIFICATION) {
                return;
            }

            String dataGroup = HSUtils.getDataString(aggregatedPrivacyInfo.dataGroup, true);
            String purposesString = HSUtils.generatePurposesString(aggregatedPrivacyInfo.purposes, "<br>");
            AccessType accessType = aggregatedPrivacyInfo.accessType;

            ArrayList<SinkPrivacyInfo> sinkInfoList = HSStatus.getMyPrivacyInfoMap().getSinkIDsBySourceID(ID);

            if (!sinkInfoList.isEmpty()) {
                StringBuilder sinkDescriptionBuilder = new StringBuilder();
                for (SinkPrivacyInfo sinkPrivacyInfo : sinkInfoList) {
                    if (sinkPrivacyInfo.accessType == STORED_ON_DEVICE) {
                        sinkDescriptionBuilder.append(String.format("<p>This app may store %s on device.</p>", sinkPrivacyInfo.dataGroup));
                    } else if (sinkPrivacyInfo.accessType == STORED_ON_CLOUD) {
                        sinkDescriptionBuilder.append(String.format("<p>This app may store %s on cloud.</p>", sinkPrivacyInfo.dataGroup));
                    } else if (sinkPrivacyInfo.accessType == SENT_OFF_DEVICE) {
                        sinkDescriptionBuilder.append(String.format("<p> This app may send %s off device.</p>", sinkPrivacyInfo.dataGroup));
                    }
                }
                styledText = Html.fromHtml(String.format("%s<span style=\"color:black\">Data use purpose:</span><br>%s", sinkDescriptionBuilder, purposesString), FROM_HTML_MODE_LEGACY);
            } else {
                styledText = Html.fromHtml(String.format("<p>This app will not send the %s data or information derived from the %s data out of the phone.</p><span style=\"color:black\">Data use purpose:</span><br>%s", dataGroup, dataGroup, purposesString), FROM_HTML_MODE_LEGACY);
            }

            if (accessType == ONE_TIME_COLLECTION || accessType == RECURRING_COLLECTION) {
                title = String.format("%s accessed %s at %s (%d times in the last hour)", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup, currentTime, AccessHistory.getInstance().getAccessTimesInLastHour(ID));
            } else if (accessType == CONTINUOUS_COLLECTION) {
                title = String.format("%s is accessing %s (since %s)", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup, currentTime);
            } else {
                return;
            }
            intent = new Intent(HSStatus.getApplicationContext(), PrivacyCenterActivity.class);
            intent.putExtra(PrivacyPreferenceFragment.DATA_USE_KEY, ID);

        } else if (HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID) instanceof SinkPrivacyInfo) {
            SinkPrivacyInfo aggregatedPrivacyInfo = (SinkPrivacyInfo) HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID);
            if (aggregatedPrivacyInfo == null) {
                return;
            }
            jitNoticeFrequency = readJitNoticeFrequencyFromSharedPref(aggregatedPrivacyInfo.jitNoticeFrequency, ID);
            if (jitNoticeFrequency == JitNoticeFrequency.DO_NOT_SEND_NOTIFICATION) {
                return;
            }
            String dataGroup = aggregatedPrivacyInfo.dataGroup;
            String purposesString = HSUtils.generatePurposesString(aggregatedPrivacyInfo.purposes, "<br>");
            AccessType accessType = aggregatedPrivacyInfo.accessType;

            styledText = Html.fromHtml(String.format("<span style=\"color:black\">Data use purpose:</span><br>%s", purposesString), FROM_HTML_MODE_LEGACY);

            if (accessType == STORED_ON_DEVICE) {
                title = String.format("%s stored %s on device", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup);
            } else if (accessType == SENT_OFF_DEVICE) {
                title = String.format("%s sent %s off device", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup);
            } else if (accessType == STORED_ON_CLOUD) {
                title = String.format("%s stored %s on cloud", HSUtils.getApplicationName(HSStatus.getApplicationContext()), dataGroup);
            } else {
                return;
            }
            intent = new Intent(HSStatus.getApplicationContext(), PrivacyCenterActivity.class);

        } else {
            return;
        }

        int currentID = (int) System.currentTimeMillis() & 0xffff;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, currentID, intent, 0);
        int notificationSmallIcon = R.drawable.baseline_privacy_tip_black_18dp;
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(styledText)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(styledText))
                .setSmallIcon(notificationSmallIcon)
                .addAction(R.drawable.ic_launcher_foreground, "View more settings", pendingIntent)
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
        notificationManager.notify(notificationID, notification);
    }

    static public void cancelPrivacyNotification(Context context, String ID) {
        if (context == null) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
    }
}
