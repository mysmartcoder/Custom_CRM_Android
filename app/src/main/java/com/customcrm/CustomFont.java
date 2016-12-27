package com.customCRM;

/**
 * Created by User on 06/09/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 09/07/2016.
 */
public class CustomFont
{
    public static final String ROOT = "fonts/", FONT1 = ROOT + "OpenSans-Regular.ttf";
    public static final String FONT2 = ROOT + "PTN57F.ttf";
    public static final String FONT3 = ROOT + "Lora-Regular.ttf";
    public static final String FONT4 = ROOT + "DroidSerif-Regular.ttf";

    public static Typeface getTypeface(Context context, String font)
    {
        return Typeface.createFromAsset(context.getAssets(), font);
    }


    public static void markAsIconContainer(View v, Typeface typeface)
    {
        if (v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                View child = vg.getChildAt(i);
                markAsIconContainer(child,typeface);
            }
        }
        else if (v instanceof TextView)
        {
            ((TextView) v).setTypeface(typeface);
        }
    }
}

