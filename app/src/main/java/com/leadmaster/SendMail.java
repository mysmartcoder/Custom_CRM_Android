package com.leadmaster;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SendMail extends AppCompatActivity
{
    SessionManager manager=new SessionManager();

    ArrayList<String> names;
    ArrayList lib;

    SendMail_LibAdapter adapter;

    TextView backBtn_sendmail,add_spinner_button_dash,attachment_sendmail;
    EditText sendemail_subject,sendemail_compose;
    AutoCompleteTextView sendemail_to;
    SendMailAutoCompleteAdapter sendMailAutoCompleteAdapter;

    EditText sendemail_from;
//    TextView sendemail_from;

    Button send_email_cancel_footer_button,send_email_footer_button;

    ImageView sendmail_button;

    final static int SELECT_IMAGE=1234;

    String contactId,recordId,to_emailID,companyName;

    LinearLayout attachment_container;
    LinearLayout[] attachment_container_child;
    TextView[] displayLib,displayPath,deleteLib;
    int attach_count=0;

    ArrayList<String> filename;
    ArrayList<String> filePath;
//    ImageView iv;

//    AutoCompleteTextView sendmail_search_to;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(SendMail.this));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.sendmail_toolbar);
        toolbar.setBackgroundColor(manager.getColor(SendMail.this));

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.backBtn_sendmail), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.add_spinner_button_dash), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.attachment_sendmail), iconFont);

        lib=new ArrayList();
        filename=new ArrayList<String>();
        filePath=new ArrayList<String>();

        adapter=new SendMail_LibAdapter(SendMail.this, lib);

        backBtn_sendmail=(TextView)findViewById(R.id.backBtn_sendmail);
        add_spinner_button_dash=(TextView)findViewById(R.id.add_spinner_button_dash);
        attachment_sendmail=(TextView)findViewById(R.id.attachment_sendmail);

        attachment_container=(LinearLayout) findViewById(R.id.attachment_container);

//        sendemail_from=(TextView) findViewById(R.id.sendemail_from);
        sendemail_from=(EditText) findViewById(R.id.sendemail_from);
        sendemail_to=(AutoCompleteTextView)findViewById(R.id.sendemail_to);
        sendemail_subject=(EditText)findViewById(R.id.sendemail_subject);
        sendemail_compose=(EditText)findViewById(R.id.sendemail_compose);

//        sendmail_search_to=(AutoCompleteTextView)findViewById(R.id.sendmail_search_to);

        sendemail_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String countryName = sendMailAutoCompleteAdapter.getItem(position).getName();
                sendemail_to.setText(countryName);
            }
        });

        send_email_cancel_footer_button=(Button)findViewById(R.id.send_email_cancel_footer_button);
        send_email_footer_button=(Button)findViewById(R.id.send_email_footer_button);

        sendmail_button=(ImageView)findViewById(R.id.sendmail_button);

