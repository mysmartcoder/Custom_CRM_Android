package com.customCRM;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CommonWebView extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */

    android.webkit.WebView web;
    int i = 0, j = 0;
    String SearchQuery = null;
    String titlebar = "";
    String frag = null;

    TextView backButton,titleDisplay;
   /* Button callback_footer, webhome_footer, webaccount_footer, webcontacts_footer,
            websetting_footer, webmyshortcut_footer, webrecentitems_footer,webdashboard_footer;
    String DatabaseIdIntent,WorkGroupIdIntent,LoginIntent;*/
    SessionManager manager;
//    String userNameSession,passSession;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        manager=new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(CommonWebView.this));
        }

        if(manager.getFontStyle(CommonWebView.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.cw_parent), mainFont);
        }
        else if(manager.getFontStyle(CommonWebView.this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.cw_parent), mainFont);
        }
        else if(manager.getFontStyle(CommonWebView.this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.cw_parent), mainFont);
        }
        else if(manager.getFontStyle(CommonWebView.this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.cw_parent), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.backButtonLL), iconFont);

        backButton=(TextView)findViewById(R.id.backBtn_webview);

        titleDisplay=(TextView)findViewById(R.id.webview_toolbar);
        titleDisplay.setBackgroundColor(manager.getColor(CommonWebView.this));

        Toolbar toolbar = (Toolbar)findViewById(R.id.cw_toolbar);
        toolbar.setBackgroundColor(manager.getColor(CommonWebView.this));

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        /*manager.setToday(CommonWebView.this,getDate());
        Log.e("Today ==>",manager.getToday(CommonWebView.this));
//        Intent i=getIntent();
        LoginIntent=manager.getUserId(CommonWebView.this);
        DatabaseIdIntent=manager.getDB(CommonWebView.this);
        WorkGroupIdIntent=manager.getWG(CommonWebView.this);

//        manager = new SessionManager();
        userNameSession=manager.getUserName(CommonWebView.this);
        passSession=manager.getUserPass(CommonWebView.this);

        Log.e("Info==>","LoginId= "+LoginIntent+" DatabaseId= "+DatabaseIdIntent+" WorkGroupId= "+WorkGroupIdIntent+" User=>"+userNameSession+" Pass=>"+passSession);



        websetting_footer =(Button)findViewById(R.id.websetting_footer);
//        callback_footer=(Button)findViewById(R.id.callback_footer);
        webhome_footer =(Button)findViewById(R.id.webhome_footer);
        webaccount_footer =(Button)findViewById(R.id.webaccount_footer);
        webcontacts_footer =(Button)findViewById(R.id.webcontacts_footer);
        webmyshortcut_footer =(Button)findViewById(R.id.webmyshortcut_footer);
        webrecentitems_footer =(Button)findViewById(R.id.webrecentitem_footer);
        webdashboard_footer=(Button)findViewById(R.id.webdashboard_footer);

        webdashboard_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encp = encrptVal();
                String url = manager.getMainUrl(CommonWebView.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_dashboard.asp&screenwidth=";

//                    var url = url_main + "/mobile_auth.asp?key=" + encrptVal + "&topage=mobile_dashboard.asp&screenwidth="+  screenwidth+"";
                Log.e("Dashboard", url);

                Fragment webviewFragment = new Common_WebView();

                Bundle bundle = new Bundle();

                bundle.putString("url", url);
                bundle.putString("frg","dash");

                webviewFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containerView,webviewFragment,null);
                fragmentTransaction.commit();

                Intent intent = new Intent(CommonWebView.this, Dashboard_Activity.class);
                intent.putExtra("url", url);
                intent.putExtra("frg", "dash");

                startActivity(intent);

                web = (android.webkit.CommonWebView) findViewById(R.id.webview);
                web.setWebViewClient(new myWebClient());
                web.getSettings().setLoadsImagesAutomatically(true);
                web.getSettings().setJavaScriptEnabled(true);
                web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                web.loadUrl(url);

            }
        });

        websetting_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Original

                Setting_Activity settingsFragment = new Setting_Activity();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, settingsFragment, null);
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

        webhome_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                Fragment dashFragment = new Dashboard_Activity();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, dashFragment, null);
                fragmentTransaction.commit();

//                Intent i=new Intent(getApplicationContext(),CallbackTask_Activity.class);
//                startActivity(i);
            }
        });

        webaccount_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getLoginPriv(CommonWebView.this, "AccountLink").equals("true")) {
                    Log.e("true", manager.getLoginPriv(CommonWebView.this, "AccountLink"));
                    Fragment accountsFragment = new Accounts_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, accountsFragment, null);
                    fragmentTransaction.commit();
                } else {
                    Log.e("false", manager.getLoginPriv(CommonWebView.this, "AccountLink"));
                    Fragment accountsFragment = new NotAuthorized();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, accountsFragment, null);
                    fragmentTransaction.commit();
                }


//                Intent i=new Intent(getApplicationContext(),Accounts_Activity.class);
//                startActivity(i);
            }
        });

        webcontacts_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                if (manager.getLoginPriv(CommonWebView.this, "ContactLink").equals("true")) {
                    Log.e("true", manager.getLoginPriv(CommonWebView.this, "ContactLink"));
                    Fragment contactsFragment = new Contacts_Activity();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, contactsFragment, null);
                    fragmentTransaction.commit();
                } else {
                    Log.e("false", manager.getLoginPriv(CommonWebView.this, "ContactLink"));
                    Fragment accountsFragment = new NotAuthorized();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, accountsFragment, null);
                    fragmentTransaction.commit();
                }
//                Intent i=new Intent(getApplicationContext(),Contacts_Activity.class);
//                startActivity(i);
            }
        });

        webmyshortcut_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Original
                Fragment accountsFragment = new MyShortcuts_Activity();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Log.e("Main frag : ", "" + fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, accountsFragment, null);
                fragmentTransaction.commit();

            }
        });

        webrecentitems_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//original
                Fragment recentItemFragment = new RecentItems_Activity();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, recentItemFragment, null);
                fragmentTransaction.commit();
            }
        });
*/
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        frag = bundle.getString("frg");



        if(bundle != null) {

            if (bundle.containsKey("startIndex")) {
                i = bundle.getInt("startIndex");
            }

            if (bundle.containsKey("endIndex")) {
                j = bundle.getInt("endIndex");
            }

            if (bundle.containsKey("searchQuery")) {
                SearchQuery = bundle.getString("searchQuery");

            }

            if (bundle.containsKey("titlebar")) {
                titlebar = bundle.getString("titlebar");
                Log.e("titlebarString",titlebar);
            }
        }
        Log.e("PassUrl", url);
        Log.e("Fragment", frag);
