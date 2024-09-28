package com.trueearning.pro;

import androidx.annotation.Keep;

@Keep
public class ViewPagerItem {

    String mainHeading, heading1, heading2, heading3, heading4;

    public ViewPagerItem(String mainHeading, String heading1, String heading2, String heading3, String heading4) {

        this.mainHeading = mainHeading;
        this.heading1 = heading1;
        this.heading2 = heading2;
        this.heading3 = heading3;
        this.heading4 = heading4;
    }
}