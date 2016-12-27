package com.customCRM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
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
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dashboard_Activity extends Fragment
{

    static int check=1;
    ProgressDialog pd;
//    ListView recentListView;
//    ListView todayListView;

    ArrayList<Dash_Recent_Data> list;
    ArrayList<Dash_Today_Data> list2;

    TextView NADisplay;
    TextView NADisplay2;
    CardView todayEventCard;

    Dash_Recent_adapter adapter;
    Dash_Today_adapter adapter2;

//    EditText searchEditText;
    AutoCompleteTextView searchEditText;
    DashAutoCompleteAdapter dashAutoCompleteAdapter;

    TextView searchImageButton;

    SessionManager manager;

    private Activity mActivity;

    TextView refreshBtn,refreshBtn1;
//    TextView today_refreshBtn;
    TextView dash_today_cal;
//    TextView dateDisplay;

    RadioGroup radioGroup;
//    RadioButton acc_radio,opp_radio,con_radio,case_radio;

    Spinner spinner;
    String selectedItem;

    WebView login;

//    ViewPager salesPipeline;
//    CardView selesPipeCard;

    Dash_SalesPipeLine_adapter pager_adapter;
    String[] profit,lost,totalWon,totalLost;
    ImageView[] image;

    LinearLayout linearLayout1,linearLayout2;

    LinearLayout[] child,child2,child3;

    TextView[] record_tv,company_tv,editor_tv,action_tv;
    TextView[] recordTE_tv,callbackIdTE_tv,companyTE_tv,eventTypeTE_tv,startTE_tv,iconTE_tv;

//    TextView[] eventNameTE_tv;

    String[] record_st,company_st,action_st,editor_st;
    String[] recordTE_st,callbackId_st,companyTE_st,eventType_st,eventName_st,startTime_st;

    int x;

    TextView pipeHeader,pipeDashHeader,pipeDashButton;

    String currencySign;

    TextView add_spinner_button_dash;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_dashboard, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        manager=new SessionManager();

        if(manager.getLoginPriv(mActivity,"ISO_Code").equals("GBP"))
        {
            currencySign="£";
        }
        else if(manager.getLoginPriv(mActivity,"ISO_Code").equals("EUR"))
        {
            currencySign="€";
        }
        else if(manager.getLoginPriv(mActivity,"ISO_Code").equals("JPY"))
        {
            currencySign="¥";
        }
        else if(manager.getLoginPriv(mActivity,"ISO_Code").equals("CHF"))
        {
            currencySign="CHF";
        }
        else if(manager.getLoginPriv(mActivity,"ISO_Code").equals("USD"))
        {
            currencySign="$";
        }

        profit=new String[5];
        lost=new String[5];
        totalWon=new String[5];
        totalLost=new String[5];

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.dahboard_parent), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.dash_recent_header), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.pipeline_ll1), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.dahboard_parent), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.dash_recent_header), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.pipeline_ll1), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.dahboard_parent), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.dash_recent_header), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.pipeline_ll1), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.dahboard_parent), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.dash_recent_header), mainFont);
//            CustomFont.markAsIconContainer(view.findViewById(R.id.pipeline_ll1), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
//        FontManager.markAsIconContainer(view.findViewById(R.id.dash_recent_header), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_search_ll), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_TodayEvent_header), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.dash_recent_header1), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.add_spinner_button_dash), iconFont);

//        GetRecentItems getRecentItems=new GetRecentItems();
//        getRecentItems.execute();


//        salesPipeline=(ViewPager)view.findViewById(R.id.salesPipeline_viewpager);
//        selesPipeCard=(CardView)view.findViewById(R.id.oppPipe_card);

        refreshBtn1=(TextView)view.findViewById(R.id.dash_refreshBtn1);

//        if(manager.getCustomLabel(mActivity,"Opportunity").equals(""))
//        {
//            Log.e("SalesPipe","if");
//            selesPipeCard.setVisibility(View.GONE);
//            refreshBtn1.setVisibility(View.VISIBLE);
//            GetRecentItems getRecentItems=new GetRecentItems();
//            getRecentItems.execute();
//        }
//        else
//        {
//            Log.e("SalesPipe","else");
//            selesPipeCard.setVisibility(View.VISIBLE);
//            refreshBtn1.setVisibility(View.GONE);
//            GetOppPipeline getOppPipeline=new GetOppPipeline();
//            getOppPipeline.execute();
//        }

        login=(WebView)view.findViewById(R.id.login_browser);

        NADisplay=(TextView)view.findViewById(R.id.dash_today_NA);
        NADisplay2=(TextView)view.findViewById(R.id.dash_recent_NA);

//        pipeHeader=(TextView)view.findViewById(R.id.oppPipeHeader);
//        pipeDashHeader=(TextView)view.findViewById(R.id.oppPipe_dash_Header);
//        pipeDashButton=(TextView)view.findViewById(R.id.pipeline_btn);

//        pipeDashButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Fragment opportunityFragment = new Oppotunity_Activity();
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.containerView,opportunityFragment,null);
//                fragmentTransaction.commit();
//            }
//        });

//        pipeDashHeader.setText(manager.getCustomLabel(mActivity,"Opportunity")+" Pipeline");

        list=new ArrayList<Dash_Recent_Data>();
        list2=new ArrayList<Dash_Today_Data>();
        adapter=new Dash_Recent_adapter(mActivity, list);
        adapter2=new Dash_Today_adapter(mActivity, list2);

//        searchEditText=(EditText)view.findViewById(R.id.dash_search_edit);
        searchEditText=(AutoCompleteTextView)view.findViewById(R.id.dash_search_edit);


