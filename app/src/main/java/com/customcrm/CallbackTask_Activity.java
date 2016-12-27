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

public class CallbackTask_Activity extends Fragment
{
    TextView title;

    ListView lvcallbacktask;
    ArrayList list;

    ProgressDialog pd;

    SessionManager manager;

    String searchEventName=null;
    EditText searchEdit;
    ImageButton searchBtn;

    int totalRcordDIsplay;
    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    private Activity mActivity;

    String RecordsId="";
    String checkDeletion;

    CallbackTask_adapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_callback_task, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvcallbacktask=(ListView)view.findViewById(R.id.callback_listview);

        searchEdit=(EditText)view.findViewById(R.id.callback_search_edit);
        searchBtn=(ImageButton)view.findViewById(R.id.search_callback_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_CallBack);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_CallBack);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_CallBack);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_CallBack);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_CallBack);
        adapter=new CallbackTask_adapter(mActivity, list,i,j);

        title=(TextView)view.findViewById(R.id.callback_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Event"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.cb_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.cb_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.callback_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.callback_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.callback_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.callback_parent), mainFont);
        }


        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                searchEventName = bundle.getString("searchQuery");

                searchEdit.setText(searchEventName);
                if(searchEventName.isEmpty() || searchEventName.equals("null") || searchEventName == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchEventName);

                    SearchTotalCallback searchTotalCallBackEvent=new SearchTotalCallback("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchEventName);

                    SearchTotalCallback searchTotalCallBackEvent=new SearchTotalCallback(searchEventName);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalCallback getTotalCallBackEvent=new GetTotalCallback();
            getTotalCallBackEvent.execute();
        }

        lvcallbacktask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Callbacktask_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
//                 manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getCallback_Id() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + currentListData.getRcd_Id() + "&CompanyID=" + manager.getWG(mActivity) + "&CallBackID=" + currentListData.getCallback_Id() + "&appkeyword=&pagetype=callback";

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","callback");
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
                intent.putExtra("frg", "callback");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchEventName=searchEdit.getText().toString();

                if(searchEventName.isEmpty() || searchEventName.equals("null") || searchEventName == null)
                {
                    Toast.makeText(getActivity(),"Please enter Event Name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();

                    i=1;j=15;
                    SearchTotalCallback searchTotalCallBackEvent=new SearchTotalCallback(searchEventName);
                    searchTotalCallBackEvent.execute();

                }
            }
        });

        //FontAwesome Code
        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container_callback), iconFont);

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchEventName=searchEdit.getText().toString();

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
                    if(searchEventName.equals("null") && searchEventName == null && searchEventName.isEmpty())
                    {
                        GetTotalCallback getTotalCallBackEvent=new GetTotalCallback();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCallback searchTotalCallBackEvent=new SearchTotalCallback(searchEventName);
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
                searchEventName=searchEdit.getText().toString();

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
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    if(searchEventName.isEmpty() || searchEventName.equals("null") || searchEventName == null)
                    {
                        GetTotalCallback getTotalCallBackEvent=new GetTotalCallback();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCallback searchTotalCallBackEvent=new SearchTotalCallback(searchEventName);
                        searchTotalCallBackEvent.execute();
                    }
                }

            }
        });

//        GetTotalCallback getTotalCallBackEvent=new GetTotalCallback();
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

    private class GetSearchCallback extends AsyncTask<Void, Void, Void>
    {

        int i,j;

        public GetSearchCallback(int i, int j)
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
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.dismiss();

            lvcallbacktask.setAdapter(new CallbackTask_adapter(mActivity, list,i,j));
        }
    }
    private void GetData(int start, int end)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchCallBackEvent";
        String METHOD_NAME = "SearchCallBackEvent";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("eventname", "");
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("eventdate", "");


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
                String account = TypeEventData.getProperty("Company").toString();
                String callback = TypeEventData.getProperty("EventName").toString();
                String startTime = TypeEventData.getProperty("StartTime").toString();
                String endTime = TypeEventData.getProperty("EndTime").toString();
                String eventstatus = TypeEventData.getProperty("EventStatus").toString();
                String rec_id = TypeEventData.getProperty("RECDNO").toString();
                String callbak_id = TypeEventData.getProperty("CallBackID").toString();

                Callbacktask_Data  callbacktaskData=new Callbacktask_Data();

                if(account.equals("anyType{}"))
                {
                    account="none";
                }else
                {
                    account=account+"";
                }

                if(callback.equals("anyType{}"))
                {
                    callback="none";
                }else
                {
                    callback=callback+"";
                }

                if(startTime.equals("anyType{}"))
                {
                    startTime="none";
                }else
                {
                    startTime=startTime+"";
                }

                if(endTime.equals("anyType{}"))
                {
                    endTime="none";
                }else
                {
                    endTime=endTime+"";
                }

                if(eventstatus.equals("anyType{}"))
                {
                    eventstatus="none";
                }else
                {
                    eventstatus=eventstatus+"";
                }
                callbacktaskData.setCompanyName(account);
                callbacktaskData.setEventName(callback);
                callbacktaskData.setStartTime(startTime);
                callbacktaskData.setEndTime(endTime);
                callbacktaskData.setEventStatus(eventstatus);

                callbacktaskData.setRcd_Id(rec_id);
                callbacktaskData.setCallback_Id(callbak_id);

                list.add(callbacktaskData);

