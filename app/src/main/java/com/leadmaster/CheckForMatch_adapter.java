package com.leadmaster;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 20/07/2016.
 */
public class CheckForMatch_adapter extends BaseAdapter {
    SessionManager manager = new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    public CheckForMatch_adapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a = (FragmentActivity) context;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public CheckForMatch_Data getItem(int position) {
        return (CheckForMatch_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_checkformatch_search, parent, false);
            mViewHolder = new MyViewHolder(convertView);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.cfmAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.cfmAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.cfmAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.cfmAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_raw_checkForMatch), iconFont);

        final CheckForMatch_Data currentListData = getItem(position);
        Log.e("adapterPos", "" + position);

        if(manager.getCustomLabel(context,"Banner - Accounts").equals("") || manager.getLoginPriv(context,"AccountLink").equals("false"))
        {
            mViewHolder.tvCompanyDisplay.setVisibility(View.GONE);
            mViewHolder.tvCompany.setVisibility(View.GONE);
        }
        else
        {
            Log.e("Slider","Account Add");
            mViewHolder.tvCompanyDisplay.setVisibility(View.VISIBLE);
            mViewHolder.tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.tvCompanyDisplay.setText(manager.getCustomLabel(context,"Banner - Accounts")+" : ");
            mViewHolder.tvCompany.setText(currentListData.getCompany());
        }

        if(manager.getCustomLabel(context,"Banner - Contacts").equals("") || manager.getLoginPriv(context,"ContactLink").equals("false"))
        {
            mViewHolder.tvNameDisplay.setVisibility(View.GONE);
            mViewHolder.tvName.setVisibility(View.GONE);
        }
        else
        {
            Log.e("Slider","Contact Add");
            manager.getCustomLabel(context,"Banner - Contacts");
            mViewHolder.tvName.setVisibility(View.VISIBLE);
            mViewHolder.tvNameDisplay.setVisibility(View.VISIBLE);
            mViewHolder.tvNameDisplay.setText(manager.getCustomLabel(context,"Banner - Contacts")+" : ");
            mViewHolder.tvName.setText(currentListData.getFirstName().toString() + " " + currentListData.getLastName().toString());
        }

        mViewHolder.tvPhone.setText(currentListData.getPhone());

        if (currentListData.getPhone().equals("none")) {
//            mViewHolder.call_CheckForMatch_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.call_CheckForMatch_btn.setVisibility(View.GONE);
        } else {
            mViewHolder.call_CheckForMatch_btn.setVisibility(View.VISIBLE);
            mViewHolder.call_CheckForMatch_btn.setTextColor(Color.parseColor("#0051A1"));
        }

        mViewHolder.select_CheckForMatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmSalesProgress.asp?RECDNO=" + currentListData.getRecordId();
                Log.d("MainURL", url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","checkForMatch");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(context, CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "checkForMatch");

                context.startActivity(intent);


            }
        });

        mViewHolder.call_CheckForMatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (currentListData.getPhone().equals("none"))
                {
                    Toast.makeText(context, "Contact not available", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentListData.getPhone().toString()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                                    Manifest.permission.CALL_PHONE)) {
                                Log.e("PermissionResult : ", "If shouldShowRequestPermissionRationale");
                                ActivityCompat.requestPermissions(a,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1);
                                // Show an expanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {
                                Log.e("PermissionResult : ", "Else shouldShowRequestPermissionRationale");

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions(a,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }


//                        Toast.makeText(context,"Please check call permission in your device settings",Toast.LENGTH_LONG).show();
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
//                        return;
                        }
                        else
                        {
                            a.startActivity(intent);
                        }
                    }

                }

            }
        });

        mViewHolder.website_CheckForMatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String url="https://www.google.com/search?q="+currentListData.getCompany();
               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","checkForMatch");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","checkForMatch");

                context.startActivity(intent);

            }
        });

        return convertView;
    }

    private class MyViewHolder
    {
        TextView tvCompany,tvName,tvPhone;
        TextView tvCompanyDisplay,tvNameDisplay;

        Button website_CheckForMatch_btn,call_CheckForMatch_btn,select_CheckForMatch_btn;

        public MyViewHolder(View item)
        {
            tvCompany = (TextView) item.findViewById(R.id.company_name_checkForMatch);
            tvCompanyDisplay = (TextView) item.findViewById(R.id.checkformatch_account);

            tvName = (TextView) item.findViewById(R.id.firstNlastName_checkForMatch);
            tvNameDisplay = (TextView) item.findViewById(R.id.checkformatch_contact);

            tvPhone = (TextView) item.findViewById(R.id.phone_checkForMatch);

            select_CheckForMatch_btn=(Button)item.findViewById(R.id.select_btn_CheckForMatch);
            call_CheckForMatch_btn=(Button)item.findViewById(R.id.call_btn_CheckForMatch);
            website_CheckForMatch_btn=(Button)item.findViewById(R.id.url_btn_CheckForMatch);
        }
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

}
