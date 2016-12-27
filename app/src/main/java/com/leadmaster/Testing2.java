package com.leadmaster;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Testing2 extends AppCompatActivity
{
    TextView backButton;

    Calendar calendar;

    SessionManager manager=new SessionManager();

    TextView displayTest1_name;

    AutoCompleteTextView accountSearchET;
    AutoCompleteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing2);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.test2LL), iconFont);

        accountSearchET=(AutoCompleteTextView)findViewById(R.id.accountSearchET);

        adapter = new AutoCompleteAdapter(Testing2.this, android.R.layout.simple_dropdown_item_1line);
        accountSearchET.setAdapter(adapter);

        //when autocomplete is clicked
        accountSearchET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String countryName = adapter.getItem(position).getName();
                accountSearchET.setText(countryName);
            }
        });

        backButton=(TextView)findViewById(R.id.backBtn_test2);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        displayTest1_name=(TextView)findViewById(R.id.displayTest2_name);
        displayTest1_name.setText("Add new "+manager.getCustomLabel(Testing2.this,"Opportunity"));

        /*final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(Testing2.this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Log.e("Date",""+dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
*/
//        CheckData checkData=new CheckData();
//        checkData.execute();
    }

    private class CheckData extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd=new ProgressDialog(Testing2.this);
            pd.setTitle("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/AddLead";
            String METHOD_NAME = "AddLead";
            String NAMESPACE = "LMServiceNamespace";
            String URL = "http://api.leadmaster.com/lmservice/LMService.asmx";

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("logon_id", manager.getUserId(Testing2.this));
                Request.addProperty("pwd", manager.getUserPass(Testing2.this));
                Request.addProperty("company_id", manager.getWG(Testing2.this));

                SoapObject field = new SoapObject(NAMESPACE,"LeadData");
                Request.addSoapObject(field);

                SoapObject record = new SoapObject(NAMESPACE,"TypeLeadData");
                record.addProperty("Company","abc");
                field.addSoapObject(record);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String fieldName = TypeEventData.getProperty("RECDNO").toString();
                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            }
            catch (Exception ex)
            {
                Log.e("Error",""+ex);
            }
            Log.e("Url==>",URL);
            Log.e("check", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.dismiss();
        }
    }
}