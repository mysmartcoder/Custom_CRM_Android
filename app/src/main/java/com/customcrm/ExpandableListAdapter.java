package com.customCRM;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 01/07/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    SessionManager manager =new SessionManager();
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    FragmentActivity a;
//    private final LayoutInflater inf;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        a= (FragmentActivity) context;

        Log.e("_listDataHeader",""+_listDataHeader);

//        inf = LayoutInflater.from(a);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        if(manager.getFontStyle(_context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_item_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_item_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_item_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_item_parent), mainFont);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


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
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView iv=(ImageView)convertView.findViewById(R.id.headerIcon);
        TextView countText=(TextView)convertView.findViewById(R.id.countDisplay);
        ImageView downUpIcon=(ImageView)convertView.findViewById(R.id.updownIcon);
        TextView lblListHeader2=(TextView)convertView.findViewById(R.id.lblListHeader_2);
        View divider = (View) convertView.findViewById(R.id.menu_divider);
        final DrawerLayout drawerLayout = (DrawerLayout) ((MainActivity) a).findViewById(R.id.drawer_layout);

        LinearLayout setting_layout=(LinearLayout)convertView.findViewById(R.id.drawer_header);
        setting_layout.setBackgroundColor(manager.getColor(_context));

        if(manager.getFontStyle(_context).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_group_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_group_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_group_parent), mainFont);
        }
        else if(manager.getFontStyle(_context).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(_context.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(convertView.findViewById(R.id.list_group_parent), mainFont);
        }

//        lblListHeader.setTypeface(null, Typeface.BOLD);
//        final DrawerLayout drawer = (DrawerLayout) convertView.findViewById(R.id.drawer_layout);

        lblListHeader2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment callbackFragment = new CallbackTask_Activity();
                FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,callbackFragment,null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
//                ((MainActivity)getActivity()).closeDrawer();
//                MainActivity mainActivity=new MainActivity();
//                mainActivity.closeDrawer();

//                drawer.closeDrawer(GravityCompat.START);
            }
        });

        Log.e("HeaderTitle",headerTitle+","+groupPosition);

//        if(headerTitle.equals("anyType{}"))
//        {

//        }
//        else
//        {
            if(headerTitle.equals("Add"))
            {
//                iv.setImageResource(R.drawable.ic_action_add_submenu);
                iv.setImageResource(R.drawable.ic_add_white_24dp);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.VISIBLE);
                divider.getLayoutParams().height=5;
                divider.setVisibility(View.VISIBLE);

                int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
                downUpIcon.setImageResource(imageResourceId);
            }
            else if(headerTitle.equals("Home"))
            {
                iv.setImageResource(R.drawable.ic_action_home_icon);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
            }
//        else if(headerTitle.equals(manager.getCustomLabel(_context,"Event")))
//        {
//            iv.setImageResource(R.drawable.ic_action_callback);
//            lblListHeader.setText(headerTitle);
//            countText.setVisibility(View.VISIBLE);
//            countText.setText("2");
//            downUpIcon.setVisibility(View.GONE);
//            lblListHeader2.setVisibility(View.GONE);
//        }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Banner - Contacts")))
            {
                iv.setImageResource(R.drawable.contact);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.VISIBLE);
                countText.setText(manager.getCon(_context));
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);

                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Banner - Accounts")))
            {
                iv.setImageResource(R.drawable.account);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.VISIBLE);
                countText.setText(manager.getAccount(_context));
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);

                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Shortcuts")))
            {
                iv.setImageResource(R.drawable.myshortcut);
                lblListHeader.setText(headerTitle);
                lblListHeader2.setVisibility(View.GONE);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);

                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Recent Items")))
            {
                iv.setImageResource(R.drawable.recent_items);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);

                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals("Dashboard"))
            {
                iv.setImageResource(R.drawable.dashbord);
                lblListHeader.setText(headerTitle);
                lblListHeader2.setVisibility(View.GONE);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);

                divider.setVisibility(View.VISIBLE);
                divider.getLayoutParams().height=5;
            }
            else if(headerTitle.equals("Events"))
            {
                iv.setImageResource(R.drawable.ic_action_calendar);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.VISIBLE);
                lblListHeader2.setText("CallBack/Tasks");

                divider.setVisibility(View.GONE);

                if(manager.getLoginPriv(_context,"AssignCallBack").equals("true"))
                {
                    Log.e("True","Call");
                    lblListHeader2.setVisibility(View.VISIBLE);
                    lblListHeader2.setText("CallBack/Tasks");
                }
                else
                {
                    Log.e("False","Call");
                    lblListHeader2.setVisibility(View.GONE);
                }

//                if(manager.getCustomLabel(_context,"Event").equals("anyType{}"))
//                {
//                    lblListHeader2.setVisibility(View.GONE);
//                }
//                else
//                {
//                }

            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Opportunity")))
            {
                iv.setImageResource(R.drawable.opportuniti);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.VISIBLE);
                countText.setText(manager.getOpp(_context));
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Quote")))
            {
                iv.setImageResource(R.drawable.quote_icon);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.VISIBLE);
                countText.setText(manager.getQuote(_context));
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Case")))
            {
                iv.setImageResource(R.drawable.cases);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.VISIBLE);
                countText.setText(manager.getCases(_context));
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"My Searches")))
            {
                iv.setImageResource(R.drawable.search);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals(manager.getCustomLabel(_context,"Banner - Library")))
            {
                iv.setImageResource(R.drawable.library);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals("Logout"))
            {
                iv.setImageResource(R.drawable.logout);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals("My Calendar"))
            {
                iv.setImageResource(R.drawable.my_calendar);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
            else if(headerTitle.equals("Group Calendar"))
            {
                iv.setImageResource(R.drawable.ic_perm_contact_calendar_white);
                lblListHeader.setText(headerTitle);
                countText.setVisibility(View.GONE);
                downUpIcon.setVisibility(View.GONE);
                lblListHeader2.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
//                divider.getLayoutParams().height=5;
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
}