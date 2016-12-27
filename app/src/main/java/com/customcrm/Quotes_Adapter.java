package com.customCRM;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 10/08/2016.
 */
public class Quotes_Adapter extends BaseAdapter
{
    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    int i,j;

    public Quotes_Adapter(Context context, ArrayList myList,int i ,int j)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;

        this.i=i;
        this.j=j;

        PrepareDrawer.allSet(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Quotes_Data getItem(int position) {
        return (Quotes_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyViewHolder mViewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.raw_quotes, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.quoteAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.quoteAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.quoteAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.quoteAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_Quotes), iconFont);

        final Quotes_Data currentListData = getItem(position);

        mViewHolder.tvQuoteName.setPaintFlags(mViewHolder.tvQuoteName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvQuoteName.setText(currentListData.getQuoteName());

        mViewHolder.tvCompany.setPaintFlags(mViewHolder.tvCompany.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewHolder.tvCompany.setText(currentListData.getCompany());

        mViewHolder.tvFirst.setText(currentListData.getFirstName());
        mViewHolder.tvLast.setText(currentListData.getLastName());
//        float total= Float.parseFloat(currentListData.getQuoteTotal());
        String total= String.format("%.02f", Float.parseFloat(currentListData.getQuoteTotal()));
//        mViewHolder.tvTotal.setText(manager.getLoginPriv(context,"ISO_Code")+" "+currentListData.getQuoteTotal());
        String sign="";

        if(manager.getLoginPriv(context,"ISO_Code").equals("GBP"))
        {
            sign="£";
        }
        else if(manager.getLoginPriv(context,"ISO_Code").equals("EUR"))
        {
            sign="€";
        }
        else if(manager.getLoginPriv(context,"ISO_Code").equals("JPY"))
        {
            sign="¥";
        }
        else if(manager.getLoginPriv(context,"ISO_Code").equals("CHF"))
        {
            sign="CHF";
        }
        else if(manager.getLoginPriv(context,"ISO_Code").equals("USD"))
        {
            sign="$";
        }
        mViewHolder.tvTotal.setText(sign+""+total);
//        mViewHolder.tvTotal.setText(manager.getLoginPriv(context,"ISO_Code")+" "+total);

        if(manager.getCustomLabel(context,"Quote Name").equals(""))
        {
            mViewHolder.q_tvQuoteName.setVisibility(View.GONE);
            mViewHolder.tvQuoteName.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.q_tvQuoteName.setVisibility(View.VISIBLE);
            mViewHolder.tvQuoteName.setVisibility(View.VISIBLE);
            mViewHolder.q_tvQuoteName.setText(manager.getCustomLabel(context,"Quote Name"));
        }

        if(manager.getCustomLabel(context,"Company").equals(""))
        {
            mViewHolder.q_tvCompany.setVisibility(View.GONE);
            mViewHolder.tvCompany.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.q_tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.tvCompany.setVisibility(View.VISIBLE);
            mViewHolder.q_tvCompany.setText(manager.getCustomLabel(context,"Company"));
        }

        if(manager.getCustomLabel(context,"CONTACT_FIRST_NAME").equals(""))
        {
            mViewHolder.q_tvFirst.setVisibility(View.GONE);
            mViewHolder.tvFirst.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.q_tvFirst.setVisibility(View.VISIBLE);
            mViewHolder.tvFirst.setVisibility(View.VISIBLE);
            mViewHolder.q_tvFirst.setText(manager.getCustomLabel(context,"CONTACT_FIRST_NAME"));
        }

        if(manager.getCustomLabel(context,"CONTACT_LAST_NAME").equals(""))
        {
            mViewHolder.q_tvLast.setVisibility(View.GONE);
            mViewHolder.tvLast.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.q_tvLast.setVisibility(View.VISIBLE);
            mViewHolder.tvLast.setVisibility(View.VISIBLE);
            mViewHolder.q_tvLast.setText(manager.getCustomLabel(context,"CONTACT_LAST_NAME"));
        }

        mViewHolder.select_Quote_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
//                String url =manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=&pagetype=account";
//                manager.getMainUrl()  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_opportunity_EditForm.asp&RECDNO=" + currentListData.getRecord_id() + "&CompanyID=" + manager.getWG(context) + "&oppid="+currentListData.getOpp_id();
                String url = manager.getMainUrl(context)  + "/mobile_auth.asp?key=" + encp + "&topage=mobile_quote_EditForm.asp&RECDNO=" + currentListData.getRecord_id() +"&QuoteID="+currentListData.getQuoteID() + "&CompanyID=" + manager.getWG(context) + "&sid="+manager.getUserId(context)+"&appkeyword=&pagetype=quote";
                EditText searchQuery=(EditText)a.findViewById(R.id.quote_search_edit);
                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","quote");
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
                intent.putExtra("frg","quote");
                intent.putExtra("startIndex",i);
                intent.putExtra("endIndex",j);
                intent.putExtra("searchQuery",searchQuery.getText().toString());
                context.startActivity(intent);



            }
        });

        mViewHolder.delete_Quote_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                String encp=encrptVal();
//                String url = manager.getMainUrl() + "/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + mViewHolder.tvRecord.getText().toString() + "&CompanyID=" + manager.getWG(context) + "&appkeyword=" + "appkeyword" + "&pagetype=account";
//                Log.d("MainURL",url);

                new AlertDialog.Builder(context).setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this quote?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
//                                Toast.makeText(context,"Remaining",Toast.LENGTH_LONG).show();
                                DeleteAccRecord searchTotalCallBackEvent=new DeleteAccRecord(currentListData.getQuoteID());
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



    private class MyViewHolder
    {
        TextView tvQuoteName, tvCompany, tvFirst, tvLast, tvTotal;

        TextView q_tvQuoteName, q_tvCompany, q_tvFirst, q_tvLast, q_tvTotal;

        Button delete_Quote_btn,select_Quote_btn;

        public MyViewHolder(View item)
        {
            tvQuoteName = (TextView) item.findViewById(R.id.Quote_name);
            tvCompany = (TextView) item.findViewById(R.id.Quote_company);
            tvFirst = (TextView) item.findViewById(R.id.Quote_firstname);
            tvLast = (TextView) item.findViewById(R.id.Quote_lastname);
            tvTotal = (TextView)item.findViewById(R.id.Quote_total);

            q_tvQuoteName = (TextView) item.findViewById(R.id.q_name);
            q_tvCompany = (TextView) item.findViewById(R.id.q_company);
            q_tvFirst = (TextView) item.findViewById(R.id.q_first);
            q_tvLast = (TextView) item.findViewById(R.id.q_last);
            q_tvTotal = (TextView)item.findViewById(R.id.q_total);

            delete_Quote_btn=(Button)item.findViewById(R.id.delete_btn_Quote);
            select_Quote_btn=(Button)item.findViewById(R.id.select_btn_Quote);
        }
    }


    private class DeleteAccRecord extends AsyncTask<Void, Void, Void>
    {

        String recordId;
        public DeleteAccRecord(String recordId)
        {
            this.recordId=recordId;
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
            String SOAP_ACTION = "LMServiceNamespace/DeleteQuotes";
            String METHOD_NAME = "DeleteQuotes";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("QuoteIDs", recordId);
                Request.addProperty("LogonID", manager.getUserId(context));
                Request.addProperty("CompanyID", manager.getWG(context));


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;

                checkDeletion=resultRequestSOAP.getProperty("DeleteQuotesResult").toString();

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
                Toast.makeText(context,"Quote delete successfully",Toast.LENGTH_LONG).show();

                Fragment accountFragment = new QuotesActivity();

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

}
