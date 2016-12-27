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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyShortcuts_Activity extends Fragment
{
    TextView title;

    ExpandableListView lvMyShortcut;
//    ArrayList<MyShortcut_Data> list;

    ProgressDialog pd;

    SessionManager manager;

//    EditText searchEdit;
    ImageButton searchBtn;

    private Activity mActivity;
    MyShortcut_ExpandableList adapter=null;
//    MyShortcut_adapter adapter=null;

    List<String> listDataHeader;
    HashMap<String, List<MyShortcut_Data>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_my_shortcuts, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listDataHeader = new ArrayList<String>();
//        listDataHeader1 = new HashMap<>();

        listDataChild = new HashMap<String, List<MyShortcut_Data>>();

        lvMyShortcut=(ExpandableListView)view.findViewById(R.id.myShortcut_listview);
//        searchEdit=(EditText) view.findViewById(R.id.search_myshortcut);
        manager=new SessionManager();
//        list=new ArrayList<MyShortcut_Data>();
//        adapter=new MyShortcut_adapter(mActivity, list);
        adapter=new MyShortcut_ExpandableList(mActivity,listDataHeader,listDataChild);



        title=(TextView)view.findViewById(R.id.myshortcut_toolbar_title);
        title.setText(manager.getCustomLabel(mActivity,"Shortcuts"));

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.shortcut_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mys_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mys_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mys_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.mys_parent), mainFont);
        }


        lvMyShortcut.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

//        searchEdit.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//
//                Log.e("text1",s.toString());
////                adapter.getFilter().filter(s.toString());
//
//                Log.e("called","call");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                String t=s.toString();
//                Log.e("text_1",t);
//            }
//        });

        GetMyShortcut getMyShortcut=new GetMyShortcut();
        getMyShortcut.execute();


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

    private class GetMyShortcut extends AsyncTask<Void, Void, Void>
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
            int test=1;

            boolean recordFlag=false,caseFlag=false,oppFlag=false,contactFlag=false,reportFlag=false;

            List<MyShortcut_Data> recordField=new ArrayList<>();
            List<MyShortcut_Data> oppField=new ArrayList<>();
            List<MyShortcut_Data> conField=new ArrayList<>();
            List<MyShortcut_Data> caseField=new ArrayList<>();
            List<MyShortcut_Data> reportField=new ArrayList<>();

            while(test<=2)
            {
                SoapObject resultRequestSOAP = null;
                String SOAP_ACTION = null,METHOD_NAME = null,NAMESPACE = null,URL = null;
                //MySearches
                if(test==1)
                {
                    recordFlag=true;caseFlag=true;oppFlag=true;contactFlag=true;

                    resultRequestSOAP = null;
                    SOAP_ACTION = "LMServiceNamespace/MySearches";
                    METHOD_NAME = "MySearches";
                    NAMESPACE = "LMServiceNamespace";
                    URL = manager.getUrl();
                }
                //MyShortcut
                if(test==2)
                {
                    reportFlag=true;

                    resultRequestSOAP = null;
                    SOAP_ACTION = "LMServiceNamespace/MyShortcut";
                    METHOD_NAME = "MyShortcut";
                    NAMESPACE = "LMServiceNamespace";
                    URL = manager.getUrl();
                }

                try
                {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                    Request.addProperty("username", manager.getUserName(getActivity()));
                    Request.addProperty("pwd", manager.getUserPass(getActivity()));
                    Request.addProperty("company_id", manager.getWG(getActivity()));
                    Log.e("1","First");
                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;
                    soapEnvelope.setOutputSoapObject(Request);
                    Log.e("2","Second");
                    HttpTransportSE transport = new HttpTransportSE(URL,30000);

                    transport.call(SOAP_ACTION, soapEnvelope);
                    //list=new ArrayList();
                    Log.e("3","Third");
                    resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                    // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                    for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                    {
                        SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                        String key = TypeEventData.getProperty("Key").toString();
                        Log.e("key",key);
                        if(! key.equals("anyType{}"))
                        {

                            if(key.startsWith("00:") && recordFlag)
                            {
                                listDataHeader.add("Records");
                                recordFlag=false;
                            }
                            else if(key.startsWith("11:") && oppFlag)
                            {
                                listDataHeader.add("Opportunity");
                                oppFlag=false;
                            }
                            else if(key.startsWith("22:") && contactFlag)
                            {
                                listDataHeader.add("Contact");
                                contactFlag=false;
                            }
                            else if(key.startsWith("33:") && caseFlag)
                            {
                                listDataHeader.add("Case");
                                caseFlag=false;
                            }
//                            else if(reportFlag)
//                            {
//                                listDataHeader.add("Reports");
//                                reportFlag=false;
//                            }

                            String value = TypeEventData.getProperty("Value").toString();
                            MyShortcut_Data  callbacktaskData=new MyShortcut_Data();

                            if(key.startsWith("00:"))
                            {
                                callbacktaskData.setValue(value);
                                callbacktaskData.setKey(key);
                                recordField.add(callbacktaskData);
                            }
                            else if(key.startsWith("11:"))
                            {
                                callbacktaskData.setValue(value);
                                callbacktaskData.setKey(key);
                                oppField.add(callbacktaskData);
                            }
                            else if(key.startsWith("22:"))
                            {
                                callbacktaskData.setValue(value);
                                callbacktaskData.setKey(key);
                                conField.add(callbacktaskData);
                            }
                            else if(key.startsWith("33:"))
                            {
                                callbacktaskData.setValue(value);
                                callbacktaskData.setKey(key);
                                caseField.add(callbacktaskData);
                            }
                            else
                            {
                                callbacktaskData.setValue(value);
                                callbacktaskData.setKey(key);

                                if(key.startsWith(manager.getCustomLabel(mActivity,"Record")+" - ") || key.startsWith("Record - "))
                                {
                                    recordField.add(callbacktaskData);
                                    if(recordFlag)
                                    {
                                        listDataHeader.add("Records");
                                        recordFlag=false;
                                    }

                                }
                                else if(key.startsWith(manager.getCustomLabel(mActivity,"Opportunity")+" - ") || key.startsWith("Opportunity - "))
                                {
                                    oppField.add(callbacktaskData);
                                    if(oppFlag)
                                    {
                                        listDataHeader.add("Opportunity");
                                        oppFlag=false;
                                    }

                                }
                                else if(key.startsWith(manager.getCustomLabel(mActivity,"Contact")+" - ") || key.startsWith("Contact - "))
                                {
                                    conField.add(callbacktaskData);
                                    if(contactFlag)
                                    {
                                        listDataHeader.add("Contact");
                                        contactFlag=false;
                                    }

                                }
                                else
                                {
                                    reportField.add(callbacktaskData);
                                    if(reportFlag)
                                    {
                                        listDataHeader.add("Reports");
                                        reportFlag=false;
                                    }
                                }

                            }



//                            list.add(callbacktaskData);
                        }
                        else
                        {
                            Log.e("key","Empty");
                        }

                    }
                    Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

                }
                catch (Exception ex)
                {

                }
                Log.e("Url==>",URL);
                Log.e("1", "Result SECOND API: " + resultRequestSOAP);
