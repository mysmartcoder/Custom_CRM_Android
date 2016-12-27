package com.customCRM;

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
import android.support.v7.app.AlertDialog;
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

public class Oppotunity_Activity extends Fragment
{
    TextView title;

    ListView lvOpp;
    ArrayList list;

    ProgressDialog pd;

    SessionManager manager;

    String searchOpportunuty=null;
    EditText searchEdit;
    ImageButton searchBtn;

    int totalRcordDIsplay;

    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    private Activity mActivity;


    String RecordsId="";
    String checkDeletion;

    Opportunity_adapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_oppotunity, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvOpp=(ListView)view.findViewById(R.id.opportunity_listview);

        searchEdit=(EditText)view.findViewById(R.id.opportunity_search_edit);
        searchEdit.setHint("Search by "+manager.getCustomLabel(mActivity,"Opportunity"));

        searchBtn=(ImageButton)view.findViewById(R.id.search_opportunity_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_Opp);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_Opp);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_Opp);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_Opp);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_Opp);

        adapter=new Opportunity_adapter(mActivity, list,i,j);

        title=(TextView)view.findViewById(R.id.opportunity_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Opportunity"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.opp_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.opp_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.opp_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.opp_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.opp_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.opp_parent), mainFont);
        }

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("query"))
            {
                Log.e("success","1");
                searchOpportunuty = bundle.getString("query");
                searchEdit.setText(searchOpportunuty);
                if(searchOpportunuty.isEmpty() || searchOpportunuty.equals("null") || searchOpportunuty == null)
                {
                }
                else
                {
                    list.clear();

                    i = 1;
                    j=15;

                    Log.e("i,j,search",i+","+j+","+searchOpportunuty);

                    SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity(searchOpportunuty);
                    searchTotalCallBackEvent.execute();
                }
            }

            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                searchOpportunuty = bundle.getString("searchQuery");

                searchEdit.setText(searchOpportunuty);
                if(searchOpportunuty.isEmpty() || searchOpportunuty.equals("null") || searchOpportunuty == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchOpportunuty);

                    SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchOpportunuty);

                    SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity(searchOpportunuty);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalOpportunity getTotalCallBackEvent=new GetTotalOpportunity();
            getTotalCallBackEvent.execute();
        }

        lvOpp.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Opportunity_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";

                String url = manager.getMainUrl(getActivity())  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_opportunity_EditForm.asp&RECDNO=" + currentListData.getRecord_id() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=opportunity&oppid="+currentListData.getOpp_id();

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","opp");
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
                intent.putExtra("frg", "opp");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);



            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchOpportunuty=searchEdit.getText().toString();

                if(searchOpportunuty.isEmpty() || searchOpportunuty.equals("null") || searchOpportunuty == null)
                {
                    Toast.makeText(getActivity(),"Please enter "+manager.getCustomLabel(mActivity,"Opportunity"),Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();
                    i=1;j=15;

                    SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity(searchOpportunuty);
                    searchTotalCallBackEvent.execute();

                }
            }
        });

        //FontAwesome Code
        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container_Opportunity), iconFont);

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchOpportunuty=searchEdit.getText().toString();

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

                    if(searchOpportunuty.equals("null") && searchOpportunuty == null && searchOpportunuty.isEmpty())
                    {
                        GetTotalOpportunity getTotalCallBackEvent=new GetTotalOpportunity();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity(searchOpportunuty);
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
                searchOpportunuty=searchEdit.getText().toString();

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
//                    j=j-15;
                    j=y;

                    if(searchOpportunuty.isEmpty() || searchOpportunuty.equals("null") || searchOpportunuty == null)
                    {
                        GetTotalOpportunity getTotalCallBackEvent=new GetTotalOpportunity();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {


                        SearchTotalOpportunity searchTotalCallBackEvent=new SearchTotalOpportunity(searchOpportunuty);
                        searchTotalCallBackEvent.execute();
                    }
                }

            }
        });

