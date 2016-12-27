package com.leadmaster;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class QuickSearch_Activity extends Fragment
{
    ImageButton searchButton;
    EditText quickSearchEdittext;
    ListView quick_search_listView;

    QuickSearch_adapter adapter=null;

    ArrayList list;
    ProgressDialog pd;
    private Activity mActivity;
    SessionManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_quick_search, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        manager=new SessionManager();
        list=new ArrayList();

        adapter=new QuickSearch_adapter(mActivity, list);

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.qs_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.qs_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.qs_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.qs_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.qs_parent), mainFont);
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("keyCode: ", ""+keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK )
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Log.i("KEYCodeBack", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Fragment fragment = new Serches_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,fragment,null);
                    fragmentTransaction.commit();

                    fragmentManager.executePendingTransactions();

                    return true;
                } else {
                    return false;
                }
            }
        });

        searchButton=(ImageButton)view.findViewById(R.id.search_quick_btn);
        quick_search_listView=(ListView)view.findViewById(R.id.quick_search_listview);
        quickSearchEdittext=(EditText)view.findViewById(R.id.quick_search_edit);

        quick_search_listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.e("clicked","click");
                final QuickSearch_Data currentListData = adapter.getItem(position);
                String encp=encrptVal();
                String url = manager.getMainUrl(getActivity())+"/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRecordId();

                Log.d("MainURL",url);

                Intent intent = new Intent(getActivity(),CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","quickSearch");

                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String keywordSearch = quickSearchEdittext.getText().toString();

                if(keywordSearch.isEmpty() || keywordSearch.equals("null") || keywordSearch == null)
                {
                    Toast.makeText(mActivity,"Please enter keyword in textbox",Toast.LENGTH_LONG).show();
                }
                else
                {
                    list.clear();
                    GetQuickSearch getQuickSearch=new GetQuickSearch(keywordSearch);
                    getQuickSearch.execute();
                }
            }
        });


        return view;
    }

    private class GetQuickSearch extends AsyncTask<Void, Void, Void>
    {
        String keyword;
        public GetQuickSearch(String keywordSearch)
        {
            keyword=keywordSearch;
        }

        @Override
        protected void onPreExecute() {
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
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SearchLeadData";
            String METHOD_NAME = "SearchLeadData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getActivity()));
                Request.addProperty("pwd", manager.getUserPass(getActivity()));
                Request.addProperty("company_id", manager.getWG(getActivity()));
                Request.addProperty("keyword", keyword);

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
                    String recordId = TypeEventData.getProperty("RecordID").toString();
                    String firstName = TypeEventData.getProperty("FirstName").toString();
                    String lastName = TypeEventData.getProperty("LastName").toString();
                    String company = TypeEventData.getProperty("Company").toString();
                    String oppName = TypeEventData.getProperty("OppName").toString();

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
                    if(company.equals("anyType{}"))
                    {
                        company="none";
                    }else
                    {
                        company=company+"";
                    }
                    if(oppName.equals("anyType{}"))
                    {
                        oppName="none";
                    }else
                    {
                        oppName=oppName+"";
                    }



                    QuickSearch_Data  callbacktaskData=new QuickSearch_Data();

                    callbacktaskData.setRecordId(recordId);
                    callbacktaskData.setFirstname(firstName);
                    callbacktaskData.setLastname(lastName);
                    callbacktaskData.setCompany(company);
                    callbacktaskData.setOppName(oppName);

                    list.add(callbacktaskData);

                    Log.e("recordid",recordId);
                }


                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
            } catch (Exception ex) {
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
            pd.cancel();
            Log.e("postQuick","quick");
            quick_search_listView.setAdapter(new QuickSearch_adapter(mActivity, list));

        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
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

