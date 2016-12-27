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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MS_Case_Activity extends Fragment
{
    TextView title;

    ListView lvMSRecords;
    ArrayList list;

    String[] filterList;

    ProgressDialog pd;

    SessionManager manager;

    String searchCompanyName=null;

//    EditText searchEdit;

    ImageButton searchBtn;

    int totalRcordDIsplay;
    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    //    int totalPageNumber=0;
    int totalPageNumber=0;
    int changePageNumber=1;

    static int check=0;
    private Activity mActivity;

    MS_Case_adapter adapter=null;

    String RecordsId="";
    String checkDeletion;

    String searchId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_ms__case, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
//        filterList=new List();

        lvMSRecords=(ListView)view.findViewById(R.id.mscase_listview);

        pd=new ProgressDialog(mActivity);

        check=0;

        title=(TextView)view.findViewById(R.id.mscase_toolbar_title);

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.mscase_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.mscase_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mscase_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mscase_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mscase_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mscase_parent), mainFont);
        }

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("titlebar"))
            {
                title.setText(bundle.getString("titlebar"));
            }

            if(bundle.containsKey("searchID"))
            {
                searchId=bundle.getString("searchID");
            }
        }

        Log.e("SearchId",searchId);

        totalIndexDisplay=(TextView)view.findViewById(R.id.mscase_totalIndex);
        startIndexDisplay=(TextView)view.findViewById(R.id.mscase_startingIndex);
        endIndexDisplay=(TextView)view.findViewById(R.id.mscase_endingIndex);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_MSCase);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_MSCase);

        adapter=new MS_Case_adapter(mActivity, list,i,j);

        GetTotalAccount getTotalAccount=new GetTotalAccount();
        getTotalAccount.execute();

        lvMSRecords.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final MS_Case_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
//                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=account";
//                String url = manager.getMainUrl(getActivity())  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_opportunity_EditForm.asp&RECDNO=" + currentListData.getRecord_id() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=opportunity&oppid="+currentListData.getOpp_id();
                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAddCase.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword= &pagetype=case&id="+currentListData.getCaseId();

                Log.d("MainURL",url);

                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "mscase");
//                intent.putExtra("startIndex", i);
//                intent.putExtra("endIndex", j);
//                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);

            }
        });

        //FontAwesome Code
        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container_MSCase), iconFont);

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                if(changePageNumber <= totalPageNumber)
//                {
                changePageNumber++;
//                }
                Log.e("ChangePageNumber",changePageNumber+"");
//                Log.e("TotalPageNumber",totalPageNumber+"");

//                if(check!=1)
//                {
//                    Log.e("check","execute");
//                    searchCompanyName=searchEdit.getText().toString();
//                }

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

//                    if(searchCompanyName.equals("null") && searchCompanyName == null && searchCompanyName.isEmpty())
//                    {
                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
                    getTotalCallBackEvent.execute();
//                    }
//                    else
//                    {
//                        SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
//                        searchTotalCallBackEvent.execute();
//                    }

                }

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                if(changePageNumber <= totalPageNumber)
//                {
                changePageNumber--;
//                }
                Log.e("ChangePageNumber",changePageNumber+"");
//                Log.e("TotalPageNumber",totalPageNumber+"");

//                if(check!=1)
//                {
//                    Log.e("check","execute");
//                    searchCompanyName=searchEdit.getText().toString();
//                }


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
                    Log.e("exteute","true");
                    list.clear();

                    i=i-15;
                    j=y;

//                    if(searchCompanyName.isEmpty() || searchCompanyName.equals("null") || searchCompanyName == null)
//                    {
                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
                    getTotalCallBackEvent.execute();
