package com.customCRM;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectDB_Activity extends AppCompatActivity
{
    SoapPrimitive resultString;
    String Logon;
    String DbId,DBid,WGId;
    ArrayList list;
    Spinner spDBlist;
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

        setContentView(R.layout.activity_select_db);

        manager=new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(SelectDB_Activity.this));
        }

//        Intent i=getIntent();
//        Logon=i.getStringExtra("response");
        Logon=manager.getUserId(SelectDB_Activity.this);

        spDBlist=(Spinner)findViewById(R.id.selectDBSpinner);

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.db_display);
        toolbar.setBackgroundColor(manager.getColor(SelectDB_Activity.this));

        if(manager.getFontStyle(this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.db_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.db_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.db_display), mainFont);
        }
        else if(manager.getFontStyle(this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.db_display), mainFont);
        }

        spDBlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                HashMap map = (HashMap) parent.getItemAtPosition(position);
                DBid = (String) map.get("DbId");

                GetWorkgroup getWorkgroup=new GetWorkgroup();
                getWorkgroup.execute();
                Log.e("Before","B");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetDatabase getDatabase = new GetDatabase();
        getDatabase.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pd.cancel();
    }

    private class GetDatabase extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd=new ProgressDialog(SelectDB_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            getDatabaseProcess();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            String[] from = { "DbName","DbId" };
            int[] to = { R.id.tname};
            SimpleAdapter adapter=new SimpleAdapter(SelectDB_Activity.this,list,R.layout.single,from,to);
            spDBlist.setAdapter(adapter);
            pd.cancel();
        }
    }

    private void getDatabaseProcess()
    {
        String SOAP_ACTION = "LMServiceNamespace/GetDatabase";
        String METHOD_NAME = "GetDatabase";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try
        {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("logon_id", Logon);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,60000);

            transport.call(SOAP_ACTION, soapEnvelope);
            list=new ArrayList();
            // list.add("-Selecte Database-");

            HashMap map0=new HashMap();

            map0.put("DbId","0");
            map0.put("DbName", "---Select Database---");

            list.add(map0);

            SoapObject resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
            // resultString = (SoapPrimitive) soapEnvelope.getResponse();
            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
            {
                SoapObject TypeDataBaseData = (SoapObject) resultRequestSOAP .getProperty(i);
                String DbName = TypeDataBaseData.getProperty("DatabaseName").toString();
                DbId = TypeDataBaseData.getProperty("DatabaseID").toString();

                HashMap map=new HashMap();

                map.put("DbId",DbId);
                map.put("DbName",DbName);

                list.add(map);

//                Log.e("id.......", DbName+" :"+DbId);
            }
//            Log.d("list", String.valueOf(list));



//            Log.d( "Result SECOND API: " , String.valueOf(resultRequestSOAP));
//             Log.d( "Result SECOND API: " , String.valueOf(resultString));
        } catch (Exception ex)
        {
//            Log.d( "Error: " , ex.getMessage());
        }

    }


    private class GetWorkgroup extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {

            pd=new ProgressDialog(SelectDB_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
//            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Log.i(TAG, "doInBackground");

            Log.e("Before","A");
            String SOAP_ACTION ="LMServiceNamespace/GetWorkgroups";
            String METHOD_NAME = "GetWorkgroups";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("logon_id", Logon);
                Request.addProperty("database_id", DBid);

                Log.e("DB_id Selected==>",DBid);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,60000);

                transport.call(SOAP_ACTION, soapEnvelope);
//                list=new ArrayList();


                SoapObject resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                count=resultRequestSOAP.getPropertyCount();
                if(count==1)
                {
                    SoapObject TypeDataBaseData = (SoapObject) resultRequestSOAP .getProperty(0);
                    String WgName = TypeDataBaseData.getProperty("WorkgroupName").toString();
                    WGId = TypeDataBaseData.getProperty("WorkgroupID").toString();
                }


                Log.e( "Count==>" , ""+ count+"  ,,,"+WGId);
//                Log.e(TAG, "Result Workgroup API: " + resultRequestSOAP);
                // Log.e(TAG, "Result SECOND API: " + resultString);
            }
            catch (Exception ex)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            Log.i(TAG, "onPostExecute");
            pd.cancel();

            if(DBid.equals("0"))
            {
//                Toast.makeText(getApplicationContext(),"Not Selected",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(count>1)
                {
//                    Toast.makeText(getApplicationContext(),"Multiple : "+count+" DBid=> "+DBid,Toast.LENGTH_LONG).show();
                    manager.setDB(SelectDB_Activity.this,DBid);
                    Intent i= new Intent(SelectDB_Activity.this,SelectWorkGroup_Activity.class);
                    startActivity(i);
                    finish();

                }
                else
                {
//                    Toast.makeText(getApplicationContext(),"Out:"+count+"DBid=>"+DBid,Toast.LENGTH_LONG).show();
                    manager.setDB(SelectDB_Activity.this,DBid);
                    manager.setWG(SelectDB_Activity.this,WGId);

                    GetCustomLabels getCustomLabels=new GetCustomLabels();
                    getCustomLabels.execute();

                    GetLoginPri getLoginPri=new GetLoginPri();
                    getLoginPri.execute();

//                    Intent i= new Intent(SelectDB_Activity.this,MainActivity.class);
//                    i.putExtra("id",DBid);
//                    i.putExtra("WorkGroupId",WGId);
//                    i.putExtra("Logon", Logon);
//                    startActivity(i);
                }

            }

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
            pd=new ProgressDialog(SelectDB_Activity.this);
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

                Request.addProperty("LogonID", manager.getUserId(SelectDB_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(SelectDB_Activity.this));
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
                        manager.setCustomLabel(SelectDB_Activity.this, key, value);
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
//            Intent i= new Intent(SelectDB_Activity.this,MainActivity.class);
////                    i.putExtra("id",DBid);
////                    i.putExtra("WorkGroupId",WGId);
////                    i.putExtra("Logon", Logon);
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
            pd=new ProgressDialog(SelectDB_Activity.this);
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

                Request.addProperty("LogonID", manager.getUserId(SelectDB_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(SelectDB_Activity.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                Log.e("test1","execute");
                Log.e("test1",""+resultRequestSOAP.getPropertyCount());

                Log.e("ReviewAndTakeAct",""+resultRequestSOAP.getProperty("ReviewAndTakeAct"));
                Log.e("ViewSalesProgress",""+resultRequestSOAP.getProperty("ViewSalesProgress"));
                Log.e("FullEdit",""+resultRequestSOAP.getProperty("FullEdit"));
                Log.e("AssignCallBack",""+resultRequestSOAP.getProperty("AssignCallBack"));
                Log.e("AccountLink",""+resultRequestSOAP.getProperty("AccountLink"));
                Log.e("RecordOnMap",""+resultRequestSOAP.getProperty("RecordOnMap"));
                Log.e("DefaultPage",""+resultRequestSOAP.getProperty("DefaultPage"));

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
                String emailId = resultRequestSOAP.getProperty("Email").toString();

                manager.setLoginPriv(SelectDB_Activity.this, "ReviewAndTakeAct", reviewAndTakeAct);
                manager.setLoginPriv(SelectDB_Activity.this, "ViewSalesProgress", viewSalesProgress);
                manager.setLoginPriv(SelectDB_Activity.this, "FullEdit", fullEdit);
                manager.setLoginPriv(SelectDB_Activity.this, "AssignCallBack", assignCallBack);
                manager.setLoginPriv(SelectDB_Activity.this, "AccountLink", accountLink);
                manager.setLoginPriv(SelectDB_Activity.this, "ContactLink", contactLink);
                manager.setLoginPriv(SelectDB_Activity.this, "RecordOnMap", recordOnMap);
                manager.setLoginPriv(SelectDB_Activity.this, "DefaultPage", defaultPage);
                manager.setLoginPriv(SelectDB_Activity.this, "ShowQuote", showQuote);
                manager.setLoginPriv(SelectDB_Activity.this, "ISO_Code", iso);
                manager.setLoginPriv(SelectDB_Activity.this, "MyCalendar", myCal);
                manager.setLoginPriv(SelectDB_Activity.this, "GroupCalendar", groupCal);
                manager.setLoginPriv(SelectDB_Activity.this, "Email", emailId);

//                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//                {
//                    Log.e("test2","execute");
//                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
//
////                    Log.e("test1","execute");
//
//                    String reviewAndTakeAct = TypeEventData.getProperty("ReviewAndTakeAct").toString();
//                    String viewSalesProgress = TypeEventData.getProperty("ViewSalesProgress").toString();
//                    String fullEdit = TypeEventData.getProperty("FullEdit").toString();
//                    String assignCallBack = TypeEventData.getProperty("AssignCallBack").toString();
//                    String accountLink = TypeEventData.getProperty("AccountLink").toString();
//                    String recordOnMap = TypeEventData.getProperty("RecordOnMap").toString();
//                    String defaultPage = TypeEventData.getProperty("DefaultPage").toString();
//
//                    Log.e("test1",TypeEventData.getProperty("DefaultPage").toString());
//                    manager.setLoginPriv(SelectDB_Activity.this, "reviewAndTakeAct", reviewAndTakeAct);
//                    manager.setLoginPriv(SelectDB_Activity.this, "viewSalesProgress", viewSalesProgress);
//                    manager.setLoginPriv(SelectDB_Activity.this, "fullEdit", fullEdit);
//                    manager.setLoginPriv(SelectDB_Activity.this, "assignCallBack", assignCallBack);
//                    manager.setLoginPriv(SelectDB_Activity.this, "accountLink", accountLink);
//                    manager.setLoginPriv(SelectDB_Activity.this, "recordOnMap", recordOnMap);
//                    manager.setLoginPriv(SelectDB_Activity.this, "defaultPage", defaultPage);
//
//
//                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            }
            catch (Exception ex)
            {

            }
            Log.e("Url==>",URL);
            Log.e("LogOnPriv", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            Intent i= new Intent(SelectDB_Activity.this,MainActivity.class);
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



    @Override
    protected void onStop() {
        super.onStop();
        pd.cancel();
    }
}
