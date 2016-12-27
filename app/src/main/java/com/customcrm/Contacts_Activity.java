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
import android.widget.AbsListView;
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

public class Contacts_Activity extends Fragment
{
    TextView title;

    ListView lvcallbacktask;
    String TAG = "Response";
    ArrayList list;
    ProgressDialog pd;

    SessionManager manager;

    String lastname=null;
    EditText searchEdit;
    ImageButton searchBtn;

    int totalRcordDIsplay;
    TextView totalIndexDisplay,startIndexDisplay,endIndexDisplay;
    Button prevBtn,nextBtn;
    int i=1,j=15,diff=0;

    Contact_adapter adapter=null;

    String RecordsId="";
    String checkDeletion;

    private Activity mActivity;

    static int check=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_contacts, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvcallbacktask=(ListView)view.findViewById(R.id.contact_listview);

        searchEdit=(EditText)view.findViewById(R.id.contact_search_edit);
        searchEdit.setHint("Search by "+manager.getCustomLabel(mActivity,"Banner - Contacts"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.con_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        LinearLayout footer = (LinearLayout) view.findViewById(R.id.con_footer);
        footer.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.con_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.con_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.con_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.con_parent), mainFont);
        }

        searchBtn=(ImageButton)view.findViewById(R.id.search_contact_btn);

        totalIndexDisplay=(TextView)view.findViewById(R.id.totalIndex_Contact);
        startIndexDisplay=(TextView)view.findViewById(R.id.startingIndex_Contact);
        endIndexDisplay=(TextView)view.findViewById(R.id.endingIndex_Contact);
        nextBtn=(Button)view.findViewById(R.id.nextBtn_Contact);
        prevBtn=(Button)view.findViewById(R.id.prevBtn_Contact);

        adapter=new Contact_adapter(mActivity, list,i,j);

        title=(TextView)view.findViewById(R.id.contact_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Banner - Contacts"));

        Bundle bundle = this.getArguments();

        if( bundle != null)
        {

            if(bundle.containsKey("query"))
            {
                Log.e("success","1");
                lastname = bundle.getString("query");
                searchEdit.setText(lastname);
                if(lastname.isEmpty() || lastname.equals("null") || lastname == null)
                {
                }
                else
                {
                    list.clear();

                    i = 1;
                    j=15;

                    Log.e("i,j,search",i+","+j+","+lastname);

                    SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact(lastname);
                    searchTotalCallBackEvent.execute();
                }
            }

            if(bundle.containsKey("searchQuery"))
            {
                Log.e("success","1");
                lastname = bundle.getString("searchQuery");

                searchEdit.setText(lastname);
                if(lastname.isEmpty() || lastname.equals("null") || lastname == null)
                {
//                    GetTotalAccount getTotalCallBackEvent=new GetTotalAccount();
//                    getTotalCallBackEvent.execute();
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+lastname);

                    SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact("");
                    searchTotalCallBackEvent.execute();
                }
                else
                {
                    list.clear();
//                    GetSearchAccount2 getSearchCallBackEvent2=new GetSearchAccount2();
//                    getSearchCallBackEvent2.execute();
                    i = bundle.getInt("startIndex");
                    j=bundle.getInt("endIndex");

                    Log.e("i,j,search",i+","+j+","+lastname);

                    SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact(lastname);
                    searchTotalCallBackEvent.execute();

                }
            }

        }
        else
        {
            GetTotalContact getTotalCallBackEvent=new GetTotalContact();
            getTotalCallBackEvent.execute();
        }



        lvcallbacktask.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Contact_Data currentListData =adapter.getItem(position);
                String encp=encrptVal();
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=contact";
                String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage="+manager.getLoginPriv(mActivity,"DefaultPage")+"&RECDNO=" + currentListData.getRecordId() + "&CompanyID=" + manager.getWG(mActivity) + "&appkeyword=&pagetype=contact&contactid="+currentListData.getContactId();
//                url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" + companyId + "&RECDNO=" + recdNo + "&appkeyword=&pagetype=contact";
                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","contact");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchEdit.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(Contacts_Activity.this).commit();