//        iv=(ImageView)findViewById(R.id.iv1);

        add_spinner_button_dash.setTextColor(manager.getColor(SendMail.this));

        Bundle bundle = getIntent().getExtras();
        to_emailID = bundle.getString("To_EmailID");
        contactId = bundle.getString("contactId");
        recordId = bundle.getString("recordId");
        companyName = bundle.getString("companyName");
        if (companyName != null && companyName.equals("none"))
        {
            companyName = "";
        }

        Log.e("GetData",to_emailID+","+contactId+","+recordId+","+companyName);

        sendemail_from.setText(manager.getLoginPriv(SendMail.this,"Email"));
        sendemail_to.setText(to_emailID);

        backBtn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_spinner_button_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(SendMail.this,add_spinner_button_dash);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.send_mail_menu, popup.getMenu());

                popup.getMenu().add("Local");
                if(! manager.getCustomLabel(SendMail.this,"Banner - Accounts").equals("") && ! manager.getCustomLabel(SendMail.this,"Banner - Contacts").equals(""))
                {
                    SubMenu subMenu=popup.getMenu().addSubMenu("Live");
                    if(! manager.getCustomLabel(SendMail.this,"Banner - Accounts").equals(""))
                    {
                        subMenu.add("Lead");
                    }
                    if(! manager.getCustomLabel(SendMail.this,"Banner - Contacts").equals(""))
                    {
                        subMenu.add("Contact");
                    }
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Local"))
                        {
                            callLocalEmail();
                        }
                        if(item.getTitle().equals("Lead"))
                        {
                            sendemail_to.setText("");
                            sendMailAutoCompleteAdapter=new SendMailAutoCompleteAdapter(SendMail.this,android.R.layout.simple_dropdown_item_1line,"lead");
                            sendemail_to.setAdapter(sendMailAutoCompleteAdapter);
                        }
                        if(item.getTitle().equals("Contact"))
                        {
                            sendemail_to.setText("");
                            sendMailAutoCompleteAdapter=new SendMailAutoCompleteAdapter(SendMail.this,android.R.layout.simple_dropdown_item_1line,"contact");
                            sendemail_to.setAdapter(sendMailAutoCompleteAdapter);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        sendmail_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int i=0;i<filePath.size();i++)
                {
                    Log.e("Path",displayPath[i].getText().toString());
                }

                String subject=sendemail_subject.getText().toString();
                String compose=sendemail_compose.getText().toString();

                if(sendemail_to.getText().toString().trim().equals("") || subject.trim().equals("") || compose.trim().equals(""))
                {
                    if(sendemail_to.getText().toString().trim().equals(""))
                    {
                        sendemail_to.setError("Mandatory Field");
                    }
                    if(subject.trim().equals(""))
                    {
                        sendemail_subject.setError("Mandatory Field");
                    }
                    if(compose.trim().equals(""))
                    {
                        sendemail_compose.setError("Mandatory Field");
                    }
                }
                else if(! isValidEmail(sendemail_to.getText().toString()))
                {
                    sendemail_to.setError("Incorrect Email Id");
                }
                else
                {
                    SendEmailCall sendEmailCall=new SendEmailCall(recordId,contactId,companyName,to_emailID,subject,compose);
                    sendEmailCall.execute();
                }
            }
        });

        send_email_footer_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int i=0;i<filePath.size();i++)
                {
                    Log.e("Path",displayPath[i].getText().toString());
                }
                String subject=sendemail_subject.getText().toString();
                String compose=sendemail_compose.getText().toString();

                if(sendemail_to.getText().toString().trim().equals("") || subject.trim().equals("") || compose.trim().equals(""))
                {
                    if(sendemail_to.getText().toString().trim().equals(""))
                    {
                        sendemail_to.setError("Mandatory Field");
                    }
                    if(subject.trim().equals(""))
                    {
                        sendemail_subject.setError("Mandatory Field");
                    }
                    if(compose.trim().equals(""))
                    {
                        sendemail_compose.setError("Mandatory Field");
                    }
                }
                else if(! isValidEmail(sendemail_to.getText().toString()))
                {
                    sendemail_to.setError("Incorrect Email Id");
                }
                else
                {
                    SendEmailCall sendEmailCall=new SendEmailCall(recordId,contactId,companyName,to_emailID,subject,compose);
                    sendEmailCall.execute();
                }
            }
        });

        send_email_cancel_footer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attachment_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(SendMail.this,attachment_sendmail);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.attachment_menu, popup.getMenu());

                popup.getMenu().add("Gallery");
                popup.getMenu().add("Library");

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Gallery"))
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);//
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
                        }
                        if(item.getTitle().equals("Library"))
                        {
                            GetLibrary getLibrary=new GetLibrary();
                            getLibrary.execute();
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        /*New try*/

        /*sendemail_to.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count >=1){
                    if(s.charAt(start) == ' ')
                        setChips(); // generate chips
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });*/

    }


//    public ArrayList<String> getNameEmailDetails()
    public void getNameEmailDetails()
    {
        names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur != null && cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                if (cur1 != null)
                {
                    while (cur1.moveToNext())
                    {
                        //to get the contact names
                        String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.e("Name :", name);
                        String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.e("Email", email);
                        if (email != null)
                        {
                            names.add(email);
                        }
                    }
                }
                if (cur1 != null)
                {
                    cur1.close();
                }
            }
        }
