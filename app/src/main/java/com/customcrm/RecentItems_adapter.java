package com.customCRM;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 22/07/2016.
 */
public class RecentItems_adapter extends BaseAdapter
{

    SessionManager manager=new SessionManager();
    ArrayList<RecentItems_Data> myList = new ArrayList<RecentItems_Data>();
    ArrayList<RecentItems_Data> myList2 = new ArrayList<RecentItems_Data>();
    ArrayList<RecentItems_Data> original = new ArrayList<RecentItems_Data>();
    LayoutInflater inflater;
    Context context;
//    private ValueFilter valueFilter;


    FragmentActivity a;

    ProgressDialog pd;
    String checkDeletion;

    public RecentItems_adapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.myList2=myList;
        original=myList;
        this.context = context;
//        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;
        this.inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        getFilter();
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public RecentItems_Data getItem(int position) {
        return  myList.get(position);
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
            convertView = inflater.inflate(R.layout.raw_recentitems, parent, false);
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
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.riAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.riAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.riAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.riAdp_parent), mainFont);
        }

        final RecentItems_Data currentListData = getItem(position);
        Log.e("adapterPos",""+position);


//        var url = url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=" + key;

//        String text = "<a href='"+ url +"'>"+ currentListData.getValue() +"</a>";

        mViewHolder.tvValue.setText(currentListData.getValue());

        mViewHolder.tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
                String key=currentListData.getKey();

                if ( key.toLowerCase().indexOf("mobile_") == -1 )
                {
                    key="mobile_"+key;
                }

                key = key.replace("?", "&");
                key=key + "&recentitem=T";

                String url = manager.getMainUrl(context) + "/mobile_auth.asp?key=" + encp + "&topage=" + key;

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","recentItem");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","recentItem");

                context.startActivity(intent);

            }
        });

//        mViewHolder.tvValue.setText(currentListData.getValue());

        return convertView;
    }

//    @Override
//    public Filter getFilter()
//    {
//        if(valueFilter==null) {
//
//            valueFilter=new ValueFilter();
//        }
//
//        return valueFilter;
//
//    }

//    private class ValueFilter extends Filter
//    {
//
//        //Invoked in a worker thread to filter the data according to the constraint.
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint)
//        {
//            Log.e("text2",constraint.toString());
//            FilterResults results=new FilterResults();
//            if(constraint!=null && constraint.length()>0 )
//            {
//                ArrayList<RecentItems_Data> filterList=new ArrayList<RecentItems_Data>();
//                for(int i=0;i<myList2.size();i++)
//                {
//                    if((myList2.get(i).getValue().toUpperCase()).contains(constraint.toString().toUpperCase()))
//                    {
//                        RecentItems_Data contacts = new RecentItems_Data();
//                        contacts.setValue(myList2.get(i).getValue());
//                        contacts.setKey(myList2.get(i).getKey());
//                        filterList.add(contacts);
//                    }
//                }
//                results.count=filterList.size();
//                results.values=filterList;
//            }
//            else
//            {
//                Log.e("original",""+original);
//                results.count=original.size();
//                results.values=original;
//
//            }
//            Log.e("text3",""+myList2);
//            return results;
//        }

//
//        //Invoked in the UI thread to publish the filtering results in the user interface.
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results)
//        {
//            myList.clear();
//            myList.addAll((ArrayList<RecentItems_Data>) results.values);
//            Log.e("text4",""+myList.size());
//            Log.e("myList2",""+myList2);
//            if(myList.size()==0)
//            {
//                Log.e("change","change");
//            }
////            RecentItems_adapter adapter=new RecentItems_adapter(context,myList);
//
//            notifyDataSetChanged();
//        }
//    }

    private class MyViewHolder
    {
        TextView tvValue;

        public MyViewHolder(View item)
        {
            tvValue = (TextView) item.findViewById(R.id.recent_item_link);
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