//        dashAutoCompleteAdapter=new DashAutoCompleteAdapter(mActivity,android.R.layout.simple_dropdown_item_1line);

        searchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String countryName = dashAutoCompleteAdapter.getItem(position).getName();
                searchEditText.setText(countryName);
            }
        });


        searchEditText.setBackgroundColor(manager.getColor(getActivity()));

        searchImageButton=(TextView) view.findViewById(R.id.search_dash_btn);
        searchImageButton.setBackgroundColor(manager.getColor(getActivity()));

        spinner=(Spinner)view.findViewById(R.id.spinner);

        // Spinner Drop down elements
        final List<String> categories = new ArrayList<String>();
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


//        spinner.setSelection(0);
//        selectedItem=spinner.getSelectedItem().toString();

//        selectedItem=manager.getCustomLabel(getActivity(),"Banner - Accounts");

        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, categories)
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout,R.id.spinner_text, categories)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
                View row=inflater.inflate(R.layout.spinner_layout_2, parent, false);
                TextView label=(TextView)row.findViewById(R.id.spinnerText);
                label.setText(categories.get(position));

                if(manager.getFontStyle(getActivity()).equals("open-sans"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }

                return row;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
                View row=inflater.inflate(R.layout.spinner_layout_2, parent, false);
                TextView label=(TextView)row.findViewById(R.id.spinnerText);
                label.setText(categories.get(position));

                if(manager.getFontStyle(getActivity()).equals("open-sans"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
                    CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                }
                return row;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setSelection(0);
        selectedItem=spinner.getSelectedItem().toString();
//        refreshBtn=(TextView)view.findViewById(R.id.dash_refreshBtn);

//        today_refreshBtn=(TextView)view.findViewById(R.id.dash_today_refresh);
        dash_today_cal=(TextView)view.findViewById(R.id.dash_today_cal);
//        dateDisplay=(TextView)view.findViewById(R.id.dash_date);

//        dateDisplay.setText(manager.getToday(getActivity()));

//        searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position==0)
                {
                    Log.e("selected","1");
                    selectedItem=manager.getCustomLabel(getActivity(),"Banner - Accounts");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));

                    dashAutoCompleteAdapter=new DashAutoCompleteAdapter(mActivity,android.R.layout.simple_dropdown_item_1line,"Acc");
                    searchEditText.setAdapter(dashAutoCompleteAdapter);
                }
                if(position==1)
                {
                    Log.e("selected","2");
                    selectedItem=manager.getCustomLabel(getActivity(),"Banner - Contacts");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Contacts"));

                    dashAutoCompleteAdapter=new DashAutoCompleteAdapter(mActivity,android.R.layout.simple_dropdown_item_1line,"Con");
                    searchEditText.setAdapter(dashAutoCompleteAdapter);
                }
                if(position==2)
                {
                    Log.e("selected","3");
                    selectedItem=manager.getCustomLabel(getActivity(),"Opportunity");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Opportunity"));

                    dashAutoCompleteAdapter=new DashAutoCompleteAdapter(mActivity,android.R.layout.simple_dropdown_item_1line,"Opp");
                    searchEditText.setAdapter(dashAutoCompleteAdapter);
                }
                if(position==3)
                {
                    Log.e("selected","4");
                    selectedItem=manager.getCustomLabel(getActivity(),"Case");
                    searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Case"));

                    dashAutoCompleteAdapter=new DashAutoCompleteAdapter(mActivity,android.R.layout.simple_dropdown_item_1line,"Case");
                    searchEditText.setAdapter(dashAutoCompleteAdapter);
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




//        recentListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                final Dash_Recent_Data currentListData =adapter.getItem(position);
//                String encp=encrptVal();
//                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=account";
////              manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";
//
//                Log.d("MainURL",url);
//                Log.e("Call","Recent");
//              /*  Fragment webviewFragment = new Common_WebView();
//
//                Bundle bundle = new Bundle();
//
//                bundle.putString("url", url);
//                bundle.putString("frg","dash");
//
//                webviewFragment.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
//                fragmentTransaction.commit();*/
//                Intent intent = new Intent(getActivity(),CommonWebView.class);
//                intent.putExtra("url", url);
//                intent.putExtra("frg", "dash");
//
//                getActivity().startActivity(intent);
//
//            }
//        });

//        todayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final Dash_Today_Data currentListData =adapter2.getItem(position);
////                final Dash_Today_Data currentListData2 =new Dash_Today_adapter(mActivity,list2).getItem(position);
//
//                String encp=encrptVal();
//
//                Log.e("Call","Today'sEvent");
//                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&CallBackID=" + currentListData.getCallbackId() + "&appkeyword=&pagetype=callback";
//
//                Log.d("MainURL",url);
//
//              /*  Fragment webviewFragment = new Common_WebView();
//
//                Bundle bundle = new Bundle();
//
//                bundle.putString("url", url);
//                bundle.putString("frg","dash");
//
//                webviewFragment.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
//                fragmentTransaction.commit();*/
//                Intent intent = new Intent(getActivity(),CommonWebView.class);
//                intent.putExtra("url", url);
//                intent.putExtra("frg", "acc");
//                getActivity().startActivity(intent);
//            }
//        });

//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
//                list.clear();
//                list2.clear();
////                GetRecentItems getRecentItems=new GetRecentItems();
////                getRecentItems.execute();
//
//                if(manager.getCustomLabel(mActivity,"Opportunity").equals(""))
//                {
//                    selesPipeCard.setVisibility(View.GONE);
//                    refreshBtn1.setVisibility(View.VISIBLE);
//                    GetRecentItems getRecentItems=new GetRecentItems();
//                    getRecentItems.execute();
//                }
//                else
//                {
//                    selesPipeCard.setVisibility(View.VISIBLE);
//                    refreshBtn1.setVisibility(View.GONE);
//                    GetOppPipeline getOppPipeline=new GetOppPipeline();
//                    getOppPipeline.execute();
//                }
//            }
//        });

        refreshBtn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                list.clear();
                list2.clear();
//                if(manager.getCustomLabel(mActivity,"Opportunity").equals(""))
//                {
//                    selesPipeCard.setVisibility(View.GONE);
//                    refreshBtn1.setVisibility(View.VISIBLE);
                    GetRecentItems getRecentItems=new GetRecentItems();
                    getRecentItems.execute();
//                }
//                else
//                {
//                    selesPipeCard.setVisibility(View.VISIBLE);
//                    refreshBtn1.setVisibility(View.GONE);
//                    GetOppPipeline getOppPipeline=new GetOppPipeline();
//                    getOppPipeline.execute();
//                }
            }
        });

        dash_today_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp=encrptVal();

                String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_calWeek.asp";

                Log.e("Calendar",url);

                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "case");

                getActivity().startActivity(intent);


            }
        });

