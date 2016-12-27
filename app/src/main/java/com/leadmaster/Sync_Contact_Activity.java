package com.leadmaster;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Sync_Contact_Activity extends AppCompatActivity
{
    TextView backButton;
    SessionManager manager=new SessionManager();

    ArrayList list=new ArrayList();
    String firstname="",lastname="",phone="",email="",phone2="",fax="";

    String phoneType="",phone2Type="",emailType="",faxType="";

    ListView listView;

    SyncContact_adapter adapter;

    String selectPhone="";
    String[] firstnameArr,lastnameArr,phoneArr,phone2Arr,emailArr,faxArr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync__contact);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(Sync_Contact_Activity.this));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.sync_contact_toolbar);
        toolbar.setBackgroundColor(manager.getColor(Sync_Contact_Activity.this));

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.syncContactLL), iconFont);

        adapter=new SyncContact_adapter(Sync_Contact_Activity.this,list);

        listView=(ListView)findViewById(R.id.sync_contact_listview);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
        {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode)
            {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.sync_contact, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.contact_sync_id:
                        Log.e("Selected",adapter.getSelectedIds().size()+"");

                        firstnameArr=new String[adapter.getSelectedIds().size()];
                        lastnameArr=new String[adapter.getSelectedIds().size()];
                        phoneArr=new String[adapter.getSelectedIds().size()];
                        phone2Arr=new String[adapter.getSelectedIds().size()];
                        faxArr=new String[adapter.getSelectedIds().size()];
                        emailArr=new String[adapter.getSelectedIds().size()];

                        SparseBooleanArray selected = adapter.getSelectedIds();
                        SyncContact_Data  selecteditem;
                        for (int i =  (selected.size() - 1); i >= 0; i--)
                        {
                            if  (selected.valueAt(i))
                            {
                                selecteditem = adapter.getItem(selected.keyAt(i));
                                // Remove  selected items following the ids
                                //adapter.remove(selecteditem);
//                                selectPhone+=selecteditem.getPhone()+",";
                                firstnameArr[i]=selecteditem.getFirstName();
                                lastnameArr[i]=selecteditem.getLastName();
                                phoneArr[i]=selecteditem.getPhone();
                                phone2Arr[i]=selecteditem.getPhone2();
                                faxArr[i]=selecteditem.getFax();
                                emailArr[i]=selecteditem.getEmail();
                            }
                        }

                        mode.finish();

                        SyncContact syncContact=new SyncContact(firstnameArr,lastnameArr,phoneArr,phone2Arr,emailArr,faxArr);
                        syncContact.execute();

                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
            {
                // TODO  Auto-generated method stub
                final int checkedCount  = listView.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount  + " Contact Selected...");
                // Calls  toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

        });

        backButton=(TextView)findViewById(R.id.backBtn_syncContact);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS))
            {
                Log.e("PermissionResult : ","If shouldShowRequestPermissionRationale");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},101);
            }
            else
            {
                Log.e("PermissionResult : ","Else shouldShowRequestPermissionRationale");

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, 101);
            }
        }
        else
        {
            GetContact getContact=new GetContact();
            getContact.execute();
        }
    }

    private class GetContact extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog getContactPD;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            getContactPD=new ProgressDialog(Sync_Contact_Activity.this);
            getContactPD.setMessage("Wait...");
            getContactPD.setCancelable(false);
            getContactPD.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur != null && cur.getCount() > 0)
            {
                while (cur.moveToNext()) {
                    SyncContact_Data syncContact_data = new SyncContact_Data();
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    //                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor newcur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            new String[]{ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL},
                            ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                    + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
                            new String[]{String.valueOf(id)}, null);

                    if (newcur != null) {
                        if (newcur.getCount() > 0) {
                            Log.e("newcur", newcur.getCount() + "");
                            int i = 0;
                            while (newcur.moveToNext()) {
                                if (i < 1) {
                                    int lastNameIdx = newcur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
                                    int firstNameIdx = newcur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                                    Log.e("lastNameID", lastNameIdx + ":" + newcur.getString(lastNameIdx));
                                    Log.e("firstNameID", firstNameIdx + "" + newcur.getString(firstNameIdx));

                                    firstname = newcur.getString(firstNameIdx);
                                    lastname = newcur.getString(lastNameIdx);
                                    i++;
                                }
                            }
                        } else {
                            firstname = "";
                            lastname = "";
                        }
                    }

                    if (firstname == null) {
                        firstname = "";
                    }
                    if (lastname == null) {
                        lastname = "";
                    }
                    if (newcur != null) {
                        newcur.close();
                    }

                    Cursor curEmail = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);

