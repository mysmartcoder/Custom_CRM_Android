package com.customCRM;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Login_Activity extends AppCompatActivity
{
    EditText usernameET,passwordET;
    String username,password;
    Button btn;
    SessionManager manager;
    ProgressDialog pd;

    String loginID=null;
    String FullName;

    ImageView pwd_visibleBtn;
//    Button pwd_visibleBtn;

    int DBcount=0,WGcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

//        boolean checkNetwork=CheckNetwork.isNetworkAvailable(Login_Activity.this);
//
//        if(!checkNetwork)
//        {
//            new AlertDialog.Builder(Login_Activity.this)
//                    .setTitle("Connection")
//                    .setMessage("Your Internet connection is Off!")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
        usernameET=(EditText)findViewById(R.id.usernameLoginEdit);
        passwordET=(EditText)findViewById(R.id.passwordLoginEdit);

        pwd_visibleBtn=(ImageView)findViewById(R.id.pwd_visible);
//        pwd_visibleBtn=(Button)findViewById(R.id.pwd_visible);

        manager = new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(Login_Activity.this));
        }

        String i=manager.getPreferences(Login_Activity.this, "status");

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.login_parent);
        toolbar.setBackgroundColor(manager.getColor(this));

        ScrollView toolbar2 = (ScrollView) findViewById(R.id.Scroll_View);
        toolbar2.setBackgroundColor(manager.getColor(this));

        if(manager.getFontStyle(Login_Activity.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.login_parent), mainFont);
        }
        else if(manager.getFontStyle(this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.login_parent), mainFont);
        }
        else if(manager.getFontStyle(Login_Activity.this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.login_parent), mainFont);
        }
        else if(manager.getFontStyle(this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.login_parent), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.password_container), iconFont);

        if(i.equals("1"))
        {
//            String u1= manager.getUserName(Login_Activity.this);
//            String p1=manager.getUserPass(Login_Activity.this);
//            usernameET.setText(u1);
//            passwordET.setText(p1);

            if(manager.getDB(Login_Activity.this).equals("") || manager.getWG(Login_Activity.this).equals(""))
            {
                usernameET.setText(manager.getUserName(Login_Activity.this));
                passwordET.setText(manager.getUserPass(Login_Activity.this));
            }
            else
            {
                Intent login_check_intent=new Intent(Login_Activity.this,MainActivity.class);
                startActivity(login_check_intent);
                finish();
            }
        }
        else
        {
            usernameET.setText(manager.getUserName(Login_Activity.this));
            passwordET.setText(manager.getUserPass(Login_Activity.this));
        }

        btn=(Button)findViewById(R.id.login_btn);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(! CheckNetwork.isNetworkAvailable(Login_Activity.this))
                {
                    Toast.makeText(getApplicationContext(),"Please check your Internet connection",Toast.LENGTH_LONG).show();
                }
                else if(usernameET.getText().toString().equals("") || passwordET.getText().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter username or password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    username=usernameET.getText().toString();
                    password=passwordET.getText().toString();

                    CheckLogin checkLogin=new CheckLogin();
                    checkLogin.execute();
                }

            }
        });

        pwd_visibleBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() )
                {
                    case MotionEvent.ACTION_DOWN:
                        passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                    case MotionEvent.ACTION_UP:
                        passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }

                return true;
            }
        });

