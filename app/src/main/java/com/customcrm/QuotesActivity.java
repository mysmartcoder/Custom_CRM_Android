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

public class QuotesActivity extends Fragment
{

    TextView title;

    ListView lvQuote;
    ArrayList list;

    ProgressDialog pd;

    SessionManager manager;

    String searchQuote=null;

    EditText searchEdit;
    ImageButton searchBtn;

    int totalRcordDIsplay=0;

    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    private Activity mActivity;


    String RecordsId="";
    String checkDeletion;

    Quotes_Adapter adapter=null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_quotes, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvQuote=(ListView)view.findViewById(R.id.quote_listview);

        searchEdit=(EditText)view.findViewById(R.id.quote_search_edit);

//        searchEdit.setHint("Search by "+manager.getCustomLabel(mActivity,"Opportunity"));

        searchBtn=(ImageButton)view.findViewById(R.id.search_quote_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_Quote);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_Quote);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_Quote);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_Quote);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_Quote);

        adapter=new Quotes_Adapter(mActivity, list,i,j);

        title=(TextView)view.findViewById(R.id.quote_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Quote"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.quote_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.quote_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.quote_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.quote_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.quote_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.quote_parent), mainFont);
        }

//        title.setText(manager.getCustomLabel(mActivity,"Opportunity"));

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                searchQuote = bundle.getString("searchQuery");

                searchEdit.setText(searchQuote);
                if(searchQuote.isEmpty() || searchQuote.equals("null") || searchQuote == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchQuote);

                    SearchTotalQuote searchTotalCallBackEvent=new SearchTotalQuote("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchQuote);

                    SearchTotalQuote searchTotalCallBackEvent=new SearchTotalQuote(searchQuote);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalQuote getTotalCallBackEvent=new GetTotalQuote();
            getTotalCallBackEvent.execute();
        }

        lvQuote.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Quotes_Data currentListData =adapter.getItem(position);

                String encp=encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";

