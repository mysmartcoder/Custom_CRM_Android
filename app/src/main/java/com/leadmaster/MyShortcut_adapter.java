package com.leadmaster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 26/07/2016.
 */
public class MyShortcut_adapter extends BaseAdapter implements Filterable
{
    SessionManager manager=new SessionManager();
    ArrayList<MyShortcut_Data> myList = new ArrayList<MyShortcut_Data>();
    ArrayList<MyShortcut_Data> myList2 = new ArrayList<MyShortcut_Data>();
    ArrayList<MyShortcut_Data> original = new ArrayList<MyShortcut_Data>();
    LayoutInflater inflater;
    Context context;
    private ValueFilter valueFilter;


    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    boolean recordflag = true,oppflag = true,contactflag = true,caseflag = true;

    public MyShortcut_adapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.myList2=myList;
        original=myList;
        this.context = context;
        a= (FragmentActivity) context;
        this.inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MyShortcut_Data getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MyViewHolder mViewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.raw_myshortcut, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_MyShortcut), iconFont);

        final MyShortcut_Data currentListData = getItem(position);
        Log.e("adapterPos",""+position);


        if(currentListData.getLabel().equals(""))
        {
            Log.e("set","1");
            mViewHolder.tvHeader.setVisibility(View.GONE);
            mViewHolder.tvValue.setVisibility(View.VISIBLE);
//            mViewHolder.mapBtn.setVisibility(View.VISIBLE);
            mViewHolder.tvValue.setText(currentListData.getValue());

            if(currentListData.getKey().startsWith("00:") || currentListData.getKey().startsWith("11:") ||
                    currentListData.getKey().startsWith("22:") || currentListData.getKey().startsWith("33:"))
            {
                mViewHolder.mapBtn.setVisibility(View.VISIBLE);
            }
            else
            {
                mViewHolder.mapBtn.setVisibility(View.GONE);
            }
        }
        else if(currentListData.getKey().equals("") || currentListData.getValue().equals(""))
        {
            Log.e("set","2"+currentListData.getLabel()+","+currentListData.getLabel().toString());
            mViewHolder.tvValue.setVisibility(View.GONE);
            mViewHolder.mapBtn.setVisibility(View.GONE);
            mViewHolder.tvHeader.setVisibility(View.VISIBLE);
            mViewHolder.tvHeader.setText(currentListData.getLabel()+" : ");
        }




        mViewHolder.mapBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(a,currentListData.getKey()+","+currentListData.getValue(),Toast.LENGTH_LONG).show();
                String encp=encrptVal();
                String pagetype = null;
                String searchID=null;

                if(currentListData.getKey().startsWith("00:"))
                {
                    Log.e("pagetype","record");
                    pagetype = "record";
                    searchID= currentListData.getKey().substring(currentListData.getKey().lastIndexOf(":") + 1);
                }
                else if(currentListData.getKey().startsWith("11:"))
                {
                    Log.e("pagetype","opp");
                    pagetype = "opp";
                    searchID= currentListData.getKey().substring(currentListData.getKey().lastIndexOf(":") + 1);
                }
                else if(currentListData.getKey().startsWith("22:"))
                {
                    Log.e("pagetype","contact");
                    pagetype = "contact";
                    searchID= currentListData.getKey().substring(currentListData.getKey().lastIndexOf(":") + 1);
                }
                else if(currentListData.getKey().startsWith("33:"))
                {
                    Log.e("pagetype","case");
                    pagetype = "case";
                    searchID= currentListData.getKey().substring(currentListData.getKey().lastIndexOf(":") + 1);
                }

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_searchmap.asp&searchid="+ searchID + "&pagetype=" + pagetype+"&LogonID="+manager.getUserId(context);
                Log.e("url",url);

                /*Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","mySearch");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","mySearch");

                context.startActivity(intent);

            }
        });


        mViewHolder.tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context,currentListData.getKey(),Toast.LENGTH_LONG).show();
                String encp=encrptVal();

                if(currentListData.getKey().startsWith("00:"))
                {
//                    String key=currentListData.getKey();
//                    key = key.replace("?", "&");

                    String url=manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engine.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);

                    Log.d("MainURL",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    context.startActivity(intent);


                }
                else if(currentListData.getKey().startsWith("11:"))
                {
//                    String key=currentListData.getKey();
//                    key = key.replace("?", "&");

                    String url=manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engineopp.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);

                    Log.d("MainURL",url);

                    /*Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();
*/
                    Intent intent = new Intent(context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    context.startActivity(intent);

                }
                else if(currentListData.getKey().startsWith("22:"))
                {
//                    String key=currentListData.getKey();
//                    key = key.replace("?", "&");

                    String url=manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Contact.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);

                    Log.d("MainURL",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    context.startActivity(intent);


                }
                else if(currentListData.getKey().startsWith("33:"))
                {
//                    String key=currentListData.getKey();
//                    key = key.replace("?", "&");

                    String url=manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=search_EngineCase.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);

                    Log.d("MainURL",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    context.startActivity(intent);


                }
                else
                {
                    String key=currentListData.getKey();

                    key = key.replace("?", "&");

                    String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engine.asp&savedsearch=T&mobile_search=T&SearchID=" + key;

                    Log.d("MainURL",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    context.startActivity(intent);

                }

            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter
    {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            Log.e("text2", constraint.toString());

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<MyShortcut_Data> filterList = new ArrayList<MyShortcut_Data>();

                for (int i = 0; i < myList2.size(); i++)
                {
                    if ((myList2.get(i).getValue().toUpperCase()).contains(constraint.toString().toUpperCase()))
                    {
                        MyShortcut_Data contacts = new MyShortcut_Data();
                        contacts.setValue(myList2.get(i).getValue());
                        contacts.setKey(myList2.get(i).getKey());
                        contacts.setLabel(myList2.get(i).getLabel());
                        filterList.add(contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                Log.e("original", "" + original);
                results.count = original.size();
                results.values = original;

            }
            Log.e("text3", "" + myList2);
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            myList.clear();
            myList.addAll((ArrayList<MyShortcut_Data>) results.values);
            Log.e("text4",""+myList.size());
            Log.e("myList2",""+myList2);
            if(myList.size()==0)
            {
                Log.e("change","change");
            }

            notifyDataSetChanged();
        }
    }


    private class MyViewHolder
    {
        TextView tvValue,tvHeader;
        Button mapBtn;

        public MyViewHolder(View item)
        {
            tvValue = (TextView) item.findViewById(R.id.myshortcut_link);
            tvHeader = (TextView) item.findViewById(R.id.myshortcut_header);

            mapBtn=(Button)item.findViewById(R.id.map_btn_MyShortcut);
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
