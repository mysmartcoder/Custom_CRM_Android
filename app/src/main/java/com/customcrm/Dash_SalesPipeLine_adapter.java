package com.customCRM;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 21/09/2016.
 */
public class Dash_SalesPipeLine_adapter extends PagerAdapter
{
    String[] profit,lost,totalWon,totalLost;
    Context context;
    LayoutInflater inflater;
    FragmentActivity a;

    SessionManager manager=new SessionManager();

    public Dash_SalesPipeLine_adapter(Activity mActivity, String[] profit, String[] lost, String[] totalWon, String[] totalLost)
    {
        context=mActivity;
        a= (FragmentActivity) mActivity;
        this.profit=profit;
        this.lost=lost;
        this.totalWon=totalWon;
        this.totalLost=totalLost;
    }

    @Override
    public int getCount()
    {
        return profit.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        TextView profit_tv,lost_tv,totalWOn_tv,totalLost_tv;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.raw_dash_salespipe, container, false);

        profit_tv= (TextView) itemView.findViewById(R.id.profit_text);
        lost_tv=(TextView)itemView.findViewById(R.id.loss_text);
        totalWOn_tv=(TextView)itemView.findViewById(R.id.totalwon_text);
        totalLost_tv=(TextView)itemView.findViewById(R.id.totallost_text);

//        Toast.makeText(context,""+position,Toast.LENGTH_LONG).show();

        profit_tv.setText(profit[position]);
        lost_tv.setText(lost[position]);
//        totalWOn_tv.setText(totalWon[position]);
        totalWOn_tv.setText(Html.fromHtml(totalWon[position]));
        totalLost_tv.setText(Html.fromHtml(totalLost[position]));

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_1), mainFont);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_2), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_1), mainFont);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_2), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_1), mainFont);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_2), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_1), mainFont);
            CustomFont.markAsIconContainer(itemView.findViewById(R.id.custom_salespipe_2), mainFont);
        }

        if(position==0)
        {
            profit_tv.setTextColor(Color.BLACK);

            lost_tv.setTextColor(Color.BLACK);
        }


        ((ViewPager)container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager)container).removeView((LinearLayout)object);
    }
}