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
 * Created by User on 08/11/2016.
 */
public class AutoCompleteAdapter extends ArrayAdapter implements Filterable
{
    private ArrayList mCountry;

    SessionManager manager;

    public AutoCompleteAdapter(Context context, int resource)
    {
        super(context, resource);
        mCountry = new ArrayList<>();
        manager= new SessionManager();
    }

    @Override
    public int getCount()
    {
        Log.e("Check","4");
        Log.e("ErrorSize2",""+mCountry.size());
        return mCountry.size();
    }

    @Override
    public AutoCompleteCompany_Data getItem(int position)
    {
        Log.e("Check","5");
        return (AutoCompleteCompany_Data) mCountry.get(position);
    }

    @Override
    public Filter getFilter()
    {
        Filter myFilter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null)
                {
                    try
                    {
                        Log.e("Check","3");
                        //get data from the web
                        String term = constraint.toString();
                        mCountry = new DownloadCompany().execute(term).get();
                        filterResults.values = mCountry;
                        filterResults.count = mCountry.size();
                    }
                    catch (Exception e)
                    {
                        Log.d("HUS","EXCEPTION "+e);
                    }
//                    filterResults.values = mCountry;
//                    filterResults.count = mCountry.size();
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
                else
                {
                    Log.e("Check","1");
//                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.auto_complete_layout,parent,false);

        //get AutoCompleteCompany_Data
        AutoCompleteCompany_Data contry = (AutoCompleteCompany_Data) mCountry.get(position);

        TextView countryName = (TextView) view.findViewById(R.id.itemName);

        countryName.setText(contry.getName());

        return view;
    }

    //download mCountry list
    private class DownloadCompany extends AsyncTask<String,String,ArrayList>
    {

        @Override
        protected ArrayList doInBackground(String... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SearchAccountData";
            String METHOD_NAME = "SearchAccountData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();
            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(getContext()));
                Request.addProperty("pwd", manager.getUserPass(getContext()));
                Request.addProperty("company_id", manager.getWG(getContext()));
                Request.addProperty("startindex", 1);
                Request.addProperty("endindex", 30);
                Request.addProperty("company", params[0]);


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                ArrayList countryList = new ArrayList<>();

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP.getProperty(i);
                    String company = TypeEventData.getProperty("Company").toString();
                    String recordID = TypeEventData.getProperty("RecordID").toString();

                    AutoCompleteCompany_Data country=new AutoCompleteCompany_Data();
                    country.setName(company);
                    country.setRecordId(recordID);

                    countryList.add(country);
                }
                return countryList;
            }
            catch (Exception e)
            {
                Log.d("HUS", "EXCEPTION " + e);
                return null;
            }
        }
    }
}