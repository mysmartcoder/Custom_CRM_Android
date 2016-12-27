package com.customCRM;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyncContactDisplayActivity extends AppCompatActivity
{
    TextView firstnameTV,lastnameTV,phoneTV,phone2TV,emailTV,faxTV;

    String firstnameDis,lastnameDis,phoneDis,phone2Dis,faxDis,emailDis,contactIdDis;

    String phonetype="",phone2type="",emailtype="",faxtype="";

    TextView backBtn_syncContactDiaplay;

    Button syncContactDiaplay_sync_footer_button,syncContactDiaplay_edit_footer_button,
            syncContactDiaplay_cancel_footer_button,syncContactDiaplay_save_footer_button;

    SessionManager manager=new SessionManager();

    String firstname,lastname,phone,phone2,email,fax;

    LinearLayout display_contact_parent,edit_contact_parent,
            syncContact_sync_footer,syncContact_save_footer;

    EditText firstname_et,lastname_et,phone_et,phone2_et,fax_et,email_et;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_contact_display);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.backBtn_syncContactDiaplay), iconFont);

        firstname_et=(EditText)findViewById(R.id.syncContactEdit_firstname);
        lastname_et=(EditText)findViewById(R.id.syncContactEdit_lastname);
        phone_et=(EditText)findViewById(R.id.syncContactEdit_phone);
        phone2_et=(EditText)findViewById(R.id.syncContactEdit_phone2);
        fax_et=(EditText)findViewById(R.id.syncContactEdit_fax);
        email_et=(EditText)findViewById(R.id.syncContactEdit_email);


        display_contact_parent=(LinearLayout)findViewById(R.id.display_contact_parent);
        edit_contact_parent=(LinearLayout)findViewById(R.id.edit_contact_parent);
        syncContact_save_footer=(LinearLayout)findViewById(R.id.syncContact_save_footer);
        syncContact_sync_footer=(LinearLayout)findViewById(R.id.syncContact_sync_footer);

        backBtn_syncContactDiaplay=(TextView)findViewById(R.id.backBtn_syncContactDiaplay);

        backBtn_syncContactDiaplay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),Sync_Contact_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        phonetype = bundle.getString("phonetype");
        phone2type = bundle.getString("phone2type");
        emailtype = bundle.getString("emailtype");
        faxtype = bundle.getString("faxtype");

        contactIdDis = bundle.getString("id");

        firstname = firstnameDis = bundle.getString("firstname");
        lastname = lastnameDis = bundle.getString("lastname");
        phone = phoneDis = bundle.getString("phone");
        phone2 = phone2Dis = bundle.getString("phone2");
        fax = faxDis = bundle.getString("fax");
        email = emailDis = bundle.getString("email");

        firstname_et.setText(firstname);
        lastname_et.setText(lastname);
        phone_et.setText(phone);
        phone2_et.setText(phone2);
        fax_et.setText(fax);
        email_et.setText(email);

        if(phonetype.equals(""))
        {
            phonetype= String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        }
        if(phone2type.equals(""))
        {
            phone2type= String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        }
        if(emailtype.equals(""))
        {
            emailtype= String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
        }
        if(faxtype.equals(""))
        {
            faxtype= String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK);
        }

        Log.e("type",phonetype);
        Log.e("type",phone2type);
        Log.e("type",emailtype);
        Log.e("type",faxtype);
        firstnameTV=(TextView)findViewById(R.id.syncContactDiaplay_firstname);
        lastnameTV=(TextView)findViewById(R.id.syncContactDiaplay_lastname);
        phoneTV=(TextView)findViewById(R.id.syncContactDiaplay_phone);
        phone2TV=(TextView)findViewById(R.id.syncContactDiaplay_phone2);
        faxTV=(TextView)findViewById(R.id.syncContactDiaplay_fax);
        emailTV=(TextView)findViewById(R.id.syncContactDiaplay_email);

        if (firstname != null && firstname.equals(""))
        {
            firstnameDis = "--NA--";
//            firstnameTV.setVisibility(View.GONE);
//            firstname_et.setVisibility(View.GONE);
        }
        if (lastname != null && lastname.equals(""))
        {
            lastnameDis = "--NA--";
//            lastnameTV.setVisibility(View.GONE);
//            lastname_et.setVisibility(View.GONE);
        }
        if (phone != null && phone.equals(""))
        {
            phoneDis = "--NA--";
        }
        if (phone2 != null && phone2.equals(""))
        {
            phone2Dis = "--NA--";
        }
        if (fax != null && fax.equals(""))
        {
            faxDis = "--NA--";
        }
        if (email != null && email.equals(""))
        {
            emailDis = "--NA--";
        }

        firstnameTV.setText(firstnameDis);
        lastnameTV.setText(lastnameDis);
        phoneTV.setText(phoneDis);
        phone2TV.setText(phone2Dis);
        faxTV.setText(faxDis);
        emailTV.setText(emailDis);

        syncContactDiaplay_sync_footer_button=(Button)findViewById(R.id.syncContactDiaplay_sync_footer_button);

        syncContactDiaplay_sync_footer_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SyncContact syncContact=new SyncContact(firstname,lastname,phone,phone2,email,fax);
                syncContact.execute();
            }
        });

        syncContactDiaplay_edit_footer_button=(Button)findViewById(R.id.syncContactDiaplay_edit_footer_button);
        syncContactDiaplay_edit_footer_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edit_contact_parent.setVisibility(View.VISIBLE);
                display_contact_parent.setVisibility(View.GONE);
                syncContact_save_footer.setVisibility(View.VISIBLE);
                syncContact_sync_footer.setVisibility(View.GONE);