//        GetTotalOpportunity getTotalCallBackEvent=new GetTotalOpportunity();
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


    private class GetSearchOpportunity extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        Exception e=null;
        public GetSearchOpportunity(int i, int j)
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

            GetData(i,j,e);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            if(e!=null)
            {
                new AlertDialog.Builder(getActivity()).setMessage("2 Server not Responding").show();
            }
            lvOpp.setAdapter(new Opportunity_adapter(mActivity, list,i,j));


        }
    }
    private void GetData(int start, int end,Exception e)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchOpportunityData";
        String METHOD_NAME = "SearchOpportunityData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("oppname", "");

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

                String policyName = TypeEventData.getProperty("OppName").toString();
                String act = TypeEventData.getProperty("Company").toString();
                String opp = TypeEventData.getProperty("OppTotal").toString();
                String opp_stage = TypeEventData.getProperty("SalesStatus").toString();
                String acc_mgr = TypeEventData.getProperty("AcctMgr").toString();
                String phone = TypeEventData.getProperty("Phone").toString();

                String record_id = TypeEventData.getProperty("RECDNO").toString();
                String opp_id = TypeEventData.getProperty("OPPID").toString();
                String website = TypeEventData.getProperty("Website").toString();

                Opportunity_Data  callbacktaskData=new Opportunity_Data();

                if(policyName.equals("anyType{}"))
                {
                    policyName="none";
                }else
                {
                    policyName=policyName+"";
                }

                if(act.equals("anyType{}"))
                {
                    act="none";
                }else
                {
                    act=act+"";
                }

                if(opp.equals("anyType{}"))
                {
                    opp="none";
                }else
                {
                    opp=opp+"";
                }

                if(opp_stage.equals("anyType{}"))
                {
                    opp_stage="none";
                }else
                {
                    opp_stage=opp_stage+"";
                }

                if(acc_mgr.equals("anyType{}"))
                {
                    acc_mgr="none";
                }else
                {
                    acc_mgr=acc_mgr+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(website.equals("anyType{}"))
                {
                    website="none";
                }else
                {
                    website=website+"";
                }

                callbacktaskData.setPolicy_name(policyName);
                callbacktaskData.setAccount(act);
                callbacktaskData.setOpportunity(opp);
                callbacktaskData.setOpportunity_stage(opp_stage);
                callbacktaskData.setAcct_mgr(acc_mgr);
                callbacktaskData.setPhone(phone);

                callbacktaskData.setRecord_id(record_id);
                callbacktaskData.setOpp_id(opp_id);
                callbacktaskData.setUrl(website);

                list.add(callbacktaskData);


                Log.e("recordid",opp_id);

            }


            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
        } catch (Exception ex) {
            e=ex;
//            Toast.makeText(getActivity(),""+ex,Toast.LENGTH_LONG).show();
           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+""+j);
    }


    private class GetTotalOpportunity extends AsyncTask<Void, Void, Void>
    {

        Exception e=null;
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
            String SOAP_ACTION = "LMServiceNamespace/SearchOpportunityCount";
            String METHOD_NAME = "SearchOpportunityCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("oppname", "");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchOpportunityCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                manager.setCounts(getActivity(),manager.getAccount(getActivity()),manager.getCon(getActivity()),""+totalRcordDIsplay,manager.getCases(getActivity()),manager.getQuote(getActivity()));

                Log.e("TotalRecord", ""+totalRcordDIsplay);
            }
            catch (Exception ex)
            {
                e=ex;
//               Toast.makeText(getActivity(),""+ex,Toast.LENGTH_LONG).show();
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Opportunity")+" not found");
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

                GetSearchOpportunity getSearchCallBackEvent=new GetSearchOpportunity(i,j);
                getSearchCallBackEvent.execute();
            }

        }
    }

    //For Search by Opportunity...


    private class SearchTotalOpportunity extends AsyncTask<Void, Void, Void>
    {

        String opp_name;

        public SearchTotalOpportunity(String searchOpportunuty)
        {
            opp_name=searchOpportunuty;
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
            String SOAP_ACTION = "LMServiceNamespace/SearchOpportunityCount";
            String METHOD_NAME = "SearchOpportunityCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("oppname", opp_name);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchOpportunityCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord",""+ totalRcordDIsplay);
                Log.e("Opp_name", opp_name);
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Opportunity")+" not found");
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

                SearchCompanyOppotunity searchCompanyAccount=new SearchCompanyOppotunity(i,j,opp_name);
                searchCompanyAccount.execute();
            }
        }
    }

    private class SearchCompanyOppotunity extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String opp_name;

        public SearchCompanyOppotunity(int i, int j, String opp_name)
        {
            this.i=i;
            this.j=j;
            this.opp_name=opp_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");

//            pd=new ProgressDialog(mActivity);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
//            list=new ArrayList();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            searchCompany(i,j,opp_name);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();

            lvOpp.setAdapter(new Opportunity_adapter(mActivity, list,i,j));
        }
    }

    private void searchCompany(int start, int end, String oppName)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchOpportunityData";
        String METHOD_NAME = "SearchOpportunityData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("oppname", oppName);


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
                String policyName = TypeEventData.getProperty("OppName").toString();
                String act = TypeEventData.getProperty("Company").toString();
                String opp = TypeEventData.getProperty("OppTotal").toString();
                String opp_stage = TypeEventData.getProperty("SalesStatus").toString();
                String acc_mgr = TypeEventData.getProperty("AcctMgr").toString();
                String phone = TypeEventData.getProperty("Phone").toString();

                String record_id = TypeEventData.getProperty("RECDNO").toString();
                String opp_id = TypeEventData.getProperty("OPPID").toString();
                String website = TypeEventData.getProperty("Website").toString();

                Opportunity_Data  callbacktaskData=new Opportunity_Data();

                if(policyName.equals("anyType{}"))
                {
                    policyName="none";
                }else
                {
                    policyName=policyName+"";
                }

                if(act.equals("anyType{}"))
                {
                    act="none";
                }else
                {
                    act=act+"";
                }

                if(opp.equals("anyType{}"))
                {
                    opp="none";
                }else
                {
                    opp=opp+"";
                }

                if(opp_stage.equals("anyType{}"))
                {
                    opp_stage="none";
                }else
                {
                    opp_stage=opp_stage+"";
                }

                if(acc_mgr.equals("anyType{}"))
                {
                    acc_mgr="none";
                }else
                {
                    acc_mgr=acc_mgr+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(website.equals("anyType{}"))
                {
                    website="none";
                }else
                {
                    website=website+"";
                }


                callbacktaskData.setPolicy_name(policyName);
                callbacktaskData.setAccount(act);
                callbacktaskData.setOpportunity(opp);
                callbacktaskData.setOpportunity_stage(opp_stage);
                callbacktaskData.setAcct_mgr(acc_mgr);
                callbacktaskData.setPhone(phone);

                callbacktaskData.setRecord_id(record_id);
                callbacktaskData.setOpp_id(opp_id);
                callbacktaskData.setUrl(website);

                list.add(callbacktaskData);

            }


            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
        } catch (Exception ex) {
//           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
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
