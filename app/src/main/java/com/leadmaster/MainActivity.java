package com.leadmaster;

//import android.app.FragmentManager;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
{
    String DatabaseIdIntent,WorkGroupIdIntent,LoginIntent;
    SessionManager manager;
    String userNameSession,passSession;

    //ExpandableList in Drawer
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;

    ArrayList<HashMap<String,Integer>> listItems = new ArrayList<HashMap<String,Integer>>();
    HashMap<String, List<String>> listDataChild;


    Button callback_footer,home_footer,account_footer,contacts_footer,
            setting_footer,myshortcut_footer,recentitems_footer,dashboard_footer;


    String totalAccRcord,totalConRcord,totalOppRcord,totalCaseRecord,totalQuoteRecord;
    ProgressDialog pd;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_main);

        manager=new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(MainActivity.this));
        }

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.nav_header_main, expListView, false);
//        LinearLayout ll1=(LinearLayout)header.findViewById(R.id.nav_header_ll_1);

//        header.getLayoutParams().height=100;

        if(manager.getFontStyle(MainActivity.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.lvExp), mainFont);
            CustomFont.markAsIconContainer(header.findViewById(R.id.nav_parent), mainFont);
//            ll1.getLayoutParams().height=200;

        }
        else if(manager.getFontStyle(MainActivity.this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.lvExp), mainFont);
            CustomFont.markAsIconContainer(header.findViewById(R.id.nav_parent), mainFont);
//            ll1.getLayoutParams().height=200;
        }
        else if(manager.getFontStyle(MainActivity.this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.lvExp), mainFont);
            CustomFont.markAsIconContainer(header.findViewById(R.id.nav_parent), mainFont);
//            ll1.getLayoutParams().height=200;
        }
        else if(manager.getFontStyle(MainActivity.this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.lvExp), mainFont);
            CustomFont.markAsIconContainer(header.findViewById(R.id.nav_parent), mainFont);
//            ll1.getLayoutParams().height=200;
        }
//        else
//        {
//            ll1.getLayoutParams().height=200;
//        }


        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.footerLL), iconFont);
        FontManager.markAsIconContainer(header.findViewById(R.id.logout_header_drawer), iconFont);

        manager.setToday(MainActivity.this,getDate());
        Log.e("Today ==>",manager.getToday(MainActivity.this));
//        Intent i=getIntent();
        LoginIntent=manager.getUserId(MainActivity.this);
        DatabaseIdIntent=manager.getDB(MainActivity.this);
        WorkGroupIdIntent=manager.getWG(MainActivity.this);

//        manager = new SessionManager();
        userNameSession=manager.getUserName(MainActivity.this);
        passSession=manager.getUserPass(MainActivity.this);

        Log.e("Info==>","LoginId= "+LoginIntent+" DatabaseId= "+DatabaseIdIntent+" WorkGroupId= "+WorkGroupIdIntent+" User=>"+userNameSession+" Pass=>"+passSession);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(manager.getColor(MainActivity.this));

        LinearLayout footer_layout=(LinearLayout)findViewById(R.id.footerLL);
        footer_layout.setBackgroundColor(manager.getColor(MainActivity.this));

        String i=manager.getPreferences(MainActivity.this, "firstTime");

        if(i.equals("1"))
        {
            manager.setPreferences(MainActivity.this, "firstTime", "0");
//            GetCustomLabels getCustomLabels=new GetCustomLabels();
//            getCustomLabels.execute();

            GetCounter getCounter=new GetCounter();
            getCounter.execute();
        }

//        nav_UserName=(TextView)findViewById(R.id.UserName_Nav);
//        nav_UserSym=(TextView)findViewById(R.id.nav_user_symbol);
//
//        nav_UserName.setText(manager.getUserName(MainActivity.this));
//        nav_UserSym.setText(manager.getUserName(MainActivity.this).substring(0,1));


        setting_footer=(Button)findViewById(R.id.setting_footer);
