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
            permissionDataGroupMap.put(Manifest.permission.BODY_SENSORS, BodySensor);
        }
        permissionDataGroupMap.put(Manifest.permission.SEND_SMS, Sms);
        permissionDataGroupMap.put(Manifest.permission.READ_SMS, Sms);
        permissionDataGroupMap.put(Manifest.permission.RECEIVE_SMS, Sms);
        permissionDataGroupMap.put(Manifest.permission.BROADCAST_SMS, Sms);
        permissionDataGroupMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, UserFile);
        permissionDataGroupMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, UserFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissionDataGroupMap.put(Manifest.permission.READ_PHONE_NUMBERS, UniqueId);
        }
        permissionDataGroupMap.put(Manifest.permission.READ_PHONE_STATE, UniqueId);
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
        showAllDialog(context, personalDataGroupList.toArray(new PersonalDataGroup[0]), onCancelListener);
    }

    public static void showAllDialog(Context context, PersonalDataGroup[] personalDataGroups, DialogInterface.OnCancelListener onCancelListener) {
        if (context == null) {
            return;
        }
        if (personalDataGroups.length > 0) {
            showDialog(context, personalDataGroups, 0, onCancelListener);
        }
    }

    private static void showDialog(final Context context, final PersonalDataGroup [] personalDataGroups, final int position, final DialogInterface.OnCancelListener finalOnCancelListener) {
        final DialogInterface.OnCancelListener onCancelListener;
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

        SourcePrivacyInfo[] privacyInfoList =
                HSStatus.getMyPrivacyInfoMap().getSourcePrivacyInfoListByDataGroup(personalDataGroup);
        ArrayList<String> purposeList = new ArrayList<>();
        ArrayList<String> destinationList = new ArrayList<>();
        ArrayList<String> dataTypeList = new ArrayList<>();
        ArrayList<SinkPrivacyInfo> sinkPrivacyInfoArrayList = new ArrayList<>();

        boolean dataLeaked = false;
        for (SourcePrivacyInfo privacyInfo : privacyInfoList) {
            if (privacyInfo.purposes != null) {
                purposeList.addAll(Arrays.asList(privacyInfo.purposes));
            }
            sinkPrivacyInfoArrayList.addAll(HSStatus.getMyPrivacyInfoMap().getSinkIDsBySourceID(privacyInfo.ID));
        }
        String purposesString = HSUtils.generatePurposesString(
                purposeList.toArray(new String[0]), "<br>"); // "\n" // TODO: also append sink purposes

        Spanned egressDescriptionText;
        if (!sinkPrivacyInfoArrayList.isEmpty()) {
            StringBuilder accessTypeStringBuilder = new StringBuilder();
            for (SinkPrivacyInfo sinkPrivacyInfo : sinkPrivacyInfoArrayList) {
                String accessTypeString;
                if (sinkPrivacyInfo.accessType == AccessType.STORED_ON_DEVICE) {
                    accessTypeString = String.format("<p>This app may store %s on device.</p>", sinkPrivacyInfo.dataGroup);
                } else if (sinkPrivacyInfo.accessType == AccessType.STORED_ON_CLOUD) {
                    accessTypeString = String.format("<p>This app may store %s on cloud.</p>", sinkPrivacyInfo.dataGroup);
                } else if (sinkPrivacyInfo.accessType == AccessType.SENT_OFF_DEVICE) {
                    accessTypeString = String.format("<p>This app may send %s off device.</p>", sinkPrivacyInfo.dataGroup);
                } else {
                    continue;
                }
                accessTypeStringBuilder.append(accessTypeString);
            }
            egressDescriptionText = Html.fromHtml(String.format("%s<b>%s may be used for:</b><br>%s", accessTypeStringBuilder, personalDataGroup, purposesString), FROM_HTML_MODE_LEGACY);
        } else {
            egressDescriptionText = Html.fromHtml(String.format("<p>This app will not send the %s data or information derived from the %s data out of the phone.</p><b>%s may be used for:</b><br>%s", personalDataGroup, personalDataGroup, personalDataGroup, purposesString), FROM_HTML_MODE_LEGACY);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(Html.fromHtml(String.format("Privacy facts about <b>%s</b> access in %s",
                        HSUtils.getDataString(personalDataGroup, false),
                        HSUtils.getApplicationName(HSStatus.getApplicationContext()))))
                .setMessage(egressDescriptionText)
                .setNeutralButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancelListener.onCancel(dialog);
                    }
                })
                .setCancelable(false);
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
