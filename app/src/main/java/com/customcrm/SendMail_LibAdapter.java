package com.customCRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 06/12/2016.
 */
public class SendMail_LibAdapter extends BaseAdapter
{
    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    public SendMail_LibAdapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Download_Library_Data getItem(int position) {
        return (Download_Library_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_sendmail_lib, parent, false);
            mViewHolder = new MyViewHolder(convertView);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Download_Library_Data currentListData = getItem(position);
        Log.e("adapterPos",""+position);
        mViewHolder.tvLibFIle.setText(currentListData.getFileName());

        return convertView;
    }

    private class MyViewHolder
    {
        TextView tvLibFIle;

        public MyViewHolder(View item)
        {
            tvLibFIle = (TextView) item.findViewById(R.id.sendmail_lib_tv);
        }
    }
}

