package com.leadmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectWorkGroup_Activity extends AppCompatActivity
{
    SoapPrimitive resultString;
    String dbId,Logon;
    String WgId,DBid,WGId;
    ArrayList list;
    String wgIdIntent;
    Spinner spWGlist;
    int check=0;
    ProgressDialog pd;
    int count=0;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_select_work_group);

        manager=new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(SelectWorkGroup_Activity.this));
        }

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.wg_display);
        toolbar.setBackgroundColor(manager.getColor(SelectWorkGroup_Activity.this));

        if(manager.getFontStyle(this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.wg_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.wg_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.wg_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.wg_display), mainFont);
        }


//        Intent i=getIntent();
        dbId=manager.getDB(SelectWorkGroup_Activity.this);
        Logon=manager.getUserId(SelectWorkGroup_Activity.this);

//        Toast.makeText(getApplicationContext(),""+manager.getUserId(SelectWorkGroup_Activity.this),Toast.LENGTH_LONG).show();

        spWGlist=(Spinner)findViewById(R.id.selectWGSpinner);

        spWGlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                HashMap map = (HashMap) parent.getItemAtPosition(position);
                wgIdIntent = (String) map.get("WgId");

                if(wgIdIntent.equals("0"))
                {

                }
                else
                {
                    manager.setWG(SelectWorkGroup_Activity.this,wgIdIntent);

                    GetCustomLabels getCustomLabels=new GetCustomLabels();
                    getCustomLabels.execute();

                    GetLoginPri getLoginPri=new GetLoginPri();
                    getLoginPri.execute();
//                    Intent i=new Intent(SelectWorkGroup_Activity.this,MainActivity.class);
////                    i.putExtra("id",dbId);
////                    i.putExtra("WorkGroupId",wgIdIntent);
////                    i.putExtra("Logon", Logon);
//                    startActivity(i);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        GetWorkGroup getDatabase = new GetWorkGroup();
        getDatabase.execute();
    }

    private class GetWorkGroup extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd=new ProgressDialog(SelectWorkGroup_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            getWorkGroupProcess();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String[] from = { "WgName","WgId" };
            int[] to = { R.id.tname};
            SimpleAdapter adapter=new SimpleAdapter(SelectWorkGroup_Activity.this,list,R.layout.single,from,to);
            spWGlist.setAdapter(adapter);
            pd.cancel();
        }
    }

    private void getWorkGroupProcess()
    {
        String SOAP_ACTION = "LMServiceNamespace/GetWorkgroups";
        String METHOD_NAME = "GetWorkgroups";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try
        {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("logon_id", Logon);
            Request.addProperty("database_id", dbId);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,60000);

            transport.call(SOAP_ACTION, soapEnvelope);
            list=new ArrayList();
            // list.add("-Selecte Database-");

            HashMap map0=new HashMap();

            map0.put("WgId","0");
            map0.put("WgName", "---Select Workgroup---");

            list.add(map0);

            SoapObject resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
            // resultString = (SoapPrimitive) soapEnvelope.getResponse();
            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
            {
                SoapObject TypeDataBaseData = (SoapObject) resultRequestSOAP .getProperty(i);
                String WgName = TypeDataBaseData.getProperty("WorkgroupName").toString();
                WgId = TypeDataBaseData.getProperty("WorkgroupID").toString();

                HashMap map=new HashMap();

                map.put("WgId",WgId);
                map.put("WgName",WgName);

                list.add(map);

                Log.e("WorkGroup==>", WgName+" :"+WgId);
            }
            Log.e("list", String.valueOf(list));



            Log.e( "Result SECOND API: " , String.valueOf(resultRequestSOAP));
            Log.e( "Result SECOND API: " , String.valueOf(resultString));
        } catch (Exception ex) {
            Log.e( "Error: " , ex.getMessage());
        }

    }


    private class GetCustomLabels extends AsyncTask<Void, Void, Void>
    {
        SoapObject resultRequestSOAP = null;
        String labelNames = "Banner - Accounts^Contact^Company^Banner - Contacts^Lead Status^Entered^Campaign^Events^Event Name^Event^Shortcuts^My Searches^Recent Items^Acct Mgr^Opportunity^Case^Sales Rep Comments/Notes^Record^Opportunity Name^Phone^Cases^Banner - Library^Quote^Sales Rep Comments/Notes^Banner - Calendar^Shortcuts^Event Done^Quote Name^Company^First Name^Last Name^Case Date Due^Subject^Case Status^Case Priority^Case Owner^Key Contact^Lead_Source^Event Start Time^Opportunity Sales Stage^List Source^Sales Rep Comments/Notes^Total Opportunity Value^Contact Information^Profile Summary^Profile Assignments^Special Interest^Special Interest II^Special Interest III^Sales Rep Comments/Notes^Opportunity Details^Product Details^Case Additional Information^Case Details";
        SoapPrimitive resultString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            pd=new ProgressDialog(SelectWorkGroup_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetCustomLabel";
            String METHOD_NAME = "GetCustomLabel";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("LogonID", manager.getUserId(SelectWorkGroup_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(SelectWorkGroup_Activity.this));
                Request.addProperty("LabelName", labelNames);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String key = TypeEventData.getProperty("Key").toString();
                    String value = TypeEventData.getProperty("Value").toString();

                    Log.e(i+" : Key,Value ==> ",key+","+value);

                    if(value.equals("anyType{}"))
                    {

                    }else
                    {
                        manager.setCustomLabel(SelectWorkGroup_Activity.this, key, value);
                    }

                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            }
            catch (Exception ex)
            {

            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
//            Intent i=new Intent(SelectWorkGroup_Activity.this,MainActivity.class);
//                    i.putExtra("id",dbId);
//                    i.putExtra("WorkGroupId",wgIdIntent);
//                    i.putExtra("Logon", Logon);
//            startActivity(i);
//            Log.d("Post","Execute");
        }
    }

    private class GetLoginPri extends AsyncTask<Void, Void, Void>
    {
        SoapObject resultRequestSOAP = null;
        SoapPrimitive resultString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            pd=new ProgressDialog(SelectWorkGroup_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetLogonPrivileges";
            String METHOD_NAME = "GetLogonPrivileges";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("LogonID", manager.getUserId(SelectWorkGroup_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(SelectWorkGroup_Activity.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                String reviewAndTakeAct = resultRequestSOAP.getProperty("ReviewAndTakeAct").toString();
                String viewSalesProgress = resultRequestSOAP.getProperty("ViewSalesProgress").toString();
                String fullEdit = resultRequestSOAP.getProperty("FullEdit").toString();
                String assignCallBack = resultRequestSOAP.getProperty("AssignCallBack").toString();
                String accountLink = resultRequestSOAP.getProperty("AccountLink").toString();
                String contactLink = resultRequestSOAP.getProperty("ContactLink").toString();
                String recordOnMap = resultRequestSOAP.getProperty("RecordOnMap").toString();
                String defaultPage = resultRequestSOAP.getProperty("DefaultPage").toString();
                String showQuote = resultRequestSOAP.getProperty("ShowQuote").toString();
                String iso = resultRequestSOAP.getProperty("ISO_Code").toString();
                String myCal = resultRequestSOAP.getProperty("MyCalendar").toString();
                String groupCal = resultRequestSOAP.getProperty("GroupCalendar").toString();
                String email = resultRequestSOAP.getProperty("Email").toString();


                manager.setLoginPriv(SelectWorkGroup_Activity.this, "ReviewAndTakeAct", reviewAndTakeAct);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "ViewSalesProgress", viewSalesProgress);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "FullEdit", fullEdit);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "AssignCallBack", assignCallBack);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "AccountLink", accountLink);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "ContactLink", contactLink);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "RecordOnMap", recordOnMap);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "DefaultPage", defaultPage);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "ShowQuote", showQuote);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "ISO_Code", iso);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "MyCalendar", myCal);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "GroupCalendar", groupCal);
                manager.setLoginPriv(SelectWorkGroup_Activity.this, "Email", email);

