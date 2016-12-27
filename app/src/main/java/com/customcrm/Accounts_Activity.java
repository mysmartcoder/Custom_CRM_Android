package com.customCRM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.MultiChoiceModeListener;
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

public class Accounts_Activity extends Fragment
{
    TextView title;

    ListView lvAccounts;
    ArrayList list;

    String[] filterList;

    ProgressDialog pd;

    SessionManager manager;

    String searchCompanyName=null;

    EditText searchEdit;

    ImageButton searchBtn;

    int totalRcordDIsplay;
    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    static int check=0;
    private Activity mActivity;

    Accounts_adapter adapter=null;

    String RecordsId="";
    String checkDeletion;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_accounts, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
//        filterList=new List();

        lvAccounts=(ListView)view.findViewById(R.id.accounts_listview);

        pd=new ProgressDialog(mActivity);

        check=0;

        title=(TextView)view.findViewById(R.id.account_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Banner - Accounts"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.acc_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.acc_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.Account_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.Account_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.Account_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.Account_parent), mainFont);
        }

        searchEdit=(EditText)view.findViewById(R.id.account_search_edit);
//        searchEdit.setThreshold(2);

        searchEdit.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Accounts"));
        searchBtn=(ImageButton)view.findViewById(R.id.search_account_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_Acc);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_Acc);

        adapter=new Accounts_adapter(mActivity, list,i,j);

//        PrepareDrawer prepareDrawer=new PrepareDrawer();
//        PrepareDrawer.allSet(getActivity());

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {
            if(bundle.containsKey("query"))
            {
                Log.e("success","1");
                check=1;
                searchCompanyName = bundle.getString("query");
                searchEdit.setText(searchCompanyName);
                if(searchCompanyName.isEmpty() || searchCompanyName.equals("null") || searchCompanyName == null)
                {
                    Toast.makeText(getActivity(),"Please enter Company Name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i=1;j=15;
                    SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
                    searchTotalCallBackEvent.execute();

                }
            }

            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                check=1;
                searchCompanyName = bundle.getString("searchQuery");

                searchEdit.setText(searchCompanyName);
                if(searchCompanyName.isEmpty() || searchCompanyName.equals("null") || searchCompanyName == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchCompanyName);

                    SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+searchCompanyName);

                    SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
            getTotalCallBackEvent.execute();


        }

        lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Accounts_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
                String url =manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=account";
