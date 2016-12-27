package com.customCRM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CheckForMatch_Activity extends Fragment
{
    ListView lvCheckForMatch;
    ArrayList list;
    ProgressDialog pd;
    private Activity mActivity;
    SessionManager manager;

    String company="",phone="",email="",address="",lastName="";
    Button checkForMatch_btn;
    EditText company_et,phone_et,address_et,email_et,lastname_et;
    int totalRcordDIsplay;

    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    CheckForMatch_adapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_check_for_match, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        manager=new SessionManager();
        list=new ArrayList();
        lvCheckForMatch=(ListView)view.findViewById(R.id.checkForMatch_listview);

        adapter=new CheckForMatch_adapter(mActivity, list);

        lvCheckForMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final CheckForMatch_Data currentListData = adapter.getItem(position);

                String encp=encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
                String url=manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmSalesProgress.asp?RECDNO=" + currentListData.getRecordId();
                Log.d("MainURL",url);

                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","checkForMatch");

                getActivity().startActivity(intent);

            }
        });

        company_et=(EditText)view.findViewById(R.id.checkForMatch_company_et);
        phone_et=(EditText)view.findViewById(R.id.checkForMatch_phone_et);
        address_et=(EditText)view.findViewById(R.id.checkForMatch_address_et);
        email_et=(EditText)view.findViewById(R.id.checkForMatch_email_et);
        lastname_et=(EditText)view.findViewById(R.id.checkForMatch_lastname_et);

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.cfm_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.cfm_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.cfm_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.cfm_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.cfm_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.cfm_parent), mainFont);
        }

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_CheckForMatch);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_CheckForMatch);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_CheckForMatch);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_CheckForMatch);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_CheckForMatch);


        Typeface iconFont = FontManager.getTypeface(mActivity.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container_CheckForMatch), iconFont);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("keyCode: ", ""+keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK )
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Log.i("KEYCodeBack", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Fragment fragment = new Serches_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,fragment,null);
                    fragmentTransaction.commit();

                    fragmentManager.executePendingTransactions();

                    return true;
                } else {
                    return false;
                }
            }
        });

        checkForMatch_btn=(Button)view.findViewById(R.id.check_for_match_btn);

        checkForMatch_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                list.clear();
                company=company_et.getText().toString();
                email=email_et.getText().toString();
                lastName=lastname_et.getText().toString();
                address=address_et.getText().toString();
                phone=phone_et.getText().toString();
                i=1;j=15;

                GetTotalSearch getTotalSearch=new GetTotalSearch(company,email,lastName,address,phone);
                getTotalSearch.execute();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                company=company_et.getText().toString();
                email=email_et.getText().toString();
                lastName=lastname_et.getText().toString();
                address=address_et.getText().toString();
                phone=phone_et.getText().toString();

                int x,y;

                x=i=j+1;
                diff = totalRcordDIsplay - j;
                if(diff < 15)
                {
                    y=totalRcordDIsplay;
                    nextBtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    diff=0;
                    y=j+15;
                }

                if(x>=1 && y>=15 && y<=totalRcordDIsplay)
                {
                    list.clear();
//                i=16;j=30;
                    i=j+1;
                    diff = totalRcordDIsplay - j;
                    if(diff < 15)
                    {
                        j=totalRcordDIsplay;
                        nextBtn.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        diff=0;
                        j=j+15;
                    }


                    GetTotalSearch getTotalSearch=new GetTotalSearch(company,email,lastName,address,phone);
                    getTotalSearch.execute();
                }

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                company=company_et.getText().toString();
                email=email_et.getText().toString();
                lastName=lastname_et.getText().toString();
                address=address_et.getText().toString();
                phone=phone_et.getText().toString();

                int x,y;
                x=i-15;
                if(diff!=0)
                {
                    y=j-diff;
                    diff=0;
                    Log.e("print",""+j+","+y+","+diff);
                }
                else
                {
                    y=j-15;
                    Log.e("print",""+j+","+y);
                }

                if(x>=1 && y>=15)
                {
                    list.clear();
//                i=16;j=30;
//                i=1;j=15
                    i=i-15;

                    j=y;

                    GetTotalSearch getTotalSearch=new GetTotalSearch(company,email,lastName,address,phone);
                    getTotalSearch.execute();

                }
            }
        });

        return view;
    }

    private class GetTotalSearch extends AsyncTask<Void, Void, Void>
    {
        String company,email,lastName,address,phone;

        public GetTotalSearch(String company, String email, String lastName, String address, String phone)
        {
            this.company=company;
            this.email=email;
            this.lastName=lastName;
            this.address=address;
            this.phone=phone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/CheckForMatchCount";
            String METHOD_NAME = "CheckForMatchCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("LastName", lastName);
                Request.addProperty("Company", company);
                Request.addProperty("Email", email);
                Request.addProperty("Address", address);
                Request.addProperty("Phone", phone);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("CheckForMatchCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord", ""+totalRcordDIsplay);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Post","Execute");

            if(totalRcordDIsplay==0)
            {
                pd.dismiss();

                totalIndexDisplay.setText("Record not found");
                startIndexDisplay.setText("");
                endIndexDisplay.setText("");
                nextBtn.setVisibility(View.INVISIBLE);
                prevBtn.setVisibility(View.INVISIBLE);
            }
            else
            {
                if(j > totalRcordDIsplay)
                {
                    j=totalRcordDIsplay;
                }
                if(j==totalRcordDIsplay)
                {
                    nextBtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    nextBtn.setVisibility(View.VISIBLE);
                }

                if(i==1)
                {
                    prevBtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    prevBtn.setVisibility(View.VISIBLE);
                }

                totalIndexDisplay.setText(" "+totalRcordDIsplay);
                startIndexDisplay.setText(i+" to ");
                endIndexDisplay.setText(j+" of");

                GetDataSearch getDataSearch=new GetDataSearch(i,j,company,email,lastName,address,phone);
                getDataSearch.execute();
            }
        }
    }

    private class GetDataSearch extends AsyncTask<Void, Void, Void>
    {

        int start,end;
        String company,email,lastName,address,phone;
        public GetDataSearch(int i, int j, String company, String email, String lastName, String address, String phone)
        {
            start=i;end=j;
            this.company=company;
            this.email=email;
            this.lastName=lastName;
            this.address=address;
            this.phone=phone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");
//            pd=new ProgressDialog(mActivity);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/CheckForMatch";
            String METHOD_NAME = "CheckForMatch";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("LastName", lastName);
                Request.addProperty("Company", company);
                Request.addProperty("Email", email);
                Request.addProperty("Address", address);
                Request.addProperty("Phone", phone);
                Request.addProperty("startindex", start);
                Request.addProperty("endindex", end);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);


                transport.call(SOAP_ACTION, soapEnvelope);

                Log.e("Check1","Check");
                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                Log.e("Check2","Check");
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    Log.e("Check3","Check");
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);

                    String recordID = TypeEventData.getProperty("RecordID").toString();
                    String company = TypeEventData.getProperty("Company").toString();
                    String firstName = TypeEventData.getProperty("FirstName").toString();
                    String lastName = TypeEventData.getProperty("LastName").toString();
                    String phone = TypeEventData.getProperty("Phone").toString();
