package com.customCRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by User on 20/07/2016.
 */
public class QuickSearch_adapter extends BaseAdapter
{
    SessionManager manager=new SessionManager();
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;

    FragmentActivity a;
    ProgressDialog pd;

    public QuickSearch_adapter(Context context, ArrayList myList)
    {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        a= (FragmentActivity) context;
    }
    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public QuickSearch_Data getItem(int position) {
        return (QuickSearch_Data) myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_quick_search, parent, false);
            mViewHolder = new MyViewHolder(convertView);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

//        convertView.setFocusableInTouchMode(true);
//        convertView.requestFocus();
//        convertView.setOnKeyListener(new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                Log.i("keyCode: ", ""+keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    Log.i("KEYCodeBack", "onKey Back listener is working!!!");
//                    a.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                    Fragment fragment = new Serches_Activity();
//                    FragmentManager fragmentManager = a.getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.containerView,fragment,null);
//                    fragmentTransaction.commit();
//
//                    fragmentManager.executePendingTransactions();
//
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        if(manager.getFontStyle(context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.qsAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.qsAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.qsAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.qsAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_container_QuickSearch), iconFont);

        final QuickSearch_Data currentListData = getItem(position);
        Log.e("adapterPos",""+position);
        mViewHolder.tv_CompanyName.setText(currentListData.getCompany());
        mViewHolder.tv_FirstName.setText(currentListData.getFirstname()+" "+currentListData.getLastname());
//        mViewHolder.tv_OppName.setText(currentListData.getOppName());

        if(manager.getCustomLabel(context,"Opportunity").equals(""))
        {
            mViewHolder.tv_OppName.setVisibility(View.GONE);
            mViewHolder.tv_OppNameDisplay.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.tv_OppName.setVisibility(View.VISIBLE);
            mViewHolder.tv_OppNameDisplay.setVisibility(View.VISIBLE);

            mViewHolder.tv_OppName.setText(currentListData.getOppName());
            mViewHolder.tv_OppNameDisplay.setText(manager.getCustomLabel(context,"Opportunity")+" Name : ");
        }


        mViewHolder.select_QuickSearch_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
                String url = manager.getMainUrl(context)+"/mobile_auth.asp?key=" + encp + "&topage=mobile_RFullEdit.asp&RECDNO=" + currentListData.getRecordId();

                Log.d("MainURL",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","quickSearch");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","quickSearch");

                context.startActivity(intent);

            }
        });

        return convertView;
    }

    private class MyViewHolder
    {
        TextView tv_CompanyName,tv_FirstName,tv_OppName,tv_OppNameDisplay;

        Button select_QuickSearch_btn;
        public MyViewHolder(View item)
        {
            tv_CompanyName = (TextView) item.findViewById(R.id.companyName_quickSearch);
            tv_FirstName = (TextView) item.findViewById(R.id.firstNlastName_quickSearch);
            tv_OppName=(TextView) item.findViewById(R.id.oppName_quickSearch);
            tv_OppNameDisplay=(TextView) item.findViewById(R.id.OpportunityName_QuickSearch);

            select_QuickSearch_btn=(Button)item.findViewById(R.id.select_quicksearch);

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