//              manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";

                Log.d("MainURL",url);

             /*   Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","acc");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchEdit.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "acc");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);

            }
        });

        lvAccounts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lvAccounts.setMultiChoiceModeListener(new MultiChoiceModeListener()
        {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
//                Toast.makeText(getActivity(),"prepare action mode",Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
//                Toast.makeText(getActivity(),"destroy action mode",Toast.LENGTH_LONG).show();

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
//                Toast.makeText(getActivity(),"create actionmode",Toast.LENGTH_LONG).show();
                MenuInflater inflater = mActivity.getMenuInflater();
                inflater.inflate(R.menu.selection_operation_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item)
            {
                // TODO  Auto-generated method stub
                switch  (item.getItemId())
                {
                    case R.id.option_menu_assign:


                        SparseBooleanArray  selected = adapter.getSelectedIds();
                        Accounts_Data  selecteditem = null;
                        for (int i =  (selected.size() - 1); i >= 0; i--)
                        {
                            if  (selected.valueAt(i))
                            {
                                selecteditem = adapter.getItem(selected.keyAt(i));
                                // Remove  selected items following the ids
                                //adapter.remove(selecteditem);
                                RecordsId+=selecteditem.getRecordId()+",";
                            }
                        }
                        RecordsId = RecordsId.substring(0, RecordsId.length() - 1);
//                                Toast.makeText(getActivity(),selecteditem.getRecordId(),Toast.LENGTH_LONG).show();
                        Log.e("Records",RecordsId);

                        String encp=encrptVal();
                        String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" +  manager.getWG(getActivity()) + "&RECDNO=" + RecordsId;

                        Log.d("MainURL",url);
                        mode.finish();
                        /*Fragment webviewFragment = new Common_WebView();

                        Bundle bundle = new Bundle();

                        bundle.putString("url", url);
                        bundle.putString("frg","acc");

                        webviewFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                        fragmentTransaction.commit();

                        fragmentManager.executePendingTransactions();*/
                        Intent intent = new Intent(getActivity(),CommonWebView.class);
                        intent.putExtra("url", url);
                        intent.putExtra("frg", "acc");
                        intent.putExtra("startIndex", i);
                        intent.putExtra("endIndex", j);
                        intent.putExtra("searchQuery", searchEdit.getText().toString());
                        getActivity().startActivity(intent);

                        return true;
                    case R.id.option_menu_delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to delete selected record(s)?");

                        builder.setNegativeButton("No", new  DialogInterface.OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub

                            }
                        });
                        builder.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {

                            @Override
                            public void  onClick(DialogInterface dialog, int which)
                            {

                                // TODO  Auto-generated method stub
                                SparseBooleanArray  selected = adapter.getSelectedIds();
                                Accounts_Data  selecteditem = null;
                                for (int i =  (selected.size() - 1); i >= 0; i--)
                                {
                                    if  (selected.valueAt(i))
                                    {
                                        selecteditem = adapter.getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                        //adapter.remove(selecteditem);
                                        RecordsId+=selecteditem.getRecordId()+",";
                                    }
                                }
                                RecordsId = RecordsId.substring(0, RecordsId.length() - 1);
//                                Toast.makeText(getActivity(),selecteditem.getRecordId(),Toast.LENGTH_LONG).show();
                                Log.e("Records",RecordsId);
                                DeleteAccRecords deleteAccRecords=new DeleteAccRecords(RecordsId);
                                deleteAccRecords.execute();
                                // Close CAB
                                mode.finish();
                                selected.clear();

                            }
                        });
                        AlertDialog alert =  builder.create();
                        alert.setIcon(R.drawable.delete_alert);// dialog  Icon
                        alert.setTitle("Confirmation"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
            {
//               // TODO  Auto-generated method stub
                final int checkedCount  = lvAccounts.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount  + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchCompanyName=searchEdit.getText().toString();

                if(searchCompanyName.isEmpty() || searchCompanyName.equals("null") || searchCompanyName == null)
                {
                    Toast.makeText(getActivity(),"Please enter Company Name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();

                    i=1;j=15;
                    SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
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
                if(check!=1)
                {
                    Log.e("check","execute");
                    searchCompanyName=searchEdit.getText().toString();
                }

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

                    if(searchCompanyName.equals("null") && searchCompanyName == null && searchCompanyName.isEmpty())
                    {
                        GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
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
                if(check!=1)
                {
                    Log.e("check","execute");
                    searchCompanyName=searchEdit.getText().toString();
                }


                int x,y;
                x=i-15;
//                y=j-15;

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
//                i=16;j=30;
//                i=1;j=15
                    i=i-15;
//                    j=j-15;
                    j=y;
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    if(searchCompanyName.isEmpty() || searchCompanyName.equals("null") || searchCompanyName == null)
                    {
                        GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalCompany searchTotalCallBackEvent=new SearchTotalCompany(searchCompanyName);
                        searchTotalCallBackEvent.execute();
                    }
                }
                else
                {
                    Log.e("exteute","false");
                    Log.e("exteute",""+x+","+y);
                }

            }
        });

//        GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//        getTotalCallBackEvent.execute();

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
////                if(keyCode == KeyEvent.KEYCODE_BACK )
//                    if(event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK )
////                    if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    // handle back button's click listener
//
////                    getFragmentManager().popBackStack();
//                    Fragment fragment = new Dashboard_Activity();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.containerView,fragment,null);
//                    fragmentTransaction.commit();
//
//                    return true;
//                }
//                return false;
//            }
//        });

        return view;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode)
