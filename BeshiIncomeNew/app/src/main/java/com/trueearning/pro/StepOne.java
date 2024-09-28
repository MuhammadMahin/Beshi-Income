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

public class StepOne extends AppCompatActivity {

    AppCompatButton buttonFinal;
    LottieAnimationView lottieAnimationView, lottie1, lottie2;
    MaxInterstitialAd interstitialAd;
    int adsCount = 0;
    TextView textView, textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        initAppLovin();


        buttonFinal = findViewById(R.id.final_button);
        lottieAnimationView = findViewById(R.id.step_one_on_lottie);
        lottie1 = findViewById(R.id.lottie3);
        lottie2 = findViewById(R.id.final_lottie3);

        textView = findViewById(R.id.main_heading3);
        textView1 = findViewById(R.id.main_heading31);
        buttonFinal.setVisibility(View.INVISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            buttonFinal.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
        }, 10000);


        buttonFinal.setOnClickListener(view -> {
            if (adsCount < 1){
                if (interstitialAd.isReady()) {
                    interstitialAd.showAd();
                    adsCount += 1;
                }
            }else {
                startActivity(new Intent(StepOne.this, MahinActivity.class));
                overridePendingTransition(0, 0);
                finishAffinity();
            }
        });

    }



    private void initAppLovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> interstitialAdLoad());
    }


    private void interstitialAdLoad() {
        interstitialAd = new MaxInterstitialAd(getResources().getString(R.string.step_one_interest), StepOne.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Toast.makeText(StepOne.this, "You can click now", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Toast.makeText(StepOne.this, "3/3 প্লে করা সফল হয়েছে!\n 15tk Added successfully!!", Toast.LENGTH_LONG).show();
                buttonFinal.setText("এবার সেভ করুন!");
                lottie1.setVisibility(View.INVISIBLE);
                lottie2.setVisibility(View.VISIBLE);
                textView.setText("15tk Successfully Added!");
                textView1.setText("Now the Final Step..!");
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