//        today_refreshBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                list.clear();
//                list2.clear();
//                GetRecentItems getRecentItems=new GetRecentItems();
//                getRecentItems.execute();
//            }
//        });



//        searchEditText.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));

//        salesPipeline=(ViewPager)view.findViewById(R.id.salesPipeline_viewpager);
//        selesPipeCard=(CardView)view.findViewById(R.id.oppPipe_card);


//        pager_adapter=new Dash_SalesPipeLine_adapter(mActivity,profit,lost,totalWon,totalLost);
//
//        salesPipeline.setAdapter(pager_adapter);

//        LinearLayout viewpager_dots_container = (LinearLayout) view.findViewById(R.id.viewpager_dots_container);

//        image = new ImageView[profit.length];
//        for(int x=0;x<profit.length;x++)
//        {
//            image[x] = new ImageView(getActivity());

//            if(x==0)
//            {
//                image[x].setBackgroundResource(R.drawable.on_pos);
//            }
//            else
//            {
//                image[x].setBackgroundResource(R.drawable.off_pos);
//            }
//
//            viewpager_dots_container.addView(image[x]);
//        }

//        salesPipeline.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
//        {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
//            {
//
//                if(position==0)
//                {
//                    pipeHeader.setText("Overview");
//                }
//                else if(position==1)
//                {
//                    pipeHeader.setText("This week");
//                }
//                else if(position==2)
//                {
//                    pipeHeader.setText("This month");
//                }
//                else if(position==3)
//                {
//                    pipeHeader.setText("This quarter");
//                }
//                else if(position==4)
//                {
//                    pipeHeader.setText("This year");
//                }
////                linearLayout1.removeAllViews();
//                for(int x=0;x<profit.length;x++)
//                {
////                    image = new ImageView(getActivity());
//                    if(x==position)
//                    {
//                        image[x].setBackgroundResource(R.drawable.on_pos);
//                    }
//                    else
//                    {
//                        image[x].setBackgroundResource(R.drawable.off_pos);
//                    }
////                    linearLayout1.addView(image);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position)
//            {
////                Toast.makeText(mActivity,""+position,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state)
//            {
//
//            }
//        });

        GetRecentItems getRecentItems=new GetRecentItems();
        getRecentItems.execute();

        linearLayout1 = (LinearLayout) view.findViewById(R.id.dash_recentActivity);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.dash_todayEvent);

        add_spinner_button_dash=(TextView)view.findViewById(R.id.add_spinner_button_dash);
        add_spinner_button_dash.setTextColor(manager.getColor(getActivity()));
        add_spinner_button_dash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Banner - Accounts")))
                {
                    Intent i=new Intent(getActivity(),AddRecord_Dynamic.class);
                    getActivity().startActivity(i);
                }
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Banner - Contacts")))
                {
                    Intent i=new Intent(getActivity(),AddContact_Dynamic.class);
                    getActivity().startActivity(i);
                }
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Opportunity")))
                {
                    Intent i=new Intent(getActivity(),AddOpp_Dynamic.class);
                    getActivity().startActivity(i);
                }
                if(selectedItem.equals(manager.getCustomLabel(getActivity(),"Case")))
                {
                    Intent i=new Intent(getActivity(),AddCase_Dynamic.class);
                    getActivity().startActivity(i);
                }
            }
        });

        return view;
    }

    private class GetOppPipeline extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
            Log.d("GetData","GetData");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            GetData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.cancel();
            if(profit[0]==null)
            {
                GetRecentItems getRecentItems=new GetRecentItems();
                getRecentItems.execute();
            }
            else
            {
                pager_adapter=new Dash_SalesPipeLine_adapter(mActivity,profit,lost,totalWon,totalLost);

//                salesPipeline.setAdapter(pager_adapter);

                GetRecentItems getRecentItems=new GetRecentItems();
                getRecentItems.execute();
            }
        }
    }
    private void GetData()
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/GetSalesPipeline";
        String METHOD_NAME = "GetSalesPipeline";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("LogonID", manager.getUserId(getActivity()));
            Request.addProperty("CompanyID", manager.getWG(getActivity()));

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,30000);

            transport.call(SOAP_ACTION, soapEnvelope);
            //list=new ArrayList();


            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