//        {
//            case 1:
//            {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//
//                   Log.e("PermissionResult : ","If onRequestPermissionsResult");
//
//                }
//                else
//                {
//                    Log.e("PermissionResult : ","Else onRequestPermissionsResult");
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'switch' lines to check for other
//            // permissions this app might request
//        }
//    }

    private class GetSearchAccount extends AsyncTask<Void, Void, Void>
    {

        int i,j;

        public GetSearchAccount(int i, int j)
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
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
//
//            if(list.equals(null))
//            {
//                new AlertDialog.Builder(mActivity).setMessage("Server not responding").show();
//            }
//            else
//            {
                lvAccounts.setAdapter(new Accounts_adapter(mActivity, list,i,j));
//            }
        }
    }
    private void GetData(int start, int end)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchAccountData";
        String METHOD_NAME = "SearchAccountData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("company", "");

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
                String company = TypeEventData.getProperty("Company").toString();
                String lead_status = TypeEventData.getProperty("Lead_Status").toString();
                String lead_source = TypeEventData.getProperty("Lead_Source").toString();
                String phone = TypeEventData.getProperty("Phone").toString();
                String recordId = TypeEventData.getProperty("RecordID").toString();
                String latitude = TypeEventData.getProperty("Latitude").toString();
                String longitude = TypeEventData.getProperty("Longitude").toString();

                String acc_mgr = TypeEventData.getProperty("AcctMgr").toString();
                String campaign = TypeEventData.getProperty("Mkt_Program_ID").toString();
                String email = TypeEventData.getProperty("Email").toString();
                String contactID = TypeEventData.getProperty("ContactID").toString();

                Log.e("ContactId",contactID);

                Accounts_Data  callbacktaskData=new Accounts_Data();

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

                if(lead_status.equals("anyType{}"))
                {
                    lead_status="none";
                }else
                {
                    lead_status=lead_status+"";
                }

                if(lead_source.equals("anyType{}"))
                {
                    lead_source="none";
                }else
                {
                    lead_source=lead_source+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(acc_mgr.equals("anyType{}"))
                {
                    acc_mgr="none";
                }else
                {
                    acc_mgr=acc_mgr+"";
                }
                if(campaign.equals("anyType{}"))
                {
                    campaign="none";
                }else
                {
                    campaign=campaign+"";
                }
                if(email.equals("anyType{}"))
                {
                    email="";
                }else
                {
                    email=email+"";
                }

                callbacktaskData.setCompany(company);
                callbacktaskData.setEntered(entered);
                callbacktaskData.setLeadSource(lead_source);
                callbacktaskData.setLeadStatus(lead_status);
                callbacktaskData.setPhone(phone);
                callbacktaskData.setRecordId(recordId);
                callbacktaskData.setLang(longitude);
                callbacktaskData.setLat(latitude);

                callbacktaskData.setAcc_mgr(acc_mgr);
                callbacktaskData.setCampaign(campaign);
                callbacktaskData.setEmail(email);

                callbacktaskData.setContactId(contactID);

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
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+""+j);
    }


    private class GetTotalAccount extends AsyncTask<Void, Void, Void>
    {

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
            String SOAP_ACTION = "LMServiceNamespace/SearchAccountCount";
            String METHOD_NAME = "SearchAccountCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("company", "");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchAccountCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();


                manager.setCounts(getActivity(),""+totalRcordDIsplay,manager.getCon(getActivity()),manager.getOpp(getActivity()),manager.getCases(getActivity()),manager.getQuote(getActivity()));

                Log.e("TotalRecord", ""+totalRcordDIsplay);
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

                GetSearchAccount getSearchCallBackEvent=new GetSearchAccount(i,j);
                getSearchCallBackEvent.execute();
            }