//                updateContact("Tony","Stark","159753","456123","stark@gmail.com","784512",phonetype,phone2type,emailtype,faxtype,contactIdDis);
            }
        });

        syncContactDiaplay_save_footer_button=(Button)findViewById(R.id.syncContactDiaplay_save_footer_button);
        syncContactDiaplay_save_footer_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean check=updateContact(firstname_et.getText().toString(),lastname_et.getText().toString(),phone_et.getText().toString()
                        ,phone2_et.getText().toString(),email_et.getText().toString(),fax_et.getText().toString(),phonetype,phone2type,emailtype,faxtype,contactIdDis);

                if(check)
                {
                    Intent i = new Intent(getApplicationContext(),Sync_Contact_Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });

        syncContactDiaplay_cancel_footer_button=(Button)findViewById(R.id.syncContactDiaplay_cancel_footer_button);
        syncContactDiaplay_cancel_footer_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edit_contact_parent.setVisibility(View.GONE);
                display_contact_parent.setVisibility(View.VISIBLE);

                syncContact_save_footer.setVisibility(View.GONE);
                syncContact_sync_footer.setVisibility(View.VISIBLE);
            }
        });
    }


    private class SyncContact extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog syncPD;

        int countResponse=0;

        String firstnameArr,lastnameArr,phoneArr,phone2Arr,emailArr,faxArr;

        public SyncContact(String firstnameArr, String lastnameArr, String phoneArr, String phone2Arr, String emailArr , String faxArr)
        {
            this.firstnameArr=firstnameArr;
            this.lastnameArr=lastnameArr;
            this.phoneArr=phoneArr;
            this.phone2Arr=phone2Arr;
            this.emailArr=emailArr;
            this.faxArr=faxArr;
            Log.e("PassedArray", "Firstname : "+ this.firstnameArr);
            Log.e("PassedArray", "Lastname : "+ this.lastnameArr);
            Log.e("PassedArray", "Phone : "+ this.phoneArr);
            Log.e("PassedArray", "Phone2 : "+ this.phone2Arr);
            Log.e("PassedArray", "Email : "+ this.emailArr);
            Log.e("PassedArray", "Fax : "+ this.faxArr);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            syncPD=new ProgressDialog(SyncContactDisplayActivity.this);
            syncPD.setTitle("Wait...");
            syncPD.setCancelable(false);
            syncPD.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/AddLead";
            String METHOD_NAME = "AddLead";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("logon_id", manager.getUserId(SyncContactDisplayActivity.this));
                Request.addProperty("pwd", manager.getUserPass(SyncContactDisplayActivity.this));
                Request.addProperty("company_id", manager.getWG(SyncContactDisplayActivity.this));

                SoapObject field = new SoapObject(NAMESPACE,"LeadData");
                Request.addSoapObject(field);

                SoapObject record = new SoapObject(NAMESPACE,"TypeLeadData");
                if(! firstnameArr.equals(""))
                {
                    Log.e("Final","FN "+firstnameArr);
                    record.addProperty("FirstName",firstnameArr);
                }
                if(! lastnameArr.equals(""))
                {
                    Log.e("Final","LN "+lastnameArr);
                    record.addProperty("LastName",lastnameArr);
                }
                if(! phoneArr.equals(""))
                {
                    Log.e("Final","PH "+phoneArr);
                    record.addProperty("Phone",phoneArr);
                }
                if(! phone2Arr.equals(""))
                {
                    Log.e("Final","PH2 "+phone2Arr);
                    record.addProperty("Cell_Phone",phone2Arr);
                }
                if(! emailArr.equals(""))
                {
                    Log.e("Final","E "+emailArr);
                    record.addProperty("Email",emailArr);
                }
                if(! faxArr.equals(""))
                {
                    Log.e("Final","FAX "+faxArr);
                    record.addProperty("Fax",faxArr);
                }

                field.addSoapObject(record);

                Log.e("For","Complete");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

                countResponse=resultRequestSOAP.getPropertyCount();
                Log.e("Count",resultRequestSOAP.getPropertyCount()+"");
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String recdno = TypeEventData.getProperty("RECDNO").toString();
                    Log.e("ReordNO:",recdno);
                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("check", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            syncPD.dismiss();
            if(countResponse==0)
            {
                Toast.makeText(getApplicationContext(),"Some kind of problem in Sync. Please try again!",Toast.LENGTH_LONG).show();
                recreate();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Synchronization completed !",Toast.LENGTH_LONG).show();
                recreate();
            }
        }
    }


    /*Demo*/

    public boolean updateContact(String firstname_i, String lastname_i, String phone_i, String phone2_i, String email_i,String fax_i,String phonetype,String phone2type,String emailtype,String faxtype,String ContactId)
    {
        boolean success = true;
        String phnumexp = "^[0-9]*$";

        try
        {
            firstname_i = firstname_i.trim();
            lastname_i = lastname_i.trim();
            phone_i = phone_i.trim();
            phone2_i = phone2_i.trim();
            email_i = email_i.trim();
            fax_i= fax_i.trim();

            Log.e("lastname",lastname_i);

            ContentResolver contentResolver  = getContentResolver();

            String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";

//            String[] emailParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
            String[] nameParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
//            String[] numberParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

            ArrayList<ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

            if(!firstname_i.equals(""))
            {
                if(firstname.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
//                    contentValues.put(ContactsContract.Data.CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstname_i);

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                }
                else
                {
                    ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                            .withSelection(where,nameParams)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstname_i)
                            .build());
                }
            }

            if(!lastname_i.equals(""))
            {
                // Name
                ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
                builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contactIdDis), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});
                builder.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname);