//            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//            {
                SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(0);

                String totalDeal = TypeEventData.getProperty("TotalDeal").toString();
                String totalDealValue = TypeEventData.getProperty("TotalDealValue").toString();

                String dealWonName = TypeEventData.getProperty("DealWonName").toString();

                String dealWonWeek = TypeEventData.getProperty("DealWonWeek").toString();
                String dealWonWeekCount = TypeEventData.getProperty("DealWonWeekCount").toString();

                String dealWonMonth = TypeEventData.getProperty("DealWonMonth").toString();
                String dealWonMonthCount = TypeEventData.getProperty("DealWonMonthCount").toString();

                String dealWonQuater = TypeEventData.getProperty("DealWonQuater").toString();
                String dealWonQuaterCount = TypeEventData.getProperty("DealWonQuaterCount").toString();

                String dealWonYear = TypeEventData.getProperty("DealWonYear").toString();
                String dealWonYearCount = TypeEventData.getProperty("DealWonYearCount").toString();

                String dealLostName = TypeEventData.getProperty("DealLostName").toString();

                String dealLostWeek = TypeEventData.getProperty("DealLostWeek").toString();
                String dealLostWeekCount = TypeEventData.getProperty("DealLostWeekCount").toString();

                String dealLostMonth = TypeEventData.getProperty("DealLostMonth").toString();
                String dealLostMonthCount = TypeEventData.getProperty("DealLostMonthCount").toString();

                String dealLostQuater = TypeEventData.getProperty("DealLostQuater").toString();
                String dealLostQuaterCount = TypeEventData.getProperty("DealLostQuaterCount").toString();

                String dealLostYear = TypeEventData.getProperty("DealLostYear").toString();
                String dealLostYearCount = TypeEventData.getProperty("DealLostYearCount").toString();

                if(totalDeal.equals("anyType{}"))
                {
                    totalDeal="0";
                }else
                {
                    totalDeal=totalDeal+"";
                }

                profit[0]=totalDeal;
                totalWon[0]="Deals";

                if(totalDealValue.equals("anyType{}"))
                {
                    totalDealValue="0";
                }else
                {
                    totalDealValue=totalDealValue+"";
                }

//                lost[0]=totalDealValue;
                lost[0]=currencySign+""+String.format("%.02f", Float.parseFloat(totalDealValue));
                totalLost[0]="Pipeline<br>Value";

                if(dealWonName.equals("anyType{}"))
                {
                    dealWonName="Won";
                }else
                {
                    dealWonName=dealWonName+"";
                }

                if(dealWonWeek.equals("anyType{}"))
                {
                    dealWonWeek="0";
                }else
                {
                    dealWonWeek=dealWonWeek+"";
                }

//                profit[1]=dealWonWeek;
                profit[1]=currencySign+""+String.format("%.02f", Float.parseFloat(dealWonWeek));;

                if(dealWonWeekCount.equals("anyType{}"))
                {
                    dealWonWeekCount="0";
                }else
                {
                    dealWonWeekCount=dealWonWeekCount+"";
                }

//                Html.fromHtml("<font color=#000000>"+dealWonWeekCount+"</font>");
//                totalWon[1]=dealWonName+"\n"+dealWonWeekCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
//                totalWon[1]=dealWonName+"\n"+Html.fromHtml("<font color=#000000>"+dealWonWeekCount+"</font>")+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalWon[1]=dealWonName+"<br><font color=#000000><b>"+dealWonWeekCount+"</b></font>"+" " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealWonMonth.equals("anyType{}"))
                {
                    dealWonMonth="0";
                }else
                {
                    dealWonMonth=dealWonMonth+"";
                }

//                profit[2]=dealWonMonth;
                profit[2]=currencySign+""+String.format("%.02f", Float.parseFloat(dealWonMonth));;

                if(dealWonMonthCount.equals("anyType{}"))
                {
                    dealWonMonthCount="0";
                }else
                {
                    dealWonMonthCount=dealWonMonthCount+"";
                }

                totalWon[2]=dealWonName+"<br><font color=#000000><b>"+dealWonMonthCount+"</b></font>" + " " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealWonQuater.equals("anyType{}"))
                {
                    dealWonQuater="0";
                }else
                {
                    dealWonQuater=dealWonQuater+"";
                }

//                profit[3]=dealWonQuater;
                profit[3]=currencySign+""+String.format("%.02f", Float.parseFloat(dealWonQuater));

                if(dealWonQuaterCount.equals("anyType{}"))
                {
                    dealWonQuaterCount="0";
                }else
                {
                    dealWonQuaterCount=dealWonQuaterCount+"";
                }

//                totalWon[3]=dealWonName+"\n"+dealWonQuaterCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalWon[3]=dealWonName+"<br><font color=#000000><b>"+dealWonQuaterCount+" </b></font>"+ " " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealWonYear.equals("anyType{}"))
                {
                    dealWonYear="0";
                }else
                {
                    dealWonYear=dealWonYear+"";
                }

//                profit[4]=dealWonYear;
                profit[4]=currencySign+""+String.format("%.02f", Float.parseFloat(dealWonYear));;;

                if(dealWonYearCount.equals("anyType{}"))
                {
                    dealWonYearCount="0";
                }else
                {
                    dealWonYearCount=dealWonYearCount+"";
                }

//                totalWon[4]=dealWonName+"\n"+dealWonYearCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalWon[4]=dealWonName+"<br><font color=#000000><b>"+dealWonYearCount+"</b></font>" + " " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealLostName.equals("anyType{}"))
                {
                    dealLostName="Lost";
                }else
                {
                    dealLostName=dealLostName+"";
                }


                if(dealLostWeek.equals("anyType{}"))
                {
                    dealLostWeek="0";
                }else
                {
                    dealLostWeek=dealLostWeek+"";
                }

//                lost[1]=dealLostWeek;
                lost[1]=currencySign+""+String.format("%.02f", Float.parseFloat(dealLostWeek));;;

                if(dealLostWeekCount.equals("anyType{}"))
                {
                    dealLostWeekCount="0";
                }else
                {
                    dealLostWeekCount=dealLostWeekCount+"";
                }

//                totalLost[1]=dealLostName+"\n"+dealLostWeekCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalLost[1]=dealLostName+"<br><font color=#000000><b>"+dealLostWeekCount+"</b></font>" + " " +manager.getCustomLabel(mActivity,"Opportunity");


                if(dealLostMonth.equals("anyType{}"))
                {
                    dealLostMonth="0";
                }else
                {
                    dealLostMonth=dealLostMonth+"";
                }

