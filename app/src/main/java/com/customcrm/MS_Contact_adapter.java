package com.customCRM;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 17/10/2016.
 */
public class MS_Contact_adapter extends BaseAdapter {
    SessionManager manager = new SessionManager();
    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;


    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    int i, j;

    public MS_Contact_adapter(Context context, ArrayList myList, int i, int j) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a = (FragmentActivity) context;
        this.i = i;
        this.j = j;

//        PrepareDrawer.allSet(context);
    }

    private SparseBooleanArray mSelectedItemsIds = new SparseBooleanArray();
    ;

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    // Remove selection after unchecked
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    // Item checked on selection
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    // Get number of selected item
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MS_Contact_Data getItem(int position) {
        return (MS_Contact_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_mscontacts, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msconAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msconAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msconAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msconAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_mscontact), iconFont);

        final MS_Contact_Data currentListData = getItem(position);

        mViewHolder.tvEntered.setText(currentListData.getEntered());

        mViewHolder.tvContact.setPaintFlags(mViewHolder.tvContact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvContact.setText(currentListData.getCont());

        mViewHolder.tvAcc.setPaintFlags(mViewHolder.tvAcc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvAcc.setText(currentListData.getAcc());

        mViewHolder.tvLead.setText(currentListData.getLead());
        mViewHolder.tvCampaign.setText(currentListData.getCampaign());
        mViewHolder.tvAccMgr.setText(currentListData.getAcc_mgr());
        mViewHolder.tvPhone.setText(currentListData.getPhone());
        mViewHolder.tvRecord.setText(currentListData.getRecordId());


        if (manager.getCustomLabel(context, "Entered").equals("")) {
            mViewHolder.con_tvEntered.setVisibility(View.GONE);
            mViewHolder.tvEntered.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.con_tvEntered.setText(manager.getCustomLabel(context, "Entered"));
        }

        if (manager.getCustomLabel(context, "Key Contact").equals("")) {
            mViewHolder.con_tvContact.setVisibility(View.GONE);
            mViewHolder.tvContact.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvContact.setVisibility(View.VISIBLE);
            mViewHolder.tvContact.setVisibility(View.VISIBLE);
            mViewHolder.con_tvContact.setText(manager.getCustomLabel(context, "Key Contact"));
        }

        if (manager.getCustomLabel(context, "Company").equals("")) {
            mViewHolder.con_tvAcc.setVisibility(View.GONE);
            mViewHolder.tvAcc.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvAcc.setVisibility(View.VISIBLE);
            mViewHolder.tvAcc.setVisibility(View.VISIBLE);
            mViewHolder.con_tvAcc.setText(manager.getCustomLabel(context, "Company"));
        }

        if (manager.getCustomLabel(context, "Lead_Status").equals("")) {
            mViewHolder.con_tvLead.setVisibility(View.GONE);
            mViewHolder.tvLead.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvLead.setVisibility(View.VISIBLE);
            mViewHolder.tvLead.setVisibility(View.VISIBLE);
            mViewHolder.con_tvLead.setText(manager.getCustomLabel(context, "Lead_Status"));
        }

        if (manager.getCustomLabel(context, "Mkt_Program_ID").equals("")) {
            mViewHolder.con_tvCampaign.setVisibility(View.GONE);
            mViewHolder.tvCampaign.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvCampaign.setVisibility(View.VISIBLE);
            mViewHolder.tvCampaign.setVisibility(View.VISIBLE);
            mViewHolder.con_tvCampaign.setText(manager.getCustomLabel(context, "Mkt_Program_ID"));
        }

        if (manager.getCustomLabel(context, "EE_REP").equals("")) {
            mViewHolder.con_tvAccMgr.setVisibility(View.GONE);
            mViewHolder.tvAccMgr.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvAccMgr.setVisibility(View.VISIBLE);
            mViewHolder.tvAccMgr.setVisibility(View.VISIBLE);
            mViewHolder.con_tvAccMgr.setText(manager.getCustomLabel(context, "EE_REP"));
        }

        if (manager.getCustomLabel(context, "PRI_PHONE").equals("")) {
            mViewHolder.con_tvPhone.setVisibility(View.GONE);
            mViewHolder.tvPhone.setVisibility(View.GONE);
        } else {
            mViewHolder.con_tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.con_tvPhone.setText(manager.getCustomLabel(context, "PRI_PHONE"));
        }


        if (currentListData.getPhone().equals("none")) {
//            mViewHolder.call_cont_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.call_cont_btn.setVisibility(View.GONE);
        } else {
            mViewHolder.call_cont_btn.setVisibility(View.VISIBLE);
            mViewHolder.call_cont_btn.setTextColor(Color.parseColor("#0051A1"));
        }

        if (currentListData.getLang().equals("0") || currentListData.getLang().equals("0")) {
//            mViewHolder.map_cont_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.map_cont_btn.setVisibility(View.GONE);
        } else {
            mViewHolder.map_cont_btn.setVisibility(View.VISIBLE);
            mViewHolder.map_cont_btn.setTextColor(Color.parseColor("#d50000"));
        }

        mViewHolder.select_cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();
                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=" + manager.getLoginPriv(context, "DefaultPage") + "&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=contact&contactid=" + currentListData.getContactId();
//                url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + companyId + "&RECDNO=" + recdNo + "&appkeyword=&pagetype=contact";
//                EditText searchQuery = (EditText) a.findViewById(R.id.contact_search_edit);
                Log.d("MainURL", url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","contact");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchQuery.getText().toString());

                Log.e("i,j,search",i+","+j+","+searchQuery.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context, CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "contact");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
//                intent.putExtra("searchQuery", searchQuery.getText().toString());
                context.startActivity(intent);


            }
        });

        mViewHolder.assign_cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + manager.getWG(context) + "&RECDNO=" + currentListData.getRecordId() + "&appkeyword=" + "" + "&pagetype=contact";
