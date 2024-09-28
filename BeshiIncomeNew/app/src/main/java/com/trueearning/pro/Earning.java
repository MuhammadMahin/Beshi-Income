package com.trueearning.pro;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@Keep
public class Earning extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private MaxInterstitialAd sInterAds;
    LottieAnimationView lottieAnimationView;


    PendingIntent pendingIntent;
     Intent i;
     int adsPly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        i = new Intent(Earning.this, MahinActivity.class);
        initAppLovin();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = getActivity(Earning.this,
                    0, i, FLAG_UPDATE_CURRENT | FLAG_MUTABLE);
        }else {
            pendingIntent = getActivity(Earning.this,
                    0, i, PendingIntent.FLAG_UPDATE_CURRENT|FLAG_IMMUTABLE);
        }


        bottomNavigationView = findViewById(R.id.earning_bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.item_earning_page);

        lottieAnimationView = findViewById(R.id.e_earning_lottie);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home_page:
                    startActivity(new Intent(Earning.this, Home.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
                case R.id.item_profile_page:
                    startActivity(new Intent(Earning.this, UserProfile.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
            }
            return false;
        });


        lottieAnimationView.setOnClickListener(view -> {
            startActivity(new Intent(Earning.this, Home.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });


    }
    private void initAppLovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
            interstitialAdLoad();
        });
    }

    private void interstitialAdLoad() {
        sInterAds = new MaxInterstitialAd(getResources().getString(R.string.earning_interest), Earning.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                if (adsPly < 1) {
                    if (sInterAds.isReady()) {
                        sInterAds.showAd();
                        adsPly++;
                    }
                }
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                sInterAds.loadAd();

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        };
        sInterAds.setListener(adListener);
        sInterAds.loadAd();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Earning.this, Home.class));
        overridePendingTransition(0,0);
        finishAffinity();
    }
}