//                lost[2]=dealLostMonth;
                lost[2]=currencySign+""+String.format("%.02f", Float.parseFloat(dealLostMonth));;;;

                if(dealLostMonthCount.equals("anyType{}"))
                {
                    dealLostMonthCount="0";
                }else
                {
                    dealLostMonthCount=dealLostMonthCount+"";
                }

//                totalLost[2]=dealLostName+"\n"+dealLostMonthCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalLost[2]=dealLostName+"<br><font color=#000000><b>"+dealLostMonthCount+"</b></font>"+ " " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealLostQuater.equals("anyType{}"))
                {
                    dealLostQuater="0";
                }else
                {
                    dealLostQuater=dealLostQuater+"";
                }

//                lost[3]=dealLostQuater;
                lost[3]=currencySign+""+String.format("%.02f", Float.parseFloat(dealLostQuater));;;;

                if(dealLostQuaterCount.equals("anyType{}"))
                {
                    dealLostQuaterCount="0";
                }else
                {
                    dealLostQuaterCount=dealLostQuaterCount+"";
                }

//                totalLost[3]=dealLostName+"\n"+dealLostQuaterCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalLost[3]=dealLostName+"<br><font color=#000000><b>"+dealLostQuaterCount+"</b></font>"+ " " +manager.getCustomLabel(mActivity,"Opportunity");

                if(dealLostYear.equals("anyType{}"))
                {
                    dealLostYear="0";
                }else
                {
                    dealLostYear=dealLostYear+"";
                }

//                lost[4]=dealLostYear;
                lost[4]=currencySign+""+String.format("%.02f", Float.parseFloat(dealLostYear));;;;

                if(dealLostYearCount.equals("anyType{}"))
                {
                    dealLostYearCount="0";
                }else
                {
                    dealLostYearCount=dealLostYearCount+"";
                }

//                totalLost[4]=dealLostName+"\n"+dealLostYearCount+ " " +manager.getCustomLabel(mActivity,"Opportunity");
                totalLost[4]=dealLostName+"<br><font color=#000000><b>"+dealLostYearCount+"</b></font>"+ " " +manager.getCustomLabel(mActivity,"Opportunity");

//            }

            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

        }
        catch (Exception ex)
        {
           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("Url==>",URL);
        Log.e("check", "Result SECOND API: " + resultRequestSOAP);
    }


    private class GetRecentItems extends AsyncTask<Void, Void, Void> implements View.OnClickListener
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");

//            if(selesPipeCard.getVisibility()==View.GONE)
//            {
                pd=new ProgressDialog(mActivity);
                pd.setMessage("Wait...");
                pd.setCancelable(false);
                pd.show();
//            }

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

                Log.e("Connect","1");
                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();
                Log.e("Connect","2");
                resultRequestSOAP  = (SoapObject) soapEnvelope.getResponse();
                Log.e("resltRequestSOAP",resultRequestSOAP.getPropertyCount()+"");

                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                record_st=new String[resultRequestSOAP.getPropertyCount()];
                company_st=new String[resultRequestSOAP.getPropertyCount()];
                editor_st=new String[resultRequestSOAP.getPropertyCount()];
                action_st=new String[resultRequestSOAP.getPropertyCount()];

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String recdno = TypeEventData.getProperty("RECDNO").toString();
                    String company = TypeEventData.getProperty("Company").toString();
                    String editor = TypeEventData.getProperty("ActivityEditedBy").toString();
                    String lastAction = TypeEventData.getProperty("LastAction").toString();

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

                    record_st[i]=recdno;
                    company_st[i]=company;
                    action_st[i]=lastAction;
                    editor_st[i]=editor;

                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

