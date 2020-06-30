package com.example.honeysucklelib.HoneysuckleLib;

import android.Manifest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static com.example.honeysucklelib.HoneysuckleLib.PersonalDataGroup.*;

public class PermissionNotice {

    private static HashMap<String, PersonalDataGroup> permissionDataGroupMap = new HashMap<>();
    static {
        permissionDataGroupMap.put(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Location);
        permissionDataGroupMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, Location);
        permissionDataGroupMap.put(Manifest.permission.ACCESS_FINE_LOCATION, Location);
        permissionDataGroupMap.put(Manifest.permission.ACCESS_MEDIA_LOCATION, Location);
        permissionDataGroupMap.put(Manifest.permission.WRITE_CONTACTS, Contacts);
        permissionDataGroupMap.put(Manifest.permission.READ_CONTACTS, Contacts);
        permissionDataGroupMap.put(Manifest.permission.WRITE_CALENDAR, Calendar);
        permissionDataGroupMap.put(Manifest.permission.READ_CALENDAR, Calendar);
        permissionDataGroupMap.put(Manifest.permission.CAMERA, Camera);
        permissionDataGroupMap.put(Manifest.permission.RECORD_AUDIO, Microphone);
        permissionDataGroupMap.put(Manifest.permission.WRITE_CALL_LOG, CallLogs);
        permissionDataGroupMap.put(Manifest.permission.READ_CALL_LOG, CallLogs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            permissionDataGroupMap.put(Manifest.permission.BODY_SENSORS, Sensor);
        }
        permissionDataGroupMap.put(Manifest.permission.SEND_SMS, SMS);
        permissionDataGroupMap.put(Manifest.permission.READ_SMS, SMS);
        permissionDataGroupMap.put(Manifest.permission.RECEIVE_SMS, SMS);
        permissionDataGroupMap.put(Manifest.permission.BROADCAST_SMS, SMS);
        permissionDataGroupMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, User_File);
        permissionDataGroupMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, User_File);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showAllDialog(Context context, String[] permissionStrings, DialogInterface.OnCancelListener onCancelListener) {
        if (context == null) {
            return;
        }
        ArrayList<PersonalDataGroup> personalDataGroupList = new ArrayList<>();
        for (String permissionString: permissionStrings) {
            if (permissionDataGroupMap.containsKey(permissionString) &&
                    !personalDataGroupList.contains(permissionDataGroupMap.get(permissionString))) {
                personalDataGroupList.add(permissionDataGroupMap.get(permissionString));
            }
        }
        if (personalDataGroupList.size() > 0) {
            showDialog(context, personalDataGroupList.toArray(new PersonalDataGroup[0]), 0, onCancelListener);
        }
    }

    private static void showDialog(final Context context, final PersonalDataGroup [] personalDataGroups, final int position, final DialogInterface.OnCancelListener finalOnCancelListener) {
        DialogInterface.OnCancelListener onCancelListener;
        if (position == personalDataGroups.length - 1) {
            onCancelListener = finalOnCancelListener;
        } else {
            onCancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    showDialog(context, personalDataGroups, position + 1, finalOnCancelListener);
                }
            };
        }
        PersonalDataGroup personalDataGroup = personalDataGroups[position];

        AnnotationInfo [] annotationInfoList =
                HSStatus.getMyAnnotationInfoMap().getAnnotationInfoListByDataGroup(personalDataGroup);
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
