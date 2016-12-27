package com.leadmaster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 05/07/2016.
 */
public class CallbackTask_adapter extends BaseAdapter
{
    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    int selectedStatus;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    int i,j;

    public CallbackTask_adapter(Context context, ArrayList myList,int i, int j)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;

        this.i=i;
        this.j=j;
    }

    @Override
    public int getCount()
    {
        return myList.size();
    }

    @Override
    public Callbacktask_Data getItem(int position)
    {
        return (Callbacktask_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_callbacktask, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.callAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.callAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.callAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.callAdp_parent), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_callback), iconFont);

        final Callbacktask_Data currentListData = getItem(position);

        mViewHolder.tvCompany.setPaintFlags(mViewHolder.tvCompany.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvCompany.setText(currentListData.getCompanyName());

        mViewHolder.tvEvent.setPaintFlags(mViewHolder.tvEvent.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvEvent.setText(currentListData.getEventName());

        mViewHolder.tvStartTime.setText(currentListData.getStartTime());
        mViewHolder.tvEndTime.setText(currentListData.getEndTime());
//        mViewHolder.tvEventStatus.setText(currentListData.getEventStatus());
        mViewHolder.tvEventStatus.setSelection(Integer.parseInt(currentListData.getEventStatus()));

        selectedStatus= Integer.parseInt(currentListData.getEventStatus());

        if(manager.getCustomLabel(context,"Company").equals(""))
        {
            mViewHolder.cb_tvCompany.setVisibility(View.GONE);
            mViewHolder.tvCompany.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.cb_tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.cb_tvCompany.setText(manager.getCustomLabel(context,"Company"));
        }

        if(manager.getCustomLabel(context,"Event Name").equals(""))
        {
            mViewHolder.cb_tvEvent.setVisibility(View.GONE);
            mViewHolder.tvEvent.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.cb_tvEvent.setVisibility(View.VISIBLE);
            mViewHolder.tvEvent.setVisibility(View.VISIBLE);
            mViewHolder.cb_tvEvent.setText(manager.getCustomLabel(context,"Event Name"));
        }

        if(manager.getCustomLabel(context,"Event Start Time").equals(""))
        {
            mViewHolder.cb_tvStartTime.setVisibility(View.GONE);
            mViewHolder.tvStartTime.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.cb_tvStartTime.setVisibility(View.VISIBLE);
            mViewHolder.tvStartTime.setVisibility(View.VISIBLE);
            mViewHolder.cb_tvStartTime.setText(manager.getCustomLabel(context,"Event Start Time"));
        }

        if(manager.getCustomLabel(context,"Event Done").equals(""))
        {
            mViewHolder.cb_tvEventStatus.setVisibility(View.GONE);
            mViewHolder.tvEventStatus.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.cb_tvEventStatus.setVisibility(View.VISIBLE);
            mViewHolder.tvEventStatus.setVisibility(View.VISIBLE);
            mViewHolder.cb_tvEventStatus.setText(manager.getCustomLabel(context,"Event Done"));
        }

//        mViewHolder.tvEventStatus.getSelectedItem();

        mViewHolder.tvEventStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.e("SelectedEventStatus",""+position);
                if(Integer.parseInt(currentListData.getEventStatus())!=position)
                {
                    Log.e("Called","SelectedEventStatus");
                    ChangeEventStatus changeEventStatus=new ChangeEventStatus(currentListData.getCallback_Id(),position);
                    changeEventStatus.execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mViewHolder.select_Callback_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
//                 manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getCallback_Id() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + currentListData.getRcd_Id() + "&CompanyID=" + manager.getWG(context) + "&CallBackID=" + currentListData.getCallback_Id() + "&appkeyword=  &pagetype= callback";
                EditText searchQuery=(EditText)a.findViewById(R.id.callback_search_edit);
                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","callback");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchQuery.getText().toString());

                Log.e("i,j,search",i+","+j+","+searchQuery.getText().toString());


                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","callback");
                intent.putExtra("startIndex",i);
                intent.putExtra("endIndex",j);
                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.add_Callback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbEditEvent.asp&add_CallBack=T" + "&appkeyword=&pagetype=callback";

                EditText searchQuery=(EditText)a.findViewById(R.id.callback_search_edit);

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","callback");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchQuery.getText().toString());

                Log.e("i,j,search",i+","+j+","+searchQuery.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","callback");
                intent.putExtra("startIndex",i);
                intent.putExtra("endIndex",j);
                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.edit_Callback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp=encrptVal();

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbEditEvent.asp&frompage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRcd_Id() + "&CompanyID=7639" + manager.getWG(context) + "&CallBackId=" + currentListData.getCallback_Id() + "&appkeyword=&pagetype=callback";

                EditText searchQuery=(EditText)a.findViewById(R.id.callback_search_edit);
                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","callback");
                bundle.putInt("startIndex",i);
                bundle.putInt("endIndex",j);
                bundle.putString("searchQuery",searchQuery.getText().toString());

                Log.e("i,j,search",i+","+j+","+searchQuery.getText().toString());

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","callback");
                intent.putExtra("startIndex",i);
                intent.putExtra("endIndex",j);
                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);

            }
        });

        mViewHolder.calendar_Callback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp=encrptVal();

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_calWeek.asp";

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","callback");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","callback");

                context.startActivity(intent);

            }
        });

        mViewHolder.delete_Callback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this callback?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