//                    }
//                    else
//                    {
//                        SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
//                        searchTotalCallBackEvent.execute();
//                    }
                }
                else
                {
                    Log.e("exteute","false");
                    Log.e("exteute",""+x+","+y);
                }

            }
        });


        return view;
    }

    private class GetTotalAccount extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("TotalData","TotalData");

            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetSavedSearchDataCount";
            String METHOD_NAME = "GetSavedSearchDataCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("LogonID", manager.getUserId(getActivity()));
                Request.addProperty("CompanyID", manager.getWG(getActivity()));
                Request.addProperty("SearchID", searchId);
                Request.addProperty("searchType", "case");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                Log.e("TotalCount",resultRequestSOAP.getProperty("GetSavedSearchDataCountResult").toString());

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("GetSavedSearchDataCountResult").toString());

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
            Log.d("Post","Execute");

            if(totalRcordDIsplay==0)
            {
                pd.dismiss();

                totalIndexDisplay.setText("Case not found");
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

                GetSearchAccount getSearchCallBackEvent=new GetSearchAccount(i,j);
                getSearchCallBackEvent.execute();
//                GetSearchAccount getSearchCallBackEvent=new GetSearchAccount();
//                getSearchCallBackEvent.execute();
            }
        }
    }

    private class GetSearchAccount extends AsyncTask<Void, Void, Void>
    {
        int i,j;
        //
        public GetSearchAccount(int i, int j)
        {
            this.i=i;
            this.j=j;
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
//            GetData(i,j);
            GetData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            if(list.size()==0)
            {
                totalIndexDisplay.setText("Case not found");
                startIndexDisplay.setText("");
                endIndexDisplay.setText("");

                nextBtn.setVisibility(View.INVISIBLE);
                prevBtn.setVisibility(View.INVISIBLE);
            }
            else
            {
                lvMSRecords.setAdapter(new MS_Case_adapter(mActivity, list,i,j));
            }
        }
    }
    //    private void GetData(int start, int end)
    private void GetData()
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/GetSavedSearchData";
        String METHOD_NAME = "GetSavedSearchData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("LogonID", manager.getUserId(getActivity()));
            Request.addProperty("CompanyID", manager.getWG(getActivity()));
            Request.addProperty("SearchID", searchId);
            Request.addProperty("PageNumber", changePageNumber);
            Request.addProperty("Pagesize", 15);
            Request.addProperty("searchType", "case");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,30000);

            transport.call(SOAP_ACTION, soapEnvelope);
            //list=new ArrayList();


            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
            // resultString = (SoapPrimitive) soapEnvelope.getResponse();
            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
            {
                SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);

                String entered = TypeEventData.getProperty("Entered").toString();
                String dateDue = TypeEventData.getProperty("DateDue").toString();
                String company = TypeEventData.getProperty("Company").toString();
                String subject = TypeEventData.getProperty("Subject").toString();
                String status = TypeEventData.getProperty("Status").toString();
                String priority = TypeEventData.getProperty("Priority").toString();
                String owner = TypeEventData.getProperty("Owner").toString();
                String phone = TypeEventData.getProperty("Phone").toString();

                String recordId = TypeEventData.getProperty("RecordID").toString();
                String latitude = TypeEventData.getProperty("Latitude").toString();
                String longitude = TypeEventData.getProperty("Longitude").toString();
                String caseID = TypeEventData.getProperty("CaseID").toString();

                MS_Case_Data  callbacktaskData=new MS_Case_Data();

                if(entered.equals("anyType{}"))
                {
                    entered="none";
                }else
                {
                    entered=entered+"";
                }

                if(company.equals("anyType{}"))
                {
                    company="none";
                }else
                {
                    company=company+"";
                }

                if(dateDue.equals("anyType{}"))
                {
                    dateDue="none";
                }else
                {
                    dateDue=dateDue+"";
                }

                if(subject.equals("anyType{}"))
                {
                    subject="none";
                }else
                {
                    subject=subject+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(status.equals("anyType{}"))
                {
                    status="none";
                }else
                {
                    status=status+"";
                }

                if(priority.equals("anyType{}"))
                {
                    priority="none";
                }else
                {
                    priority=priority+"";
                }
                if(owner.equals("anyType{}"))
                {
                    owner="none";
                }else
                {
                    owner=owner+"";
                }


                callbacktaskData.setEntered(entered);
                callbacktaskData.setDatedue(dateDue);
                callbacktaskData.setCompany(company);
                callbacktaskData.setSubject(subject);
                callbacktaskData.setStatus(status);
                callbacktaskData.setPriority(priority);
                callbacktaskData.setOwner(owner);
                callbacktaskData.setPhone(phone);

                callbacktaskData.setRecordId(recordId);
                callbacktaskData.setCaseId(caseID);
                callbacktaskData.setLatitude(latitude);
                callbacktaskData.setLongitude(longitude);

                list.add(callbacktaskData);

                Log.e("recordid",recordId);
                Log.e("lat_long",latitude+","+longitude);


            }


            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
        }
        catch (Exception ex)
        {
            Log.e("", "Error: " + ex.getMessage());
            Log.e("", "Error: " + ex+"");
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
//        Log.e("i,j",""+i+""+j);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                Log.e("keycode,event",keyCode+","+event);
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
//                if(event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Fragment fragment = new MyShortcuts_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,fragment,null);
                    fragmentTransaction.commit();
//
//                    Intent i=new Intent(getActivity(),MainActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(i);

                    return true;
                }
                return false;
            }
        });

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