//        pwd_visibleBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(passwordET.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD))
//                {
//                    Log.e("if","called");
//                    pwd_visibleBtn.setTextColor(Color.parseColor("#0051A1"));
//                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
//                }
//                else
//                {
//                    Log.e("else","called");
//                    pwd_visibleBtn.setTextColor(Color.parseColor("#616161"));
//                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//
//            }
//        });
    }

    private class CheckLogin extends AsyncTask<String,String,String>
    {
        SoapObject resultRequestSOAP = null;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd=new ProgressDialog(Login_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String SOAP_ACTION = "LMServiceNamespace/APIValidateLogonData";
            String METHOD_NAME = "APIValidateLogonData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request=new SoapObject(NAMESPACE,METHOD_NAME);
                Request.addProperty("username",username);
                Request.addProperty("pwd",password);

                Log.e("Check==>",username+","+password);

                SoapSerializationEnvelope soapEnvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet=true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport=new HttpTransportSE(URL,30000);
                transport.call(SOAP_ACTION,soapEnvelope);

//                resultString=(SoapPrimitive)soapEnvelope.getResponse();
                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    loginID = TypeEventData.getProperty("LogonID").toString();
                    FullName = TypeEventData.getProperty("UserName").toString();

                    Log.e(i+" : Key,Value ==> ",loginID+","+FullName);

                }

//                loginID = resultRequestSOAP.getProperty("LogonID").toString();
//                FullName = resultRequestSOAP.getProperty("UserName").toString();

                Log.e("loginId",loginID);

                manager.setPreferences(Login_Activity.this, "status", "1");
                manager.setPreferences(Login_Activity.this, "firstTime", "1");
                manager.setUser(Login_Activity.this,username,password,loginID,FullName);

            }
            catch (Exception e)
            {
//                Log.e("Error==>", e.getMessage());
//                Toast.makeText(getApplicationContext(),"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
            }
            Log.e("Check==>",manager.getUrl()+","+username+","+password);
            Log.e("result==>", loginID+","+FullName);
            Log.e("result==>", String.valueOf(resultRequestSOAP));

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