//            }

        }
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    //For Search by Company Name...

    private class SearchTotalCompany extends AsyncTask<Void, Void, Void>
    {

        String company;
        public SearchTotalCompany(String searchCompanyName)
        {
            company=searchCompanyName;
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
            String SOAP_ACTION = "LMServiceNamespace/SearchAccountCount";
            String METHOD_NAME = "SearchAccountCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("company", company);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchAccountCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord", ""+totalRcordDIsplay);
                Log.e("Company", company);
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

                SearchCompanyAccount searchCompanyAccount=new SearchCompanyAccount(i,j,company);
                searchCompanyAccount.execute();
            }


        }
    }

    private class SearchCompanyAccount extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String company;

        public SearchCompanyAccount(int i, int j, String company)
        {
            this.i=i;
            this.j=j;
            this.company=company;
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
            searchCompany(i,j,company);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
//            customAdapter=new CustomAdapter(Accounts_Activity.this,R.layout.raw_callbacktask,list);
//            lvAccounts.setAdapter(new Accounts_adapter(Accounts_Activity.this, list));
            lvAccounts.setAdapter(new Accounts_adapter(mActivity, list,i,j));

//            GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//            getTotalCallBackEvent.execute();

        }
    }

    private void searchCompany(int start, int end, String searchCompanyName)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchAccountData";
        String METHOD_NAME = "SearchAccountData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("company", searchCompanyName);


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
                String company = TypeEventData.getProperty("Company").toString();
                String lead_status = TypeEventData.getProperty("Lead_Status").toString();
                String lead_source = TypeEventData.getProperty("Lead_Source").toString();
                String phone = TypeEventData.getProperty("Phone").toString();
                String recordId = TypeEventData.getProperty("RecordID").toString();
                String latitude = TypeEventData.getProperty("Latitude").toString();
                String longitude = TypeEventData.getProperty("Longitude").toString();

                String acc_mgr = TypeEventData.getProperty("AcctMgr").toString();
                String campaign = TypeEventData.getProperty("Mkt_Program_ID").toString();
                String email = TypeEventData.getProperty("Email").toString();

                String contactID = TypeEventData.getProperty("ContactID").toString();
                Log.e("ContactId",contactID);

                Accounts_Data  callbacktaskData=new Accounts_Data();

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

                if(lead_status.equals("anyType{}"))
                {
                    lead_status="none";
                }else
                {
                    lead_status=lead_status+"";
                }

                if(lead_source.equals("anyType{}"))
                {
                    lead_source="none";
                }else
                {
                    lead_source=lead_source+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(acc_mgr.equals("anyType{}"))
                {
                    acc_mgr="none";
                }else
                {
                    acc_mgr=acc_mgr+"";
                }
                if(campaign.equals("anyType{}"))
                {
                    campaign="none";
                }else
                {
                    campaign=campaign+"";
                }
                if(email.equals("anyType{}"))
                {
                    email="";
                }else
                {
                    email=email+"";
                }


                callbacktaskData.setCompany(company);
                callbacktaskData.setEntered(entered);
                callbacktaskData.setLeadSource(lead_source);
                callbacktaskData.setLeadStatus(lead_status);
                callbacktaskData.setPhone(phone);
                callbacktaskData.setRecordId(recordId);
                callbacktaskData.setLang(longitude);
                callbacktaskData.setLat(latitude);

                callbacktaskData.setAcc_mgr(acc_mgr);
                callbacktaskData.setCampaign(campaign);
                callbacktaskData.setEmail(email);

                callbacktaskData.setContactId(contactID);

                Log.e("recordid",recordId);

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

    private class DeleteAccRecords extends AsyncTask<Void, Void, Void>
    {

        String recordId;
        public DeleteAccRecords(String recordId)
        {
            this.recordId=recordId;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Accounts_Activity.this);
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/DeleteRecords";
            String METHOD_NAME = "DeleteRecords";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("RecordIDs", recordId);
                Request.addProperty("LogonID", manager.getUserId(getActivity()));
                Request.addProperty("CompanyID", manager.getWG(getActivity()));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteRecordsResult").toString();

                Log.e("deletion", checkDeletion);

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

            if(pd != null && pd.isShowing())
            {
                pd.dismiss();
            }
            RecordsId="";
            if(checkDeletion.equals("SUCCESS"))
            {
                Toast.makeText(getActivity(),"Record delete successfully",Toast.LENGTH_LONG).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = new Accounts_Activity();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, newFragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                fragmentManager.executePendingTransactions();


            }
            else
            {
                Toast.makeText(getActivity(),"Operation Failed",Toast.LENGTH_LONG).show();
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

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                Log.e("keycode,event",keyCode+","+event);
                if(keyCode == KeyEvent.KEYCODE_BACK )
//                if(event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Fragment fragment = new Dashboard_Activity();
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


//    private class FilterAcc extends AsyncTask<Void, Void, Void>
//    {
//        String word;
//
//        public FilterAcc(String word)
//        {
//            this.word=word;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.d("GetData","GetData");
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params)
//        {
//            FilterData(word);
////            GetTotalRecord();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            pd.cancel();
//
//            searchEdit.setAdapter(new ArrayAdapter<String>
//                    (getActivity(),android.R.layout.select_dialog_item, filterList));
//
//        }
//    }
//    private void FilterData(String word)
//    {
//        SoapObject resultRequestSOAP = null;
//        String SOAP_ACTION = "LMServiceNamespace/SearchAccountData";
//        String METHOD_NAME = "SearchAccountData";
//        String NAMESPACE = "LMServiceNamespace";
//        String URL = manager.getUrl();
//
//        try {
//            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//            Request.addProperty("username", manager.getUserName(getActivity()));
//            Request.addProperty("pwd", manager.getUserPass(getActivity()));
//            Request.addProperty("company_id", manager.getWG(getActivity()));
//            Request.addProperty("startindex", 1);
//            Request.addProperty("endindex", 10);
//            Request.addProperty("company", word);
//
//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            soapEnvelope.dotNet = true;
//            soapEnvelope.setOutputSoapObject(Request);
//
//            HttpTransportSE transport = new HttpTransportSE(URL,30000);
//
//            transport.call(SOAP_ACTION, soapEnvelope);
//            //list=new ArrayList();
//
//
//            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
//            // resultString = (SoapPrimitive) soapEnvelope.getResponse();
//            filterList=new String[resultRequestSOAP.getPropertyCount()];
//
//            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//            {
//                SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
//                String company = TypeEventData.getProperty("Company").toString();
//                Accounts_Data  callbacktaskData=new Accounts_Data();
//
//
//                if(company.equals("anyType{}"))
//                {
//                    company="none";
//                }else
//                {
//                    company=company+"";
//                }
//                filterList[i]=company;
//
//
//            }
//
//
//            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
//
////             Log.e(TAG, "Result SECOND API: " + resultString);
//        } catch (Exception ex) {
////           Log.e("", "Error: " + ex.getMessage());
//        }
//        Log.e("Url==>",URL);
//        Log.e("1", "Result SECOND filterList: " + filterList+","+totalRcordDIsplay);
//    }

}