*/
                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "contact");
                intent.putExtra("startIndex", i);
                intent.putExtra("endIndex", j);
                intent.putExtra("searchQuery", searchEdit.getText().toString());
                getActivity().startActivity(intent);
            }
        });

        lvcallbacktask.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lvcallbacktask.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
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

                        SparseBooleanArray selected = adapter.getSelectedIds();

                        Contact_Data  selecteditem = null;

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
                        Log.e("Records", RecordsId);

                        String encp=encrptVal();
                        String url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAssign.asp&frompage=mobile_RFullEdit.asp&cid=" +  manager.getWG(getActivity()) + "&RECDNO=" + RecordsId;

                        Log.d("MainURL",url);
                        mode.finish();
                       /* Fragment webviewFragment = new Common_WebView();

                        Bundle bundle = new Bundle();

                        bundle.putString("url", url);
                        bundle.putString("frg","contact");

                        webviewFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                        fragmentTransaction.commit();*/
                        Intent intent = new Intent(getActivity(),CommonWebView.class);
                        intent.putExtra("url", url);
                        intent.putExtra("frg", "contact");
                        intent.putExtra("startIndex", i);
                        intent.putExtra("endIndex", j);
                        intent.putExtra("searchQuery", searchEdit.getText().toString());
                        getActivity().startActivity(intent);
                        return true;
                    case R.id.option_menu_delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to delete selected contact(s)?");

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
                                Contact_Data  selecteditem = null;
                                for (int i =  (selected.size() - 1); i >= 0; i--)
                                {
                                    if  (selected.valueAt(i))
                                    {
                                        selecteditem = adapter.getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                        //adapter.remove(selecteditem);
                                        RecordsId+=selecteditem.getContactId()+",";
                                    }
                                }
                                RecordsId = RecordsId.substring(0, RecordsId.length() - 1);
//                                Toast.makeText(getActivity(),selecteditem.getRecordId(),Toast.LENGTH_LONG).show();
                                Log.e("Records",RecordsId);
                                DeleteContactRecords deleteAccRecords=new DeleteContactRecords(RecordsId);
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
                final int checkedCount  = lvcallbacktask.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount  + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }
        });



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastname=searchEdit.getText().toString();

                if(lastname.isEmpty() || lastname.equals("null") || lastname == null)
                {
                    Toast.makeText(getActivity(),"Please enter Last name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();

                    i=1;j=15;
                    SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact(lastname);
                    searchTotalCallBackEvent.execute();

                }
            }
        });

        //FontAwesome Code
        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.icons_container), iconFont);

        nextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lastname=searchEdit.getText().toString();

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

                    if(lastname.equals("null") && lastname == null && lastname.isEmpty())
                    {
                        GetTotalContact getTotalCallBackEvent=new GetTotalContact();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {
                        SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact(lastname);
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
                lastname=searchEdit.getText().toString();

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

                    i=i-15;
                    j=y;

                    if(lastname.isEmpty() || lastname.equals("null") || lastname == null)
                    {
                        GetTotalContact getTotalCallBackEvent=new GetTotalContact();
                        getTotalCallBackEvent.execute();
                    }
                    else
                    {


                        SearchTotalContact searchTotalCallBackEvent=new SearchTotalContact(lastname);
                        searchTotalCallBackEvent.execute();
                    }
                }

            }
        });

