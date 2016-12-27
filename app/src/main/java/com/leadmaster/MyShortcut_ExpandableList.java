package com.leadmaster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 05/08/2016.
 */
public class MyShortcut_ExpandableList extends BaseExpandableListAdapter
{
    SessionManager manager =new SessionManager();;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MyShortcut_Data>> _listDataChild;
    FragmentActivity a;
//    Context context;
//    private final LayoutInflater inf;

    public MyShortcut_ExpandableList(Context context, List<String> listDataHeader,
                                 HashMap<String, List<MyShortcut_Data>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        a= (FragmentActivity) context;
//        inf = LayoutInflater.from(a);
    }

    @Override
    public MyShortcut_Data getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        final MyShortcut_Data currentListData =  getChild(groupPosition, childPosition);
//        Log.e("adapterPos",""+position);
//        mViewHolder.tvEntered.setText(currentListData.getEntered());

//        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.myshortcut_listitem, null);
        }

        if(manager.getFontStyle(_context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdp_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdp_parent), mainFont);
        }


        Typeface iconFont = FontManager.getTypeface(_context.getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(convertView.findViewById(R.id.icons_child_MyShortcut), iconFont);

        TextView txtListChild = (TextView) convertView.findViewById(R.id.myshortcut_child_link);
        Button mapBtn = (Button) convertView.findViewById(R.id.map_childbtn_MyShortcut);

        if(currentListData.getKey().startsWith("00:"))
        {
            txtListChild.setText(currentListData.getValue());
            mapBtn.setVisibility(View.VISIBLE);
        }
        else if(currentListData.getKey().startsWith("11:"))
        {
            txtListChild.setText(currentListData.getValue());
            mapBtn.setVisibility(View.VISIBLE);
        }
        else if(currentListData.getKey().startsWith("22:"))
        {
            txtListChild.setText(currentListData.getValue());
            mapBtn.setVisibility(View.VISIBLE);
        }
        else if(currentListData.getKey().startsWith("33:"))
        {
            txtListChild.setText(currentListData.getValue());
            mapBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.e("Reports",currentListData.getKey());
            txtListChild.setText(currentListData.getKey());
            mapBtn.setVisibility(View.GONE);
        }

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                String url = manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_searchmap.asp&searchid="+ searchID + "&pagetype=" + pagetype+"&LogonID="+manager.getUserId(_context);
                Log.e("url",url);

              /*  Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","mySearch");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(_context,CommonWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("frg","mySearch");

                _context.startActivity(intent);
            }
        });

        txtListChild.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
                if(currentListData.getKey().startsWith("00:"))
                {

                    Log.e("SearchID",currentListData.getKey().substring(3));
                    Fragment quotesFragment = new MS_Records_Activity();
                    Bundle bundle = new Bundle();

                    bundle.putString("titlebar",currentListData.getValue());
                    bundle.putString("searchID",currentListData.getKey().substring(3));

                    quotesFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,quotesFragment,null);
                    fragmentTransaction.commit();


//                    String url=manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engine.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);
//
//                    Log.d("MainURL",url);
//
//                    Intent intent = new Intent(_context,CommonWebView.class);
//                    intent.putExtra("url",url);
//                    intent.putExtra("frg","mySearch");
//
//                    _context.startActivity(intent);
                }
                else if(currentListData.getKey().startsWith("11:"))
                {

                    Log.e("SearchID",currentListData.getKey().substring(3));
                    Fragment quotesFragment = new MS_Opp_Activity();
                    Bundle bundle = new Bundle();

                    bundle.putString("titlebar",currentListData.getValue());
                    bundle.putString("searchID",currentListData.getKey().substring(3));

                    quotesFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,quotesFragment,null);
                    fragmentTransaction.commit();


//                    String url=manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engineopp.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);
//
//                    Log.d("MainURL",url);
//
//                    Intent intent = new Intent(_context,CommonWebView.class);
//                    intent.putExtra("url",url);
//                    intent.putExtra("frg","mySearch");
//
//                    _context.startActivity(intent);

                  /*  Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                }
                else if(currentListData.getKey().startsWith("22:"))
                {
                    Log.e("SearchID",currentListData.getKey().substring(3));

                    Fragment quotesFragment = new MS_Contact_Activity();
                    Bundle bundle = new Bundle();

                    bundle.putString("titlebar",currentListData.getValue());
                    bundle.putString("searchID",currentListData.getKey().substring(3));

                    quotesFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,quotesFragment,null);
                    fragmentTransaction.commit();

//                    String url=manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Contact.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);
//
//                    Log.d("MainURL",url);
//
//                    Intent intent = new Intent(_context,CommonWebView.class);
//                    intent.putExtra("url",url);
//                    intent.putExtra("frg","mySearch");
//
//                    _context.startActivity(intent);
//

                    /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();
*/
                }
                else if(currentListData.getKey().startsWith("33:"))
                {
                    Log.e("SearchID",currentListData.getKey().substring(3));

                    Fragment quotesFragment = new MS_Case_Activity();
                    Bundle bundle = new Bundle();

                    bundle.putString("titlebar",currentListData.getValue());
                    bundle.putString("searchID",currentListData.getKey().substring(3));

                    quotesFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,quotesFragment,null);
                    fragmentTransaction.commit();


