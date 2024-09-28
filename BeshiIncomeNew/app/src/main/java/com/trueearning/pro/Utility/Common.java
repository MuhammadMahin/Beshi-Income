package com.trueearning.pro.Utility;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

public class Common {


    public static boolean isConnectedToInternet(@NonNull Context context) {
        ConnectivityManager cManager = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cManager.getActiveNetwork() != null && cManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}

