package com.leadmaster;

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
 * Created by User on 02/09/2016.
 */
public class MS_Records_adapter extends BaseAdapter
{

    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    int i,j;

    public MS_Records_adapter(Context context, ArrayList myList,int i,int j)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;
        this.i=i;
        this.j=j;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MS_Records_Data getItem(int position) {
        return (MS_Records_Data) myList.get(position);
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
            convertView = inflater.inflate(R.layout.raw_msrecords, parent, false);
            mViewHolder = new MyViewHolder(convertView);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msrAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msrAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msrAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.msrAdp_parent), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_MSRec), iconFont);

        final MS_Records_Data currentListData = getItem(position);
//        Log.e("adapterPos",""+position);
//        Log.e("adapterPos",""+position+","+currentListData.getEntered());
//        Log.e("adapterPos",""+position+","+currentListData.getCompany());
//        Log.e("adapterPos",""+position+","+currentListData.getLeadStatus());
//        Log.e("adapterPos",""+position+","+currentListData.getLeadSource());
//        Log.e("adapterPos",""+position+","+currentListData.getPhone());
//        Log.e("adapterPos",""+position+","+currentListData.getRecordId());
//        Log.e("adapterPos",""+position+","+currentListData.getAcc_mgr());
//        Log.e("adapterPos",""+position+","+currentListData.getCampaign());

        mViewHolder.tvEntered.setText(currentListData.getEntered());

        mViewHolder.tvCompany.setPaintFlags(mViewHolder.tvCompany.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvCompany.setText(currentListData.getCompany());

        mViewHolder.tvLeadStatus.setText(currentListData.getLeadStatus());
        mViewHolder.tvLeadSource.setText(currentListData.getLeadSource());
        mViewHolder.tvPhone.setText(currentListData.getPhone());
        mViewHolder.tvRecord.setText(currentListData.getRecordId());

        mViewHolder.tvAcct_mgr.setText(currentListData.getAcc_mgr());
        mViewHolder.tvCampign.setText(currentListData.getCampaign());

        if(manager.getCustomLabel(context,"Entered").equals(""))
        {
            mViewHolder.acc_tvEntered.setVisibility(View.GONE);
            mViewHolder.tvEntered.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvEntered.setText(manager.getCustomLabel(context,"Entered"));
        }

        if(manager.getCustomLabel(context,"Company").equals(""))
        {
            mViewHolder.acc_tvCompany.setVisibility(View.GONE);
            mViewHolder.tvCompany.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvCompany.setText(manager.getCustomLabel(context,"Company"));
        }

        if(manager.getCustomLabel(context,"Lead_Status").equals(""))
        {
            mViewHolder.acc_tvLeadStatus.setVisibility(View.GONE);
            mViewHolder.tvLeadStatus.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvLeadStatus.setVisibility(View.VISIBLE);
            mViewHolder.tvLeadStatus.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvLeadStatus.setText(manager.getCustomLabel(context,"Lead_Status"));
        }

        if(manager.getCustomLabel(context,"Lead_Source").equals(""))
        {
            mViewHolder.acc_tvLeadSource.setVisibility(View.GONE);
            mViewHolder.tvLeadSource.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvLeadSource.setVisibility(View.VISIBLE);
            mViewHolder.tvLeadSource.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvLeadSource.setText(manager.getCustomLabel(context,"Lead_Source"));
        }

        if(manager.getCustomLabel(context,"EE_REP").equals(""))
        {
            mViewHolder.acc_tvAcct_mgr.setVisibility(View.GONE);
            mViewHolder.tvAcct_mgr.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvAcct_mgr.setVisibility(View.VISIBLE);
            mViewHolder.tvAcct_mgr.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvAcct_mgr.setText(manager.getCustomLabel(context,"EE_REP"));
        }

        if(manager.getCustomLabel(context,"Mkt_Program_ID").equals(""))
        {
            mViewHolder.acc_tvCampign.setVisibility(View.GONE);
            mViewHolder.tvCampign.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvCampign.setVisibility(View.VISIBLE);
            mViewHolder.tvCampign.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvCampign.setText(manager.getCustomLabel(context,"Mkt_Program_ID"));
        }

        if(manager.getCustomLabel(context,"PRI_PHONE").equals(""))
        {
            mViewHolder.acc_tvPhone.setVisibility(View.GONE);
            mViewHolder.tvPhone.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.acc_tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.acc_tvPhone.setText(manager.getCustomLabel(context,"PRI_PHONE"));
        }



        if(currentListData.getPhone().equals("none"))
        {
//            mViewHolder.call_Acc_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.call_Acc_btn.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.call_Acc_btn.setVisibility(View.VISIBLE);
            mViewHolder.call_Acc_btn.setTextColor(Color.parseColor("#0051A1"));
        }

        if(currentListData.getLang().equals("0") || currentListData.getLang().equals("0"))
        {
//            mViewHolder.map_Acc_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.map_Acc_btn.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.map_Acc_btn.setVisibility(View.VISIBLE);
            mViewHolder.map_Acc_btn.setTextColor(Color.parseColor("#d50000"));
        }

        mViewHolder.select_Acc_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
                String url =manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(context,"DefaultPage")+"&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
//                EditText searchQuery=(EditText)a.findViewById(R.id.msrecors_search_edit);
                Log.d("MainURL",url);

                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","msrec");
//                intent.putExtra("startIndex",i);
//                intent.putExtra("endIndex",j);
//                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.assign_Acc_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";

                String url =manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + manager.getWG(context) + "&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&appkeyword=&pagetype=account";

//                EditText searchQuery=(EditText)a.findViewById(R.id.msrecors_search_edit);

                Log.d("MainURL",url);

                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","msrec");
//                intent.putExtra("startIndex",i);
//                intent.putExtra("endIndex",j);
//                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.delete_Acc_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                DeleteAccRecord searchTotalCallBackEvent=new DeleteAccRecord(currentListData.getRecordId());
                                searchTotalCallBackEvent.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        })
                        .setIcon(R.drawable.delete_alert)
                        .show();

            }
        });



        mViewHolder.map_Acc_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(currentListData.getLang().equals("0") || currentListData.getLang().equals("0"))
                {
                    Toast.makeText(context,"Location not available",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Log.e("Lat,Long",currentListData.getLat()+","+currentListData.getLang());
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?saddr=0,0&daddr="+currentListData.getLat()+","+currentListData.getLang()+""));
                            Uri.parse("http://maps.google.com/maps?daddr="+currentListData.getLat()+","+currentListData.getLang()+ " (" + currentListData.getCompany() + ")"));
//                    Uri.parse("http://maps.google.com/maps?q=loc:" + currentListData.getLat() + "," + currentListData.getLang() + " (" + currentListData.getCompany() + ")"));
                    a.startActivity(intent);
                }
            }
        });


        mViewHolder.call_Acc_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(currentListData.getPhone().equals("none"))
                {
                    Toast.makeText(context,"Contact not available",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentListData.getPhone()));

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

    private class MyViewHolder
    {
        TextView acc_tvEntered, acc_tvCompany, acc_tvLeadStatus, acc_tvLeadSource, acc_tvPhone, acc_tvRecord, acc_tvAcct_mgr, acc_tvCampign;

        TextView tvEntered, tvCompany, tvLeadStatus, tvLeadSource, tvPhone, tvRecord, tvAcct_mgr, tvCampign;

        Button assign_Acc_btn,delete_Acc_btn,map_Acc_btn,call_Acc_btn,select_Acc_btn;
        public MyViewHolder(View item)
        {
            tvEntered = (TextView) item.findViewById(R.id.MSRec_entered);
            tvCompany = (TextView) item.findViewById(R.id.MSRec_company);
            tvLeadStatus = (TextView) item.findViewById(R.id.MSRec_leadStatus);
            tvLeadSource=(TextView) item.findViewById(R.id.MSRec_leadSource);
            tvPhone=(TextView)item.findViewById(R.id.MSRec_phone);
            tvRecord=(TextView)item.findViewById(R.id.MSRecords_record_id);
            tvCampign=(TextView)item.findViewById(R.id.MSRec_campaign);
            tvAcct_mgr=(TextView)item.findViewById(R.id.MSRec_acc_mgr);


            assign_Acc_btn=(Button)item.findViewById(R.id.assign_btn_MSRec);
            delete_Acc_btn=(Button)item.findViewById(R.id.delete_btn_MSRec);
            map_Acc_btn=(Button)item.findViewById(R.id.map_btn_MSRec);
            call_Acc_btn=(Button)item.findViewById(R.id.call_btn_MSRec);
            select_Acc_btn=(Button)item.findViewById(R.id.select_btn_MSRec);

            acc_tvEntered=(TextView)item.findViewById(R.id.MSRec_tventered);
            acc_tvCompany=(TextView)item.findViewById(R.id.MSRec_tvcompany);
            acc_tvLeadStatus=(TextView)item.findViewById(R.id.MSRec_tvleadstatus);
            acc_tvLeadSource=(TextView)item.findViewById(R.id.MSRec_tvleadsource);
            acc_tvPhone=(TextView)item.findViewById(R.id.MSRec_tvphone);
            acc_tvAcct_mgr=(TextView)item.findViewById(R.id.MSRec_tvacctMgr);
            acc_tvCampign=(TextView)item.findViewById(R.id.MSRec_tvcamp);
//            acc_tvRecord=(TextView)item.findViewById(R.id.acc_tv);

        }
    }

    private class DeleteAccRecord extends AsyncTask<Void, Void, Void>
    {

        String recordId;
        public DeleteAccRecord(String recordId)
        {
            this.recordId=recordId;
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
            String SOAP_ACTION = "LMServiceNamespace/DeleteRecord";
            String METHOD_NAME = "DeleteRecord";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("RecordID", recordId);
                Request.addProperty("LogonID", manager.getUserId(context));
                Request.addProperty("CompanyID", manager.getWG(context));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteRecordResult").toString();

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
                Toast.makeText(context,"Record delete successfully",Toast.LENGTH_LONG).show();

                Fragment accountFragment = new MS_Records_Activity();

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,accountFragment,null);
                fragmentTransaction.commit();

            }
            else
            {
                Toast.makeText(context,"Operation Failed",Toast.LENGTH_LONG).show();
            }

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