//                    String url=manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=search_EngineCase.asp&savedsearch=T&mobile_search=T&SearchID="+currentListData.getKey().substring(3);
//
//                    Log.d("MainURL",url);
//
//                    Intent intent = new Intent(_context,CommonWebView.class);
//                    intent.putExtra("url",url);
//                    intent.putExtra("frg","mySearch");
//
//                    _context.startActivity(intent);


                      /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/


                }
                else
                {
                    String key=currentListData.getValue();

                    key = key.replace("?", "&");

                    String url = manager.getMainUrl(_context) + "/mobile_auth.asp?key=" + encp + "&topage=search_Engine.asp&savedsearch=T&mobile_search=T&SearchID=" + key;

                    Log.d("MainURL",url);

                    /*Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","mySearch");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(_context,CommonWebView.class);
                    intent.putExtra("url",url);
                    intent.putExtra("frg","mySearch");

                    _context.startActivity(intent);
                }


            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
//        return (rCollection.get(weekdata.get(groupPosition())).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.myshortcut_listgroup, null);
        }

        LinearLayout toolbar = (LinearLayout) convertView.findViewById(R.id.shortcut_tab);
        toolbar.setBackgroundColor(manager.getColor(_context));

        if(manager.getFontStyle(_context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdpheader_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdpheader_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdpheader_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.mysAdpheader_parent), mainFont);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.myshortcut_group_header);
        ImageView downUpIcon=(ImageView)convertView.findViewById(R.id.updownIcon_myshortcut);

        if(headerTitle.equals("Records"))
        {
            lblListHeader.setText(headerTitle);

            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            downUpIcon.setImageResource(imageResourceId);
        }
        if(headerTitle.equals("Opportunity"))
        {
            lblListHeader.setText(headerTitle);
            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            downUpIcon.setImageResource(imageResourceId);
        }
        if(headerTitle.equals("Contact"))
        {
            lblListHeader.setText(headerTitle);
            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            downUpIcon.setImageResource(imageResourceId);
        }
        if(headerTitle.equals("Case"))
        {
            lblListHeader.setText(headerTitle);
            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            downUpIcon.setImageResource(imageResourceId);
        }
        if(headerTitle.equals("Reports"))
        {
            lblListHeader.setText(headerTitle);
            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            downUpIcon.setImageResource(imageResourceId);
        }

        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(_context);
        String pass=manager.getUserPass(_context);
        String dbId=manager.getDB(_context);
        String wgId=manager.getWG(_context);

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