//        callback_footer=(Button)findViewById(R.id.callback_footer);
        home_footer=(Button)findViewById(R.id.home_footer);
        account_footer=(Button)findViewById(R.id.account_footer);
        contacts_footer=(Button)findViewById(R.id.contacts_footer);
        myshortcut_footer=(Button)findViewById(R.id.myshortcut_footer);
        recentitems_footer=(Button)findViewById(R.id.recentitem_footer);
        dashboard_footer=(Button)findViewById(R.id.dashboard_footer);



        dashboard_footer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String encp=encrptVal();
                String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dashboard.asp&screenwidth=";

//                    var url = url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=mobile_dashboard.asp&screenwidth="+  screenwidth+"";
                Log.e("Dashboard",url);

               /* Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","dash");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();
*/
                Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "dash");

               startActivity(intent);

            }
        });

        setting_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Original
                Fragment settingsFragment = new Setting_Activity();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,settingsFragment,null);
                fragmentTransaction.commit();
//                Intent i=new Intent(getApplicationContext(),Setting_Activity.class);
//                startActivity(i);
            }
        });

//        callback_footer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////Original
//                Fragment callbackFragment = new CallbackTask_Activity();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.containerView,callbackFragment,null);
//                fragmentTransaction.commit();
//
////                Intent i=new Intent(getApplicationContext(),CallbackTask_Activity.class);
////                startActivity(i);
//            }
//        });

        home_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                Fragment dashFragment = new Dashboard_Activity();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,dashFragment,null);
                fragmentTransaction.commit();

//                Intent i=new Intent(getApplicationContext(),CallbackTask_Activity.class);
//                startActivity(i);
            }
        });

        account_footer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(manager.getLoginPriv(MainActivity.this,"AccountLink").equals("true"))
                {
                    Log.e("true",manager.getLoginPriv(MainActivity.this,"AccountLink"));
                    Fragment accountsFragment = new Accounts_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                    fragmentTransaction.commit();
                }
                else
                {
                    Log.e("false",manager.getLoginPriv(MainActivity.this,"AccountLink"));
                    Fragment accountsFragment = new NotAuthorized();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                    fragmentTransaction.commit();
                }


//                Intent i=new Intent(getApplicationContext(),Accounts_Activity.class);
//                startActivity(i);
            }
        });

        contacts_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                if(manager.getLoginPriv(MainActivity.this,"ContactLink").equals("true"))
                {
                    Log.e("true",manager.getLoginPriv(MainActivity.this,"ContactLink"));
                    Fragment contactsFragment = new Contacts_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,contactsFragment,null);
                    fragmentTransaction.commit();
                }
                else
                {
                    Log.e("false",manager.getLoginPriv(MainActivity.this,"ContactLink"));
                    Fragment accountsFragment = new NotAuthorized();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                    fragmentTransaction.commit();
                }
//                Intent i=new Intent(getApplicationContext(),Contacts_Activity.class);
//                startActivity(i);
            }
        });

        myshortcut_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                Fragment accountsFragment = new MyShortcuts_Activity();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Log.e("Main frag : ",""+fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                fragmentTransaction.commit();

            }
        });

        recentitems_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//original
                Fragment recentItemFragment = new RecentItems_Activity();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,recentItemFragment,null);
                fragmentTransaction.commit();
            }
        });

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

//        LinearLayout setting_layout=(LinearLayout)view.findViewById(R.id.setting_layout);
        expListView.setBackgroundColor(manager.getColor(MainActivity.this));



        // preparing list data

        prepareListData();


        Log.e("listDataHeader",""+listDataHeader);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.nav_header_main, expListView, false);

        TextView nav_UserName=(TextView)header.findViewById(R.id.UserName_Nav);
        TextView nav_UserSym=(TextView)header.findViewById(R.id.nav_user_symbol);
        TextView nav_Logout=(TextView)header.findViewById(R.id.logout_header_drawer);

        nav_Logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                manager.setPreferences(MainActivity.this, "status", "0");
                manager.setPreferences(MainActivity.this, "firstTime", "0");
                manager.LogOut(MainActivity.this);

                String encp=encrptVal();
                String logout_url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=index_logoff.asp";

                Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                intent.putExtra("url", logout_url);
                intent.putExtra("frg", "logout");

                startActivity(intent);
            }
        });
