package com.customCRM;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Setting_Activity extends Fragment
{
//    Button changePassBtn,changeUrlBtn,changeBGColor,changeFontStyle,changeFontSize;
    LinearLayout changePassBtn,changeUrlBtn,changeFontStyle,changeFontSize,sync_contact_parent;
    LinearLayout changeBGColor,change_rate_parent,change_share_parent,change_feedback_parent,logout_setting;

    Button color1,color2,color3,color4,color5,color6;

    SessionManager manager;
    String selection,fontselection;
    private Activity mActivity;

    ProgressDialog pd;

//    TextView bg;
    private int mSelectedColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_setting, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager= new SessionManager();

        mSelectedColor = ContextCompat.getColor(getActivity(), R.color.default_color);
        mSelectedColor = manager.getColor(getActivity());

        TextView nav_UserName=(TextView)view.findViewById(R.id.UserName_Nav_setting);
        TextView nav_UserSym=(TextView)view.findViewById(R.id.nav_user_symbol_setting);

        GradientDrawable bgShape = (GradientDrawable)nav_UserSym.getBackground();
        bgShape.setColor(manager.getColor(getActivity()));

        nav_UserName.setText( manager.getUserFullName(getActivity()).substring(0,1).toUpperCase() + manager.getUserFullName(getActivity()).substring(1) );
        nav_UserSym.setText(manager.getUserFullName(getActivity()).substring(0,1).toUpperCase());



        color1=(Button)view.findViewById(R.id.btn1);
        color2=(Button)view.findViewById(R.id.btn2);
        color3=(Button)view.findViewById(R.id.btn3);
        color4=(Button)view.findViewById(R.id.btn4);
        color5=(Button)view.findViewById(R.id.btn5);
        color6=(Button)view.findViewById(R.id.btn6);

        changePassBtn=(LinearLayout) view.findViewById(R.id.change_pass_parent);
        changeUrlBtn=(LinearLayout)view.findViewById(R.id.change_url_parent);
        changeBGColor=(LinearLayout)view.findViewById(R.id.change_back_parent);
        changeFontStyle=(LinearLayout)view.findViewById(R.id.change_font_parent);
        change_rate_parent=(LinearLayout)view.findViewById(R.id.change_rate_parent);
        change_share_parent=(LinearLayout)view.findViewById(R.id.change_share_parent);
        change_feedback_parent=(LinearLayout)view.findViewById(R.id.change_feedback_parent);
        logout_setting=(LinearLayout)view.findViewById(R.id.logout_setting);
        sync_contact_parent=(LinearLayout)view.findViewById(R.id.sync_contact_parent);

        changeBGColor.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.setting_layout), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.setting_layout), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.setting_layout), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.setting_layout), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_back_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_font_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_url_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_pass_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_rate_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.change_share_parent), iconFont);
        FontManager.markAsIconContainer(view.findViewById(R.id.logout_setting), iconFont);

        ImageView imageViewIcon = (ImageView) view.findViewById(R.id.back_setting_parent);
        imageViewIcon.setColorFilter(Color.parseColor("#EDAD41"));

        ImageView feedbackIcon = (ImageView) view.findViewById(R.id.feedback_icon);
        feedbackIcon.setColorFilter(Color.parseColor("#138FDC"));

        ImageView sync_contact_icon = (ImageView) view.findViewById(R.id.sync_contact_icon);
        sync_contact_icon.setColorFilter(Color.parseColor("#1565C0"));
//        imageViewIcon.setColorFilter(getContext().getResources().getColor(R.color.blue));

        changePassBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment changePassword_activity = new ChangePassword_Activity();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,changePassword_activity,null);
                fragmentTransaction.commit();
            }
        });

        changeFontStyle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_font_style_dialog);

                final Spinner url_spinner=(Spinner)dialog.findViewById(R.id.setting_fontstyle_list);
                Button dialogButton_Cancel = (Button) dialog.findViewById(R.id.setting_fontstyle_Cancel);
                Button dialogButton_OK = (Button) dialog.findViewById(R.id.setting_fontstyle_OK);

                // Spinner Drop down elements
                final List<String> categories = new ArrayList<String>();