//                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//                {
//                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
//
//                    String reviewAndTakeAct = TypeEventData.getProperty("ReviewAndTakeAct").toString();
//                    String viewSalesProgress = TypeEventData.getProperty("ViewSalesProgress").toString();
//                    String fullEdit = TypeEventData.getProperty("FullEdit").toString();
//                    String assignCallBack = TypeEventData.getProperty("AssignCallBack").toString();
//                    String accountLink = TypeEventData.getProperty("AccountLink").toString();
//                    String recordOnMap = TypeEventData.getProperty("RecordOnMap").toString();
//                    String defaultPage = TypeEventData.getProperty("DefaultPage").toString();
//
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "reviewAndTakeAct", reviewAndTakeAct);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "viewSalesProgress", viewSalesProgress);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "fullEdit", fullEdit);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "assignCallBack", assignCallBack);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "accountLink", accountLink);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "recordOnMap", recordOnMap);
//                    manager.setLoginPriv(SelectWorkGroup_Activity.this, "defaultPage", defaultPage);
//
//
//                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            }
            catch (Exception ex)
            {

            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            Intent i= new Intent(SelectWorkGroup_Activity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("id",DBid);
//                    i.putExtra("WorkGroupId",WGId);
//                    i.putExtra("Logon", Logon);
            startActivity(i);
            finish();
            Log.d("Post","Execute");
        }
    }
}