//        nav_UserName.getLayoutParams().height=100;

        GradientDrawable bgShape = (GradientDrawable)nav_UserSym.getBackground();
        bgShape.setColor(manager.getColor(MainActivity.this));
//        nav_UserSym.setBackgroundColor(manager.getColor(MainActivity.this));

//        String str = "java";
//        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);

        String Fullname="";
        if(manager.getUserFullName(MainActivity.this).contains(" "))
        {
            String[] name = manager.getUserFullName(MainActivity.this).split(" ");
            for(int nameCap=0;nameCap<name.length;nameCap++)
            {
                if(nameCap==0)
                {
                    Fullname=name[nameCap].substring(0,1).toUpperCase() + name[nameCap].substring(1);
                }
                else
                {
                    Fullname=Fullname+" "+name[nameCap].substring(0,1).toUpperCase() + name[nameCap].substring(1);
                }
            }
        }
        else
        {
            Fullname = manager.getUserFullName(MainActivity.this).substring(0,1).toUpperCase() + manager.getUserFullName(MainActivity.this).substring(1);
        }
//        nav_UserName.setText( manager.getUserFullName(MainActivity.this).substring(0,1).toUpperCase() + manager.getUserFullName(MainActivity.this).substring(1) );
        nav_UserName.setText( Fullname );
        nav_UserSym.setText(manager.getUserFullName(MainActivity.this).substring(0,1).toUpperCase());

        expListView.addHeaderView(header, null, false);

        expListView.setAdapter(listAdapter);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Collapsed",Toast.LENGTH_SHORT).show();

                if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Event")))
                {
                    Fragment callbackFragment = new CallbackTask_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,callbackFragment,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

//                    Intent i=new Intent(getApplicationContext(),CallbackTask_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Banner - Contacts")))
                {
                    if(manager.getLoginPriv(MainActivity.this,"ContactLink").equals("true"))
                    {
                        Fragment callbackFragment = new Contacts_Activity();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,callbackFragment,null);
                        fragmentTransaction.commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    else
                    {
                        Fragment callbackFragment = new NotAuthorized();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,callbackFragment,null);
                        fragmentTransaction.commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
//                    Intent i=new Intent(getApplicationContext(),Contacts_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals("Home"))
                {
                    Fragment home = new Dashboard_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,home,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

//                    Intent i=new Intent(getApplicationContext(),Contacts_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals("My Calendar"))
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(getApplicationContext(),MyCalendar_Activity.class);
                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals("Group Calendar"))
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(getApplicationContext(),GroupCalendar_Activity.class);
                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Banner - Accounts")))
                {
                    if(manager.getLoginPriv(MainActivity.this,"AccountLink").equals("true"))
                    {
                        Fragment accountsFragment = new Accounts_Activity();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                        fragmentTransaction.commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    else
                    {
                        Fragment accountsFragment = new NotAuthorized();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                        fragmentTransaction.commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }

//                    Intent i=new Intent(getApplicationContext(),Accounts_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Shortcuts")))
                {
                    Fragment accountsFragment = new MyShortcuts_Activity();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Log.e("frag 1",""+fragmentManager.getBackStackEntryCount());

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,accountsFragment,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

//                    Intent i=new Intent(getApplicationContext(),MyShortcuts_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Recent Items")))
                {
                    Fragment recentItemsFragment = new RecentItems_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,recentItemsFragment,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
//                    Intent i=new Intent(getApplicationContext(),RecentItems_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals("Dashboard"))
                {
                    drawer.closeDrawer(GravityCompat.START);
                    String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dashboard.asp&screenwidth=";

//                    var url = url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=mobile_dashboard.asp&screenwidth="+  screenwidth+"";
                    Log.e("Dashboard",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","dash");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    startActivity(intent);


                }
                else if(listDataHeader.get(groupPosition).equals("Events"))
                {
                    drawer.closeDrawer(GravityCompat.START);
                    String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_calWeek.asp";

                    Log.e("Calendar",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","dash");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();
*/
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    startActivity(intent);

                }


                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Opportunity")))
                {
                    Fragment opportunityFragment = new Oppotunity_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,opportunityFragment,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

//                    Intent i=new Intent(getApplicationContext(),Oppotunity_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Quote")))
                {
                    Fragment quotesFragment = new QuotesActivity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,quotesFragment,null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

//                    Intent i=new Intent(getApplicationContext(),Oppotunity_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Case")))
                {
                    Fragment casesFragment = new Cases_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,casesFragment,null);
                    fragmentTransaction.commit();
//
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"My Searches")))
                {
                    Fragment searchFragment = new Serches_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,searchFragment,null);
                    fragmentTransaction.commit();
//
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
//                    Intent i=new Intent(getApplicationContext(),Serches_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals(manager.getCustomLabel(MainActivity.this,"Banner - Library")))
                {
                    Fragment searchFragment = new Library_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,searchFragment,null);
                    fragmentTransaction.commit();
//
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
//                    Intent i=new Intent(getApplicationContext(),Library_Activity.class);
//                    startActivity(i);
                }
                else if(listDataHeader.get(groupPosition).equals("Logout"))
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    manager.setPreferences(MainActivity.this, "status", "0");
                    manager.setPreferences(MainActivity.this, "firstTime", "0");
                    manager.LogOut(MainActivity.this);
                    String encp=encrptVal();
                    String logout_url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=index_logoff.asp";

                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", logout_url);
                    intent.putExtra("frg", "logout");

                    startActivity(intent);
                }
                return false;
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//original
//                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : "
//                                + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
//                                Toast.LENGTH_SHORT).show();
                Log.e("Child",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Record")))
                {
                    drawer.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(MainActivity.this,AddRecord_Dynamic.class);
                    startActivity(i);

//                    String encp=encrptVal();
//                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_frmlead.asp";
//
//                    Log.e("AddRecord Url",url);
//
//                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
//                    intent.putExtra("url", url);
//                    intent.putExtra("frg", "dash");
//                    intent.putExtra("titlebar","record");
//                    startActivity(intent);

                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Banner - Contacts")))
                {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(MainActivity.this,AddContact_Dynamic.class);
                    startActivity(i);

                    /*String encp=encrptVal();
                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgcontact.asp&fromPage=mobile_dlgcontact.asp&add_contact=T&RECDNO=0&contactid=0&CompanyID=" + manager.getWG(MainActivity.this);
                    Log.e("AddContact Url",url);
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");
                    intent.putExtra("titlebar","contact");
                    startActivity(intent);*/
                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Opportunity")))
                {
                    drawer.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(MainActivity.this,AddOpp_Dynamic.class);
                    startActivity(i);

                    /*String encp=encrptVal();
                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_opportunity_EditForm.asp&frompage=mobile_opportunity_EditForm.asp&add_Opportunity=T&RECDNO=0&OppID=0";
                    Log.e("AddOpp Url",url);
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");
                    intent.putExtra("titlebar","opp");
                    startActivity(intent);*/
                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Quote")))
                {
                    drawer.closeDrawer(GravityCompat.START);
                    String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_quote_EditForm.asp&add_quote=T&CompanyID=" + manager.getWG(MainActivity.this) +"&ContactID=0&RECDNO=0";

                    Log.e("AddQuotes Url",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","dash");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    intent.putExtra("titlebar","quote");

                    startActivity(intent);

                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Sales Rep Comments/Notes")))//Sales Rep Comments/Notes
                {
                    drawer.closeDrawer(GravityCompat.START);
                    String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_AddNote.asp&add_notes=T&CompanyID=" + manager.getWG(MainActivity.this);

                    Log.e("AddNote Url",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","dash");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    intent.putExtra("titlebar","note");

                    startActivity(intent);

                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Event")))
                {
                    drawer.closeDrawer(GravityCompat.START);
                    String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbEditEvent.asp"+"&add_CallBack=T";

                    Log.e("AddTask Url",url);

                   /* Fragment webviewFragment = new Common_WebView();

                    Bundle bundle = new Bundle();

                    bundle.putString("url", url);
                    bundle.putString("frg","dash");

                    webviewFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    intent.putExtra("titlebar","event");

                    startActivity(intent);

                }
                else  if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Add "+manager.getCustomLabel(MainActivity.this,"Case")))
                {
                    drawer.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(MainActivity.this,AddCase_Dynamic.class);
                    startActivity(i);
                    /*String encp=encrptVal();

                    String url = manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dlgAddcase.asp&RECDNO=0&id=0&add_Case=T";

                    Log.e("AddCase Url",url);
                    Intent intent = new Intent(MainActivity.this, CommonWebView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("frg", "dash");

                    intent.putExtra("titlebar","case");

                   startActivity(intent);*/

                }

                return false;
            }
        });

//        String i=manager.getPreferences(MainActivity.this, "firstTime");
//
//        if(i.equals("1"))
//        {
//            manager.setPreferences(MainActivity.this, "firstTime", "0");
//            GetCustomLabels getCustomLabels=new GetCustomLabels();
//            getCustomLabels.execute();
//
////            GetCounter getCounter=new GetCounter();
////            getCounter.execute();
//        }

        //Add the Very First i.e Squad Fragment to the Container
        Fragment squadFragment = new Dashboard_Activity();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView,squadFragment,null);
        fragmentTransaction.commit();

//        GetTotal getTotal=new GetTotal();
//        getTotal.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    Log.e("MainPermissionResult : ","If shouldShowRequestPermissionRationale");
                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("MainPermissionResult : ","Else shouldShowRequestPermissionRationale");
                }
                return;
            }

            case 2:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    Log.e("MainPermissionResult : ","If shouldShowRequestPermissionRationale");
                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("MainPermissionResult : ","Else shouldShowRequestPermissionRationale");
                }
            }
            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed()
    {
        Log.e("counting",getSupportFragmentManager().getBackStackEntryCount()+"");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            finish();
            super.onBackPressed();

            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            {

                Log.i("MainActivity", "popping backstack");

                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
            else
            {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.containerView);

            if (f instanceof Dashboard_Activity)
            {
                Log.i("counrFrag",getSupportFragmentManager().getBackStackEntryCount()+"");
                Log.i("MainActivity", "nothing on backstack, calling super");
                super.onBackPressed();
            }
            else
            {
                Intent i= new Intent(MainActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
//                Fragment fragment = new Dashboard_Activity();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerView,fragment,null);
//                fragmentTransaction.commit();
            }
            }
        }

    }

    private void prepareListData()
    {
        int count=0;

        listDataHeader = new ArrayList<String>();
//        listDataHeader1 = new HashMap<>();

        listDataChild = new HashMap<String, List<String>>();
//        listDataChild1 = new HashMap<>();

        // Adding child data
        listDataHeader.add("Home");

        if(manager.getLoginPriv(MainActivity.this,"MyCalendar").equals("true"))
        {
            listDataHeader.add("My Calendar");
//            count++;
        }
        if(manager.getLoginPriv(MainActivity.this,"GroupCalendar").equals("true"))
        {
            listDataHeader.add("Group Calendar");
//            count++;
        }

        listDataHeader.add("Dashboard");
        listDataHeader.add("Add");
        listDataHeader.add("Events");

        if(manager.getCustomLabel(MainActivity.this,"Shortcuts").equals(""))
        {
        }
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Shortcuts"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Banner - Accounts").equals("") || manager.getLoginPriv(MainActivity.this,"AccountLink").equals("false"))
        {}
        else
        {
            Log.e("Slider","Account Add");
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Banner - Accounts"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Banner - Contacts").equals("") || manager.getLoginPriv(MainActivity.this,"ContactLink").equals("false"))
        {}
        else
        {
            Log.e("Slider","Contact Add");
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Banner - Contacts"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Opportunity").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Opportunity"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Quote").equals("") || manager.getLoginPriv(MainActivity.this,"ShowQuote").equals("false"))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Quote"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Case").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Case"));
        }

        if(manager.getCustomLabel(MainActivity.this,"My Searches").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"My Searches"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Recent Items").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Recent Items"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Banner - Library").equals(""))
        {}
        else
        {
            listDataHeader.add(manager.getCustomLabel(MainActivity.this,"Banner - Library"));
        }
        listDataHeader.add("Logout");


        // Adding child data
        List<String> addField = new ArrayList<String>();
        if(manager.getCustomLabel(MainActivity.this,"Banner - Accounts").equals("") || manager.getLoginPriv(MainActivity.this,"AccountLink").equals("false"))
        {
        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Record"));
        }
        if(manager.getCustomLabel(MainActivity.this,"Banner - Contacts").equals("") || manager.getLoginPriv(MainActivity.this,"ContactLink").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Banner - Contacts"));
        }
        if(manager.getCustomLabel(MainActivity.this,"Opportunity").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Opportunity"));
        }

//        addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Record"));
//        addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Contact"));
//        addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Opportunity"));
        if(manager.getCustomLabel(MainActivity.this,"Quote").equals("") || manager.getLoginPriv(MainActivity.this,"ShowQuote").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Quote"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Sales Rep Comments/Notes").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Sales Rep Comments/Notes"));
        }

        if(manager.getLoginPriv(MainActivity.this,"AssignCallBack").equals("false"))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Event"));
        }

        if(manager.getCustomLabel(MainActivity.this,"Case").equals(""))
        {

        }
        else
        {
            addField.add("Add "+manager.getCustomLabel(MainActivity.this,"Case"));
        }


        List<String> otherField = new ArrayList<String>();

        Log.e("Count",count+"");
        listDataChild.put(listDataHeader.get(count), otherField);// Header, Child data
        count++;
        Log.e("Count",count+"");
        if(manager.getLoginPriv(MainActivity.this,"MyCalendar").equals("true"))
        {
            listDataChild.put(listDataHeader.get(count), otherField);
            count++;
            Log.e("Count",count+"");
        }
        if(manager.getLoginPriv(MainActivity.this,"GroupCalendar").equals("true"))
        {
            listDataChild.put(listDataHeader.get(count), otherField);
            count++;
            Log.e("Count",count+"");
        }

        listDataChild.put(listDataHeader.get(count), otherField);
        count++;
        Log.e("Count",count+"");
        listDataChild.put(listDataHeader.get(count), addField);
        count++;
        Log.e("Count",count+"");
        listDataChild.put(listDataHeader.get(count), otherField);
