package com.customCRM;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 26/11/2016.
 */
public class SyncContact_adapter extends BaseAdapter
{
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;
    SessionManager manager=new SessionManager();

    public SyncContact_adapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.context=context;
        inflater = LayoutInflater.from(this.context);
    }

    private SparseBooleanArray mSelectedItemsIds = new  SparseBooleanArray();

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
    public SyncContact_Data getItem(int position)
    {
        return (SyncContact_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyViewHolder mViewHolder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.raw_sync_contact, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final SyncContact_Data syncContact_data=getItem(position);

        mViewHolder.firstName.setText(syncContact_data.getFirstName());
        mViewHolder.lastName.setText(syncContact_data.getLastName());
        mViewHolder.email.setText(syncContact_data.getEmail());
        mViewHolder.phone.setText(syncContact_data.getPhone());
        mViewHolder.phone2.setText(syncContact_data.getPhone2());
        mViewHolder.fax.setText(syncContact_data.getFax());

        if(! syncContact_data.getFirstName().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.VISIBLE);
            mViewHolder.lastName.setVisibility(View.GONE);
            mViewHolder.email.setVisibility(View.GONE);
            mViewHolder.phone.setVisibility(View.GONE);
            mViewHolder.phone2.setVisibility(View.GONE);
            mViewHolder.fax.setVisibility(View.GONE);
        }
        else if(! syncContact_data.getLastName().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.GONE);
            mViewHolder.lastName.setVisibility(View.VISIBLE);
            mViewHolder.email.setVisibility(View.GONE);
            mViewHolder.phone.setVisibility(View.GONE);
            mViewHolder.phone2.setVisibility(View.GONE);
            mViewHolder.fax.setVisibility(View.GONE);
        }
        else if(! syncContact_data.getEmail().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.GONE);
            mViewHolder.lastName.setVisibility(View.GONE);
            mViewHolder.email.setVisibility(View.VISIBLE);
            mViewHolder.phone.setVisibility(View.GONE);
            mViewHolder.phone2.setVisibility(View.GONE);
            mViewHolder.fax.setVisibility(View.GONE);
        }
        else if(! syncContact_data.getPhone().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.GONE);
            mViewHolder.lastName.setVisibility(View.GONE);
            mViewHolder.email.setVisibility(View.GONE);
            mViewHolder.phone.setVisibility(View.VISIBLE);
            mViewHolder.phone2.setVisibility(View.GONE);
            mViewHolder.fax.setVisibility(View.GONE);
        }
        else if(! syncContact_data.getPhone2().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.GONE);
            mViewHolder.lastName.setVisibility(View.GONE);
            mViewHolder.email.setVisibility(View.GONE);
            mViewHolder.phone.setVisibility(View.GONE);
            mViewHolder.phone2.setVisibility(View.VISIBLE);
            mViewHolder.fax.setVisibility(View.GONE);
        }
        else if(! syncContact_data.getFax().equals(""))
        {
            mViewHolder.firstName.setVisibility(View.GONE);
            mViewHolder.lastName.setVisibility(View.GONE);
            mViewHolder.email.setVisibility(View.GONE);
            mViewHolder.phone.setVisibility(View.GONE);
            mViewHolder.phone2.setVisibility(View.GONE);
            mViewHolder.fax.setVisibility(View.VISIBLE);
        }

        /*mViewHolder.raw_sync_contact_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();

                bundle.putString("phonetype", syncContact_data.getPhoneType());
                bundle.putString("phone2type", syncContact_data.getPhone2Type());
                bundle.putString("emailtype", syncContact_data.getEmailType());
                bundle.putString("faxtype", syncContact_data.getFaxType());

                bundle.putString("id", syncContact_data.getContactId());

                bundle.putString("firstname", syncContact_data.getFirstName());
                bundle.putString("lastname", syncContact_data.getLastName());
                bundle.putString("phone", syncContact_data.getPhone());
                bundle.putString("phone2", syncContact_data.getPhone2());
                bundle.putString("fax", syncContact_data.getFax());
                bundle.putString("email", syncContact_data.getEmail());

                Intent i=new Intent(context,SyncContactDisplayActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });*/

        return convertView;
    }

    private class MyViewHolder
    {
        TextView firstName,lastName,email,phone,phone2,fax;
        LinearLayout raw_sync_contact_parent;

        public MyViewHolder(View item)
        {
            firstName=(TextView)item.findViewById(R.id.firstnameDisplay_syncon);
            lastName=(TextView)item.findViewById(R.id.lastnameDisplay_syncon);
            email=(TextView)item.findViewById(R.id.emailDisplay_syncon);
            phone=(TextView)item.findViewById(R.id.phoneDisplay_syncon);
            phone2=(TextView)item.findViewById(R.id.phone2Display_syncon);
            fax=(TextView)item.findViewById(R.id.faxDisplay_syncon);

            raw_sync_contact_parent=(LinearLayout)item.findViewById(R.id.raw_sync_contact_parent);
        }
    }
}