//            if(profit[0]==null)
//            {
//                selesPipeCard.setVisibility(View.GONE);
//            }

            if(record_st != null && record_st.length > 0)
            {
                if(record_st[0]==null)
                {
                    linearLayout1.removeAllViews();
                    NADisplay2.setVisibility(View.VISIBLE);
//                Log.e("LVSize",""+recentListView.getCount());
                    GetTodayEvent getTodayEvent=new GetTodayEvent();
                    getTodayEvent.execute();

                }
                else
                {
                    NADisplay2.setVisibility(View.GONE);
                    DisplayOne();

                    GetTodayEvent getTodayEvent=new GetTodayEvent();
                    getTodayEvent.execute();
                }
            }
            else
            {
//                pd.cancel();
//                Toast.makeText(mActivity,"Please check your Internet or reload page...",Toast.LENGTH_LONG).show();
                NADisplay2.setVisibility(View.VISIBLE);

                GetTodayEvent getTodayEvent=new GetTodayEvent();
                getTodayEvent.execute();
            }
        }

        @Override
        public void onClick(View v)
        {
//            Toast.makeText(mActivity,record_tv[v.getId()].getText(),Toast.LENGTH_LONG).show();
            Log.e("RecordId==>",""+record_tv[v.getId()].getText());

            String encp=encrptVal();
            String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + record_tv[v.getId()].getText() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=account";

            Log.d("MainURL",url);
            Log.e("Call","Recent");

            Intent intent = new Intent(getActivity(),CommonWebView.class);
            intent.putExtra("url", url);
            intent.putExtra("frg", "dash");

            getActivity().startActivity(intent);

        }

        public void DisplayAll()
        {

            child = new LinearLayout[record_st.length];
            record_tv = new TextView[record_st.length];
            company_tv = new TextView[record_st.length];
            editor_tv = new TextView[record_st.length];
            action_tv = new TextView[record_st.length];
//            devider = new View[record_st.length];

            linearLayout1.removeAllViews();
            Button more=new Button(mActivity);
            more.setText("Close...");
            more.setTextColor(Color.parseColor("#0051A1"));
            more.setGravity(Gravity.RIGHT);
            more.setVisibility(View.VISIBLE);
            for(x=0;x<record_st.length;x++)
            {
                Log.e("x",""+x);
                Log.e("record",""+record_tv[x]);
                child[x]=new LinearLayout(mActivity);
                child[x].setId(x);

                record_tv[x] = new TextView(mActivity);
                company_tv[x] = new TextView(mActivity);
                action_tv[x] = new TextView(mActivity);
                editor_tv[x] = new TextView(mActivity);
//                devider[x] = new View(mActivity);

                record_tv[x].setText(record_st[x]);
                record_tv[x].setVisibility(View.GONE);

                company_tv[x].setText(company_st[x]);
                company_tv[x].setTextSize(20);
                company_tv[x].setTextColor(Color.parseColor("#439ECB"));

                editor_tv[x].setText("by "+editor_st[x]);
                editor_tv[x].setTextSize(12);
                editor_tv[x].setTextColor(Color.parseColor("#439ECB"));

                action_tv[x].setText(action_st[x]);
                action_tv[x].setTextSize(15);

                child[x].setOrientation(LinearLayout.VERTICAL);
                child[x].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                child[x].setOnClickListener(this);

//                devider[x].setBackgroundColor(getResources().getColor(android.R.color.black));
//                devider[x].setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));

                child[x].addView(record_tv[x]);
                child[x].addView(company_tv[x]);
                child[x].addView(editor_tv[x]);
                child[x].addView(action_tv[x]);
//                child[x].addView(devider[x]);

                linearLayout1.addView(child[x]);

                if(manager.getFontStyle(mActivity).equals("open-sans"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT1);
                    CustomFont.markAsIconContainer(child[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout1, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("pt-sans"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT2);
                    CustomFont.markAsIconContainer(child[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout1, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("Lora-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT3);
                    CustomFont.markAsIconContainer(child[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout1, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("DroidSerif-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT4);
                    CustomFont.markAsIconContainer(child[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout1, mainFont);
                }
            }
            linearLayout1.addView(more);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DisplayOne();
                }
            });
        }

        public void DisplayOne()
        {
            child = new LinearLayout[record_st.length];
            record_tv = new TextView[record_st.length];
            company_tv = new TextView[record_st.length];
            editor_tv = new TextView[record_st.length];
            action_tv = new TextView[record_st.length];
//            devider = new View[record_st.length];

            linearLayout1.removeAllViews();

            Button more=new Button(mActivity);
            more.setText("More>>>");
            more.setTextColor(Color.parseColor("#0051A1"));
            more.setGravity(Gravity.RIGHT);
            more.setVisibility(View.VISIBLE);

            child[0]=new LinearLayout(mActivity);
            child[0].setId(0);

            record_tv[0] = new TextView(mActivity);
            company_tv[0] = new TextView(mActivity);
            action_tv[0] = new TextView(mActivity);
            editor_tv[0] = new TextView(mActivity);
//            devider[0] = new View(mActivity);

            record_tv[0].setText(record_st[0]);
            record_tv[0].setVisibility(View.GONE);

            company_tv[0].setText(company_st[0]);
            company_tv[0].setTextSize(20);
            company_tv[0].setTextColor(Color.parseColor("#439ECB"));

            editor_tv[0].setText("by "+editor_st[0]);
            editor_tv[0].setTextSize(12);
            editor_tv[0].setTextColor(Color.parseColor("#439ECB"));

            action_tv[0].setText(action_st[0]);
            action_tv[0].setTextSize(15);

            child[0].setOrientation(LinearLayout.VERTICAL);
            child[0].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child[0].setOnClickListener(this);

//            devider[0].setBackgroundColor(getResources().getColor(android.R.color.black));
//            devider[0].setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));

            child[0].addView(record_tv[0]);
            child[0].addView(company_tv[0]);
            child[0].addView(editor_tv[0]);
            child[0].addView(action_tv[0]);
//            child[0].addView(devider[0]);

            linearLayout1.addView(child[0]);

            linearLayout1.addView(more);

            if(manager.getFontStyle(mActivity).equals("open-sans"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT1);
                CustomFont.markAsIconContainer(child[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout1, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("pt-sans"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT2);
                CustomFont.markAsIconContainer(child[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout1, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("Lora-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT3);
                CustomFont.markAsIconContainer(child[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout1, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("DroidSerif-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT4);
                CustomFont.markAsIconContainer(child[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout1, mainFont);
            }

            more.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DisplayAll();
                }
            });
        }

    }

    private class GetTodayEvent extends AsyncTask<Void, Void, Void> implements View.OnClickListener
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("GetData", "GetData");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/TodaysCallBackEvent";
            String METHOD_NAME = "TodaysCallBackEvent";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
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
                Log.e("ResultCount",""+resultRequestSOAP.getPropertyCount());

                recordTE_st=new String[resultRequestSOAP.getPropertyCount()];
                callbackId_st=new String[resultRequestSOAP.getPropertyCount()];
                companyTE_st=new String[resultRequestSOAP.getPropertyCount()];
                startTime_st=new String[resultRequestSOAP.getPropertyCount()];
                eventType_st=new String[resultRequestSOAP.getPropertyCount()];
//                eventName_st=new String[resultRequestSOAP.getPropertyCount()];

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    Log.e("For",""+i);
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String recdno = TypeEventData.getProperty("RECDNO").toString();
                    String callbackno = TypeEventData.getProperty("CallBackID").toString();

                    String company = TypeEventData.getProperty("Company").toString();
                    String startTime = TypeEventData.getProperty("StartTime").toString();
                    String eventType = TypeEventData.getProperty("EventType").toString();
                    String eventName = TypeEventData.getProperty("EventName").toString();

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

                    recordTE_st[i]=recdno;
                    Log.e("recordTE_st["+i+"]","==>"+recordTE_st[i]);
                    callbackId_st[i]=callbackno;

                    companyTE_st[i]=company;
                    eventType_st[i]=eventType;
                    startTime_st[i]=startTime;
//                    eventName_st[i]=eventName;


                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            login.getSettings().setJavaScriptEnabled(true);
            login.getSettings().setLoadWithOverviewMode(true);
            login.getSettings().setUseWideViewPort(true);
            login.loadUrl(manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encrptVal() + "&topage=mobile_frmwelcome.asp");

            login.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                    view.loadUrl(url);
                    Log.e("Url","Loaded");
                    return false;
                }

                @Override
                public void onPageFinished(android.webkit.WebView view, String url)
                {
                    super.onPageFinished(view, url);

//                    pd.cancel();
                    if(recordTE_st != null && recordTE_st.length > 0)
                    {
                        if (recordTE_st[0]==null)
                        {
                            Log.e("Call","if");
                            linearLayout2.removeAllViews();
                            NADisplay.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.e("Call","else");
                            NADisplay.setVisibility(View.GONE);
                            DisplayOne();
                        }
                    }
                    else
                    {
                        Toast.makeText(mActivity,"Please check your Internet or reload page...",Toast.LENGTH_LONG).show();
                        NADisplay.setVisibility(View.VISIBLE);
                    }
                    pd.cancel();
                }
            });

/*
            login.setWebChromeClient(new WebChromeClient()
            {
                public void onProgressChanged(WebView view, int progress)
                {
                    //Make the bar disappear after URL is loaded, and changes string to Loading...

                    // Return the app name after finish loading
//                    Toast.makeText(getActivity(),"complete "+progress,Toast.LENGTH_SHORT).show();
                    if(progress == 100)
                    {
                        pd.cancel();
                        if(recordTE_st != null && recordTE_st.length > 0)
                        {
                            if (recordTE_st[0]==null)
                            {
                                Log.e("Call","if");
                                linearLayout2.removeAllViews();
                                NADisplay.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Log.e("Call","else");
                                NADisplay.setVisibility(View.GONE);
                                DisplayOne();
                            }
//                            pd.cancel();
                        }
                        else
                        {
//                            pd.cancel();
                            Toast.makeText(mActivity,"Please check your Internet or reload page...",Toast.LENGTH_LONG).show();
                            NADisplay.setVisibility(View.VISIBLE);
                        }

                    }
                }
            });
*/
//            login.setWebViewClient(new WebViewClient());
        }

        public void DisplayAll()
        {

            child2 = new LinearLayout[recordTE_st.length];
            child3 = new LinearLayout[recordTE_st.length];

            recordTE_tv = new TextView[recordTE_st.length];
            callbackIdTE_tv = new TextView[recordTE_st.length];
            companyTE_tv = new TextView[recordTE_st.length];
            startTE_tv = new TextView[recordTE_st.length];
            iconTE_tv = new TextView[recordTE_st.length];
            eventTypeTE_tv = new TextView[recordTE_st.length];
//            eventNameTE_tv = new TextView[recordTE_st.length];
//            devider = new View[recordTE_st.length];

            linearLayout2.removeAllViews();

            Button more=new Button(mActivity);
            more.setText("Close...");
            more.setTextColor(Color.parseColor("#0051A1"));
            more.setGravity(Gravity.RIGHT);
            more.setVisibility(View.VISIBLE);

            for(x=0 ; x<recordTE_st.length ; x++)
            {
                Log.e("x",""+x);
                Log.e("record",""+recordTE_tv[x]);
                child2[x]=new LinearLayout(mActivity);
                child3[x]=new LinearLayout(mActivity);

                child2[x].setId(x);
                child3[x].setId(Integer.parseInt("300"+x));

                recordTE_tv[x] = new TextView(mActivity);
                callbackIdTE_tv[x] = new TextView(mActivity);
                companyTE_tv[x] = new TextView(mActivity);
                startTE_tv[x] = new TextView(mActivity);
                iconTE_tv[x] = new TextView(mActivity);
                eventTypeTE_tv[x] = new TextView(mActivity);
//                eventNameTE_tv[x] = new TextView(mActivity);
//                devider[x] = new View(mActivity);

                recordTE_tv[x].setText(recordTE_st[x]);
                recordTE_tv[x].setVisibility(View.GONE);

                callbackIdTE_tv[x].setText(callbackId_st[x]);
                callbackIdTE_tv[x].setVisibility(View.GONE);

                companyTE_tv[x].setText(companyTE_st[x]);
                companyTE_tv[x].setTextSize(20);
                companyTE_tv[x].setTextColor(Color.parseColor("#439ECB"));

                startTE_tv[x].setText("\tat "+startTime_st[x]);
                iconTE_tv[x].setText(getResources().getText(R.string.fa_recentitems));

                startTE_tv[x].setTextSize(12);
                iconTE_tv[x].setTextSize(14);
//                startTE_tv[x].setTextColor(Color.parseColor("#439ECB"));

                eventTypeTE_tv[x].setText(eventType_st[x]);
                eventTypeTE_tv[x].setTextSize(15);

                child3[x].setOrientation(LinearLayout.HORIZONTAL);
                child3[x].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                child3[x].addView(iconTE_tv[x]);
                child3[x].addView(startTE_tv[x]);

                child2[x].setOrientation(LinearLayout.VERTICAL);
                child2[x].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                child2[x].setOnClickListener(this);

//                devider[x].setBackgroundColor(getResources().getColor(android.R.color.black));
//                devider[x].setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));

                child2[x].addView(recordTE_tv[x]);
                child2[x].addView(companyTE_tv[x]);
//                child2[x].addView(startTE_tv[x]);
                child2[x].addView(child3[x]);
                child2[x].addView(eventTypeTE_tv[x]);
//                child2[x].addView(eventNameTE_tv[x]);
//                child2[x].addView(devider[x]);

                linearLayout2.addView(child2[x]);

                if(manager.getFontStyle(mActivity).equals("open-sans"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT1);
                    CustomFont.markAsIconContainer(child2[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout2, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("pt-sans"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT2);
                    CustomFont.markAsIconContainer(child2[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout2, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("Lora-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT3);
                    CustomFont.markAsIconContainer(child2[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout2, mainFont);
                }
                else if(manager.getFontStyle(mActivity).equals("DroidSerif-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT4);
                    CustomFont.markAsIconContainer(child2[x], mainFont);
                    CustomFont.markAsIconContainer(linearLayout2, mainFont);
                }

            }

            linearLayout2.addView(more);

            for(int j=0;j<child3.length;j++)
            {
                Typeface iconFont = FontManager.getTypeface(mActivity.getApplicationContext(), FontManager.FONTAWESOME);
                FontManager.markAsIconContainer(child3[j], iconFont);
            }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DisplayOne();
                }
            });
        }

        public void DisplayOne()
        {
            child2 = new LinearLayout[recordTE_st.length];
            child3 = new LinearLayout[recordTE_st.length];

            recordTE_tv = new TextView[recordTE_st.length];
            callbackIdTE_tv = new TextView[recordTE_st.length];
            companyTE_tv = new TextView[recordTE_st.length];
            eventTypeTE_tv = new TextView[recordTE_st.length];
            startTE_tv = new TextView[recordTE_st.length];
            iconTE_tv = new TextView[recordTE_st.length];
//            eventNameTE_tv = new TextView[recordTE_st.length];
//            devider = new View[record_st.length];

            linearLayout2.removeAllViews();

            Button more=new Button(mActivity);
            more.setText("More>>>");
            more.setTextColor(Color.parseColor("#0051A1"));
            more.setGravity(Gravity.RIGHT);
            more.setVisibility(View.VISIBLE);

            child2[0]=new LinearLayout(mActivity);
            child3[0]=new LinearLayout(mActivity);

            child2[0].setId(0);
            child3[0].setId(Integer.parseInt("300"+0));

            recordTE_tv[0] = new TextView(mActivity);
            callbackIdTE_tv[0] = new TextView(mActivity);
            companyTE_tv[0] = new TextView(mActivity);
            eventTypeTE_tv[0] = new TextView(mActivity);
//            eventNameTE_tv[0] = new TextView(mActivity);
            startTE_tv[0] = new TextView(mActivity);
            iconTE_tv[0] = new TextView(mActivity);

            recordTE_tv[0].setText(recordTE_st[0]);
            recordTE_tv[0].setVisibility(View.GONE);

            callbackIdTE_tv[0].setText(callbackId_st[0]);
            callbackIdTE_tv[0].setVisibility(View.GONE);

            companyTE_tv[0].setText(companyTE_st[0]);
            companyTE_tv[0].setTextSize(20);
            companyTE_tv[0].setTextColor(Color.parseColor("#439ECB"));

            startTE_tv[0].setText("\tat "+startTime_st[0]);
            if(isAdded())
            {
                iconTE_tv[0].setText(getResources().getText(R.string.fa_recentitems));
            }

            startTE_tv[0].setTextSize(12);
            iconTE_tv[0].setTextSize(14);
//            startTE_tv[0].setTextColor(Color.parseColor("#439ECB"));

            eventTypeTE_tv[0].setText(eventType_st[0]);
            eventTypeTE_tv[0].setTextSize(15);

            child3[0].setOrientation(LinearLayout.HORIZONTAL);
            child2[0].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            child2[0].setOrientation(LinearLayout.VERTICAL);
            child2[0].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child2[0].setOnClickListener((View.OnClickListener) this);

//            devider[0].setBackgroundColor(getResources().getColor(android.R.color.black));
//            devider[0].setLayoutParams (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));

            child3[0].addView(iconTE_tv[0]);
            child3[0].addView(startTE_tv[0]);

            child2[0].addView(recordTE_tv[0]);
            child2[0].addView(callbackIdTE_tv[0]);
            child2[0].addView(companyTE_tv[0]);
//            child2[0].addView(startTE_tv[0]);
            child2[0].addView(child3[0]);
            child2[0].addView(eventTypeTE_tv[0]);
//            child2[0].addView(devider[0]);

            linearLayout2.addView(child2[0]);

            linearLayout2.addView(more);

            if(manager.getFontStyle(mActivity).equals("open-sans"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT1);
                CustomFont.markAsIconContainer(child2[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout2, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("pt-sans"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT2);
                CustomFont.markAsIconContainer(child2[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout2, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("Lora-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT3);
                CustomFont.markAsIconContainer(child2[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout2, mainFont);
            }
            else if(manager.getFontStyle(mActivity).equals("DroidSerif-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(mActivity.getApplicationContext(), CustomFont.FONT4);
                CustomFont.markAsIconContainer(child2[0], mainFont);
                CustomFont.markAsIconContainer(linearLayout2, mainFont);
            }

            Typeface iconFont = FontManager.getTypeface(mActivity.getApplicationContext(), FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(child3[0], iconFont);

            more.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DisplayAll();
                }
            });
        }

        @Override
        public void onClick(View v)
        {
//            Toast.makeText(mActivity,recordTE_tv[v.getId()].getText(),Toast.LENGTH_LONG).show();

            Log.e("RecordID,CallbackID",recordTE_tv[v.getId()].getText()+","+callbackIdTE_tv[v.getId()].getText());
            String encp=encrptVal();

            Log.e("Call","Today'sEvent");
            String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + recordTE_tv[v.getId()].getText() + "&CompanyID=" + manager.getWG(mActivity) + "&CallBackID=" + callbackIdTE_tv[v.getId()].getText() + "&appkeyword=&pagetype=callback";

            Log.d("MainURL",url);

            Intent intent = new Intent(getActivity(),CommonWebView.class);
            intent.putExtra("url", url);
            intent.putExtra("frg", "acc");
            getActivity().startActivity(intent);
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