//        return names;
    }

    private class SendEmailCall extends AsyncTask<Void,Void,Void>
    {
        int success=0;
        ProgressDialog progressDialog;

        String recordId,contactId,companyName,to_emailID,subject,compose;

        public SendEmailCall(String recordId, String contactId, String companyName, String to_emailID, String subject, String compose)
        {
            this.recordId=recordId;
            this.contactId=contactId;
            this.companyName=companyName;
            this.to_emailID=to_emailID;
            this.subject=subject;
            this.compose=compose;

            Log.e("GetData",this.to_emailID+","+this.contactId+","+this.recordId+","+this.companyName+","+this.subject+","+this.compose);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog=new ProgressDialog(SendMail.this);
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SendEmail";
            String METHOD_NAME = "SendEmail";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CompanyID", manager.getWG(SendMail.this));
                Request.addProperty("LogonID", manager.getUserId(SendMail.this));
                Request.addProperty("RECDNO", recordId);
                Request.addProperty("ContactID", contactId);
                Request.addProperty("CompanyName", companyName);
                Request.addProperty("ToAddress", to_emailID);
                Request.addProperty("Subject", subject);
                Request.addProperty("emailBody", compose);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                success=resultRequestSOAP.getPropertyCount();
                Log.e("resultCount",resultRequestSOAP.getPropertyCount()+"");

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String key = TypeEventData.getProperty("Key").toString();
                    String value = TypeEventData.getProperty("Value").toString();

                    Log.e("Key:Value = ",key+" : "+value);
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

            progressDialog.dismiss();
            if(success==0)
            {
                Toast.makeText(getApplicationContext(),"Email not send.Please try again!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Successfully Email send!",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    private class LocalEmails extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog localEmailsPD;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            localEmailsPD=new ProgressDialog(SendMail.this);
            localEmailsPD.setMessage("Wait...");
            localEmailsPD.setCancelable(false);
            localEmailsPD.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            getNameEmailDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            localEmailsPD.dismiss();

            final Dialog dialog = new Dialog(SendMail.this);

            View view = getLayoutInflater().inflate(R.layout.custom_local_email, null);

            ListView lv = (ListView) view.findViewById(R.id.local_email_listview);
            Button local_email_cancel_footer_button=(Button)view.findViewById(R.id.local_email_cancel_footer_button);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(SendMail.this,android.R.layout.simple_list_item_1,names);

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
//                    Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                    sendemail_to.setText(parent.getItemAtPosition(position).toString());
                    sendemail_to.requestFocus();
                    sendemail_to.setSelection(sendemail_to.getText().length());
                    dialog.dismiss();
                    /*String to=sendemail_to.getText().toString();
                        to+=" "+item.getTitle()+" ";
                        Log.e("To",to);
                        sendemail_to.setText(to);
                        sendemail_to.requestFocus();
                        sendemail_to.setSelection(sendemail_to.getText().length());*/
                }
            });

            local_email_cancel_footer_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private class GetLibrary extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog libPD;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            libPD = new ProgressDialog(SendMail.this);
            libPD.setMessage("Wait...");
            libPD.setCancelable(false);
            libPD.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetLibraryDownloadQueueFileList";
            String METHOD_NAME = "GetLibraryDownloadQueueFileList";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("logon_id", manager.getUserId(SendMail.this));
                Request.addProperty("companyid", manager.getWG(SendMail.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String fileId = TypeEventData.getProperty("FileID").toString();
                    String fileName = TypeEventData.getProperty("FileName").toString();
                    String filePath = TypeEventData.getProperty("FilePath").toString();

                    Download_Library_Data  callbacktaskData=new Download_Library_Data();

                    if(fileId.equals("anyType{}"))
                    {
                        fileId="none";
                    }else
                    {
                        fileId=fileId+"";
                    }

                    if(fileName.equals("anyType{}"))
                    {
                        fileName="none";
                    }else
                    {
                        fileName=fileName+"";
                    }

                    if(filePath.equals("anyType{}"))
                    {
                        filePath="none";
                    }else
                    {
                        filePath=filePath+"";
                    }


                    callbacktaskData.setFileId(fileId);
                    callbacktaskData.setFileName(fileName);
                    callbacktaskData.setFilePath(filePath);

                    lib.add(callbacktaskData);

                    Log.e("recordid",fileId);


                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
                Log.e("Error",ex+"");
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            Log.e("list===============", ""+lib.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            libPD.dismiss();

            final Dialog dialog = new Dialog(SendMail.this);

            View view = getLayoutInflater().inflate(R.layout.custom_local_email, null);

            ListView lv = (ListView) view.findViewById(R.id.local_email_listview);
            Button local_email_cancel_footer_button=(Button)view.findViewById(R.id.local_email_cancel_footer_button);

            lv.setAdapter(new SendMail_LibAdapter(SendMail.this, lib));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    final Download_Library_Data currentListData =adapter.getItem(position);

                    Log.e("FilePath",currentListData.getFilePath());
                    Log.e("FileName",currentListData.getFileName());

                    attachment_container.removeAllViews();
                    filename.add(currentListData.getFileName());
                    filePath.add(currentListData.getFilePath());

                    attachment_container_child=new LinearLayout[filename.size()];
                    displayLib=new TextView[filename.size()];
                    displayPath=new TextView[filename.size()];
                    deleteLib=new TextView[filename.size()];

                    Log.e("Size",filename.size()+"");

                    for(int i=0;i<filename.size();i++)
                    {
                        attachment_container_child[i]=new LinearLayout(SendMail.this);
                        LinearLayout.LayoutParams parentparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        parentparam.setMargins(0,10,0,10);
                        attachment_container_child[i].setLayoutParams(parentparam);
                        attachment_container_child[i].setOrientation(LinearLayout.HORIZONTAL);
                        attachment_container_child[i].setWeightSum(5);
                        attachment_container_child[i].setBackgroundResource(R.drawable.custom_shadow_attachment);

                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 4);
                        param.setMargins(10,10,0,10);
                        displayLib[i]=new TextView(SendMail.this);
                        displayLib[i].setLayoutParams(param);
                        displayLib[i].setText(filename.get(i));
                        displayLib[i].setTextSize(18);
                        displayLib[i].setSingleLine(true);
                        displayLib[i].setEllipsize(TextUtils.TruncateAt.END);

                        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 4);
                        param.setMargins(10,10,0,10);
                        displayPath[i]=new TextView(SendMail.this);
                        displayPath[i].setLayoutParams(param3);
                        displayPath[i].setText(convertLibToBase64(filePath.get(i)));
                        displayPath[i].setTextSize(18);
                        displayPath[i].setVisibility(View.GONE);
                        displayPath[i].setSingleLine(true);
                        displayPath[i].setEllipsize(TextUtils.TruncateAt.END);

                        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                        param1.setMargins(10,10,10,10);
                        deleteLib[i]=new TextView(SendMail.this);
                        deleteLib[i].setLayoutParams(param1);
                        deleteLib[i].setGravity(Gravity.RIGHT);
                        deleteLib[i].setTextColor(Color.parseColor("#616161"));
                        deleteLib[i].setText(getResources().getString(R.string.fa_addDrawer_remove));
                        deleteLib[i].setTextSize(18);


                        final int finalAttach_count = i;
                        deleteLib[finalAttach_count].setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
//                                Toast.makeText(getApplicationContext(),displayPath[finalAttach_count].getText().toString(),Toast.LENGTH_LONG).show();
                                filename.remove(finalAttach_count);
                                filePath.remove(finalAttach_count);
                                attachment_container_child[finalAttach_count].removeAllViews();
                            }
                        });

                        attachment_container_child[i].addView(displayLib[i]);
                        attachment_container_child[i].addView(deleteLib[i]);

                        attachment_container.addView(attachment_container_child[i]);

                        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
                        FontManager.markAsIconContainer(deleteLib[i], iconFont);
                    }
                    dialog.dismiss();
                }
            });

            local_email_cancel_footer_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public String convertLibToBase64(String libPath)
    {
        Log.e("path",libPath);
        byte[] data;
        String base64 = null;
        try
        {
            data = libPath.getBytes("UTF-8");
             base64 = Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("CovertError",e+"");
        }
        Log.e("convertpath",base64);
        return base64;
    }

    /*Code for create chips*/
    /*final Editable e = sendemail_to.getEditableText();
    final SpannableStringBuilder sb = new SpannableStringBuilder();
    sb.append("test");
    sb.setSpan(new ImageSpan(this, android.R.drawable.btn_star), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    e.append(sb);*/
    public void setChips()
    {
        SpannableStringBuilder ssb = new SpannableStringBuilder(sendemail_to.getText());
        // split string wich comma
        String chips[] = sendemail_to.getText().toString().trim().split(" ");
        int x =0;
        // loop will generate ImageSpan for every country name separated by comma
        for(String c : chips)
        {
            // inflate chips_edittext layout
            LayoutInflater lf = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            TextView textView = (TextView) lf.inflate(R.layout.chips_edittext, null);
            textView.setText(c); // set text
//                setFlags(textView, c); // set flag image
            // capture bitmapt of genreated textview
            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(spec, spec);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.translate(-textView.getScrollX(), -textView.getScrollY());
            textView.draw(canvas);
            textView.setDrawingCacheEnabled(true);
            Bitmap cacheBmp = textView.getDrawingCache();
            Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
            textView.destroyDrawingCache();  // destory drawable
            // create bitmap drawable for imagespan
            BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
            bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
            // create and set imagespan
            ssb.setSpan(new ImageSpan(bmpDrawable),x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            x = x+ c.length() +1;
        }
        // set chips span
        sendemail_to.setText(ssb);
        // move cursor to last
        sendemail_to.setSelection(sendemail_to.getText().length());
    }

    public static boolean isValidEmail(CharSequence target)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void callLocalEmail()
    {

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
            LocalEmails localEmails=new LocalEmails();
            localEmails.execute();
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
                    LocalEmails localEmails=new LocalEmails();
                    localEmails.execute();
                    Log.e("MainPermissionResult : ", "If shouldShowRequestPermissionRationale");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        Log.e("filepath",data.getData().toString());
//                        iv.setImageBitmap(bitmap);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(SendMail.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