//        count++;
        Log.e("Count",count+"");

        for(int i=count;i<listDataHeader.size();i++)//i=5
        {
            Log.e("Count",i+"");
            listDataChild.put(listDataHeader.get(i), otherField);
        }
    }

    private class GetCustomLabels extends AsyncTask<Void, Void, Void>
    {
        SoapObject resultRequestSOAP = null;
        String labelNames = "Banner - Accounts^Contact^Company^Banner - Contacts^Lead Status^Entered^Campaign^Events^Event Name^Event^Shortcuts^My Searches^Recent Items^Acct Mgr^Opportunity^Case^Sales Rep Comments/Notes^Record^Opportunity Name^Phone^Cases^Banner - Library^Quote^Sales Rep Comments/Notes^Banner - Calendar^Shortcuts^Event Done^Quote Name^Company^First Name^Last Name^Case Date Due^Subject^Case Status^Case Priority^Case Owner^Key Contact^Lead_Source^Event Start Time^Opportunity Sales Stage^List Source^Sales Rep Comments/Notes^Total Opportunity Value^Contact Information^Profile Summary^Profile Assignments^Special Interest^Special Interest II^Special Interest III^Sales Rep Comments/Notes^Opportunity Details^Product Details^Case Additional Information^Case Details";
        SoapPrimitive resultString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            pd=new ProgressDialog(MainActivity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetCustomLabel";
            String METHOD_NAME = "GetCustomLabel";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("LogonID", manager.getUserId(MainActivity.this));
                Request.addProperty("CompanyID", manager.getWG(MainActivity.this));
                Request.addProperty("LabelName", labelNames);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String key = TypeEventData.getProperty("Key").toString();
                    String value = TypeEventData.getProperty("Value").toString();

                    Log.e(i+" : Key,Value ==> ",key+","+value);
                    manager.setCustomLabel(MainActivity.this, key, value);
                }


                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

//             Log.e(TAG, "Result SECOND API: " + resultString);
            }
            catch (Exception ex)
            {
//           Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + resultRequestSOAP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            pd.cancel();
            Log.d("Post","Execute");
//            GetCounter getCounter=new GetCounter();
//            getCounter.execute();


        }
    }

    private class GetCounter extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            pd=new ProgressDialog(MainActivity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
//            list=new ArrayList();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            int test=5;

            for(int i=0;i<test;i++)
            {
                SoapObject resultRequestSOAP = null;
                String SOAP_ACTION = null;
                String METHOD_NAME = null;
                String NAMESPACE = null;
                String URL = manager.getUrl();
                String query= null;

                if(i==0)
                {
                    SOAP_ACTION = "LMServiceNamespace/SearchAccountCount";
                    METHOD_NAME = "SearchAccountCount";
                    NAMESPACE = "LMServiceNamespace";
                    query="company";
                }
                if(i==1)
                {
                    SOAP_ACTION = "LMServiceNamespace/SearchContactCount";
                    METHOD_NAME = "SearchContactCount";
                    NAMESPACE = "LMServiceNamespace";
                    query="lastname";
                }
                if(i==2)
                {
                    SOAP_ACTION = "LMServiceNamespace/SearchOpportunityCount";
                    METHOD_NAME = "SearchOpportunityCount";
                    NAMESPACE = "LMServiceNamespace";
                    query="oppname";
                }
                if(i==3)
                {
                    SOAP_ACTION = "LMServiceNamespace/SearchCasesCount";
                    METHOD_NAME = "SearchCasesCount";
                    NAMESPACE = "LMServiceNamespace";
                    query="subject";
                }
                if(i==4)
                {
                    SOAP_ACTION = "LMServiceNamespace/SearchQuoteCount";
                    METHOD_NAME = "SearchQuoteCount";
                    NAMESPACE = "LMServiceNamespace";
                    query="quotename";
                }

                try {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                    Request.addProperty("username", manager.getUserName(MainActivity.this));
                    Request.addProperty("pwd", manager.getUserPass(MainActivity.this));
                    Request.addProperty("company_id", manager.getWG(MainActivity.this));
                    Request.addProperty(query, "");

                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;
                    soapEnvelope.setOutputSoapObject(Request);

                    HttpTransportSE transport = new HttpTransportSE(URL,30000);

                    transport.call(SOAP_ACTION, soapEnvelope);
                    //list=new ArrayList();


                    resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                    if(i==0)
                    {
                        totalAccRcord=resultRequestSOAP.getProperty("SearchAccountCountResult").toString();
                    }
                    if(i==1)
                    {
                        totalConRcord=resultRequestSOAP.getProperty("SearchContactCountResult").toString();
                    }
                    if(i==2)
                    {
                        totalOppRcord=resultRequestSOAP.getProperty("SearchOpportunityCountResult").toString();
                    }
                    if(i==3)
                    {
                        totalCaseRecord=resultRequestSOAP.getProperty("SearchCasesCountResult").toString();
                    }
                    if(i==4)
                    {
                        totalQuoteRecord=resultRequestSOAP.getProperty("SearchQuoteCountResult").toString();
                    }

                    // resultString = (SoapPrimitive) soapEnvelope.getResponse();


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            Log.e("TotalRecord", ""+totalAccRcord+","+totalConRcord+","+totalOppRcord+","+totalCaseRecord);
            manager.setCounts(MainActivity.this,totalAccRcord,totalConRcord,totalOppRcord,totalCaseRecord,totalQuoteRecord);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Post","Execute");

//            login.getSettings().setLoadsImagesAutomatically(true);
//            login.getSettings().setJavaScriptEnabled(true);
//            login.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//            login.loadUrl(manager.getMainUrl(MainActivity.this) + "/mobile_auth.asp?key=" + encrptVal() + "&topage=mobile_frmwelcome.asp");

            pd.cancel();
            expListView.setAdapter(listAdapter);
        }
    }


    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(MainActivity.this);
        String pass=manager.getUserPass(MainActivity.this);
        String dbId=manager.getDB(MainActivity.this);
        String wgId=manager.getWG(MainActivity.this);


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

    public String getDate()
    {
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

}
