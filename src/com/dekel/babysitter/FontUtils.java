package com.dekel.babysitter;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {

    public static Typeface getTypefaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Alef-Bold.ttf");
    }

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Alef-Regular.ttf");
    }
}