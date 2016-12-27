package com.leadmaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard_Activity extends Fragment
{
    static int check=1;
    ProgressDialog pd;
    ListView recentListView;
    ListView todayListView;

    ArrayList<Dash_Recent_Data> list;
    ArrayList<Dash_Today_Data> list2;

    TextView NADisplay;
    TextView NADisplay2;
    CardView todayEventCard;

    Dash_Recent_adapter adapter;
    Dash_Today_adapter adapter2;

    EditText searchEditText;
    TextView searchImageButton;

    SessionManager manager;

    private Activity mActivity;

    TextView refreshBtn;
    TextView today_refreshBtn;
    TextView dash_today_cal;
//    TextView dateDisplay;

    RadioGroup radioGroup;
//    RadioButton acc_radio,opp_radio,con_radio,case_radio;

    Spinner spinner;
    String selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_dashboard, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        manager=new SessionManager();

        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_recent_header), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_search_ll), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_TodayEvent_header), iconFont);

        GetRecentItems getRecentItems=new GetRecentItems();
        getRecentItems.execute();

        recentListView=(ListView)view.findViewById(R.id.dash_recentActivity);
        todayListView=(ListView)view.findViewById(R.id.dash_todayEvent);

        NADisplay=(TextView)view.findViewById(R.id.dash_today_NA);
        NADisplay2=(TextView)view.findViewById(R.id.dash_recent_NA);
        todayEventCard=(CardView)view.findViewById(R.id.cardview_todayevent);

        list=new ArrayList<Dash_Recent_Data>();
        list2=new ArrayList<Dash_Today_Data>();
        adapter=new Dash_Recent_adapter(mActivity, list);
        adapter2=new Dash_Today_adapter(mActivity, list2);

        searchEditText=(EditText)view.findViewById(R.id.dash_search_edit);
        searchImageButton=(TextView) view.findViewById(R.id.search_dash_btn);

        spinner=(Spinner)view.findViewById(R.id.spinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
//        categories.add("Select");

        if(! manager.getLoginPriv(getActivity(),"AccountLink").equals("false"))
        {
            categories.add(manager.getCustomLabel(getActivity(),"Banner - Accounts"));
        }
        if(! manager.getLoginPriv(getActivity(),"ContactLink").equals("false"))
        {
            categories.add(manager.getCustomLabel(getActivity(),"Banner - Contacts"));
        }
        if(! manager.getCustomLabel(getActivity(),"Case").equals(""))
        {
            categories.add(manager.getCustomLabel(getActivity(),"Opportunity"));
        }
        if(! manager.getCustomLabel(getActivity(),"Opportunity").equals(""))
        {
            categories.add(manager.getCustomLabel(getActivity(),"Case"));
        }

//        spinner.setPrompt("Select");
        spinner.setSelection(0);
//        selectedItem=manager.getCustomLabel(getActivity(),"Banner - Accounts");

        // Creating adapter for spinner

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout,R.id.spinner_text, categories);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
//        radioGroup=(RadioGroup)view.findViewById(R.id.radiogroup);

//        acc_radio=(RadioButton)view.findViewById(R.id.acc_radio);
//        con_radio=(RadioButton)view.findViewById(R.id.con_radio);
//        opp_radio=(RadioButton)view.findViewById(R.id.opp_radio);
//        case_radio=(RadioButton)view.findViewById(R.id.case_radio);

//        acc_radio.setText(manager.getCustomLabel(getActivity(),"Banner - Accounts"));
//        con_radio.setText(manager.getCustomLabel(getActivity(),"Banner - Contacts"));
//        opp_radio.setText(manager.getCustomLabel(getActivity(),"Opportunity"));
//        case_radio.setText(manager.getCustomLabel(getActivity(),"Case"));

//        if(manager.getLoginPriv(getActivity(),"AccountLink").equals("false"))
//        {
//            acc_radio.setEnabled(false);
//        }
//        if(manager.getLoginPriv(getActivity(),"ContactLink").equals("false"))
//        {
//            con_radio.setEnabled(false);
//        }
//        if(manager.getCustomLabel(getActivity(),"Case").equals(""))
//        {
//            case_radio.setEnabled(false);
//        }
//        if(manager.getCustomLabel(getActivity(),"Opportunity").equals(""))
//        {
//            opp_radio.setEnabled(false);
//        }

        refreshBtn=(TextView)view.findViewById(R.id.dash_refreshBtn);
        today_refreshBtn=(TextView)view.findViewById(R.id.dash_today_refresh);
        dash_today_cal=(TextView)view.findViewById(R.id.dash_today_cal);