//                Log.e("recordid",recordId);
//                Log.e("lat_long",latitude+","+longitude);

            }


            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
        } catch (Exception ex)
        {
//           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+""+j);
    }


    private class GetTotalCallback extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
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
            String SOAP_ACTION = "LMServiceNamespace/SearchCallBackCount";
            String METHOD_NAME = "SearchCallBackCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("eventname", "");
                Request.addProperty("eventdate", "");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchCallBackCountResult").toString());
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
//            if(pd != null && pd.isShowing())
//            {
//                pd.dismiss();
//            }

            if(totalRcordDIsplay==0)
            {
                pd.dismiss();

                totalIndexDisplay.setText("Event not found");
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

                GetSearchCallback getSearchCallBackEvent=new GetSearchCallback(i,j);
                getSearchCallBackEvent.execute();
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    //For Search by Company Name...

    private class SearchTotalCallback extends AsyncTask<Void, Void, Void>
    {

        String event;
        public SearchTotalCallback(String searchEventName)
        {
            event=searchEventName;
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
            String SOAP_ACTION = "LMServiceNamespace/SearchCallBackCount";
            String METHOD_NAME = "SearchCallBackCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("eventname", event);
                Request.addProperty("eventdate","");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchCallBackCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord", ""+totalRcordDIsplay);
                Log.e("Company", event);
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
//            if(pd != null && pd.isShowing())
//            {
//                pd.dismiss();
//            }

            if(totalRcordDIsplay==0)
            {
                pd.dismiss();

                totalIndexDisplay.setText("Event not found");
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

                SearchCallback searchCompanyAccount=new SearchCallback(i,j,event);
                searchCompanyAccount.execute();
            }
        }
    }

    private class SearchCallback extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String event;

        public SearchCallback(int i, int j, String event)
        {
            this.i=i;
            this.j=j;
            this.event=event;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");
//            pd=new ProgressDialog(Accounts_Activity.this);
//            pd=new ProgressDialog(mActivity);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
//            list=new ArrayList();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            searchCompany(i,j,event);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
//            customAdapter=new CustomAdapter(Accounts_Activity.this,R.layout.raw_callbacktask,list);
//            lvAccounts.setAdapter(new Accounts_adapter(Accounts_Activity.this, list));
            lvcallbacktask.setAdapter(new CallbackTask_adapter(mActivity, list,i,j));

//            GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//            getTotalCallBackEvent.execute();

        }
    }

    private void searchCompany(int start, int end, String searchEventName)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchCallBackEvent";
        String METHOD_NAME = "SearchCallBackEvent";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("eventname", searchEventName);
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("eventdate", "");


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
                String account = TypeEventData.getProperty("Company").toString();
                String callback = TypeEventData.getProperty("EventName").toString();
                String startTime = TypeEventData.getProperty("StartTime").toString();
                String endTime = TypeEventData.getProperty("EndTime").toString();
                String eventstatus = TypeEventData.getProperty("EventStatus").toString();

                String rec_id = TypeEventData.getProperty("RECDNO").toString();
                String callbak_id = TypeEventData.getProperty("CallBackID").toString();

                Callbacktask_Data  callbacktaskData=new Callbacktask_Data();

                if(account.equals("anyType{}"))
                {
                    account="none";
                }else
                {
                    account=account+"";
                }

                if(callback.equals("anyType{}"))
                {
                    callback="none";
                }else
                {
                    callback=callback+"";
                }

                if(startTime.equals("anyType{}"))
                {
                    startTime="none";
                }else
                {
                    startTime=startTime+"";
                }

                if(endTime.equals("anyType{}"))
                {
                    endTime="none";
                }else
                {
                    endTime=endTime+"";
                }

                if(eventstatus.equals("anyType{}"))
                {
                    eventstatus="none";
                }else
                {
                    eventstatus=eventstatus+"";
                }
                callbacktaskData.setCompanyName(account);
                callbacktaskData.setEventName(callback);
                callbacktaskData.setStartTime(startTime);
                callbacktaskData.setEndTime(endTime);
                callbacktaskData.setEventStatus(eventstatus);

                callbacktaskData.setRcd_Id(rec_id);
                callbacktaskData.setCallback_Id(callbak_id);

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
    public void onPause() {
        super.onPause();
        pd.dismiss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        pd.dismiss();
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