//                    Log.e("EmailCount", curEmail.getCount() + "");
                    if (curEmail != null && curEmail.getCount() == 0) {
                        email = "";
                    }
                    int emailCount = 0;
                    if (curEmail != null) {
                        while (curEmail.moveToNext()) {
                            if (emailCount < 1) {
                                int type = curEmail.getInt(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                emailType = String.valueOf(type);
                                email = curEmail.getString(curEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                Log.e("Email", email);
                                emailCount++;
                            }
                        }
                    }
                    if (curEmail != null) {
                        curEmail.close();
                    }

                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        int i = 0, j = 0;
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (pCur != null) {
                            Log.e("PhoneCount", pCur.getCount() + "");
                        }
                        if (pCur != null) {
                            while (pCur.moveToNext()) {
                                int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX || type == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME || type == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) {
                                    if (j < 1) {
                                        faxType = String.valueOf(type);
                                        fax = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        j++;
                                    }
                                } else {
                                    if (i < 2) {
                                        //                                if(type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                        //                                {
                                        if (i == 0) {
                                            phoneType = String.valueOf(type);
                                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                            Log.e("phone", phone);
                                            Log.e("PhoneType", type + "");
                                        }
                                        //                                }
                                        //                                else
                                        //                                {
                                        if (i == 1) {
                                            phone2Type = String.valueOf(type);
                                            phone2 = pCur.getString(pCur.getColumnIndex(
                                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                                            Log.e("phone2", phone2);
                                            Log.e("Phone2Type", type + "");
                                        } else {
                                            phone2 = "";
                                        }
                                        //                                }
                                        i++;
                                    }
                                }
                            }
                        }
                        if (pCur != null) {
                            pCur.close();
                        }
                    } else {
                        phone = "";
                        phone2 = "";
                        fax = "";
                    }
                    if (!(firstname.equals("") && lastname.equals("") && email.equals("") && phone.equals(""))) {
                        syncContact_data.setContactId(id);
                        syncContact_data.setFirstName(firstname);
                        syncContact_data.setLastName(lastname);
                        syncContact_data.setEmail(email);
                        syncContact_data.setPhone(phone);
                        syncContact_data.setPhone2(phone2);
                        syncContact_data.setFax(fax);

                        syncContact_data.setPhoneType(phoneType);
                        syncContact_data.setPhone2Type(phone2Type);
                        syncContact_data.setEmailType(emailType);
                        syncContact_data.setFaxType(faxType);

                        list.add(syncContact_data);
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            getContactPD.dismiss();
            listView.setAdapter(new SyncContact_adapter(Sync_Contact_Activity.this,list));
        }
    }



    private class SyncContact extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog syncPD;

        int countResponse=0;

        String[] firstnameArr,lastnameArr,phoneArr,phone2Arr,emailArr,faxArr;

        public SyncContact(String[] firstnameArr, String[] lastnameArr, String[] phoneArr, String[] phone2Arr, String[] emailArr , String[] faxArr)
        {
            this.firstnameArr=firstnameArr.clone();
            this.lastnameArr=lastnameArr.clone();
            this.phoneArr=phoneArr.clone();
            this.phone2Arr=phone2Arr.clone();
            this.emailArr=emailArr.clone();
            this.faxArr=faxArr.clone();
            Log.e("PassedArray", "Firstname : "+Arrays.toString(this.firstnameArr));
            Log.e("PassedArray", "Lastname : "+Arrays.toString(this.lastnameArr));
            Log.e("PassedArray", "Phone : "+Arrays.toString(this.phoneArr));
            Log.e("PassedArray", "Phone2 : "+Arrays.toString(this.phone2Arr));
            Log.e("PassedArray", "Email : "+Arrays.toString(this.emailArr));
            Log.e("PassedArray", "Fax : "+Arrays.toString(this.faxArr));
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            syncPD=new ProgressDialog(Sync_Contact_Activity.this);
            syncPD.setMessage("Wait...");
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
                Request.addProperty("logon_id", manager.getUserId(Sync_Contact_Activity.this));
                Request.addProperty("pwd", manager.getUserPass(Sync_Contact_Activity.this));
                Request.addProperty("company_id", manager.getWG(Sync_Contact_Activity.this));

                SoapObject field = new SoapObject(NAMESPACE,"LeadData");
                Request.addSoapObject(field);

                Log.e("Fields",""+firstnameArr.length);
                for(int i=0;i<firstnameArr.length;i++)
                {
                    SoapObject record = new SoapObject(NAMESPACE,"TypeLeadData");
                    if(! firstnameArr[i].equals(""))
                    {
                        Log.e("Final","FN "+firstnameArr[i]);
                        record.addProperty("FirstName",firstnameArr[i]);
                    }
                    if(! lastnameArr[i].equals(""))
                    {
                        Log.e("Final","LN "+lastnameArr[i]);
                        record.addProperty("LastName",lastnameArr[i]);
                    }
                    if(! phoneArr[i].equals(""))
                    {
                        Log.e("Final","PH "+phoneArr[i]);
                        record.addProperty("Phone",phoneArr[i]);
                    }
                    if(! phone2Arr[i].equals(""))
                    {
                        Log.e("Final","PH2 "+phone2Arr[i]);
                        record.addProperty("Cell_Phone",phone2Arr[i]);
                    }
                    if(! emailArr[i].equals(""))
                    {
                        Log.e("Final","E "+emailArr[i]);
                        record.addProperty("Email",emailArr[i]);
                    }
                    if(! faxArr[i].equals(""))
                    {
                        Log.e("Final","FAX "+faxArr[i]);
                        record.addProperty("Fax",faxArr[i]);
                    }

                    field.addSoapObject(record);
                }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 101:
            {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    Log.e("MainPermissionResult : ", "If shouldShowRequestPermissionRationale");
                    GetContact getContact=new GetContact();
                    getContact.execute();
                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("MainPermissionResult : ", "Else shouldShowRequestPermissionRationale");
                }
            }
        }

    }
}