//        dateDisplay=(TextView)view.findViewById(R.id.dash_date);

//        dateDisplay.setText(manager.getToday(getActivity()));

//        searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
//                if(position==0)
//                {
//                    selectedItem="";
//                    searchEditText.setHint("Please Select any option");
//                }
                if(position==0)
                {
                    selectedItem=manager.getCustomLabel(getActivity(),"Banner - Accounts");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));
                }
                if(position==1)
                {
                    selectedItem=manager.getCustomLabel(getActivity(),"Banner - Contacts");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Contacts"));
                }
                if(position==2)
                {
                    selectedItem=manager.getCustomLabel(getActivity(),"Opportunity");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Opportunity"));
                }
                if(position==3)
                {
                    selectedItem=manager.getCustomLabel(getActivity(),"Case");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Case"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                if(acc_radio.isChecked())
//                if(selectedItem.equals(""))
//                {
//                    Toast.makeText(getActivity(),"Please Select any option",Toast.LENGTH_LONG).show();
//                }
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Banner - Accounts")))
                {
                    if(! searchEditText.getText().toString().equals(""))
                    {
                        Fragment accountFragment = new Accounts_Activity();

                        Bundle bundle = new Bundle();

                        bundle.putString("query",searchEditText.getText().toString());

                        accountFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,accountFragment,null);
                        fragmentTransaction.commit();
                    }
                    else
                    {
//                        Log.e("true",manager.getLoginPriv(MainActivity.this,"AccountLink"));
                        Fragment accountsFragment = new Accounts_Activity();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                        fragmentTransaction.commit();
                    }

                }

//                if(con_radio.isChecked())
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Banner - Contacts")))
                {
                    if(! searchEditText.getText().toString().equals(""))
                    {
                        Fragment contactFragment = new Contacts_Activity();

                        Bundle bundle = new Bundle();

                        bundle.putString("query",searchEditText.getText().toString());

                        contactFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,contactFragment,null);
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        Fragment contactsFragment = new Contacts_Activity();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,contactsFragment,null);
                        fragmentTransaction.commit();
                    }
//                    Toast.makeText(getActivity(),"Contact",Toast.LENGTH_LONG).show();

                }

//                if(opp_radio.isChecked())
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Opportunity")))
                {
                    if(! searchEditText.getText().toString().equals(""))
                    {
                        //                    Toast.makeText(getActivity(),"Opp",Toast.LENGTH_LONG).show();
                        Fragment oppFragment = new Oppotunity_Activity();

                        Bundle bundle = new Bundle();

                        bundle.putString("query",searchEditText.getText().toString());

                        oppFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,oppFragment,null);
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        Fragment opportunityFragment = new Oppotunity_Activity();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,opportunityFragment,null);
                        fragmentTransaction.commit();
                    }

                }
//                if(case_radio.isChecked())
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Case")))
                {
                    if(! searchEditText.getText().toString().equals(""))
                    {
                        //                    Toast.makeText(getActivity(),"Case",Toast.LENGTH_LONG).show();
                        Fragment caseFragment = new Cases_Activity();

                        Bundle bundle = new Bundle();

                        bundle.putString("query",searchEditText.getText().toString());

                        caseFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,caseFragment,null);
                        fragmentTransaction.commit();

                    }
                    else
                    {
                        Fragment casesFragment = new Cases_Activity();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,casesFragment,null);
                        fragmentTransaction.commit();
                    }
                }
//                else
//                {
//                    Toast.makeText(getActivity(),"Please enter keyword for search",Toast.LENGTH_LONG).show();
//                }



            }
        });



        recentListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Dash_Recent_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=account";
//              manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";

                Log.d("MainURL",url);
                Log.e("Call","Recent");
                Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","dash");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();

            }
        });

        todayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Dash_Today_Data currentListData =adapter2.getItem(position);
//                final Dash_Today_Data currentListData2 =new Dash_Today_adapter(mActivity,list2).getItem(position);

                String encp=encrptVal();

                Log.e("Call","Today'sEvent");
                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&CallBackID=" + currentListData.getCallbackId() + "&appkeyword=&pagetype=callback";

                Log.d("MainURL",url);

                Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","dash");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
                list.clear();
                list2.clear();
                GetRecentItems getRecentItems=new GetRecentItems();
                getRecentItems.execute();
            }
        });

        dash_today_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp=encrptVal();

                String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_calWeek.asp";

                Log.e("Calendar",url);

                Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","dash");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();

            }
        });

        today_refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list2.clear();
                GetRecentItems getRecentItems=new GetRecentItems();
                getRecentItems.execute();
            }
        });

        searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));

