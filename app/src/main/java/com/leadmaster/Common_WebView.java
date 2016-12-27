package com.leadmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Common_WebView extends Fragment
{

    WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_common__web_view, container, false);

        Bundle bundle = this.getArguments();
        String url = bundle.getString("url");
        final String frag=bundle.getString("frg");

        int i=0,j=0;
        String SearchQuery = null;

        if(bundle != null)
    {
        if(bundle.containsKey("startIndex"))
        {
            i = bundle.getInt("startIndex");
        }

        if(bundle.containsKey("endIndex"))
        {
            j = bundle.getInt("endIndex");
        }

        if(bundle.containsKey("searchQuery"))
        {
            SearchQuery = bundle.getString("searchQuery");
        }
    }

//        int j=bundle.getInt("endIndex");
//        String SearchQuery = bundle.getString("searchQuery");


        Log.e("PassUrl",url);
        Log.e("Fragment",frag);
//        Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();

        if(frag.equals("map"))
        {
            final Fragment fragment = getChildFragmentManager().findFragmentById(R.id.map_display1);
            if(fragment!=null)
            {
                getChildFragmentManager().beginTransaction().remove(fragment).commit();
//                Fragment frag1 = new Map_Search_Activity();
//                getFragmentManager().beginTransaction().remove(frag1).commit();

            }
        }
        else{}
        if(frag.equals("logout"))
        {
            SessionManager manager=new SessionManager();
            manager.setPreferences(getActivity(), "status", "0");
            manager.LogOut(getActivity());

            webView=(WebView)view.findViewById(R.id.common_browser);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Intent i=new Intent(getActivity(),Login_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
//                    getActivity().finish();
                }
            });
        }
        else
        {
            webView=(WebView)view.findViewById(R.id.common_browser);
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
        }

//
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        final int finalI = i;
        final int finalJ = j;
        final String finalSearchQuery = SearchQuery;

        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("keyCode: ", ""+keyCode);
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
//                if( keyCode == KeyEvent.KEYCODE_BACK)
                {
                    Log.i("KEYCodeBack", "onKey Back listener is working!!!");
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    if (frag.equals("logout"))
                    {
                        Log.e("finish", "Called");
                        getActivity().finish();
                        return true;
                    }
                    else
                    {
                        Fragment fragment = null;

                        if (frag.equals("acc"))
                        {
                            fragment = new Accounts_Activity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("case"))
                        {
                            fragment = new Cases_Activity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("contact"))
                        {
                            fragment = new Contacts_Activity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("dash"))
                        {
                            fragment = new Dashboard_Activity();

//                        Bundle bundle = new Bundle();
//
//                        bundle.putInt("startIndex", finalI);
//                        bundle.putInt("endIndex", finalJ);
//                        bundle.putString("searchQuery", finalSearchQuery);
//
//                        fragment.setArguments(bundle);
                        }
                        else if (frag.equals("callback"))
                        {
                            fragment = new CallbackTask_Activity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("opp"))
                        {
                            fragment = new Oppotunity_Activity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("quote"))
                        {
                            fragment = new QuotesActivity();

                            Bundle bundle = new Bundle();

                            bundle.putInt("startIndex", finalI);
                            bundle.putInt("endIndex", finalJ);
                            bundle.putString("searchQuery", finalSearchQuery);

                            fragment.setArguments(bundle);
                        }
                        else if (frag.equals("quickSearch"))
                        {
                            fragment = new QuickSearch_Activity();
                        }
                        else if (frag.equals("checkForMatch"))
                        {
                            fragment = new CheckForMatch_Activity();
                        }
                        else if (frag.equals("advSearch"))
                        {
                            fragment = new Serches_Activity();
                        }
                        else if (frag.equals("recentItem"))
                        {
                            fragment = new RecentItems_Activity();
                        }
                        else if (frag.equals("mySearch"))
                        {
                            fragment = new MyShortcuts_Activity();
                        }
                        else if (frag.equals("lib"))
                        {
                            fragment = new Library_Activity();
                        }
                        else if (frag.equals("map"))
                        {
                            fragment = new Serches_Activity();

                        }

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        Log.e("Common frag", "" + fragmentManager.getBackStackEntryCount());
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment, null);
                        fragmentTransaction.commit();

//                    fragmentManager.executePendingTransactions();

                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
        });


        return view;
    }

    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        Fragment fragment = new MyShortcuts_Activity();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.containerView,fragment,null);
//        fragmentTransaction.commit();


    }
}
