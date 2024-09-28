package com.trueearning.pro;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.trueearning.pro.Utility.NetworkChangeListener;

@Keep
public class Home extends AppCompatActivity {

    private MaxInterstitialAd interstitialAd;


    FirebaseRemoteConfig mFirebaseRemoteConfig;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;


    AppCompatButton stepOneButton;
    LottieAnimationView lottieAnimationView, homeLottie, final_lottie;

    TextView textView, textView1;

    AppCompatButton updateButton;
    int adsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAppLovin();
        updateMe();

        AppLovinPrivacySettings.setHasUserConsent(true, Home.this);
        AppLovinPrivacySettings.setIsAgeRestrictedUser(false, Home.this);
        AppLovinPrivacySettings.setDoNotSell(false, Home.this);


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.item_home_page);
        stepOneButton = findViewById(R.id.step_one);
        lottieAnimationView = findViewById(R.id.step_one_lottie);
        final_lottie = findViewById(R.id.final_lottie);
        homeLottie = findViewById(R.id.home_lottie);

        textView = findViewById(R.id.home_txt);
        textView1 = findViewById(R.id.home_txt1);
        FirebaseApp.initializeApp(this);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_earning_page:
                    startActivity(new Intent(Home.this, Earning.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
                case R.id.item_profile_page:
                    startActivity(new Intent(Home.this, UserProfile.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
            }
            return false;
        });

        stepOneButton.setVisibility(View.INVISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            stepOneButton.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
        }, 10000);


        stepOneButton.setOnClickListener(view -> {
           if (adsCount < 1){
               if (interstitialAd.isReady()) {
                   interstitialAd.showAd();
                   adsCount += 1;
               }
           }else {
               startActivity(new Intent(Home.this, StepTwo.class));
               finishAffinity();
           }
        });

    }


    /**
     * ON_CREATE SES
     **/
    private void initAppLovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> interstitialAdLoad());

    }


    private void interstitialAdLoad() {
        interstitialAd = new MaxInterstitialAd(getResources().getString(R.string.home_interest), Home.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Toast.makeText(Home.this, "You can Play Now!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Toast.makeText(Home.this, "1/3 প্লে করা সফল হয়েছে!\n 5 Taka Added successfully!!", Toast.LENGTH_LONG).show();
                stepOneButton.setText("স্টেপ ০২'তে যান");
                textView.setText("5tk Successfully Added!");
                textView1.setText("Let's Go... Step 02!");

                homeLottie.setVisibility(View.INVISIBLE);
                final_lottie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        };
        interstitialAd.setListener(adListener);
        interstitialAd.loadAd();

    }


    private void updateMe() {

        int currentVersionCode = getCurrentVersionCode();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(20).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final String versions = mFirebaseRemoteConfig.getString("version");
                if (Integer.parseInt(versions) > currentVersionCode) {
                    showUpdateDialog();
                }
            }
        });
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                packageInfo = getPackageManager().getPackageInfo(getOpPackageName(), 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            assert packageInfo != null;
            return (int) packageInfo.getLongVersionCode();
        }
        return 0;
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        View layout_dialog = LayoutInflater.from(Home.this).inflate(R.layout.check_updates, null);
        builder.setView(layout_dialog);
        updateButton = layout_dialog.findViewById(R.id.updateButton);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        updateButton.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.muhammadmahin.fitpro")));
                finishAffinity();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Are you Really want to Exit App?")
                    .setMessage("Click on Not Now to dismiss or click exit now")
                    .setNegativeButton("Not Now", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("Yes, Exit Now", (dialogInterface, i) -> finishAffinity()).show();
            dialog.setCancelable(false);

        } else {
            Toast.makeText(getBaseContext(), "Press again to exit",
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}



