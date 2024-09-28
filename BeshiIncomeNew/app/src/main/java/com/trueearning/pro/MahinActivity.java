package com.trueearning.pro;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trueearning.pro.Utility.NetworkChangeListener;

import java.util.ArrayList;

@Keep
public class MahinActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private static final String FILE_NAME = "myFile";
    private MaxInterstitialAd interstitialAd;
    private MaxRewardedAd maxRewardedAds;

    BottomNavigationView bottomNavigationView;
    LottieAnimationView balanceLottie, lottieAnimationView, lottie;
    TextView countTimes, rewardPoint, demo;
    RelativeLayout congPage, mainPage;
    AppCompatButton rewardSaving, nextButton;
    Intent intent;
    PendingIntent pendingIntent;

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    int points = 0;
    int plays = 0;
    int steps = 0;
    int adsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahin);

        initAppLovin();


        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vpnmasterx.fast"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        congPage = findViewById(R.id.congratulation_page);
        mainPage = findViewById(R.id.active_main);
        rewardSaving = findViewById(R.id.coin_save_button);
        rewardPoint = findViewById(R.id.mahin_balance);
        demo = findViewById(R.id.demo_balance);
        lottieAnimationView = findViewById(R.id.my_lotti);
        lottie = findViewById(R.id.final_lottie_reward);

        countTimes = findViewById(R.id.countText);
        nextButton = findViewById(R.id.nextButton);

        balanceLottie = findViewById(R.id.balances_lottie);
        viewPager2 = findViewById(R.id.viewpager);


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.item_earning_page);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_home_page:
                    startActivity(new Intent(MahinActivity.this, Home.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
                case R.id.item_earning_page:
                    startActivity(new Intent(MahinActivity.this, Earning.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;

                case R.id.item_profile_page:
                    startActivity(new Intent(MahinActivity.this, UserProfile.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;

            }
            return false;
        });


        String[] mainHeading = {
                getString(R.string.main_heading1),
                getString(R.string.main_heading2),
                getString(R.string.main_heading3),
                getString(R.string.main_heading4),
                getString(R.string.main_heading5),
                getString(R.string.main_heading6),
                getString(R.string.main_heading7),
                getString(R.string.main_heading8),
                getString(R.string.main_heading9),
                getString(R.string.main_heading10),
                getString(R.string.main_heading11)
        };
        String[] heading1 = {
                getString(R.string.heading1_1),
                getString(R.string.heading1_2),
                getString(R.string.heading1_3),
                getString(R.string.heading1_4),
                getString(R.string.heading1_5),
                getString(R.string.heading1_6),
                getString(R.string.heading1_7),
                getString(R.string.heading1_8),
                getString(R.string.heading1_9),
                getString(R.string.heading1_10),
                getString(R.string.heading1_11)

        };
        String[] heading2 = {
                getString(R.string.heading2_1),
                getString(R.string.heading2_2),
                getString(R.string.heading2_3),
                getString(R.string.heading2_4),
                getString(R.string.heading2_5),
                getString(R.string.heading2_6),
                getString(R.string.heading2_7),
                getString(R.string.heading2_8),
                getString(R.string.heading2_9),
                getString(R.string.heading2_10),
                getString(R.string.heading2_11)
        };
        String[] heading3 = {
                getString(R.string.heading3_1),
                getString(R.string.heading3_2),
                getString(R.string.heading3_3),
                getString(R.string.heading3_4),
                getString(R.string.heading3_5),
                getString(R.string.heading3_6),
                getString(R.string.heading3_7),
                getString(R.string.heading3_8),
                getString(R.string.heading3_9),
                getString(R.string.heading3_10),
                getString(R.string.heading3_11)
        };
        String[] heading4 = {
                getString(R.string.heading4_1),
                getString(R.string.heading4_2),
                getString(R.string.heading4_3),
                getString(R.string.heading4_4),
                getString(R.string.heading4_5),
                getString(R.string.heading4_6),
                getString(R.string.heading4_7),
                getString(R.string.heading4_8),
                getString(R.string.heading4_9),
                getString(R.string.heading4_10),
                getString(R.string.heading4_11)
        };


        viewPagerItemArrayList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(mainHeading[i], heading1[i], heading2[i], heading3[i], heading4[i]);

            viewPagerItemArrayList.add(viewPagerItem);
        }
        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);


        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        int myReward = sharedPreferences.getInt("point", 0);


        nextButton.setVisibility(View.INVISIBLE);
        rewardSaving.setEnabled(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            nextButton.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
        }, 10000);


        nextButton.setOnClickListener(view -> {
            if (interstitialAd.isReady() && interstitialAd != null) {
                interstitialAd.showAd();
            }
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.INVISIBLE);
        });


        rewardSaving.setOnClickListener(view -> {

            if (maxRewardedAds.isReady()) {
                maxRewardedAds.showAd();
                rewardSaving.setEnabled(false);
                plays += 1;
                points += 15;

                int rewardFinal = sharedPreferences.getInt("point", 0);
                int myPlaying = sharedPreferences.getInt("plays", 0);

                int nowCount = myPlaying + plays;

                int nowPoint = rewardFinal + points;

                storePoint(nowPoint, nowCount);
                Toast.makeText(MahinActivity.this, "এই Ads এ অবশ্যই ক্লিক করতেই হবে!! আর \nPlayStore এ নিয়ে গেলে এই App টা Install  করতে হবে। ", Toast.LENGTH_LONG).show();
            } else {
                if (interstitialAd.isReady()) {
                    interstitialAd.showAd();

                    rewardSaving.setEnabled(false);
                    plays += 1;
                    points += 15;

                    int rewardFinal = sharedPreferences.getInt("point", 0);
                    int myPlaying = sharedPreferences.getInt("plays", 0);

                    int nowCount = myPlaying + plays;

                    int nowPoint = rewardFinal + points;

                    storePoint(nowPoint, nowCount);
                    Toast.makeText(MahinActivity.this, "এই Ads এ অবশ্যই ক্লিক করতেই হবে!! আর \nPlayStore এ নিয়ে গেলে এই App টা Install  করতে হবে। ", Toast.LENGTH_LONG).show();

                } else Toast.makeText(this, "Ad is not Ready, Connect United State and try again", Toast.LENGTH_LONG).show();
            }
        });


        balanceLottie.setOnClickListener(view -> {
            balanceLottie.setVisibility(View.INVISIBLE);
            demo.setVisibility(View.INVISIBLE);
            rewardPoint.setVisibility(View.VISIBLE);

            Handler h = new Handler();
            h.postDelayed(() -> {
                balanceLottie.setVisibility(View.VISIBLE);
                demo.setVisibility(View.VISIBLE);
                rewardPoint.setVisibility(View.INVISIBLE);
            }, 2500);
        });

        rewardPoint.setText(String.valueOf(myReward));
    }

    private void initAppLovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> interstitialAdLoad());
    }


    private void interstitialAdLoad() {
        interstitialAd = new MaxInterstitialAd(getResources().getString(R.string.mahin_interest), MahinActivity.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                rewardedAdLoads();

                Toast.makeText(MahinActivity.this, "You can click now", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                congPage.setVisibility(View.VISIBLE);
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

    private void rewardedAdLoads() {
        maxRewardedAds = MaxRewardedAd.getInstance(getResources().getString(R.string.reward_pro), MahinActivity.this);
        MaxRewardedAdListener maxRewardedAdListener = new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {

            }

            @Override
            public void onRewardedVideoStarted(MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                rewardSaving.setEnabled(true);

                Toast.makeText(MahinActivity.this, "reward loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                steps += 1;
//                rewardSaving.setText(getResources().getString(R.string.congrast));
                rewardSaving.setText("Yeeess!!! You Win");
                lottie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                maxRewardedAds.loadAd();

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        };
        maxRewardedAds.setListener(maxRewardedAdListener);
        maxRewardedAds.loadAd();
    }


    private void storePoint(Integer nowPoint, Integer nowCount) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();

        int rewardFinal = nowPoint;
        editor.putInt("point", rewardFinal);

        int playingFinal = nowCount;
        editor.putInt("plays", playingFinal);

        editor.apply();
    }

    private int getitem(int i) {
        return viewPager2.getCurrentItem() + i;
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


    private static final int TIME = 3000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME > System.currentTimeMillis()) {
            startActivity(new Intent(getApplicationContext(), Earning.class));
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press again to go Earning Page",
                    Toast.LENGTH_LONG).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

}
