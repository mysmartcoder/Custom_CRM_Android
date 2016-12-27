package com.customCRM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ChangePassword_Activity extends Fragment
{

    SessionManager manager;
    ProgressDialog pd;
    private Activity mActivity;

    EditText current_pass,new_pass,verify_pass;

    Button submitBtn;
//    ImageView cur_visible_btn,new_visible_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_change_password, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        manager=new SessionManager();

        current_pass=(EditText) view.findViewById(R.id.cur_password);
        new_pass=(EditText)view.findViewById(R.id.new_password);
        verify_pass=(EditText)view.findViewById(R.id.verify_password);

        submitBtn=(Button)view.findViewById(R.id.SUBMIT);

        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.change_pass_ll);
        toolbar.setBackgroundColor(manager.getColor(getActivity()));

        if(manager.getFontStyle(getActivity()).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(view.findViewById(R.id.change_pass_ll), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(view.findViewById(R.id.change_pass_ll), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(view.findViewById(R.id.change_pass_ll), mainFont);
        }
        else if(manager.getFontStyle(getActivity()).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getActivity().getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(view.findViewById(R.id.change_pass_ll), mainFont);
        }

//        cur_visible_btn=(ImageView)view.findViewById(R.id.cur_pass_vis);
//        new_visible_btn=(ImageView)view.findViewById(R.id.new_pass_vis);


        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("Password 1 ==>",manager.getUserPass(getActivity()));
                Log.e("Password 2 ==>",""+current_pass.getText().toString().equals(manager.getUserPass(getActivity())));

                if(! current_pass.getText().toString().equals(manager.getUserPass(getActivity())))
                {
                    current_pass.setError("Your Current Password is Wrong");
                }
                else if(new_pass.getText().toString().trim().length() == 0)
                {
                    new_pass.setError("Please enter password");
                }
                else if(verify_pass.getText().toString().trim().length() == 0)
                {
                    verify_pass.setError("Please verify password");
                }
                else if(! verify_pass.getText().toString().equals(new_pass.getText().toString()))
                {
                    verify_pass.setError("Wrong new password");
                }
                else
                {
//                    Toast.makeText(getActivity(),"Run",Toast.LENGTH_LONG).show();
                    Log.e("NewPassword",new_pass.getText().toString());
                    ChangePassword changePassword=new ChangePassword(new_pass.getText().toString());
                    changePassword.execute();
                }
            }
        });

//        cur_visible_btn.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                switch ( event.getAction() )
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        current_pass.setInputType(InputType.TYPE_CLASS_TEXT);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        current_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        break;
//                }
//
//                return true;
//            }
//        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
//                    if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    // handle back button's click listener

//                    getFragmentManager().popBackStack();
                    Fragment fragment = new Setting_Activity();
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

        return view;
    }


    private class ChangePassword extends AsyncTask<Void, Void, Void>
    {

        String success="";
        String newPass="";

        public ChangePassword(String s)
        {
            newPass=s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");
            pd=new ProgressDialog(mActivity);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapPrimitive soapPrimitive=null;
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/ChangePassword";
            String METHOD_NAME = "ChangePassword";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("password", newPass);
                Request.addProperty("LogonID", manager.getUserId(getActivity()));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL, 30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


//                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();
                soapPrimitive = (SoapPrimitive) soapEnvelope.getResponse();

                success = soapPrimitive.toString();
//                resultRequestSOAP.getProperty("ChangePasswordResult");

//                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//                {
//                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP.getProperty(i);
//
//                    success = TypeEventData.getProperty("ChangePasswordResult").toString();
//                }

//                int success = Integer.parseInt(resultRequestSOAP.getProperty("ChangePasswordResult").toString());
//                Log.e("1", "Result SECOND API: " + success);
//                 resultString = (SoapPrimitive) soapEnvelope.getResponse();

//                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//                {
//                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
//                    success = TypeEventData.getProperty("ChangePasswordResult").toString();
//                }
                Log.e("00000000", "Result SECOND API: " + success);
            }
            catch (Exception ex)
            {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("1", "Result SECOND API: " + success);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.cancel();
            if(success.equals("0"))
            {
                Toast.makeText(getActivity(),"Password not changed !",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Password successfully changed.",Toast.LENGTH_LONG).show();

                manager.setUser(getActivity(),manager.getUserName(getActivity()),new_pass.getText().toString(),manager.getUserId(getActivity()),manager.getUserName(getActivity()));

                Fragment fragment = new Setting_Activity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    getActivity().getSupportFragmentManager().popBackStack("gifPageTwoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Log.e("frag 3",""+fragmentManager.getBackStackEntryCount());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView,fragment,null);
                fragmentTransaction.commit();
            }

        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

}
