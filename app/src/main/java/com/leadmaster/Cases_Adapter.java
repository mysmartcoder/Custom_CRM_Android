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
 * Created by User on 08/07/2016.
 */
public class Cases_Adapter extends BaseAdapter {

    SessionManager manager = new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    int i, j;

    public Cases_Adapter(Context context, ArrayList myList, int i, int j) {
        Log.e("called", "call1");
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a = (FragmentActivity) context;

        this.i = i;
        this.j = j;
        PrepareDrawer.allSet(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Cases_Data getItem(int position) {
        return (Cases_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;
        Log.e("called", "call1");
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_case, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.caseAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.caseAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.caseAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.caseAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_Case), iconFont);

        final Cases_Data currentListData = getItem(position);

        Log.e("adapterPos", "" + position);
        mViewHolder.tvEntered.setText(currentListData.getEntered());
        mViewHolder.tvDateDue.setText(currentListData.getDatedue());

        mViewHolder.tvCompany.setPaintFlags(mViewHolder.tvCompany.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvCompany.setText(currentListData.getCompany());

        mViewHolder.tvCaseName.setPaintFlags(mViewHolder.tvCaseName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvCaseName.setText(currentListData.getSubject());

        mViewHolder.tvCaseStatus.setText(currentListData.getStatus());
        mViewHolder.tvCasePriority.setText(currentListData.getPriority());
        mViewHolder.tvCaseOwner.setText(currentListData.getOwner());
        mViewHolder.tvPhone.setText(currentListData.getPhone());


        if (manager.getCustomLabel(context, "Entered").equals("")) {
            mViewHolder.case_tvEntered.setVisibility(View.GONE);
            mViewHolder.tvEntered.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.tvEntered.setVisibility(View.VISIBLE);
            mViewHolder.case_tvEntered.setText(manager.getCustomLabel(context, "Entered"));
        }

        if (manager.getCustomLabel(context, "DateDue").equals("")) {
            mViewHolder.case_tvDateDue.setVisibility(View.GONE);
            mViewHolder.tvDateDue.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvDateDue.setVisibility(View.VISIBLE);
            mViewHolder.tvDateDue.setVisibility(View.VISIBLE);
            mViewHolder.case_tvDateDue.setText(manager.getCustomLabel(context, "DateDue"));
        }

        if (manager.getCustomLabel(context, "Company").equals("")) {
            mViewHolder.case_tvCompany.setVisibility(View.GONE);
            mViewHolder.tvCompany.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.case_tvCompany.setText(manager.getCustomLabel(context, "Company"));
        }

        if (manager.getCustomLabel(context, "Subject").equals("")) {
            mViewHolder.case_tvCaseName.setVisibility(View.GONE);
            mViewHolder.tvCaseName.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvCaseName.setVisibility(View.VISIBLE);
            mViewHolder.tvCaseName.setVisibility(View.VISIBLE);
            mViewHolder.case_tvCaseName.setText(manager.getCustomLabel(context, "Subject"));
        }

        if (manager.getCustomLabel(context, "Status").equals("")) {
            mViewHolder.case_tvCaseStatus.setVisibility(View.GONE);
            mViewHolder.tvCaseStatus.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvCaseStatus.setVisibility(View.VISIBLE);
            mViewHolder.tvCaseStatus.setVisibility(View.VISIBLE);
            mViewHolder.case_tvCaseStatus.setText(manager.getCustomLabel(context, "Status"));
        }

        if (manager.getCustomLabel(context, "Priority").equals("")) {
            mViewHolder.case_tvCasePriority.setVisibility(View.GONE);
            mViewHolder.tvCasePriority.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvCasePriority.setVisibility(View.VISIBLE);
            mViewHolder.tvCasePriority.setVisibility(View.VISIBLE);
            mViewHolder.case_tvCasePriority.setText(manager.getCustomLabel(context, "Priority"));
        }

        if (manager.getCustomLabel(context, "Case Owner").equals("")) {
            mViewHolder.case_tvCaseOwner.setVisibility(View.GONE);
            mViewHolder.tvCaseOwner.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvCaseOwner.setVisibility(View.VISIBLE);
            mViewHolder.tvCaseOwner.setVisibility(View.VISIBLE);
            mViewHolder.case_tvCaseOwner.setText(manager.getCustomLabel(context, "Case Owner"));
        }

        if (manager.getCustomLabel(context, "PRI_PHONE").equals("")) {
            mViewHolder.case_tvPhone.setVisibility(View.GONE);
            mViewHolder.tvPhone.setVisibility(View.GONE);
        } else {
            mViewHolder.case_tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.tvPhone.setVisibility(View.VISIBLE);
            mViewHolder.case_tvPhone.setText(manager.getCustomLabel(context, "PRI_PHONE"));
        }


        if (currentListData.getPhone().equals("none")) {
//            mViewHolder.call_Acc_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.call_Acc_btn.setVisibility(View.GONE);
        } else {
            mViewHolder.call_Acc_btn.setVisibility(View.VISIBLE);
            mViewHolder.call_Acc_btn.setTextColor(Color.parseColor("#0051A1"));
        }

        if (currentListData.getLatitude().equals("0") || currentListData.getLongitude().equals("0")) {
//            mViewHolder.map_Acc_btn.setTextColor(Color.parseColor("#424242"));
            mViewHolder.map_Acc_btn.setVisibility(View.GONE);
        } else {
            mViewHolder.map_Acc_btn.setVisibility(View.VISIBLE);
            mViewHolder.map_Acc_btn.setTextColor(Color.parseColor("#d50000"));
        }


        mViewHolder.select_Acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAddCase.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(context) + "&appkeyword= &pagetype=case&id=" + currentListData.getCaseId();

                EditText searchQuery = (EditText) a.findViewById(R.id.cases_search_edit);
                Log.d("MainURL", url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","case");
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
                intent.putExtra("frg", "case");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchQuery.getText().toString());
                context.startActivity(intent);


            }
        });

        mViewHolder.assign_Acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + manager.getWG(context) + "&RECDNO=" + currentListData.getRecordId() + "&appkeyword=&pagetype=case";

                EditText searchQuery = (EditText) a.findViewById(R.id.cases_search_edit);
                Log.d("MainURL", url);

                /*Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","case");
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
                intent.putExtra("frg", "case");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.delete_Acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this case?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                                DeleteCase searchTotalCallBackEvent = new DeleteCase(currentListData.getCaseId());
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


        mViewHolder.map_Acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentListData.getLatitude().equals("0") || currentListData.getLongitude().equals("0")) {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_LONG).show();

                } else {
                    Log.e("Lat,Long", currentListData.getLatitude() + "," + currentListData.getLongitude());
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?saddr=0,0&daddr="+currentListData.getLat()+","+currentListData.getLang()+""));
                            Uri.parse("http://maps.google.com/maps?daddr=" + currentListData.getLatitude() + "," + currentListData.getLongitude() + " (" + currentListData.getCompany() + ")"));
//                    Uri.parse("http://maps.google.com/maps?q=loc:" + currentListData.getLat() + "," + currentListData.getLang() + " (" + currentListData.getCompany() + ")"));
                    a.startActivity(intent);
                }
            }
        });


        mViewHolder.call_Acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentListData.getPhone().equals("none")) {
                    Toast.makeText(context, "Contact not available", Toast.LENGTH_LONG).show();
                } else {
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
        TextView tvEntered, tvDateDue,tvCompany,tvCaseName,tvCaseStatus, tvCasePriority, tvCaseOwner, tvPhone;

        TextView case_tvEntered, case_tvDateDue,case_tvCompany,case_tvCaseName,case_tvCaseStatus, case_tvCasePriority, case_tvCaseOwner, case_tvPhone;

        Button assign_Acc_btn,delete_Acc_btn,map_Acc_btn,call_Acc_btn,select_Acc_btn;
        public MyViewHolder(View item)
        {
            tvEntered = (TextView) item.findViewById(R.id.Case_entered);
            tvDateDue = (TextView) item.findViewById(R.id.Case_datedue);
            tvCompany = (TextView) item.findViewById(R.id.Case_company);
            tvCaseName=(TextView) item.findViewById(R.id.case_casename);
            tvCaseStatus=(TextView)item.findViewById(R.id.case_status);
            tvCasePriority=(TextView)item.findViewById(R.id.case_priority);
            tvCaseOwner=(TextView)item.findViewById(R.id.case_owner);
            tvPhone=(TextView)item.findViewById(R.id.case_phone);

            case_tvEntered = (TextView) item.findViewById(R.id.case_tventered);
            case_tvDateDue = (TextView) item.findViewById(R.id.case_tvdatedue);
            case_tvCompany = (TextView) item.findViewById(R.id.case_tvcompany);
            case_tvCaseName=(TextView) item.findViewById(R.id.case_tvname);
            case_tvCaseStatus=(TextView)item.findViewById(R.id.case_tvstatus);
            case_tvCasePriority=(TextView)item.findViewById(R.id.case_tvprio);
            case_tvCaseOwner=(TextView)item.findViewById(R.id.case_tvowner);
            case_tvPhone=(TextView)item.findViewById(R.id.case_tvphone);

            assign_Acc_btn=(Button)item.findViewById(R.id.assign_btn_Case);
            delete_Acc_btn=(Button)item.findViewById(R.id.delete_btn_Case);
            map_Acc_btn=(Button)item.findViewById(R.id.map_btn_Case);
            call_Acc_btn=(Button)item.findViewById(R.id.call_btn_Case);
            select_Acc_btn=(Button)item.findViewById(R.id.select_btn_Case);

        }
    }

    private class DeleteCase extends AsyncTask<Void, Void, Void>
    {

        String caseId;
        public DeleteCase(String caseId)
        {
            this.caseId=caseId;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pd=new ProgressDialog(context);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/DeleteCase";
            String METHOD_NAME = "DeleteCase";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CaseID", caseId);
                Request.addProperty("LogonID", manager.getUserId(context));
                Request.addProperty("CompanyID", manager.getWG(context));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteCaseResult").toString();

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
                Toast.makeText(context,"Case delete successfully",Toast.LENGTH_LONG).show();

                Fragment caseFragment = new Cases_Activity();

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,caseFragment,null);
                fragmentTransaction.commit();

            }
            else
            {
                Toast.makeText(context,"Operation Failed",Toast.LENGTH_LONG).show();
            }

        }
    }

}
