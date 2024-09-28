package com.trueearning.pro;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;

import com.airbnb.lottie.LottieAnimationView;

@Keep
public class FirstScreen extends MainActivity {

    private static final String FILE_NAME = "myFile";

    Animation top_anim, bottom_anim;
    ImageView imageView;
    TextView logo, slogan;
    LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        lottie = findViewById(R.id.loadingLottie);
        top_anim = AnimationUtils.loadAnimation(FirstScreen.this, R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(FirstScreen.this, R.anim.bottom_animation);
        logo = findViewById(R.id.splashText);
        slogan = findViewById(R.id.slogan);
        imageView = findViewById(R.id.splashImage);
        logo.setAnimation(bottom_anim);
        slogan.setAnimation(top_anim);


        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(() -> {
            SharedPreferences OnBoardingScreen = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

            boolean isFirstTime = OnBoardingScreen.getBoolean("firstTime", true);
            boolean isSecondTime = OnBoardingScreen.getBoolean("secondTime", true);
            if (isFirstTime) {
                SharedPreferences.Editor editor = OnBoardingScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();
                Intent intent = new Intent(FirstScreen.this, MainActivity.class);
                startActivity(intent);
            } else if(isSecondTime){
                Intent i = new Intent(FirstScreen.this, Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(imageView, "logo_image");
                pairs[1] = new Pair<View, String>(logo, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstScreen.this, pairs);
                startActivity(i, options.toBundle());

            }else{
                Intent i = new Intent(FirstScreen.this, Home.class);
                startActivity(i);
            }
            finishAffinity();

        }, SPLASH_TIME_OUT);
    }
}
