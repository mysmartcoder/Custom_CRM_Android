package com.customCRM;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Download_Library_Activity extends Fragment
{

    ListView lvDownloadLib;
    ArrayList list;
    ProgressDialog pd;

    SessionManager manager;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_download__library, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();
        list=new ArrayList();
        lvDownloadLib=(ListView)view.findViewById(R.id.download_lib_listview);

        LinearLayout toolbar = (LinearLayout)view.findViewById(R.id.lib_toolbar);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.lib_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.lib_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.lib_parent), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.lib_parent), mainFont);
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("keyCode: ", ""+keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Log.i("KEYCodeBack", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Fragment fragment = new Library_Activity();
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

        GetDownloadLib getDownloadLib=new GetDownloadLib();
        getDownloadLib.execute();

        return view;
    }

    private class GetDownloadLib extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
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
            String SOAP_ACTION = "LMServiceNamespace/GetLibraryDownloadQueueFileList";
            String METHOD_NAME = "GetLibraryDownloadQueueFileList";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("logon_id", manager.getUserId(getActivity()));
                Request.addProperty("companyid", manager.getWG(getActivity()));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String fileId = TypeEventData.getProperty("FileID").toString();
                    String fileName = TypeEventData.getProperty("FileName").toString();
                    String filePath = TypeEventData.getProperty("FilePath").toString();

                    Download_Library_Data  callbacktaskData=new Download_Library_Data();

                    if(fileId.equals("anyType{}"))
                    {
                        fileId="none";
                    }else
                    {
                        fileId=fileId+"";
                    }

                    if(fileName.equals("anyType{}"))
                    {
                        fileName="none";
                    }else
                    {
                        fileName=fileName+"";
                    }

                    if(filePath.equals("anyType{}"))
                    {
                        filePath="none";
                    }else
                    {
                        filePath=filePath+"";
                    }


                    callbacktaskData.setFileId(fileId);
                    callbacktaskData.setFileName(fileName);
                    callbacktaskData.setFilePath(filePath);

                    list.add(callbacktaskData);

                    Log.e("recordid",fileId);


                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {

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

            lvDownloadLib.setAdapter(new Download_Library_Adapter(mActivity, list));
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

}
