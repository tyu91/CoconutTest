package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated.AnnotationInfoMap;

import java.util.ArrayList;
import java.util.Arrays;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

public class PermissionNotice {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showDialog(Context context, PersonalDataGroup personalDataGroup, DialogInterface.OnCancelListener onCancelListener) {
        if (context == null) {
            return;
        }
        AnnotationInfo [] annotationInfoList =
                AnnotationInfoMap.getAnnotationInfoListByDataGroup(personalDataGroup);
        ArrayList<String> purposeList = new ArrayList<>();
        ArrayList<String> destinationList = new ArrayList<>();
        ArrayList<String> dataTypeList = new ArrayList<>();
        boolean dataLeaked = false;
        for (AnnotationInfo annotationInfo : annotationInfoList) {
            if (annotationInfo.purposes != null) {
                purposeList.addAll(Arrays.asList(annotationInfo.purposes));
            }
            if (annotationInfo.leakedDataTypes != null) {
                dataTypeList.addAll(Arrays.asList(annotationInfo.leakedDataTypes));
            }
            if (annotationInfo.destinations != null) {
                destinationList.addAll(Arrays.asList(annotationInfo.destinations));
            }
            if (annotationInfo.dataLeaked) {
                dataLeaked = true;
            }
        }
        String purposesString = HSUtils.generatePurposesString(
                purposeList.toArray(new String[0]), "<br>"); // "\n"
        String destinationsString = HSUtils.generateEgressInfoString(destinationList.toArray(new String[0]));
        String dataTypesString = HSUtils.generateEgressInfoString(dataTypeList.toArray(new String[0]));

        Spanned egressDescriptionText;
        if (dataLeaked) {
            if (destinationsString == null && dataTypesString == null) {
                egressDescriptionText = Html.fromHtml(String.format("<p>This app may send the %s data or information derived from the %s data out of the phone.</p><b>Purpose from the developer:</b><br>%s", personalDataGroup, personalDataGroup, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString == null && dataTypesString != null) {
                egressDescriptionText = Html.fromHtml(String.format("<p>This app will send %s out of the phone</p><b>Purpose from the developer:</b><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else if (destinationsString != null && dataTypesString == null) {
                egressDescriptionText = Html.fromHtml(String.format("<p>This app will send the %s data or information derived from the %s data to %s</p><b>Purpose from the developer:</b><br>%s", personalDataGroup, personalDataGroup, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            } else {
                egressDescriptionText = Html.fromHtml(String.format("<p>This app will send %s to %s</p><b>Purpose from the developer:</b><br>%s", dataTypesString, destinationsString, purposesString), FROM_HTML_MODE_LEGACY);
            }
        } else {
            egressDescriptionText = Html.fromHtml(String.format("<p>This app will not send the %s data or information derived from the %s data out of the phone.</p><b>Purpose from the developer:</b><br>%s", personalDataGroup, personalDataGroup, purposesString), FROM_HTML_MODE_LEGACY);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(Html.fromHtml(String.format("Privacy facts about <b>%s</b> access in this app", personalDataGroup.toString())))
                .setMessage(egressDescriptionText);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.setOnCancelListener(onCancelListener);
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

        // Set fixed width (280dp) and WRAP_CONTENT height
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, context.getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        // Set to TYPE_SYSTEM_ALERT so that the Service can display it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }
}
