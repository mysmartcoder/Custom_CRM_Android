package com.customCRM;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 15/08/2016.
 */
public class Dash_Today_adapter extends BaseAdapter
{
    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;


    public Dash_Today_adapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;
    }

    private SparseBooleanArray mSelectedItemsIds = new  SparseBooleanArray();;

    public void  toggleSelection(int position)
    {
        selectView(position, !mSelectedItemsIds.get(position));
    }
    // Remove selection after unchecked
    public void  removeSelection()
    {
        mSelectedItemsIds = new  SparseBooleanArray();
        notifyDataSetChanged();
    }

    // Item checked on selection
    public void selectView(int position, boolean value)
    {
        if (value)
            mSelectedItemsIds.put(position,  value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    // Get number of selected item
    public int  getSelectedCount()
    {
        return mSelectedItemsIds.size();
    }

    public  SparseBooleanArray getSelectedIds()
    {
        return mSelectedItemsIds;
    }



    @Override
    public int getCount()
    {
        return myList.size();
    }

    @Override
    public Dash_Today_Data getItem(int position)
    {
        return (Dash_Today_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_dash_today, parent, false);
            mViewHolder = new MyViewHolder(convertView);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.dash_today_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.dash_today_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.dash_today_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.dash_today_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.dash_today_start_time), iconFont);

        final Dash_Today_Data currentListData = getItem(position);
        Log.e("adapterPos",""+position);

        mViewHolder.tvEvent.setText(currentListData.getEvent());
//        mViewHolder.tvCompany.setText(currentListData.getCompany());
        mViewHolder.tvTime.setText(currentListData.getStarttime());

        if(currentListData.getEventName() != "none")
    {
        mViewHolder.tvCompany.setText(currentListData.getEventName());
    }
    else if(currentListData.getCompany() != "(SELECT)")
    {
        mViewHolder.tvCompany.setText(currentListData.getCompany());
    }
    else
    {
        mViewHolder.tvCompany.setText(currentListData.getEvent());
    }

//        mViewHolder.tvCompany.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                String encp=encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(context,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
////              manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";
//                EditText searchQuery=(EditText)a.findViewById(R.id.account_search_edit);
//                Log.d("MainURL",url);
//
//                Fragment webviewFragment = new Common_WebView();
//
//                Bundle bundle = new Bundle();
//
//                bundle.putString("url", url);
//                bundle.putString("frg","dash");
//
//                webviewFragment.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
//                fragmentTransaction.commit();
//
//            }
//        });

        return convertView;
    }

    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(context);
        String pass=manager.getUserPass(context);
        String dbId=manager.getDB(context);
        String wgId=manager.getWG(context);


        try
        {
            byte[] userNamedata = new byte[0];
            userNamedata = userName.getBytes("UTF-8");
            String userNameBase64 = Base64.encodeToString(userNamedata, Base64.DEFAULT);

            byte[] pass_data = new byte[0];
            pass_data = pass.getBytes("UTF-8");
            String passBase64 = Base64.encodeToString(pass_data, Base64.DEFAULT);

            byte[] DB_data = new byte[0];
            DB_data = dbId.getBytes("UTF-8");
            String dbBase64 = Base64.encodeToString(DB_data, Base64.DEFAULT);

            byte[] WG_data = new byte[0];
            WG_data = wgId.getBytes("UTF-8");
            String wgBase64 = Base64.encodeToString(WG_data, Base64.DEFAULT);

            userNameBase64 = userNameBase64.replace("==", "@");
            passBase64 = passBase64.replace("==", "@");
            wgBase64 = wgBase64.replace("==", "@");
            dbBase64 = dbBase64.replace("==", "@");

            String encrptVaule = userNameBase64.replace("=", "$") + "^" + passBase64.replace("=", "$") + "^" + wgBase64.replace("=", "$") + "^" + dbBase64.replace("=", "$");

            return encrptVaule;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        return encrptVal;
    }

    private class MyViewHolder
    {
        TextView tvCompany, tvEvent, tvTime;

        public MyViewHolder(View item)
        {
            tvEvent = (TextView) item.findViewById(R.id.dash_today_EventType);
            tvCompany = (TextView) item.findViewById(R.id.dash_today_company);
            tvTime=(TextView)item.findViewById(R.id.dash_today_StartTime);
        }
    }


    String getRecordId(int position)
    {
        final Dash_Today_Data currentListData = getItem(position);

        return currentListData.getRecordId();
    }
}