//                String url = manager.getMainUrl()  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_opportunity_EditForm.asp&RECDNO=" + currentListData.getRecord_id() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=opportunity&oppid="+currentListData.getOpp_id();

                String url = manager.getMainUrl(getActivity())  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_quote_EditForm.asp&RECDNO=" + currentListData.getRecord_id() +"&QuoteID="+currentListData.getQuoteID() + "&CompanyID=" + manager.getWG(mActivity) + "&sid="+manager.getUserId(mActivity)+"&appkeyword=&pagetype=quote";
                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","quote");
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
                intent.putExtra("frg", "quote");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);



            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchQuote=searchEdit.getText().toString();

                if(searchQuote.isEmpty() || searchQuote.equals("null") || searchQuote == null)
                {
                    Toast.makeText(getActivity(),"Please enter "+manager.getCustomLabel(mActivity,"Quote"),Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();
                    i=1;j=15;

                    SearchTotalQuote searchTotalCallBackEvent=new SearchTotalQuote(searchQuote);
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
                searchQuote=searchEdit.getText().toString();

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

                    if(searchQuote.equals("null") && searchQuote == null && searchQuote.isEmpty())
                    {
                        GetTotalQuote getTotalCallBackEvent=new GetTotalQuote();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalQuote searchTotalCallBackEvent=new SearchTotalQuote(searchQuote);
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
                searchQuote=searchEdit.getText().toString();

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
                    if(searchQuote.isEmpty() || searchQuote.equals("null") || searchQuote == null)
                    {
                        GetTotalQuote getTotalCallBackEvent=new GetTotalQuote();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalQuote searchTotalCallBackEvent=new SearchTotalQuote(searchQuote);
                        searchTotalCallBackEvent.execute();
                    }
                }

            }
        });

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
    private class GetSearchQuote extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        Exception e=null;
        public GetSearchQuote(int i, int j)
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
                new AlertDialog.Builder(getActivity()).setMessage("Server not Responding").show();
            }
            lvQuote.setAdapter(new Quotes_Adapter(mActivity, list,i,j));


        }
    }
    private void GetData(int start, int end,Exception e)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchQuoteData";
        String METHOD_NAME = "SearchQuoteData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("quotename", "");


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

                String quoteName = TypeEventData.getProperty("QuoteName").toString();
                String company = TypeEventData.getProperty("Company").toString();
                String firstName = TypeEventData.getProperty("FirstName").toString();
                String lastName = TypeEventData.getProperty("LastName").toString();
                String quoteTotal = TypeEventData.getProperty("QuoteTotal").toString();


                String record_id = TypeEventData.getProperty("RECDNO").toString();
                String quoteID = TypeEventData.getProperty("QuoteID").toString();

                Quotes_Data  callbacktaskData=new Quotes_Data();

                if(quoteName.equals("anyType{}"))
                {
                    quoteName="none";
                }else
                {
                    quoteName=quoteName+"";
                }

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

                if(quoteTotal.equals("anyType{}"))
                {
                    quoteTotal="none";
                }else
                {
                    quoteTotal=quoteTotal+"";
                }



                callbacktaskData.setQuoteName(quoteName);
                callbacktaskData.setCompany(company);
                callbacktaskData.setFirstName(firstName);
                callbacktaskData.setLastName(lastName);
                callbacktaskData.setQuoteTotal(quoteTotal);

                callbacktaskData.setRecord_id(record_id);
                callbacktaskData.setQuoteID(quoteID);

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


    private class GetTotalQuote extends AsyncTask<Void, Void, Void>
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
            String SOAP_ACTION = "LMServiceNamespace/SearchQuoteCount";
            String METHOD_NAME = "SearchQuoteCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("quotename", "");


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchQuoteCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                manager.setCounts(getActivity(),manager.getAccount(getActivity()),manager.getCon(getActivity()),manager.getOpp(getActivity()),manager.getCases(getActivity()),""+totalRcordDIsplay);

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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Quote")+" not found");
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

                GetSearchQuote getSearchCallBackEvent=new GetSearchQuote(i,j);
                getSearchCallBackEvent.execute();
            }
        }
    }

    //For Search by Opportunity...


    private class SearchTotalQuote extends AsyncTask<Void, Void, Void>
    {

        String opp_name;

        public SearchTotalQuote(String searchOpportunuty)
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
            String SOAP_ACTION = "LMServiceNamespace/SearchQuoteCount";
            String METHOD_NAME = "SearchQuoteCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("quotename", opp_name);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchQuoteCountResult").toString());
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Quote")+" not found");
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
//                j=totalRcordDIsplay;
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

                SearchCompanyQuote searchCompanyAccount=new SearchCompanyQuote(i,j,opp_name);
                searchCompanyAccount.execute();
            }
        }
    }


    private class SearchCompanyQuote extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String opp_name;

        public SearchCompanyQuote(int i, int j, String opp_name)
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

            lvQuote.setAdapter(new Quotes_Adapter(mActivity, list,i,j));
        }
    }

    private void searchCompany(int start, int end, String oppName)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchQuoteData";
        String METHOD_NAME = "SearchQuoteData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("quotename", oppName);


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

                String quoteName = TypeEventData.getProperty("QuoteName").toString();
                String company = TypeEventData.getProperty("Company").toString();
                String firstName = TypeEventData.getProperty("FirstName").toString();
                String lastName = TypeEventData.getProperty("LastName").toString();
                String quoteTotal = TypeEventData.getProperty("QuoteTotal").toString();


                String record_id = TypeEventData.getProperty("RECDNO").toString();
                String quoteID = TypeEventData.getProperty("QuoteID").toString();

                Quotes_Data  callbacktaskData=new Quotes_Data();

                if(quoteName.equals("anyType{}"))
                {
                    quoteName="none";
                }else
                {
                    quoteName=quoteName+"";
                }

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

                if(quoteTotal.equals("anyType{}"))
                {
                    quoteTotal="none";
                }else
                {
                    quoteTotal=quoteTotal+"";
                }




                callbacktaskData.setQuoteName(quoteName);
                callbacktaskData.setCompany(company);
                callbacktaskData.setFirstName(firstName);
                callbacktaskData.setLastName(lastName);
                callbacktaskData.setQuoteTotal(quoteTotal);


                callbacktaskData.setRecord_id(record_id);
                callbacktaskData.setQuoteID(quoteID);

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