//                builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstname);
                ops.add(builder.build());
                /*if(lastname.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname_i);

                    boolean result =  ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                    Log.e("Result",result+"");
                }
                else
                {
                    ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                            .withSelection(where,nameParams)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname_i)
                            .build());
                }*/
            }

            if(!email_i.equals(""))
            {
                if(email.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email_i);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, emailtype);

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                }
                else
                {
                    ContentProviderOperation.Builder builderPhone = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(ContactsContract.Data.CONTACT_ID + "=?"+" AND "+ContactsContract.Data.MIMETYPE + "=?" + " AND "+ ContactsContract.CommonDataKinds.Phone.TYPE+"=?",  new String[]{String.valueOf(contactIdDis), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, emailtype});
                    builderPhone.withValue(ContactsContract.CommonDataKinds.Email.DATA, email_i);
                    ops.add(builderPhone.build());
                }
            }

            if(!phone_i.equals(""))
            {
                if(phone.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_i);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phonetype);

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                }
                else
                {
                    ContentProviderOperation.Builder builderPhone = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.CONTACT_ID + "=?"+" AND "+ContactsContract.Data.MIMETYPE + "=?" + " AND "+ ContactsContract.CommonDataKinds.Phone.TYPE+"=?",  new String[]{String.valueOf(contactIdDis), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, phonetype});
                    builderPhone.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_i);
                    ops.add(builderPhone.build());
                }
            }

            if(!phone2_i.equals(""))
            {
                if(phone2.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone2_i);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phone2type);

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                }
                else
                {
                    ContentProviderOperation.Builder builderPhone = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(ContactsContract.Data.CONTACT_ID + "=?"+" AND "+ContactsContract.Data.MIMETYPE + "=?" + " AND "+ ContactsContract.CommonDataKinds.Phone.TYPE+"=?",  new String[]{String.valueOf(contactIdDis), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, phone2type});
                    builderPhone.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone2_i);
                    ops.add(builderPhone.build());
                }
            }

            if(!fax_i.equals(""))
            {
                if(fax.equals(""))
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactIdDis);
                    contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, fax_i);
                    contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, faxtype);

                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI).withValues(contentValues).build());
                }
                else
                {
                    ContentProviderOperation.Builder builderPhone = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(ContactsContract.Data.CONTACT_ID + "=?"+" AND "+ContactsContract.Data.MIMETYPE + "=?" + " AND "+ ContactsContract.CommonDataKinds.Phone.TYPE+"=?",  new String[]{String.valueOf(contactIdDis), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, faxtype});
                    builderPhone.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, fax_i);
                    ops.add(builderPhone.build());
                }
            }

            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static boolean isValidEmail(CharSequence target)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean match(String stringToCompare,String regularExpression)
    {
        boolean success = false;
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(stringToCompare);
        if(matcher.matches())
            success =true;
        return success;
    }

}