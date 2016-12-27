package com.customCRM;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 02/09/2016.
 */
public class PrepareDrawer
{
    public static void allSet(Context context)
    {
        setMenuItems(context);
    }

    public static void setMenuItems(Context context)
    {
        int count=0;
        Toolbar toolbar;
        DrawerLayout drawerLayout;
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;

//        Context context;

        FragmentActivity a;
//        this.context = context;
        a= (FragmentActivity) context;
        List<String> listDataHeader = new ArrayList<String>();
        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
        SessionManager manager=new SessionManager();

        // Adding child data
        listDataHeader.add("Home");

        if(manager.getLoginPriv(context,"MyCalendar").equals("true"))
        {
            listDataHeader.add("My Calendar");
        }
        if(manager.getLoginPriv(context,"GroupCalendar").equals("true"))
        {
            listDataHeader.add("Group Calendar");
        }

        listDataHeader.add("Dashboard");
        listDataHeader.add("Add");
        listDataHeader.add("Events");

        if(manager.getCustomLabel(context,"Shortcuts").equals(""))
        {
        }
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Shortcuts"));
        }

        if(manager.getCustomLabel(context,"Banner - Accounts").equals("") || manager.getLoginPriv(context,"AccountLink").equals("false"))
        {}
        else
        {
            Log.e("Slider","Account Add");
            listDataHeader.add(manager.getCustomLabel(context,"Banner - Accounts"));
        }

        if(manager.getCustomLabel(context,"Banner - Contacts").equals("") || manager.getLoginPriv(context,"ContactLink").equals("false"))
        {}
        else
        {
            Log.e("Slider","Contact Add");
            listDataHeader.add(manager.getCustomLabel(context,"Banner - Contacts"));
        }

        if(manager.getCustomLabel(context,"Opportunity").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Opportunity"));
        }

        if(manager.getCustomLabel(context,"Quote").equals("") || manager.getLoginPriv(context,"ShowQuote").equals("false"))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Quote"));
        }

        if(manager.getCustomLabel(context,"Case").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Case"));
        }

        if(manager.getCustomLabel(context,"My Searches").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"My Searches"));
        }

        if(manager.getCustomLabel(context,"Recent Items").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Recent Items"));
        }

        if(manager.getCustomLabel(context,"Banner - Library").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(context,"Banner - Library"));
        }
        listDataHeader.add("Logout");


        // Adding child data
        List<String> addField = new ArrayList<String>();
        if(manager.getCustomLabel(context,"Banner - Accounts").equals("") || manager.getLoginPriv(context,"AccountLink").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Record"));
        }
        if(manager.getCustomLabel(context,"Banner - Contacts").equals("") || manager.getLoginPriv(context,"ContactLink").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Banner - Contacts"));
        }
        if(manager.getCustomLabel(context,"Opportunity").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Opportunity"));
        }

//        addField.add("Add "+manager.getCustomLabel(context,"Record"));
//        addField.add("Add "+manager.getCustomLabel(context,"Contact"));
//        addField.add("Add "+manager.getCustomLabel(context,"Opportunity"));
        if(manager.getCustomLabel(context,"Quote").equals("") || manager.getLoginPriv(context,"ShowQuote").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Quote"));
        }

        if(manager.getCustomLabel(context,"Sales Rep Comments/Notes").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Sales Rep Comments/Notes"));
        }

        if(manager.getLoginPriv(context,"AssignCallBack").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Event"));
        }

        if(manager.getCustomLabel(context,"Case").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(context,"Case"));
        }


        List<String> otherField = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(count), otherField); // Header, Child data
        count++;
        if(manager.getLoginPriv(context,"MyCalendar").equals("true"))
        {
            listDataChild.put(listDataHeader.get(count), otherField);
            count++;
            Log.e("Count",count+"");
        }
        if(manager.getLoginPriv(context,"GroupCalendar").equals("true"))
        {
            listDataChild.put(listDataHeader.get(count), otherField);
            count++;
            Log.e("Count",count+"");
        }

        listDataChild.put(listDataHeader.get(count), otherField);
        count++;
        listDataChild.put(listDataHeader.get(count), addField);
        count++;
        listDataChild.put(listDataHeader.get(count), otherField);
//        count++;

        for(int i=count;i<listDataHeader.size();i++)
        {
            listDataChild.put(listDataHeader.get(i), otherField);
        }

        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        expListView = (ExpandableListView)((MainActivity)a).findViewById(R.id.lvExp);

        expListView.setAdapter(listAdapter);
    }
}
