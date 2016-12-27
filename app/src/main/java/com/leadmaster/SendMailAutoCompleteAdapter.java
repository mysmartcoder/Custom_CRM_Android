package com.leadmaster;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by User on 06/12/2016.
 */
public class SendMailAutoCompleteAdapter extends ArrayAdapter implements Filterable
{
    private ArrayList items;

    SessionManager manager;

    String type;

    public SendMailAutoCompleteAdapter(Context context, int resource, String type)
    {
        super(context, resource);

        items=new ArrayList<>();
        manager = new SessionManager();
        this.type=type;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public DashAutoCompleteItems getItem(int position)
    {
        return (DashAutoCompleteItems) items.get(position);
    }

    @Override
    public Filter getFilter()
    {
        Filter myFilter=new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults=new FilterResults();

                if(constraint != null)
                {
                    try
                    {
                        //get data from the web
                        String typedWord = constraint.toString();

                        items = new GetItems().execute(typedWord).get();
                        filterResults.values = items;
                        filterResults.count = items.size();
                    }
                    catch (Exception e)
                    {
                        Log.d("FilterErr","EXCEPTION "+e);
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                if(results != null && results.count > 0)
                {
                    Log.e("Check","2");
                    notifyDataSetChanged();
                }
//                else
//                {
//                    Log.e("Check","1");
////                    notifyDataSetInvalidated();
//                }
            }
        };
        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.auto_complete_layout,parent,false);

        //get DashAutoCompleteItems
        DashAutoCompleteItems contry = (DashAutoCompleteItems) items.get(position);

        TextView countryName = (TextView) view.findViewById(R.id.itemName);

        countryName.setText(contry.getName());

        return view;
    }

    //download mCountry list
    private class GetItems extends AsyncTask<String,String,ArrayList>
    {

        @Override
        protected ArrayList doInBackground(String... params)
        {
            Log.e("Type",type);
            ArrayList itemList = new ArrayList<>();

//            if(type.equals("Lead"))
            {
                Log.e("Called","Acc");

                SoapObject resultRequestSOAP = null;
                String SOAP_ACTION = "LMServiceNamespace/SearchEmail";
                String METHOD_NAME = "SearchEmail";
                String NAMESPACE = "LMServiceNamespace";
                String URL = manager.getUrl();
                try
                {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                    Request.addProperty("username", manager.getUserName(getContext()));
                    Request.addProperty("pwd", manager.getUserPass(getContext()));
                    Request.addProperty("company_id", manager.getWG(getContext()));
                    Request.addProperty("email", params[0]);
                    Request.addProperty("searchType", type);


                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;
                    soapEnvelope.setOutputSoapObject(Request);

                    HttpTransportSE transport = new HttpTransportSE(URL,30000);

                    transport.call(SOAP_ACTION, soapEnvelope);

                    resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                    for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                    {
                        SoapObject TypeEventData = (SoapObject) resultRequestSOAP.getProperty(i);
                        String company = TypeEventData.getProperty("Value").toString();

                        DashAutoCompleteItems dashAutoCompleteItems=new DashAutoCompleteItems();
                        dashAutoCompleteItems.setName(company);

                        itemList.add(dashAutoCompleteItems);
                    }

                }
                catch (Exception e)
                {
                    Log.d("HUS", "EXCEPTION " + e);
                    return null;
                }
            }
            return itemList;
        }
    }
}
