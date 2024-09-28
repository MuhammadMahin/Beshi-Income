package com.trueearning.pro;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;

@Keep
public class Settings extends AppCompatActivity {


    private MaxRewardedAd sRewardAds;
    private MaxInterstitialAd sInterAds;

    RelativeLayout goBack;
    LinearLayout goneText1, getGoneText2, getGoneText3, getGoneText7;
    TextView text1, text2, text3, text5, text7, text8, youtube, facebook;
    TextView text4;
    LottieAnimationView shareLottie;
    Intent intent, i, y, z, n;

    PendingIntent pendingIntent;

    int visible1 = 0;
    int visible2 = 0;
    int visible3 = 0;
    int visible7 = 0;
    int adsPly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initAds();

        i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/n/?trueearningapp/"));
        y = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCxnfV7dTWXux7vHdyLpe0kg"));
        z = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/fitpro360/privacy"));
        n = new Intent(Intent.ACTION_VIEW, Uri.parse("https://10minutesjob.com/"));

        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.muhammadmahin.fitpro"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(Settings.this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(Settings.this,
                    0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(Settings.this,
                    0, y, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, y, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(Settings.this,
                    0, z, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, z, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(Settings.this,
                    0, n, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, n, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }


        goBack = findViewById(R.id.back_from_settings);
        goneText1 = findViewById(R.id.gone_text_1);
        getGoneText2 = findViewById(R.id.gone_text_2);
        getGoneText3 = findViewById(R.id.gone_text_3);
        getGoneText7 = findViewById(R.id.gone_text_7);
        shareLottie = findViewById(R.id.share_lottie);

        text1 = findViewById(R.id.settings_txt_1);
        text2 = findViewById(R.id.settings_txt_2);
        text3 = findViewById(R.id.settings_txt_3);
        text4 = findViewById(R.id.settings_txt_4);
        text5 = findViewById(R.id.settings_txt_5);
        text7 = findViewById(R.id.settings_txt_7);
        text8 = findViewById(R.id.settings_txt_8);
        youtube = findViewById(R.id.go_to_youtube);
        facebook = findViewById(R.id.facebook);


        text1.setOnClickListener(view -> {
            if (visible1 == 0) {
                goneText1.setVisibility(View.VISIBLE);
                visible1 += 5;
            } else {
                goneText1.setVisibility(View.GONE);
                visible1 -= 5;
            }
        });


        text2.setOnClickListener(view -> {
            if (visible2 == 0) {
                getGoneText2.setVisibility(View.VISIBLE);
                visible2 += 5;
            } else {
                getGoneText2.setVisibility(View.GONE);
                visible2 -= 5;
            }
        });


        text3.setOnClickListener(view -> {
            if (visible3 == 0) {
                getGoneText3.setVisibility(View.VISIBLE);
                visible3 += 5;
            } else {
                getGoneText3.setVisibility(View.GONE);
                visible3 -= 5;
            }
        });


        getGoneText3.setOnClickListener(view -> {
            try {
                startActivity(n);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        text4.setOnClickListener(view -> {
            shareLottie.setVisibility(View.VISIBLE);
            Handler h = new Handler();
            h.postDelayed(() -> shareLottie.setVisibility(View.INVISIBLE), 5000);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareBody = "আকাউন্ট খুললেই ৫০ টাকা ফ্রি! কোনো-প্রকার ইনভেস্ট ছাড়া সম্পুর্ন বিনামূল্যে!" +
                    "\uD83D\uDC49 শুধুমাত্র সহজ Game Play করে ইনকাম আর মাত্র 20 টাকা হলেই WITHDRAW!" +
                    " পেমেন্ট এর শতভাগ গ্যারান্টি ও নিশ্চয়তা ১০০℅ !!!" +
                    " BESHI INCOME ফ্রি টাকা আয়ের Apps!\n" +
                    "ডাউনলোড করতে নিচের লিংক এ ক্লিক করে PlayStore থেকে ডাউনলোড করুন!\n" +
                    "https://beshiincomefree.page.link/beshi_income";
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Beshi Income Share");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(Settings.this,
                        0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                startActivity(shareIntent);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                startActivity(shareIntent);
            }


        });


        text5.setOnClickListener(view -> {
            try {
                startActivity(z);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        text7.setOnClickListener(view -> {
            if (visible7 == 0) {
                getGoneText7.setVisibility(View.VISIBLE);
                visible7 += 5;
            } else {
                getGoneText7.setVisibility(View.GONE);
                visible7 -= 5;
            }
        });

        text8.setOnClickListener(view -> {
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        youtube.setOnClickListener(view -> {
            try {
                startActivity(y);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        facebook.setOnClickListener(view -> {
            try {
                startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        goBack.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });
    }

    private void initAds() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> sRewardedAd());
    }

    private void sRewardedAd() {
        sRewardAds = MaxRewardedAd.getInstance(getResources().getString(R.string.reward_pro), Settings.this);
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

                if (adsPly < 1) {
                    if (sRewardAds.isReady()) {
                        sRewardAds.showAd();
                        adsPly++;
                    } else {
                        if (sInterAds.isReady()) sRewardAds.showAd();
                        adsPly += 2;
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
                sInterstitialAdLoad();

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        };
        sRewardAds.setListener(maxRewardedAdListener);
        sRewardAds.loadAd();
    }

    private void sInterstitialAdLoad() {
        sInterAds = new MaxInterstitialAd(getResources().getString(R.string.settings_interest), Settings.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                if (adsPly < 1) {
                    if (sInterAds.isReady()) {
                        sInterAds.showAd();
                        adsPly++;
                    } else {
                        if (sRewardAds.isReady()) sRewardAds.showAd();
                        adsPly += 2;
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
        startActivity(new Intent(Settings.this, UserProfile.class));
        overridePendingTransition(0, 0);
        finishAffinity();
    }
}