//                categories.add("Default");
//                categories.add("open-sans");
//                categories.add("pt-sans");
//                categories.add("Lora-Regular");
//                categories.add("DroidSerif-Regular");
                categories.add("customCRM - Default");
                categories.add("customCRM - Open-sans");
                categories.add("customCRM - PT-sans");
                categories.add("customCRM - Lora-Regular");
                categories.add("customCRM - DroidSerif-Regular");

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout_2,R.id.spinnerText,categories)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {

                        LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
                        View row=inflater.inflate(R.layout.spinner_layout_2, parent, false);
                        TextView label=(TextView)row.findViewById(R.id.spinnerText);
                        label.setText(categories.get(position));

                        if(position==4)
                        {
                            Log.e("font","1");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==1)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==2)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==3)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }

                        return row;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent)
                    {
                        LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
                        View row=inflater.inflate(R.layout.spinner_layout_2, parent, false);
                        TextView label=(TextView)row.findViewById(R.id.spinnerText);
                        label.setText(categories.get(position));

                        if(position==4)
                        {
                            Log.e("font","1");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==1)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==2)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }
                        else if(position==3)
                        {
                            Log.e("font","2");
                            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
                            CustomFont.markAsIconContainer(row.findViewById(R.id.spinnerText), mainFont);
                        }


                        return row;
                    }
                };

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                url_spinner.setAdapter(dataAdapter);
//                categories.add("Default");
//                categories.add("open-sans");
//                categories.add("pt-sans");
//                categories.add("Lora-Regular");
//                categories.add("DroidSerif-Regular");
                if(manager.getFontStyle(getActivity()).equals("Default"))
                {
                    url_spinner.setSelection(0);
                    fontselection=manager.getFontStyle(getActivity());
                }
                if(manager.getFontStyle(getActivity()).equals("open-sans"))
                {
                    url_spinner.setSelection(1);
                    fontselection=manager.getFontStyle(getActivity());
                }
                if(manager.getFontStyle(getActivity()).equals("pt-sans"))
                {
                    url_spinner.setSelection(2);
                    fontselection=manager.getFontStyle(getActivity());
                }
                if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
                {
                    url_spinner.setSelection(3);
                    fontselection=manager.getFontStyle(getActivity());
                }
                if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
                {
                    url_spinner.setSelection(4);
                    fontselection=manager.getFontStyle(getActivity());
                }
//                for (int i=0;i<url_spinner.getCount();i++)
//                {
//                    if (url_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(manager.getFontStyle(getActivity())))
//                    {
//                        url_spinner.setSelection(i);
//                        fontselection=url_spinner.getItemAtPosition(i).toString();
//                        break;
//                    }
//                }

                url_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if(position==0)
                        {
                            fontselection ="Default";
                        }
                        if(position==1)
                        {
                            fontselection ="open-sans";
                        }
                        if(position==2)
                        {
                            fontselection ="pt-sans";
                        }
                        if(position==3)
                        {
                            fontselection ="Lora-Regular";
                        }
                        if(position==4)
                        {
                            fontselection ="DroidSerif-Regular";
                        }
