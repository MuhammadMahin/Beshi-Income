package com.trueearning.pro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

@Keep
public class MainActivity extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout indicator_layout;
    Button backbtn, nextbtn, skipbtn;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);
        slideViewPager = findViewById(R.id.view_pagerA);
        indicator_layout = findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);
        slideViewPager.setAdapter(viewPagerAdapter);


        backbtn.setOnClickListener(v -> {

            if (getitem(0) > 0) {
                slideViewPager.setCurrentItem(getitem(-1), true);
            }
        });

        nextbtn.setOnClickListener(v -> {

            if (getitem(0) < 3)
                slideViewPager.setCurrentItem(getitem(1), true);

            else {
                loginProfile();
            }
        });

        skipbtn.setOnClickListener(view -> loginProfile());

        setUpindicator(0);
        slideViewPager.addOnPageChangeListener(viewListener);

    }

    //    ONCREATE ENDS HERE
    private void loginProfile() {
        Intent i = new Intent(MainActivity.this, Login.class);
        startActivity(i);
        finishAffinity();
    }

    public void setUpindicator(int position) {

        dots = new TextView[4];
        indicator_layout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(65);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            indicator_layout.addView(dots[i]);

        }
        dots[position].setTextColor(getResources().getColor(R.color.green, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
            if (position == 0) {
                backbtn.setVisibility(View.INVISIBLE);
            } else {
                backbtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private int getitem(int i) {
        return slideViewPager.getCurrentItem() + i;
    }
}