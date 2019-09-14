package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.RequiresApi;

import com.coconuttest.tyu91.coconuttest.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AccessType.ONE_TIME;
import static com.coconuttest.tyu91.coconuttest.HoneysuckleLib.AccessType.RECURRING;

public class NotificationUtils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    static public void pushPrivacyNotification(Context context, String ID) {
        if (context == null) {
            return;
        }

        AnnotationInfo aggregatedAnnotationInfo = HSStatus.getMyAnnotationInfoMap().getAnnotationInfoByID(ID);

        String dataGroup = aggregatedAnnotationInfo.dataGroup.toString().replace("_", " ");
        String purposesString = HSUtils.generatePurposesString(aggregatedAnnotationInfo.purposes, "<br>");
        String destinationsString = HSUtils.generateEgressInfoString(aggregatedAnnotationInfo.destinations);
        String dataTypesString = HSUtils.generateEgressInfoString(aggregatedAnnotationInfo.leakedDataTypes);
        AccessType accessType = aggregatedAnnotationInfo.accessType;

//        purposesString = "- backup data backup data backup data backup data backup data backup data backup data <br> - quality control quality control quality control quality control quality control quality control quality control quality control"
        Spanned styledText;
        if (aggregatedAnnotationInfo.dataLeaked) {
            if (destinationsString == null && dataTypesString == null) {
                styledText = Html.fromHtml(String.format("<p>This app may send the %s data or information derived from the %s data out of the phone.</p><span style=\"color:black\">Purpose from the developer:</span><br>%s", dataGroup, dataGroup, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString == null && dataTypesString != null) {
                styledText = Html.fromHtml(String.format("<p>This app will send %s out of the phone</p><span style=\"color:black\">Purpose from the developer:</span><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString != null && dataTypesString == null) {
                styledText = Html.fromHtml(String.format("<p>This app will send the %s data or information derived from the %s data to %s</p><span style=\"color:black\">Purpose from the developer:</span><br>%s", dataGroup, dataGroup, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else {
                styledText = Html.fromHtml(String.format("<p>This app will send %s to %s</p><span style=\"color:black\">Purpose from the developer:</span><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            }
        } else {
            styledText = Html.fromHtml(String.format("<p>This app will not send the %s data or information derived from the %s data out of the phone.</p><span style=\"color:black\">Purpose from the developer:</span><br>%s", dataGroup, dataGroup, purposesString), FROM_HTML_MODE_LEGACY);
        }

        String title;
        String currentTime = new java.text.SimpleDateFormat("MM/dd HH:mm",
                context.getResources().getConfiguration().locale)
                .format(new java.util.Date (System.currentTimeMillis()));
        if (accessType == ONE_TIME || accessType == RECURRING) {
            title = String.format("Accessed %s data (%d times in the last hour)", dataGroup, AccessHistory.getInstance().getAccessTimesInLastHour(ID));
        } else {
            title = String.format("Accessing %s data since %s", dataGroup, currentTime);
        }
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(styledText)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(styledText))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(HSStatus.getMyAnnotationInfoMap().getNotificationIDByID(ID), notification);

    }
}
