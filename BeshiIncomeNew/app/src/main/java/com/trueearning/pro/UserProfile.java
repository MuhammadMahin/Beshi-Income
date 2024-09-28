package com.trueearning.pro;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trueearning.pro.Utility.NetworkChangeListener;

import java.util.Calendar;
import java.util.Objects;

@Keep
public class UserProfile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private static final String FILE_NAME = "myFile";
    private MaxRewardedAd maxRewardedAds;
    private MaxInterstitialAd uInterstitialAd;
    AppCompatButton pleaseLogin;


    BottomNavigationView bottomNavigationView;
    TextView profHeadName, headNameDown, rewardPoint, rewardPoint2, playingTimes, balanceName, settingsIcon, closeMe;
    LottieAnimationView lottie, lottieTwo, lottieThree;
    AppCompatButton goWithdraw, withdrawButton, goHomePage;

    LinearLayout earningPage, detailRule;
    RelativeLayout withdrawPage;
    TextInputLayout withdrawDetails;
    TextView seeDetail, playTxt;
    TextView date, address, currentTime, withdrawHistory, withdrawDate;
    RelativeLayout showPlay;
    int adsCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initAds();


        profHeadName = findViewById(R.id.profile_name);
        headNameDown = findViewById(R.id.profile_Username);
        rewardPoint2 = findViewById(R.id.reward_point2);
        rewardPoint = findViewById(R.id.reward_point);
        playingTimes = findViewById(R.id.play_time);
        goHomePage = findViewById(R.id.go_to_home);
        seeDetail = findViewById(R.id.click_details);
        settingsIcon = findViewById(R.id.settings_icon);
        balanceName = findViewById(R.id.prof_demo_balance);
        closeMe = findViewById(R.id.close_withdraw_window);
        detailRule = findViewById(R.id.detail_rules);
        lottie = findViewById(R.id.prof_balances_lottie);
        lottieTwo = findViewById(R.id.prof_balances_lottie_two);
        lottieThree = findViewById(R.id.prof_balances_lottie_three);
        showPlay = findViewById(R.id.my_play_times);
        playTxt = findViewById(R.id.my_play_times_txt);

        pleaseLogin = findViewById(R.id.please_login);
        withdrawHistory = findViewById(R.id.withdraw_history);
        withdrawDate = findViewById(R.id.withdraw_date);
        date = findViewById(R.id.join_date);
        address = findViewById(R.id.user_ip);
        currentTime = findViewById(R.id.current_time);
        withdrawDetails = findViewById(R.id.input_box);
        withdrawPage = findViewById(R.id.withdraw_page);
        goWithdraw = findViewById(R.id.go_withdraw_page);
        withdrawButton = findViewById(R.id.withdraw_coins_btn);
        earningPage = findViewById(R.id.earning_linear);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.item_profile_page);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_earning_page:
                    startActivity(new Intent(UserProfile.this, Earning.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
                case R.id.item_home_page:
                    startActivity(new Intent(UserProfile.this, Home.class));
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    return true;
            }
            return false;
        });


        settingsIcon.setOnClickListener(view -> {
            startActivity(new Intent(UserProfile.this, Settings.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });

        detailRule.setOnClickListener(view -> {
            startActivity(new Intent(UserProfile.this, Settings.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });


        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String nameFromDB = sharedPreferences.getString("nameFromDB", "");
        int coins = sharedPreferences.getInt("point", 0);
        int playing = sharedPreferences.getInt("plays", 0);
        int withdraws = sharedPreferences.getInt("withdraw", 0);
        String withdrawD = sharedPreferences.getString("withdrawDate", "");
        String userEnteredUsername = sharedPreferences.getString("userEnteredUsername", "");
        String userDate = sharedPreferences.getString("date", "");
        String userIp = sharedPreferences.getString("address", "");

        playingTimes.setText(String.valueOf(playing));

        if (!nameFromDB.isEmpty() && !userEnteredUsername.isEmpty()){
            profHeadName.setText(nameFromDB);
            headNameDown.setText(userEnteredUsername);
        }else {
            pleaseLogin.setVisibility(View.VISIBLE);
        }



        if (!userDate.isEmpty() && !userIp.isEmpty()) {
            date.setText(userDate);
            address.setText(userIp);
        }
        if (withdraws != 0){
            withdrawHistory.setText(String.valueOf("Withdraw Amount: " +withdraws));
            withdrawDate.setText(String.valueOf("Date: " +withdrawD));
        }


        pleaseLogin.setOnClickListener(v -> {
            startActivity(new Intent(UserProfile.this, Login.class));
            finishAffinity();
        });



        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int day = d + 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String date = day + "-" + month + "-" + year + "; " + hour + ":" + minute;
        currentTime.setText(date);

        goWithdraw.setOnClickListener(view -> {
            if (maxRewardedAds.isReady()) {
                maxRewardedAds.showAd();
            } else {
                if (uInterstitialAd.isReady()) uInterstitialAd.showAd();
            }
            withdrawPage.setVisibility(View.VISIBLE);
            earningPage.setVisibility(View.GONE);
            rewardPoint2.setText(String.valueOf(coins));
        });

        closeMe.setOnClickListener(view -> {
            withdrawPage.setVisibility(View.GONE);
            earningPage.setVisibility(View.VISIBLE);
            rewardPoint2.setText(String.valueOf(coins));
            rewardPoint.setText(String.valueOf(coins));
        });


        seeDetail.setOnClickListener(view -> {
            startActivity(new Intent(UserProfile.this, Settings.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });

        goHomePage.setOnClickListener(view -> {
            startActivity(new Intent(UserProfile.this, Home.class));
            overridePendingTransition(0, 0);
            finishAffinity();
        });

        withdrawButton.setOnClickListener(view -> {
            if (maxRewardedAds.isReady()) {
                maxRewardedAds.showAd();
            } else {
                if (uInterstitialAd.isReady()) uInterstitialAd.showAd();
            }

            if (validatePhoneNo()) {
                int coin = sharedPreferences.getInt("point", 0);
                int zero = 0;
                String withdrawFinalString = Objects.requireNonNull(withdrawDetails.getEditText()).getText().toString().trim();
                if (coin >= 1200) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    reference.child(userEnteredUsername).child("withdraw").setValue(withdrawFinalString);
                    reference.child(userEnteredUsername).child("points").setValue(zero);

                    Objects.requireNonNull(withdrawDetails.getEditText()).setText("Withdraw Successful");
                    withdrawButton.setEnabled(false);

                    AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Successful, আপনাকে অভিনন্দন!!! আপনার উইথড্র সফল হয়েছে!!!")
                            .setMessage("Hi, Beshi Income এর পক্ষ থেকে আপনাকে অসংখ্য ধন্যবাদ!!! এবার আপনার পেমেন্ট পাওয়ার জন্য আপনার প্রোফাইলের একটা স্ক্রিনশট সহ আমাদের Facebook page এ MESSAGE দিন!")
                            .setPositiveButton("OK, Welcome!", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                    dialog.setCancelable(false);
                    Toast.makeText(UserProfile.this, "WITHDRAW SUCCESSFUL!!", Toast.LENGTH_SHORT).show();

                    storepoint(coin, zero, date);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this).setTitle("দুঃখিত, আপনার পর্যাপ্ত কয়েন নেই!")
                            .setMessage("আপনাকে ধন্যবাদ -Beshi Income ব্যবহার করার জন্য!! উইথড্র করার জন্য আগে মিনিমাম 1200 কয়েন করুন, এইজন্য শুধুমাত্র কিছু Ads দেখতে হবে!! সেটিংস আইকনে ক্লিক করে বিস্তারিত পড়ে নিন!")
                            .setPositiveButton("বুঝতে পেরেছি", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                    dialog.setCancelable(false);
                }
            } else {
                Toast.makeText(UserProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        lottie.setOnClickListener(view -> {

            rewardPoint2.setText(String.valueOf(coins));
            lottie.setVisibility(View.INVISIBLE);
            lottie.setVisibility(View.INVISIBLE);
            balanceName.setVisibility(View.INVISIBLE);
            rewardPoint.setVisibility(View.VISIBLE);

            Toast mToast = Toast.makeText(UserProfile.this, "Your Play Times is", Toast.LENGTH_LONG);
            mToast.setText("Your Play Time is:\n" + playing);
            mToast.show();
            rewardPoint.setText(String.valueOf(coins));

            lottieTwo.setVisibility(View.VISIBLE);
            lottieThree.setVisibility(View.VISIBLE);
            playTxt.setVisibility(View.INVISIBLE);
            playingTimes.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                lottie.setVisibility(View.VISIBLE);
                balanceName.setVisibility(View.VISIBLE);
                rewardPoint.setVisibility(View.INVISIBLE);
                lottieTwo.setVisibility(View.INVISIBLE);
                lottieThree.setVisibility(View.INVISIBLE);
                playTxt.setVisibility(View.VISIBLE);
                playingTimes.setVisibility(View.INVISIBLE);
            }, 5000);
        });

        showPlay.setOnClickListener(view -> {

            Toast mToast = Toast.makeText(UserProfile.this, "Your Play Times is", Toast.LENGTH_LONG);
            mToast.setText("Your Play Time is:\n" + playing);
            mToast.show();
            rewardPoint.setText(String.valueOf(coins));

            lottieTwo.setVisibility(View.VISIBLE);
            lottieThree.setVisibility(View.VISIBLE);
            playTxt.setVisibility(View.INVISIBLE);

            lottie.setVisibility(View.INVISIBLE);
            balanceName.setVisibility(View.INVISIBLE);
            rewardPoint.setVisibility(View.VISIBLE);
            playingTimes.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                lottie.setVisibility(View.VISIBLE);
                balanceName.setVisibility(View.VISIBLE);
                lottieTwo.setVisibility(View.INVISIBLE);
                lottieThree.setVisibility(View.INVISIBLE);
                playTxt.setVisibility(View.VISIBLE);
                rewardPoint.setVisibility(View.INVISIBLE);
                playingTimes.setVisibility(View.INVISIBLE);

            }, 3000);
        });
    }

    private void storepoint(Integer coin, Integer zero, String date) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();

        int mCoin = coin;

        int mZero = zero;

        editor.putInt("withdraw", mCoin);

        editor.putInt("point", mZero);
        editor.putString("withdrawDate", date);
        editor.apply();
        withdrawHistory.setText(String.valueOf("WITHDRAW AMOUNT:" + " " + mCoin + "TAKA"));
        rewardPoint.setText(String.valueOf(mZero));
        rewardPoint2.setText(String.valueOf(mZero));
        withdrawDate.setText(String.valueOf("Withdraw Date:" + date));
    }

    private boolean validatePhoneNo() {
        String val = Objects.requireNonNull(withdrawDetails.getEditText()).getText().toString();
        String phoneValidate = "(?=\\S+$)" + "(?:\\+88|88)?(01[3-9]\\d{8})";

        if (val.isEmpty()) {
            withdrawDetails.setError("Must enter a valid phoneNumber");
            return false;
        } else if (!val.matches(phoneValidate)) {
            withdrawDetails.setError("Number is Invalid, Enter only phone number!");
            return false;
        } else {
            withdrawDetails.setError(null);
            withdrawDetails.setErrorEnabled(false);
            return true;
        }

    }


    private void initAds() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
            uRewardedAd();
            uInterstitialAdLoad();
        });
    }

    private void uRewardedAd() {
        maxRewardedAds = MaxRewardedAd.getInstance(getResources().getString(R.string.reward_pro), UserProfile.this);
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
                if (adsCount < 1) {
                    if (maxRewardedAds.isReady()) {
                        maxRewardedAds.showAd();
                        adsCount++;
                    } else {
                        if (uInterstitialAd.isReady()) uInterstitialAd.showAd();
                        adsCount += 2;
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
                maxRewardedAds.loadAd();

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        };
        maxRewardedAds.setListener(maxRewardedAdListener);
        maxRewardedAds.loadAd();
    }

    private void uInterstitialAdLoad() {
        uInterstitialAd = new MaxInterstitialAd(getResources().getString(R.string.profile_interest), UserProfile.this);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                if (adsCount < 1) {
                    if (uInterstitialAd.isReady()) {
                        uInterstitialAd.showAd();
                        adsCount++;
                    } else {
                        if (maxRewardedAds.isReady()) maxRewardedAds.showAd();
                        adsCount += 2;
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
                uInterstitialAd.loadAd();

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        };
        uInterstitialAd.setListener(adListener);
        uInterstitialAd.loadAd();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Earning.class));
        overridePendingTransition(0, 0);
        finishAffinity();
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