//                        fontselection =parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                dialogButton_OK.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.e("fontelection",fontselection);
                        manager.setFontStyle(getActivity(),fontselection);
                        Toast.makeText(getActivity(),"Successfully font changed",Toast.LENGTH_LONG).show();

                        dialog.dismiss();

                        getActivity().recreate();
                    }
                });

                dialogButton_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        changeUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_url_alert_dialog);

                final EditText otherUrl=(EditText)dialog.findViewById(R.id.setting_url_other);
                final Spinner url_spinner=(Spinner)dialog.findViewById(R.id.setting_url_list);
                Button dialogButton_Cancel = (Button) dialog.findViewById(R.id.setting_url_Cancel);
                Button dialogButton_OK = (Button) dialog.findViewById(R.id.setting_url_OK);

                for (int i=0;i<url_spinner.getCount();i++)
                {
                    if (url_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(manager.getMainUrl(getActivity())))
                    {
                        url_spinner.setSelection(i);
                        selection=url_spinner.getItemAtPosition(i).toString();
                        otherUrl.setVisibility(View.GONE);
                        break;
                    }
                    else
                    {
                        Log.e("call",""+i+","+url_spinner.getItemAtPosition(i).toString());
                        url_spinner.setSelection(i);
//                        url_spinner.setSelection(url_spinner.get);
                        selection=url_spinner.getItemAtPosition(i).toString();
                        otherUrl.setVisibility(View.VISIBLE);
                        otherUrl.setText(manager.getMainUrl(getActivity()));
                    }
                }

                url_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        selection =parent.getItemAtPosition(position).toString();
                        if(selection.equals("Other"))
                        {
                            otherUrl.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            otherUrl.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
//                        selection =parent.getItemAtPosition(position).toString();
                        if(selection.equals("Other"))
                        {
                            otherUrl.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            otherUrl.setVisibility(View.GONE);
                        }
                    }
                });

                dialogButton_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String userSelection = null;

                        if(otherUrl.getVisibility()==View.VISIBLE)
                        {
                            if(otherUrl.getText().toString().trim().equals(""))
                            {
                                Toast.makeText(getActivity(),"Please enter url",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                userSelection=otherUrl.getText().toString();
                            }
                        }
                        else
                        {
                            userSelection=url_spinner.getSelectedItem().toString();
                            Log.e("Selection",url_spinner.getSelectedItem().toString());
                        }

                        manager.set_setting_MAINURL(getActivity(),userSelection);
                        Toast.makeText(getActivity(),"Successfully url changed",Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });

                dialogButton_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        color1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col1));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col1));
                getActivity().recreate();
            }
        });
        color2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col2));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col2));
                getActivity().recreate();
            }
        });
        color3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col3));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col3));
                getActivity().recreate();
            }
        });
        color4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col4));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col4));
                getActivity().recreate();
            }
        });
        color5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col5));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col5));
                getActivity().recreate();
            }
        });
        color6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                manager.setColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.col6));
                manager.setStatusColor(getActivity(), ContextCompat.getColor(getActivity(),R.color.s_col6));
                getActivity().recreate();
                /*Fragment settingsFragment = new Setting_Activity();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,settingsFragment,null);
                fragmentTransaction.commit();*/
            }
        });

        /*changeBGColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        mColors,
                        mSelectedColor,
                        3, // Number of columns
                        ColorPickerDialog.SIZE_SMALL);

                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color)
                    {
                        Log.e("ColorInt",""+color);
                        mSelectedColor = color;
                        manager.setColor(getActivity(),color);
                        getActivity().recreate();

                    }

                });

                dialog.show(getActivity().getFragmentManager(), "color_dialog_test");
            }
        });
*/
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK )
//                    if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    // handle back button's click listener

//                    getFragmentManager().popBackStack();
                    Fragment fragment = new Dashboard_Activity();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,fragment,null);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });

        logout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                manager.setPreferences(getActivity(), "status", "0");
                manager.setPreferences(getActivity(), "firstTime", "0");
                manager.LogOut(getActivity());

                String encp=encrptVal();
                String logout_url = manager.getMainUrl(getActivity()) + "/mobile_auth.asp?key=" + encp + "&topage=index_logoff.asp";

                Intent intent = new Intent(getActivity(), CommonWebView.class);
                intent.putExtra("url", logout_url);
                intent.putExtra("frg", "logout");

                startActivity(intent);
            }
        });

        change_rate_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String myUrl ="https://play.google.com/store/apps/details?id=com.customCRM&hl=en";

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl)));
            }
        });

        change_share_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "LeadMaterCRM Android Application");
                    String sAux = "\nLet me recommend you customCRM android application : \n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.customCRM&hl=en \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share customCRM"));
                }
                catch(Exception e) {
                    //e.toString();
                }
            }
        });

        change_feedback_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getActivity(),SendFeedback.class);
                startActivity(i);
            }
        });

        sync_contact_parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getActivity(),Sync_Contact_Activity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(getActivity());
        String pass=manager.getUserPass(getActivity());
        String dbId=manager.getDB(getActivity());
        String wgId=manager.getWG(getActivity());


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
