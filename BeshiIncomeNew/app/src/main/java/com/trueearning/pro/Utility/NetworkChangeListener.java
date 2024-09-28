package com.trueearning.pro.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Keep;
import androidx.appcompat.widget.AppCompatButton;

import com.trueearning.pro.R;

@Keep
public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AppCompatButton btnTry, tryConnect;


        if (!Common.isConnectedToInternet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet, null);
            builder.setView(layout_dialog);
            tryConnect = layout_dialog.findViewById(R.id.tryConnect);
            btnTry = layout_dialog.findViewById(R.id.btnRetry);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);
            tryConnect.setOnClickListener(view -> {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                ((Activity) context).finishAffinity();
            });
            btnTry.setOnClickListener(view -> {
                ((Activity) context).finishAffinity();
                onReceive(context, intent);
            });
        }
    }

}