//                        manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + manager.getWG(context) + "&RECDNO=" + currentListData.getRecordId() + "&appkeyword=" + "" + "&pagetype=contact";
//                EditText searchQuery = (EditText) a.findViewById(R.id.contact_search_edit);
                Log.d("MainURL", url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("frg","contact");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchQuery.getText().toString());

                Log.e("i,j,search",i+","+j+","+searchQuery.getText().toString());
                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context, CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "contact");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
//                intent.putExtra("searchQuery", searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.delete_cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String encp=encrptVal();
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";
//                Log.d("MainURL",url);

                new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                                DeleteContRecord searchTotalCallBackEvent = new DeleteContRecord(currentListData.getContactId());
                                searchTotalCallBackEvent.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context,"Canceled",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setIcon(R.drawable.delete_alert)
                        .show();

            }
        });


        mViewHolder.map_cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentListData.getLang().equals("0") || currentListData.getLang().equals("0")) {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_LONG).show();

                } else {

                    Log.e("Lat,Long", currentListData.getLat() + "," + currentListData.getLang());
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?saddr=0,0&daddr="+currentListData.getLat()+","+currentListData.getLang()+""));
                            Uri.parse("http://maps.google.com/maps?daddr=" + currentListData.getLat() + "," + currentListData.getLang() + " (" + currentListData.getCont() + ")"));
//                    Uri.parse("http://maps.google.com/maps?q=loc:" + currentListData.getLat() + "," + currentListData.getLang() + " (" + currentListData.getCompany() + ")"));
                    a.startActivity(intent);
                }
            }
        });


        mViewHolder.call_cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentListData.getPhone().equals("none"))
                {
                    Toast.makeText(context, "Contact not available", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentListData.getPhone().toString()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                                Manifest.permission.CALL_PHONE))
                        {
                            Log.e("PermissionResult : ","If shouldShowRequestPermissionRationale");
                            ActivityCompat.requestPermissions(a,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        }
                        else
                        {
                            Log.e("PermissionResult : ","Else shouldShowRequestPermissionRationale");

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(a,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
//                        Toast.makeText(context,"Please check call permission in your device settings",Toast.LENGTH_LONG).show();
//                        return;
                    }
                    else
                    {
                        a.startActivity(intent);
                    }

                }

            }
        });


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
        TextView tvEntered, tvContact, tvAcc, tvLead, tvCampaign,tvAccMgr,tvPhone,tvRecord;

        TextView con_tvEntered, con_tvContact, con_tvAcc, con_tvLead, con_tvCampaign,con_tvAccMgr,con_tvPhone,con_tvRecord;

        Button assign_cont_btn,delete_cont_btn,map_cont_btn,call_cont_btn,select_cont_btn;

        public MyViewHolder(View item)
        {
            tvEntered = (TextView) item.findViewById(R.id.mscont_entered);
            tvContact = (TextView) item.findViewById(R.id.mscont_contact);
            tvAcc = (TextView) item.findViewById(R.id.mscont_acc);
            tvLead=(TextView) item.findViewById(R.id.mscont_lead);
            tvCampaign=(TextView)item.findViewById(R.id.mscont_camp);
            tvAccMgr=(TextView) item.findViewById(R.id.mscont_act_mgr);
            tvPhone=(TextView)item.findViewById(R.id.mscont_phone);
            tvRecord=(TextView)item.findViewById(R.id.mscont_record_id);

            con_tvEntered = (TextView) item.findViewById(R.id.mscon_tventered);
            con_tvContact = (TextView) item.findViewById(R.id.mscon_tvcontact);
            con_tvAcc = (TextView) item.findViewById(R.id.mscon_tvaccount);
            con_tvLead=(TextView) item.findViewById(R.id.mscon_tvleadstatus);
            con_tvCampaign=(TextView)item.findViewById(R.id.mscon_tvcampaign);
            con_tvAccMgr=(TextView) item.findViewById(R.id.mscon_tvacctmgr);
            con_tvPhone=(TextView)item.findViewById(R.id.mscon_tvphone);


            assign_cont_btn=(Button)item.findViewById(R.id.assign_btn_MSCont);
            delete_cont_btn=(Button)item.findViewById(R.id.delete_btn_MSCont);
            map_cont_btn=(Button)item.findViewById(R.id.map_btn_MSCont);
            call_cont_btn=(Button)item.findViewById(R.id.call_btn_MSCont);
            select_cont_btn=(Button)item.findViewById(R.id.select_btn_MSCont);
        }
    }

    private class DeleteContRecord extends AsyncTask<Void, Void, Void>
    {

        String contactId;
        public DeleteContRecord(String contactId)
        {
            this.contactId=contactId;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Accounts_Activity.this);
            pd=new ProgressDialog(context);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/DeleteContacts";
            String METHOD_NAME = "DeleteContacts";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("ContactIDs", contactId);
                Request.addProperty("LogonID", manager.getUserId(context));
                Request.addProperty("CompanyID", manager.getWG(context));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteContactsResult").toString();

                Log.e("deletion", checkDeletion);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if(pd != null && pd.isShowing())
            {
                pd.dismiss();
            }
            if(checkDeletion.equals("SUCCESS"))
            {
                Toast.makeText(context,"Contact delete successfully",Toast.LENGTH_LONG).show();

                Fragment contactFragment = new Contacts_Activity();

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,contactFragment,null);
                fragmentTransaction.commit();

            }
            else
            {
                Toast.makeText(context,"Operation Failed",Toast.LENGTH_LONG).show();
            }

        }
    }

}

