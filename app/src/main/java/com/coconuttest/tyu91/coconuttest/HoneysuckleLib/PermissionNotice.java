package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.coconuttest.tyu91.coconuttest.HoneysuckleGenerated.MyApplication;
import com.coconuttest.tyu91.coconuttest.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PermissionNotice {

    public static void showDialog(Context context, PersonalDataGroup personalDataGroup, DialogInterface.OnCancelListener onCancelListener) {
        if (context == null) {
            return;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context).setTitle("Title").setMessage("Message");
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
