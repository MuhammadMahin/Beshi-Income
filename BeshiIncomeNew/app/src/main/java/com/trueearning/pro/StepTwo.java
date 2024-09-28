package com.trueearning.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;

public class StepTwo extends AppCompatActivity {

    AppCompatButton buttonStepTwo;
    LottieAnimationView lottieAnimationView, lottie1, lottie2;
    MaxInterstitialAd interstitialAd;
    int adsCount = 0;
    TextView textView, textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAppLovin();

        setContentView(R.layout.activity_step_two);
        buttonStepTwo = findViewById(R.id.step_two_button);
        lottieAnimationView = findViewById(R.id.step_one_two_lottie);
        textView = findViewById(R.id.main_heading2);
        textView1 = findViewById(R.id.main_heading21);

        lottie1 = findViewById(R.id.twoLottie);
        lottie2 = findViewById(R.id.final_lottie2);

        buttonStepTwo.setVisibility(View.INVISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            buttonStepTwo.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
        }, 10000);



        buttonStepTwo.setOnClickListener(view -> {
            if (adsCount < 1){
                if (interstitialAd.isReady()) {
                    interstitialAd.showAd();
                    adsCount += 1;
                }
            }else {
                startActivity(new Intent(StepTwo.this, StepOne.class));
                finishAffinity();
            }
        });
    }



    private void initAppLovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> interstitialAdLoad());
    }


    private void interstitialAdLoad() {
        interstitialAd = new MaxInterstitialAd(getResources().getString(R.string.step_one_interest), StepTwo.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Toast.makeText(StepTwo.this, "You can click now", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Toast.makeText(StepTwo.this, "2/3 প্লে করা সফল হয়েছে!\n10 Taka Added successfully!!", Toast.LENGTH_LONG).show();
                buttonStepTwo.setText("স্টেপ ০৩'এ যান");
                lottie1.setVisibility(View.INVISIBLE);
                lottie2.setVisibility(View.VISIBLE);
                textView.setText("10tk Successfully added!");
                textView1.setText("Now... Step 03!");

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
}