//                    String website = TypeEventData.getProperty("Website").toString();

                    CheckForMatch_Data  setData=new CheckForMatch_Data();

                    if(company.equals("anyType{}"))
                    {
                        company="none";
                    }else
                    {
                        company=company+"";
                    }

                    if(firstName.equals("anyType{}"))
                    {
                        firstName="none";
                    }else
                    {
                        firstName=firstName+"";
                    }

                    if(lastName.equals("anyType{}"))
                    {
                        lastName="none";
                    }else
                    {
                        lastName=lastName+"";
                    }

                    if(phone.equals("anyType{}"))
                    {
                        phone="none";
                    }else
                    {
                        phone=phone+"";
                    }

                    setData.setRecordId(recordID);
                    setData.setCompany(company);
                    setData.setFirstName(firstName);
                    setData.setLastName(lastName);
                    setData.setPhone(phone);
//                    setData.setWebsite(website);


                    Log.e("recordid",recordID);

                    list.add(setData);

                }


                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
            } catch (Exception ex) {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            Log.e("list===============", ""+list.size());
            Log.e("i,j",""+start+","+end);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d("Post","Post");

            pd.cancel();
            lvCheckForMatch.setAdapter(new CheckForMatch_adapter(mActivity, list));
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(getActivity());
        String pass=manager.getUserPass(getActivity());
        String dbId=manager.getDB(getActivity());
        String wgId=manager.getWG(getActivity());


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