//        Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();

       /* if (frag.equals("map")) {
            final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.map_display1);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Fragment frag1 = new Map_Search_Activity();
                getFragmentManager().beginTransaction().remove(frag1).commit();
            }
        } else {
        }*/
        if (frag.equals("logout"))
        {
            backButton.setVisibility(View.GONE);

            SessionManager manager = new SessionManager();
            manager.setPreferences(CommonWebView.this, "status", "0");
            manager.LogOut(CommonWebView.this);

            web = (android.webkit.WebView)findViewById(R.id.webview);
            web.loadUrl(url);
            web.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(android.webkit.WebView view, String url)
                {
                    super.onPageFinished(view, url);
                    Intent i = new Intent(CommonWebView.this, Login_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });
        }
        else
        {

            web = (android.webkit.WebView) findViewById(R.id.webview);
            web.setWebViewClient(new myWebClient());
            web.getSettings().setLoadsImagesAutomatically(true);
            web.getSettings().setJavaScriptEnabled(true);
            web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            web.loadUrl(url);
        }

        if(! titlebar.equals(""))
        {
            if(titlebar.equals("record"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Record"));
            }
            if(titlebar.equals("contact"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Banner - Contacts"));
            }
            if(titlebar.equals("opp"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Opportunity"));
            }
            if(titlebar.equals("quote"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Quote"));
            }
            if(titlebar.equals("note"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Sales Rep Comments/Notes"));
            }
            if(titlebar.equals("event"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Event"));
            }
            if(titlebar.equals("case"))
            {
                titleDisplay.setVisibility(View.VISIBLE);
                titleDisplay.setText("Add new "+manager.getCustomLabel(CommonWebView.this,"Case"));
            }
        }
        else
        {
            titleDisplay.setVisibility(View.GONE);
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    final int finalI = i;
    final int finalJ = j;
    final String finalSearchQuery = SearchQuery;

    // To handle "Back" key press event for CommonWebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        else {
                if(frag.equals("logout"))
                {
                    ActivityCompat.finishAffinity(this);
                }

            return super.onKeyDown(keyCode, event);
        }
    }
  /*  private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(CommonWebView.this);
        String pass=manager.getUserPass(CommonWebView.this);
        String dbId=manager.getDB(CommonWebView.this);
        String wgId=manager.getWG(CommonWebView.this);


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
    }*/

}