//        acc_radio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));
//            }
//        });
//
//        con_radio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Contacts"));
//            }
//        });
//
//        opp_radio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Opportunity"));
//            }
//        });
//
//        case_radio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Case"));
//            }
//        });

        return view;
    }

    private class GetRecentItems extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetRecordActivityData";
            String METHOD_NAME = "GetRecordActivityData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));

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
                    String recdno = TypeEventData.getProperty("RECDNO").toString();
                    String company = TypeEventData.getProperty("Company").toString();
                    String editor = TypeEventData.getProperty("ActivityEditedBy").toString();
                    String lastAction = TypeEventData.getProperty("LastAction").toString();

                    Dash_Recent_Data  callbacktaskData=new Dash_Recent_Data();

                    if(company.equals("anyType{}"))
                    {
                        company="(SELECT)";
                    }
                    else
                    {
                        company=company+"";
                    }

                    if(lastAction.equals("anyType{}"))
                    {
                        lastAction="none";
                    }
                    else
                    {
                        lastAction=lastAction+"";
                    }

                    if(editor.equals("anyType{}"))
                    {
                        editor="none";
                    }
                    else
                    {
                        editor=editor+"";
                    }

                    callbacktaskData.setRecordId(recdno);
                    callbacktaskData.setCompany(company);
                    callbacktaskData.setAction(lastAction);
                    callbacktaskData.setEditor(editor);

                    list.add(callbacktaskData);

                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            Log.e("list===============", ""+list.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            pd.cancel();

            if(list.size()==0)
            {
                NADisplay2.setVisibility(View.VISIBLE);
                Log.e("LVSize",""+recentListView.getCount());
                GetTodayEvent getTodayEvent=new GetTodayEvent();
                getTodayEvent.execute();

            }
            else
            {
                NADisplay2.setVisibility(View.GONE);
                recentListView.setAdapter(new Dash_Recent_adapter(mActivity, list));
                Log.e("LVSize",""+recentListView.getCount());
                GetTodayEvent getTodayEvent=new GetTodayEvent();
                getTodayEvent.execute();
            }


        }
    }

    private class GetTodayEvent extends AsyncTask<Void, Void, Void>
    {

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
            String SOAP_ACTION = "LMServiceNamespace/TodaysCallBackEvent";
            String METHOD_NAME = "TodaysCallBackEvent";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));

                Request.addProperty("eventname", "");
                Request.addProperty("startindex", "1");
                Request.addProperty("endindex", "15");

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
                    String recdno = TypeEventData.getProperty("RECDNO").toString();
                    String callbackno = TypeEventData.getProperty("CallBackID").toString();

                    String company = TypeEventData.getProperty("Company").toString();
                    String startTime = TypeEventData.getProperty("StartTime").toString();
                    String eventType = TypeEventData.getProperty("EventType").toString();
                    String eventName = TypeEventData.getProperty("EventName").toString();

                    Dash_Today_Data  callbacktaskData=new Dash_Today_Data();

                    if(company.equals("anyType{}"))
                    {
                        company="(SELECT)";
                    }
                    else
                    {
                        company=company+"";
                    }

                    if(startTime.equals("anyType{}"))
                    {
                        startTime="none";
                    }
                    else
                    {
                        startTime=startTime+"";
                    }

                    if(eventType.equals("anyType{}"))
                    {
                        eventType="none";
                    }
                    else
                    {
                        eventType=eventType+"";

                    }

                    if(eventName.equals("anyType{}"))
                    {
                        eventName="none";
                    }
                    else
                    {
                        eventName=eventName+"";

                    }

                    callbacktaskData.setRecordId(recdno);
                    callbacktaskData.setCallbackId(callbackno);

                    callbacktaskData.setCompany(company);
                    callbacktaskData.setEvent(eventType);
                    callbacktaskData.setStarttime(startTime);
                    callbacktaskData.setEventName(eventName);

                    list2.add(callbacktaskData);

                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            Log.e("list===============", ""+list2.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();

            if(list2.size()==0)
            {
//                todayEventCard.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        400));
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        400);
////                int margin = dpToPixel(8);
//                params.setMargins(0, 0, 0, 15);
//
//                todayEventCard.setLayoutParams(params);

                NADisplay.setVisibility(View.VISIBLE);
//                todayListView.setAdapter(new Dash_Today_adapter(mActivity,list2));

            }
            else
            {
                NADisplay.setVisibility(View.GONE);
                todayListView.setAdapter(new Dash_Today_adapter(mActivity,list2));
            }


        }
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


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
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
}
