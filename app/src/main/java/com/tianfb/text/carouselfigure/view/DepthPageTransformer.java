package com.tianfb.text.carouselfigure.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 *   ViewPager的切换动画
 */

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setAlpha(1 + position);
            view.setTranslationX(pageWidth * -position);
        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
        } else {
            view.setAlpha(0);
        }
    }
}