//            pd.cancel();

            if(loginID == null)
            {
                pd.cancel();
                Toast.makeText(Login_Activity.this, "Server not responding", Toast.LENGTH_LONG).show();
            }
            else if(loginID.equals("0"))
            {
                pd.cancel();
                Toast.makeText(Login_Activity.this, "Enter valid input", Toast.LENGTH_LONG).show();
            }
            else
            {
//                Intent i=new Intent(Login_Activity.this,SelectDB_Activity.class);
//                startActivity(i);
//                finish();

                GetDataBase getDataBase=new GetDataBase();
                getDataBase.execute();
            }
        }
    }


    //Check DB

    private class GetDataBase extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {

//            pd=new ProgressDialog(Login_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
//            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Log.i(TAG, "doInBackground");

            Log.e("Before","A");
            String SOAP_ACTION ="LMServiceNamespace/GetDatabase";
            String METHOD_NAME = "GetDatabase";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("logon_id", manager.getUserId(Login_Activity.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,60000);

                transport.call(SOAP_ACTION, soapEnvelope);
//                list=new ArrayList();

                SoapObject resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                DBcount=resultRequestSOAP.getPropertyCount();

                if(DBcount==1)
                {
                    SoapObject TypeDataBaseData = (SoapObject) resultRequestSOAP .getProperty(0);
                    String WgName = TypeDataBaseData.getProperty("DatabaseName").toString();
                    manager.setDB(Login_Activity.this,TypeDataBaseData.getProperty("DatabaseID").toString());
                }


                Log.e( "Count==>" , ""+ DBcount+"  ,,,"+manager.getDB(Login_Activity.this));
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
//            pd.cancel();

            if(manager.getDB(Login_Activity.this).equals("0"))
            {
//                Toast.makeText(getApplicationContext(),"Not Selected",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(DBcount>1)
                {
                    pd.cancel();
                    Intent i= new Intent(Login_Activity.this,SelectDB_Activity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    GetWorkGroup getWorkGroup=new GetWorkGroup();
                    getWorkGroup.execute();

//                    GetLoginPri getLoginPri=new GetLoginPri();
//                    getLoginPri.execute();

                }

            }

        }
    }

    //Check WG

    private class GetWorkGroup extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {

//            pd=new ProgressDialog(Login_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
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
                Request.addProperty("logon_id", manager.getUserId(Login_Activity.this));
                Request.addProperty("database_id", manager.getDB(Login_Activity.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,60000);

                transport.call(SOAP_ACTION, soapEnvelope);
//                list=new ArrayList();

                SoapObject resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                WGcount=resultRequestSOAP.getPropertyCount();

                if(WGcount==1)
                {
                    SoapObject TypeDataBaseData = (SoapObject) resultRequestSOAP .getProperty(0);
                    String WgName = TypeDataBaseData.getProperty("WorkgroupName").toString();
                    manager.setWG(Login_Activity.this,TypeDataBaseData.getProperty("WorkgroupID").toString());
                }


                Log.e( "Count==>" , ""+ WGcount+"  ,,,"+manager.getWG(Login_Activity.this));
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
//            pd.cancel();

            if(manager.getDB(Login_Activity.this).equals("0"))
            {
//                Toast.makeText(getApplicationContext(),"Not Selected",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(WGcount>1)
                {
                    pd.cancel();
                    Intent i= new Intent(Login_Activity.this,SelectWorkGroup_Activity.class);
                    startActivity(i);
                    finish();
                }
                else
                {


                    GetCustomLabels getCustomLabels=new GetCustomLabels();
                    getCustomLabels.execute();

//                    GetLoginPri getLoginPri=new GetLoginPri();
//                    getLoginPri.execute();

                }

            }

        }
    }

    //store Custom_Label

    private class GetCustomLabels extends AsyncTask<Void, Void, Void>
    {
        SoapObject resultRequestSOAP = null;
        String labelNames = "Banner - Accounts^Contact^Company^Banner - Contacts^Lead Status^Entered^Campaign^Events^Event Name^Event^Shortcuts^My Searches^Recent Items^Acct Mgr^Opportunity^Case^Sales Rep Comments/Notes^Record^Opportunity Name^Phone^Cases^Banner - Library^Quote^Sales Rep Comments/Notes^Banner - Calendar^Shortcuts^Event Done^Quote Name^Company^First Name^Last Name^Case Date Due^Subject^Case Status^Case Priority^Case Owner^Key Contact^Lead_Source^Event Start Time^Opportunity Sales Stage^List Source^Sales Rep Comments/Notes^Total Opportunity Value^Contact Information^Profile Summary^Profile Assignments^Special Interest^Special Interest II^Special Interest III^Sales Rep Comments/Notes^Opportunity Details^Product Details^Case Additional Information^Case Details";
        SoapPrimitive resultString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Login_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
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

                Request.addProperty("LogonID", manager.getUserId(Login_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(Login_Activity.this));
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
                        manager.setCustomLabel(Login_Activity.this, key, value);
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

            GetLoginPri getLoginPri=new GetLoginPri();
            getLoginPri.execute();

//            pd.cancel();
//            Intent i=new Intent(SelectWorkGroup_Activity.this,MainActivity.class);
//                    i.putExtra("id",dbId);
//                    i.putExtra("WorkGroupId",wgIdIntent);
//                    i.putExtra("Logon", Logon);
//            startActivity(i);
//            Log.d("Post","Execute");
        }
    }

    //Get LogIn_Privileges

    private class GetLoginPri extends AsyncTask<Void, Void, Void>
    {
        SoapObject resultRequestSOAP = null;
        SoapPrimitive resultString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Login_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
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

                Request.addProperty("LogonID", manager.getUserId(Login_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(Login_Activity.this));

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
                String emailID = resultRequestSOAP.getProperty("Email").toString();


                manager.setLoginPriv(Login_Activity.this, "ReviewAndTakeAct", reviewAndTakeAct);
                manager.setLoginPriv(Login_Activity.this, "ViewSalesProgress", viewSalesProgress);
                manager.setLoginPriv(Login_Activity.this, "FullEdit", fullEdit);
                manager.setLoginPriv(Login_Activity.this, "AssignCallBack", assignCallBack);
                manager.setLoginPriv(Login_Activity.this, "AccountLink", accountLink);
                manager.setLoginPriv(Login_Activity.this, "ContactLink", contactLink);
                manager.setLoginPriv(Login_Activity.this, "RecordOnMap", recordOnMap);
                manager.setLoginPriv(Login_Activity.this, "DefaultPage", defaultPage);
                manager.setLoginPriv(Login_Activity.this, "ShowQuote", showQuote);
                manager.setLoginPriv(Login_Activity.this, "ISO_Code", iso);
                manager.setLoginPriv(Login_Activity.this, "MyCalendar", myCal);
                manager.setLoginPriv(Login_Activity.this, "GroupCalendar", groupCal);
                manager.setLoginPriv(Login_Activity.this, "Email", emailID);


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
            Intent i= new Intent(Login_Activity.this,MainActivity.class);
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
    public void onBackPressed() {
        super.onBackPressed();

//        finish();
//        MainActivity mainActivity=new MainActivity();
//        mainActivity.finish();
    }
}