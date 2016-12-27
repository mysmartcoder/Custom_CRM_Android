package com.leadmaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageButton;
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

public class Cases_Activity extends Fragment
{
    TextView title;

    ListView lvCase;
    ArrayList list;

    ProgressDialog pd;

    SessionManager manager;

    String searchCaseName=null;
    EditText searchEdit;
    ImageButton searchBtn;

    String result;
    int totalRcordDIsplay;
    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    private Activity mActivity;

    Cases_Adapter adapter=null;

    String RecordsId="";
    String checkDeletion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_cases, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvCase=(ListView)view.findViewById(R.id.case_listview);

        searchEdit=(EditText)view.findViewById(R.id.cases_search_edit);
        searchEdit.setHint("Search by "+manager.getCustomLabel(mActivity,"Case"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.case_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.case_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.case_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.case_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.case_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.case_parent), mainFont);
        }

        searchBtn=(ImageButton)view.findViewById(R.id.search_case_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_Case);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_Case);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_Case);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_Case);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_Case);

        adapter=new Cases_Adapter(mActivity, list,i,j);

        title=(TextView)view.findViewById(R.id.case_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Case"));

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("query"))
            {
                Log.e("success","1");
                searchCaseName = bundle.getString("query");
                searchEdit.setText(searchCaseName);
                if(searchCaseName.isEmpty() || searchCaseName.equals("null") || searchCaseName == null)
                {
                }
                else
                {
                    list.clear();

                    i = 1;
                    j=15;

                    Log.e("i,j,search",i+","+j+","+searchCaseName);

                    SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase(searchCaseName);
                    searchTotalCallBackEvent.execute();
                }
            }

            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                searchCaseName = bundle.getString("searchQuery");

                searchEdit.setText(searchCaseName);
                if(searchCaseName.isEmpty() || searchCaseName.equals("null") || searchCaseName == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchCaseName);

                    SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchCaseName);

                    SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase(searchCaseName);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalCase getTotalCallBackEvent=new GetTotalCase();
            getTotalCallBackEvent.execute();
        }



        lvCase.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Cases_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();

                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAddCase.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword= &pagetype=case&id="+currentListData.getCaseId();

                Log.d("MainURL",url);

              /*  Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","case");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchEdit.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "case");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchCaseName=searchEdit.getText().toString();

                if(searchCaseName.isEmpty() || searchCaseName.equals("null") || searchCaseName == null)
                {
                    Toast.makeText(getActivity(),"Please enter "+manager.getCustomLabel(mActivity,"Case"),Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();

                    i=1;j=15;
                    SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase(searchCaseName);
                    searchTotalCallBackEvent.execute();

                }
            }
        });

        //FontAwesome Code
        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container_Account), iconFont);

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchCaseName=searchEdit.getText().toString();

                int x,y;

                x=i=j+1;
//                y=j+15;
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
//                    j=j+15;
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

                    if(searchCaseName.equals("null") && searchCaseName == null && searchCaseName.isEmpty())
                    {
                        GetTotalCase getTotalCallBackEvent=new GetTotalCase();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase(searchCaseName);
                        searchTotalCallBackEvent.execute();
                    }

                }

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchCaseName=searchEdit.getText().toString();

                int x,y;
                x=i-15;
//                y=j-15;

                if(diff!=0)
                {
                    y=j-diff;
                    diff=0;
                }
                else
                {
                    y=j-15;
                }


                if(x>=1 && y>=15)
                {
                    list.clear();
//                i=16;j=30;
//                i=1;j=15
                    i=i-15;
//                    j=j-15;
                    j=y;
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    if(searchCaseName.isEmpty() || searchCaseName.equals("null") || searchCaseName == null)
                    {
                        GetTotalCase getTotalCallBackEvent=new GetTotalCase();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCase searchTotalCallBackEvent=new SearchTotalCase(searchCaseName);
                        searchTotalCallBackEvent.execute();
                    }
                }

            }
        });



//        GetTotalCase getTotalCallBackEvent=new GetTotalCase();
//        getTotalCallBackEvent.execute();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK )
//                    if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    // handle back button's click listener

//                    getFragmentManager().popBackStack();
                    Fragment fragment = new Dashboard_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,fragment,null);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private class GetSearchCase extends AsyncTask<Void, Void, Void>
    {

        int i,j;

        public GetSearchCase(int i, int j)
        {
            this.i=i;
            this.j=j;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            GetData(i,j);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
//            Log.e("post","post");
//            if( result != null)
//            {
//                new AlertDialog.Builder(mActivity).setMessage("Server not responding").show();
//            }
//            Log.e("result",result);
//            if(result.equals("null"))
//            {
//                Log.e("alert","alert");
//                new AlertDialog.Builder(mActivity).setMessage("Server not responding").show();
//            }
//            else
//            {
                lvCase.setAdapter(new Cases_Adapter(mActivity, list,i,j));
//            }


        }
    }
    private void GetData(int start, int end)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchCasesData";
        String METHOD_NAME = "SearchCasesData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("subject", "");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,30000);

            transport.call(SOAP_ACTION, soapEnvelope);
            //list=new ArrayList();


            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

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

                Cases_Data  callbacktaskData=new Cases_Data();

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
        } catch (Exception ex) {
            result=""+ex;
//           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("check", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+""+j);
    }

    private class GetTotalCase extends AsyncTask<Void, Void, Void>
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
            String SOAP_ACTION = "LMServiceNamespace/SearchCasesCount";
            String METHOD_NAME = "SearchCasesCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("subject", "");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchCasesCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                manager.setCounts(getActivity(),manager.getAccount(getActivity()),manager.getCon(getActivity()),manager.getOpp(getActivity()),""+totalRcordDIsplay,manager.getQuote(getActivity()));
                Log.e("TotalRecord",""+ totalRcordDIsplay);
            }
            catch (Exception ex)
            {
                result=""+ex;
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Case")+" not found");
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
//                    j=totalRcordDIsplay;
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

                GetSearchCase getSearchCallBackEvent=new GetSearchCase(i,j);
                getSearchCallBackEvent.execute();
            }

//            }

        }
    }


    //For Search by Company Name...

    private class SearchTotalCase extends AsyncTask<Void, Void, Void>
    {

        String sub;
        public SearchTotalCase(String searchSubject)
        {
            sub=searchSubject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Accounts_Activity.this);
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
//            list=new ArrayList();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SearchCasesCount";
            String METHOD_NAME = "SearchCasesCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("subject", sub);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchCasesCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord",""+ totalRcordDIsplay);
                Log.e("Company", sub);
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Case")+" not found");
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

                SearchCase searchCompanyAccount=new SearchCase(i,j,sub);
                searchCompanyAccount.execute();
            }
        }
    }

    private class SearchCase extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String sub;

        public SearchCase(int i, int j, String sub)
        {
            this.i=i;
            this.j=j;
            this.sub=sub;
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
            searchCase(i,j,sub);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.cancel();
            lvCase.setAdapter(new Cases_Adapter(mActivity, list,i,j));
        }
    }

    private void searchCase(int start, int end, String searchCompanyName)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchCasesData";
        String METHOD_NAME = "SearchCasesData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("subject", searchCompanyName);


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

                Cases_Data  callbacktaskData=new Cases_Data();

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
        } catch (Exception ex) {
//           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("2", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+","+j);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        pd.cancel();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        pd.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

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