//
//        GetTotalContact getTotalCallBackEvent=new GetTotalContact();
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



    private class GetTotalContact extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
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
            String SOAP_ACTION = "LMServiceNamespace/SearchContactCount";
            String METHOD_NAME = "SearchContactCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("lastname", "");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchContactCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                manager.setCounts(getActivity(),manager.getAccount(getActivity()),""+totalRcordDIsplay,manager.getOpp(getActivity()),manager.getCases(getActivity()),manager.getQuote(getActivity()));

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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Banner - Contacts")+" not found");
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

                GetSearchContact getSearchCallBackEvent=new GetSearchContact(i,j);
                getSearchCallBackEvent.execute();
            }
        }
    }


    private class GetSearchContact extends AsyncTask<Void, Void, Void>
    {

        int i,j;

        public GetSearchContact(int i, int j)
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
//            list=new ArrayList();
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

            lvcallbacktask.setAdapter(new Contact_adapter(mActivity, list,i,j));
        }
    }
    private void GetData(int start, int end)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchContactData";
        String METHOD_NAME = "SearchContactData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try
        {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", "1");
            Request.addProperty("endindex", "15");
            Request.addProperty("lastname", "");

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
                String firstname = TypeEventData.getProperty("FirstName").toString();
                String lastname = TypeEventData.getProperty("LastName").toString();
                String company = TypeEventData.getProperty("Company").toString();
                String leadStatus = TypeEventData.getProperty("Lead_Status").toString();
                String campaign = TypeEventData.getProperty("Mkt_Program_ID").toString();
                String acctMgr = TypeEventData.getProperty("AcctMgr").toString();
                String phone = TypeEventData.getProperty("Phone").toString();


                String recordId = TypeEventData.getProperty("RecordID").toString();
                String contactId = TypeEventData.getProperty("ContactID").toString();
                String latitude = TypeEventData.getProperty("Latitude").toString();
                String longitude = TypeEventData.getProperty("Longitude").toString();

                String emailId = TypeEventData.getProperty("Email").toString();

                Contact_Data  callbacktaskData=new Contact_Data();

                if(entered.equals("anyType{}"))
                {
                    entered="none";
                }else
                {
                    entered=entered+"";
                }

                if(firstname.equals("anyType{}"))
                {
                    firstname="";
                }
                if(lastname.equals("anyType{}"))
                {
                    lastname="";
                }
                String contact=firstname+" "+lastname;

                if(company.equals("anyType{}"))
                {
                    company="none";
                }else
                {
                    company=company+"";
                }
                if(leadStatus.equals("anyType{}"))
                {
                    leadStatus="none";
                }else
                {
                    leadStatus=leadStatus+"";
                }
                if(campaign.equals("anyType{}"))
                {
                    campaign="none";
                }else
                {
                    campaign=campaign+"";
                }

                if(acctMgr.equals("anyType{}"))
                {
                    acctMgr="none";
                }else
                {
                    acctMgr=acctMgr+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }
                if(emailId.equals("anyType{}"))
                {
                    emailId="";
                }else
                {
                    emailId=emailId+"";
                }

                callbacktaskData.setEntered(entered);
                callbacktaskData.setCont(contact);
                callbacktaskData.setAcc(company);
                callbacktaskData.setLead(leadStatus);
                callbacktaskData.setCampaign(campaign);
                callbacktaskData.setAcc_mgr(acctMgr);
                callbacktaskData.setPhone(phone);

                callbacktaskData.setRecordId(recordId);
                callbacktaskData.setContactId(contactId);
                callbacktaskData.setLang(longitude);
                callbacktaskData.setLat(latitude);
                callbacktaskData.setEmail(emailId);


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
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+""+j);
    }

    private class SearchTotalContact extends AsyncTask<Void, Void, Void>
    {

        String lastname;
        public SearchTotalContact(String searchLastName)
        {
            lastname=searchLastName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");

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
            String SOAP_ACTION = "LMServiceNamespace/SearchContactCount";
            String METHOD_NAME = "SearchContactCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("lastname", lastname);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchContactCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord", ""+totalRcordDIsplay);
                Log.e("Company", lastname);
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

                totalIndexDisplay.setText(manager.getCustomLabel(mActivity,"Banner - Contacts")+" not found");
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

                SearchCompanyContact searchCompanyContact=new SearchCompanyContact(i,j,lastname);
                searchCompanyContact.execute();
            }

        }
    }

    private class SearchCompanyContact extends AsyncTask<Void, Void, Void>
    {

        int i,j;
        String lastname;

        public SearchCompanyContact(int i, int j, String lastname)
        {
            this.i=i;
            this.j=j;
            this.lastname=lastname;
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
            searchCompany(i,j,lastname);
//            GetTotalRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();

            lvcallbacktask.setAdapter(new Contact_adapter(mActivity, list,i,j));

        }
    }

    private void searchCompany(int start, int end, String lastname)
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/SearchContactData";
        String METHOD_NAME = "SearchContactData";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("username", manager.getUserName(getActivity()));
            Request.addProperty("pwd", manager.getUserPass(getActivity()));
            Request.addProperty("company_id", manager.getWG(getActivity()));
            Request.addProperty("startindex", start);
            Request.addProperty("endindex", end);
            Request.addProperty("lastname", lastname);


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
                String firstname = TypeEventData.getProperty("FirstName").toString();
                String lastname2 = TypeEventData.getProperty("LastName").toString();
                String company = TypeEventData.getProperty("Company").toString();
                String leadStatus = TypeEventData.getProperty("Lead_Status").toString();
                String campaign = TypeEventData.getProperty("Mkt_Program_ID").toString();
                String acctMgr = TypeEventData.getProperty("AcctMgr").toString();
                String phone = TypeEventData.getProperty("Phone").toString();


                String recordId = TypeEventData.getProperty("RecordID").toString();
                String latitude = TypeEventData.getProperty("Latitude").toString();
                String longitude = TypeEventData.getProperty("Longitude").toString();

                String emailId = TypeEventData.getProperty("Email").toString();

                Contact_Data  callbacktaskData=new Contact_Data();

                if(entered.equals("anyType{}"))
                {
                    entered="none";
                }else
                {
                    entered=entered+"";
                }

                if(firstname.equals("anyType{}"))
                {
                    firstname="";
                }
                if(lastname2.equals("anyType{}"))
                {
                    lastname2="";
                }
                String contact=firstname+" "+lastname2;


                if(company.equals("anyType{}"))
                {
                    company="none";
                }else
                {
                    company=company+"";
                }
                if(leadStatus.equals("anyType{}"))
                {
                    leadStatus="none";
                }else
                {
                    leadStatus=leadStatus+"";
                }
                if(campaign.equals("anyType{}"))
                {
                    campaign="none";
                }else
                {
                    campaign=campaign+"";
                }

                if(acctMgr.equals("anyType{}"))
                {
                    acctMgr="none";
                }else
                {
                    acctMgr=acctMgr+"";
                }

                if(phone.equals("anyType{}"))
                {
                    phone="none";
                }else
                {
                    phone=phone+"";
                }

                if(emailId.equals("anyType{}"))
                {
                    emailId="";
                }else
                {
                    emailId=emailId+"";
                }

                callbacktaskData.setEntered(entered);
                callbacktaskData.setCont(contact);
                callbacktaskData.setAcc(company);
                callbacktaskData.setLead(leadStatus);
                callbacktaskData.setCampaign(campaign);
                callbacktaskData.setAcc_mgr(acctMgr);
                callbacktaskData.setPhone(phone);

                callbacktaskData.setRecordId(recordId);
                callbacktaskData.setLang(longitude);
                callbacktaskData.setLat(latitude);
                callbacktaskData.setEmail(emailId);

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
        }
        Log.e("Url==>",URL);
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
        Log.e("list===============", ""+list.size());
        Log.e("i,j",""+i+","+j);

    }


    private class DeleteContactRecords extends AsyncTask<Void, Void, Void>
    {

        String recordId;
        public DeleteContactRecords(String recordId)
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
            String SOAP_ACTION = "LMServiceNamespace/DeleteContacts";
            String METHOD_NAME = "DeleteContacts";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("ContactIDs", recordId);
                Request.addProperty("LogonID", manager.getUserId(getActivity()));
                Request.addProperty("CompanyID", manager.getWG(getActivity()));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteContactsResult").toString();

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
                Toast.makeText(getActivity(),manager.getCustomLabel(mActivity,"Banner - Contacts")+" delete successfully",Toast.LENGTH_LONG).show();

                Fragment newFragment = new Contacts_Activity();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.containerView, newFragment);
//                transaction.addToBackStack(null);
                transaction.commit();

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

}
