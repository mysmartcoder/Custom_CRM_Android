 package com.customCRM;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;


public class SessionManager
{

//        static public String MAINurl="{dev_URL}";//    development
    //    static public String MAINurl="{QA_URL}";// production testing
    static public String MAINurl="{Prod_URL}"; //production


    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("customCRM_Pref", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);

        editor.commit();

    }

    public void LogOut(Context context)
    {
//        SharedPreferences.Editor editor1 = context.getSharedPreferences("User", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor2 = context.getSharedPreferences("DBID", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor3 = context.getSharedPreferences("WGID", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor4 = context.getSharedPreferences("CustomLabels", Context.MODE_PRIVATE).edit();
//        SharedPreferences.Editor editor5 = context.getSharedPreferences("LoginPriv", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor6 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor7 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE).edit();
//        editor1.clear();
        editor2.clear();
        editor3.clear();
        editor4.clear();
//        editor5.clear();
        editor6.clear();
        editor7.clear();
//        editor1.commit();
        editor2.commit();
        editor3.commit();
        editor4.commit();
//        editor5.commit();
        editor6.commit();
        editor7.commit();

    }

    public  String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("customCRM_Pref", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void setUser(Context context, String username, String password, String uid, String FullName)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("User", Context.MODE_PRIVATE).edit();
        editor1.putString("username", username);
        editor1.putString("password", password);
        editor1.putString("userId_pref",uid);
        editor1.putString("fullName",FullName);
        editor1.commit();
    }
    public  String getUserName(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String username = prefs1.getString("username", "");
        return username;
    }

    public  String getUserPass(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String password = prefs1.getString("password","");
        return password;
    }
    public  String getUserId(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = prefs1.getString("userId_pref","");
        return userId;
    }
    public  String getUserFullName(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = prefs1.getString("fullName","");
        return userId;
    }


    public void setDB(Context context, String dbid)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("DBID", Context.MODE_PRIVATE).edit();
        editor1.putString("DBid_pref", dbid);
        editor1.commit();
    }
    public  String getDB(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("DBID", Context.MODE_PRIVATE);
        String dbid = prefs1.getString("DBid_pref", "");
        return dbid;
    }

    public void setWG(Context context, String wgid)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("WGID", Context.MODE_PRIVATE).edit();
        editor1.putString("wgid_pref", wgid);
        editor1.commit();
    }
    public  String getWG(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("WGID", Context.MODE_PRIVATE);
        String wgid = prefs1.getString("wgid_pref", "");
        return wgid;
    }

    public String getUrl()
    {
//        String url="{Prod_API}";
        String url="{QA_API}";
//        String url="{Dev_API}";
        return url;
    }

    public void setColor(Context context, int bgColor)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("BGColor", Context.MODE_PRIVATE).edit();
        editor1.putInt("bgColor", bgColor);
        editor1.commit();
    }
    public  int getColor(Context context)
    {
        int defaultColor = Color.parseColor("#0051A1");
        SharedPreferences prefs1 = context.getSharedPreferences("BGColor", Context.MODE_PRIVATE);
        int wgid = prefs1.getInt("bgColor",defaultColor);
        return wgid;
    }

    public void setStatusColor(Context context, int bgColor)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("StatusColor", Context.MODE_PRIVATE).edit();
        editor1.putInt("statusColor", bgColor);
        editor1.commit();
    }
    public  int getStatusColor(Context context)
    {
        int defaultColor = Color.parseColor("#023B74");
        SharedPreferences prefs1 = context.getSharedPreferences("StatusColor", Context.MODE_PRIVATE);
        int wgid = prefs1.getInt("statusColor",defaultColor);
        return wgid;
    }




    public  String get_setting_MAINURL(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("MainUrl", Context.MODE_PRIVATE);
        String url = prefs1.getString("url",null);
        return url;
    }

    public void set_setting_MAINURL(Context context, String url)
    {
//        MAINurl=url;
        SharedPreferences.Editor editor1 = context.getSharedPreferences("MainUrl", Context.MODE_PRIVATE).edit();
        editor1.putString("url", url);
        editor1.commit();
    }

    public String getMainUrl(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("MainUrl", Context.MODE_PRIVATE);
        String mainUrl = prefs1.getString("url",MAINurl);
        return mainUrl;

//        String mainUrl=MAINurl;
//        return mainUrl;


//        String mainUrl="{Prod_URL}";
    }

    public String getLoginPriv(Context context,String key)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("LoginPriv", Context.MODE_PRIVATE);
        String value = prefs1.getString(key,null);
        return value;
    }

    public void setLoginPriv(Context context, String loginPriv_Key,String loginPriv_Value)
    {
        Log.e("setLoginPriv","execute");
        SharedPreferences.Editor editor1 = context.getSharedPreferences("LoginPriv", Context.MODE_PRIVATE).edit();

        if(loginPriv_Key.equals("ReviewAndTakeAct"))
        {
            Log.e("success","1");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("ViewSalesProgress"))
        {
            Log.e("success","2");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("FullEdit"))
        {
            Log.e("success","3");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("AssignCallBack"))
        {
            Log.e("success","4");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("AccountLink"))
        {
            Log.e("success","5");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("ContactLink"))
        {
            Log.e("success","6");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("RecordOnMap"))
        {
            Log.e("success","7");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("DefaultPage"))
        {
            Log.e("success","8");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("ShowQuote"))
        {
            Log.e("success","9");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("ISO_Code"))
        {
            Log.e("success","10");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("MyCalendar"))
        {
            Log.e("success","11");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("GroupCalendar"))
        {
            Log.e("success","12");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }
        else if(loginPriv_Key.equals("Email"))
        {
            Log.e("success","12");
            editor1.putString(loginPriv_Key, loginPriv_Value);
        }

        editor1.commit();
    }

    public void setCustomLabel(Context context, String customLabel_Key,String customLabel_Value)
    {
        Log.e("called","called");
        SharedPreferences.Editor editor1 = context.getSharedPreferences("CustomLabels", Context.MODE_PRIVATE).edit();

        if(customLabel_Key.equals("EE_REP"))
        {
            Log.e("success","1");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Banner - Accounts"))
        {
            Log.e("success","2");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Mkt_Program_ID"))
        {
            Log.e("success","3");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Case"))
        {
            Log.e("success","4");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Cases"))
        {
            Log.e("success","5");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Company"))
        {
            Log.e("success","6");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Banner - Contacts"))
        {
            Log.e("success","7");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Entered"))
        {
            Log.e("success","8");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Event"))
        {
            Log.e("success","9");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Event Name"))
        {
            Log.e("success","10");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Events"))
        {
            Log.e("success","11");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Lead_Status"))
        {
            Log.e("success","12");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("My Searches"))
        {
            Log.e("success","13");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Opportunity"))
        {
            Log.e("success","14");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("OppName"))
        {
            Log.e("success","15");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("PRI_PHONE"))
        {
            Log.e("success","16");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Recent Items"))
        {
            Log.e("success","17");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Record"))
        {
            Log.e("success","18");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Sales Rep Comments/Notes"))
        {
            Log.e("success","19");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Shortcuts"))
        {
            Log.e("success","20");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Banner - Calendar"))
        {
            Log.e("success","21");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Banner - Library"))
        {
            Log.e("success","22");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Lead Status"))
        {
            Log.e("success","23");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Campaign"))
        {
            Log.e("success","24");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Acct Mgr"))
        {
            Log.e("success","25");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Opportunity Name"))
        {
            Log.e("success","26");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Phone"))
        {
            Log.e("success","27");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Quote"))
        {
            Log.e("success","28");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Contact"))
        {
            Log.e("success","29");
            editor1.putString(customLabel_Key, customLabel_Value);
        }

        //new
        else if(customLabel_Key.equals("DateDue"))
        {
            Log.e("success","30");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Case Owner"))
        {
            Log.e("success","31");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Priority"))
        {
            Log.e("success","32");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Status"))
        {
            Log.e("success","33");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Company"))
        {
            Log.e("success","34");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Event Done"))
        {
            Log.e("success","35");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("CONTACT_FIRST_NAME"))
        {
            Log.e("success","36");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Key Contact"))
        {
            Log.e("success","37");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("CONTACT_LAST_NAME"))
        {
            Log.e("success","38");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Quote Name"))
        {
            Log.e("success","39");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Subject"))
        {
            Log.e("success","40");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Lead_Source"))
        {
            Log.e("success","41");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Event Start Time"))
        {
            Log.e("success","42");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Opportunity Sales Stage"))
        {
            Log.e("success","43");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Sales Rep Comments/Notes"))
        {
            Log.e("success","44");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("OppTotal"))
        {
            Log.e("success","44");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Contact Information"))
        {
            Log.e("success","45");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Profile Summary"))
        {
            Log.e("success","46");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Profile Assignments"))
        {
            Log.e("success","47");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Special Interest"))
        {
            Log.e("success","48");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Special Interest II"))
        {
            Log.e("success","49");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Special Interest III"))
        {
            Log.e("success","50");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Sales Rep Comments/Notes"))
        {
            Log.e("success","51");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Opportunity Details"))
        {
            Log.e("success","52");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Product Details"))
        {
            Log.e("success","53");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Case Additional Information"))
        {
            Log.e("success","54");
            editor1.putString(customLabel_Key, customLabel_Value);
        }
        else if(customLabel_Key.equals("Case Details"))
        {
            Log.e("success","55");
            editor1.putString(customLabel_Key, customLabel_Value);
        }

        editor1.commit();

        //Check key not provide from Api

        SharedPreferences prefs1 = context.getSharedPreferences("CustomLabels", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = context.getSharedPreferences("CustomLabels", Context.MODE_PRIVATE).edit();

        if(! prefs1.contains("EE_REP"))
        {
            editor1.putString("EE_REP","");
        }
        if(! prefs1.contains("Banner - Accounts"))
        {
            editor1.putString("Banner - Accounts","");
        }
        if(! prefs1.contains("Mkt_Program_ID"))
        {
            editor1.putString("Mkt_Program_ID","");
        }
        if(! prefs1.contains("Case"))
        {
            editor1.putString("Case","");
        }
        if(! prefs1.contains("Cases"))
        {
            editor1.putString("Cases","");
        }
        if(! prefs1.contains("Company"))
        {
            editor1.putString("Company", "");
        }
        if(! prefs1.contains("Banner - Contacts"))
        {
            editor1.putString("Banner - Contacts", "");
        }
        if(! prefs1.contains("Entered"))
        {
            editor1.putString("Entered", "");
        }
        if(! prefs1.contains("Event"))
        {
            editor1.putString("Event", "");
        }
        if(! prefs1.contains("Event Name"))
        {
            editor1.putString("Event Name", "");
        }
        if(! prefs1.contains("Events"))
        {
            editor1.putString("Events", "");
        }
        if(! prefs1.contains("Lead_Status"))
        {
            editor1.putString("Lead_Status", "");
        }
        if(! prefs1.contains("My Searches"))
        {
            editor1.putString("My Searches", "");
        }
        if(! prefs1.contains("Opportunity"))
        {
            editor1.putString("Opportunity", "");
        }
        if(! prefs1.contains("OppName"))
        {
            editor1.putString("OppName", "");
        }
        if(! prefs1.contains("PRI_PHONE"))
        {
            editor1.putString("PRI_PHONE", "");
        }
        if(! prefs1.contains("Recent Items"))
        {
            editor1.putString("Recent Items", "");
        }
        if(! prefs1.contains("Record"))
        {
            editor1.putString("Record", "");
        }
        if(! prefs1.contains("Sales Rep Comments/Notes"))
        {
            editor1.putString("Sales Rep Comments/Notes", "");
        }
        if(! prefs1.contains("Shortcuts"))
        {
            editor1.putString("Shortcuts", "");
        }
        if(! prefs1.contains("Banner - Calendar"))
        {
            editor1.putString("Banner - Calendar", "");
        }
        if(! prefs1.contains("Banner - Library"))
        {
            editor1.putString("Banner - Library", "");
        }
        if(! prefs1.contains("Lead Status"))
        {
            editor1.putString("Lead Status", "");
        }
        if(! prefs1.contains("Campaign"))
        {
            editor1.putString("Campaign", "");
        }
        if(! prefs1.contains("Acct Mgr"))
        {
            editor1.putString("Acct Mgr", "");
        }
        if(! prefs1.contains("Opportunity Name"))
        {
            editor1.putString("Opportunity Name", "");
        }
        if(!prefs1.contains("Phone"))
        {
            editor1.putString("Phone", "");
        }
        if(! prefs1.contains("Quote"))
        {
            editor1.putString("Quote", "");
        }
        if(! prefs1.contains("Contact"))
        {
            editor1.putString("Contact", "");
        }

        //new
        if(! prefs1.contains("DateDue"))
        {
            editor1.putString("DateDue", "");
        }
        if(! prefs1.contains("Case Owner"))
        {
            editor1.putString("Case Owner", "");
        }
        if(! prefs1.contains("Priority"))
        {
            editor1.putString("Priority", "");
        }
        if(! prefs1.contains("Status"))
        {
            editor1.putString("Status", "");
        }
        if(! prefs1.contains("Company"))
        {
            editor1.putString("Company", "");
        }
        if(! prefs1.contains("Event Done"))
        {
            editor1.putString("Event Done", "");
        }
        if(! prefs1.contains("CONTACT_FIRST_NAME"))
        {
            editor1.putString("CONTACT_FIRST_NAME", "");
        }
        if(! prefs1.contains("Key Contact"))
        {
            editor1.putString("Key Contact", "");
        }
        if(! prefs1.contains("CONTACT_LAST_NAME"))
        {
            editor1.putString("CONTACT_LAST_NAME", "");
        }
        if(! prefs1.contains("Quote Name"))
        {
            editor1.putString("Quote Name", "");
        }
        if(! prefs1.contains("Subject"))
        {
            editor1.putString("Subject", "");
        }
        if(! prefs1.contains("Lead_Source"))
        {
            editor1.putString("Lead_Source", "");
        }
        if(! prefs1.contains("Event Start Time"))
        {
            editor1.putString("Event Start Time", "");
        }
        if(! prefs1.contains("Opportunity Sales Stage"))
        {
            editor1.putString("Opportunity Sales Stage", "");
        }
        if(! prefs1.contains("Sales Rep Comments/Notes"))
        {
            editor1.putString("Sales Rep Comments/Notes", "");
        }
        if(! prefs1.contains("OppTotal"))
        {
            editor1.putString("OppTotal", "");
        }

        if(! prefs1.contains("Contact Information"))
        {
            editor1.putString("Contact Information", "");
        }
        if(! prefs1.contains("Profile Summary"))
        {
            editor1.putString("Profile Summary", "");
        }
        if(! prefs1.contains("Profile Assignments"))
        {
            editor1.putString("Profile Assignments", "");
        }
        if(! prefs1.contains("Special Interest"))
        {
            editor1.putString("Special Interest", "");
        }
        if(! prefs1.contains("Special Interest II"))
        {
            editor1.putString("Special Interest II", "");
        }
        if(! prefs1.contains("Special Interest III"))
        {
            editor1.putString("Special Interest III", "");
        }
        if(! prefs1.contains("Sales Rep Comments/Notes"))
        {
            editor1.putString("Sales Rep Comments/Notes", "");
        }
        if(! prefs1.contains("Opportunity Details"))
        {
            editor1.putString("Opportunity Details", "");
        }
        if(! prefs1.contains("Product Details"))
        {
            editor1.putString("Product Details", "");
        }
        if(! prefs1.contains("Case Additional Information"))
        {
            editor1.putString("Case Additional Information", "");
        }
        if(! prefs1.contains("Case Details"))
        {
            editor1.putString("Case Details", "");
        }
        editor1.commit();
    }

    public String getCustomLabel(Context context,String key)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("CustomLabels", Context.MODE_PRIVATE);
        String value = prefs1.getString(key,null);
        return value;
    }

    public void setCounts(Context context, String account, String contact, String opp, String cases, String quote)
    {
        Log.e("Counter","Set");
        SharedPreferences.Editor editor1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE).edit();

        editor1.putString("account", account);
        editor1.putString("contact", contact);
        editor1.putString("opp",opp);
        editor1.putString("cases",cases);
        editor1.putString("quote",quote);

        editor1.commit();
    }
    public  String getAccount(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE);
        String acc = prefs1.getString("account", "");
        return acc;
    }
    public  String getCon(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE);
        String con = prefs1.getString("contact", "");
        return con;
    }
    public  String getOpp(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE);
        String opp = prefs1.getString("opp", "");
        return opp;
    }
    public  String getCases(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE);
        String cases = prefs1.getString("cases", "");
        return cases;
    }
    public  String getQuote(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Counter", Context.MODE_PRIVATE);
        String quote = prefs1.getString("quote", "");
        return quote;
    }


    public void setToday(Context context, String date)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("Today", Context.MODE_PRIVATE).edit();
        editor1.putString("date", date);
        editor1.commit();
    }
    public  String getToday(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("Today", Context.MODE_PRIVATE);
        String date = prefs1.getString("date",null);
        return date;
    }

    public void setFontStyle(Context context, String date)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("FontStyle", Context.MODE_PRIVATE).edit();
        editor1.putString("fontstyle", date);
        editor1.commit();
    }

    public String getFontStyle(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("FontStyle", Context.MODE_PRIVATE);
        String font_style = prefs1.getString("fontstyle","open-sans");
        return font_style;
    }


    public void setGoupUserData(Context context,String dateView , String userId,String selectedSession)
    {
        SharedPreferences.Editor editor1 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE).edit();
        editor1.putString("GCUID", userId);
        editor1.putString("GCDV", dateView);
        editor1.putString("GCS", selectedSession);
        editor1.commit();
    }
    public  String getGroupCalUser(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE);
        return prefs1.getString("GCUID","");
    }
    public  String getGroupCalDateView(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE);
        return prefs1.getString("GCDV","");
    }
    public  String getGroupCalSession(Context context)
    {
        SharedPreferences prefs1 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE);
        return prefs1.getString("GCS","");
    }
    public  void clearGroupCal(Context context)
    {
        SharedPreferences.Editor editor7 = context.getSharedPreferences("GroupCal", Context.MODE_PRIVATE).edit();
        editor7.clear();
        editor7.commit();
    }
}