//                                Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                                DeleteCallbackRecord searchTotalCallBackEvent=new DeleteCallbackRecord(currentListData.getCallback_Id());
                                searchTotalCallBackEvent.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
//                                Toast.makeText(context,"Canceled",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setIcon(R.drawable.delete_alert)
                        .show();
            }
        });

        return convertView;
    }

    private class MyViewHolder
    {
        TextView tvCompany, tvEvent, tvStartTime, tvEndTime;

        TextView cb_tvCompany, cb_tvEvent, cb_tvStartTime, cb_tvEndTime,cb_tvEventStatus;

        Spinner tvEventStatus;

        Button add_Callback_btn,delete_Callback_btn,calendar_Callback_btn,edit_Callback_btn,select_Callback_btn;

        public MyViewHolder(View item)
        {
            tvCompany = (TextView) item.findViewById(R.id.Callback_companyName);
            tvEvent = (TextView) item.findViewById(R.id.Callback_eventName);
            tvStartTime = (TextView) item.findViewById(R.id.Callback_startTime);
            tvEndTime=(TextView) item.findViewById(R.id.Callback_endTime);
            tvEventStatus=(Spinner)item.findViewById(R.id.Callback_eventStatus);

            cb_tvCompany = (TextView) item.findViewById(R.id.cb_tvacc);
            cb_tvEvent = (TextView) item.findViewById(R.id.cb_tvname);
            cb_tvStartTime = (TextView) item.findViewById(R.id.cb_tvstarttime);
            cb_tvEndTime=(TextView) item.findViewById(R.id.cb_tvendtime);
            cb_tvEventStatus=(TextView)item.findViewById(R.id.cb_tvstatus);

            select_Callback_btn=(Button)item.findViewById(R.id.select_btn_Callback);
            add_Callback_btn=(Button)item.findViewById(R.id.add_btn_Callback);
            edit_Callback_btn=(Button)item.findViewById(R.id.edit_btn_Callback);
            calendar_Callback_btn=(Button)item.findViewById(R.id.calendar_btn_Callback);
            delete_Callback_btn=(Button)item.findViewById(R.id.delete_btn_Callback);
        }
    }

    private class DeleteCallbackRecord extends AsyncTask<Void, Void, Void>
    {

        String callbackId;
        public DeleteCallbackRecord(String callbackId)
        {
            this.callbackId=callbackId;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Accounts_Activity.this);
            pd=new ProgressDialog(context);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/DeleteCallBacks";
            String METHOD_NAME = "DeleteCallBacks";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CallBackIDs", callbackId);
                Request.addProperty("LogonID", manager.getUserId(context));
                Request.addProperty("CompanyID", manager.getWG(context));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteCallBacksResult").toString();

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
            if(checkDeletion.equals("SUCCESS"))
            {
                Toast.makeText(context,"Callback Task delete successfully",Toast.LENGTH_LONG).show();

                Fragment accountFragment = new CallbackTask_Activity();

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,accountFragment,null);
                fragmentTransaction.commit();

            }
            else
            {
                Toast.makeText(context,"Operation Failed",Toast.LENGTH_LONG).show();
            }

        }
    }






    private class ChangeEventStatus extends AsyncTask<Void, Void, Void>
    {

        String callbackId;
        int position;

        public ChangeEventStatus(String callbackId, int position)
        {
            this.callbackId=callbackId;
            this.position=position;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            Log.d("TotalData","TotalData");
//            pd=new ProgressDialog(Accounts_Activity.this);
            pd=new ProgressDialog(context);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/LMUpdateCallBackEvents";
            String METHOD_NAME = "LMUpdateCallBackEvents";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(context));
                Request.addProperty("pwd", manager.getUserPass(context));
                Request.addProperty("company_id", manager.getWG(context));
                Request.addProperty("isdone",position);
                Request.addProperty("callbackid", callbackId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("LMUpdateCallBackEventsResult").toString();

                Log.e("updatedStatus", checkDeletion);

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
//            if(selectedStatus != Integer.parseInt(checkDeletion))
//            {
                Toast.makeText(context,"Event status change successfully",Toast.LENGTH_LONG).show();

                Fragment accountFragment = new CallbackTask_Activity();

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,accountFragment,null);
                fragmentTransaction.commit();

//            }
//            else
//            {
//                Toast.makeText(context,"Operation Failed",Toast.LENGTH_LONG).show();
//            }

        }
    }

    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(context);
        String pass=manager.getUserPass(context);
        String dbId=manager.getDB(context);
        String wgId=manager.getWG(context);


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