//                Log.e("list===============", ""+list.size());



                test++;
            }

            for(int j=0;j<listDataHeader.size();j++)
            {
                if(listDataHeader.get(j).equals("Records"))
                {
                    listDataChild.put(listDataHeader.get(j),recordField);
                }
                if(listDataHeader.get(j).equals("Opportunity"))
                {
                    listDataChild.put(listDataHeader.get(j),oppField);
                }
                if(listDataHeader.get(j).equals("Contact"))
                {
                    listDataChild.put(listDataHeader.get(j),conField);
                }
                if(listDataHeader.get(j).equals("Case"))
                {
                    listDataChild.put(listDataHeader.get(j),caseField);
                }
                if(listDataHeader.get(j).equals("Reports"))
                {
                    listDataChild.put(listDataHeader.get(j),reportField);
                }

            }


            Log.e("ListDataChild",""+listDataChild);
            Log.e("ListDataHeader",""+listDataHeader);
            Log.e("ListDataHeaderSize",""+listDataHeader.size());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            pd.cancel();
            lvMyShortcut.setAdapter(new MyShortcut_ExpandableList(mActivity,listDataHeader,listDataChild));
            if(! (listDataHeader.size()==0))
            {
                lvMyShortcut.expandGroup(0);
            }
        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pd.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        pd.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
//        pd.cancel();
//
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
//                {
//                    // handle back button's click listener
//
//                    Fragment fragment = new Dashboard_Activity();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
    }


}
