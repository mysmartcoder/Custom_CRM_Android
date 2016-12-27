package com.leadmaster;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class SendFeedback extends AppCompatActivity
{
    TextView backButton;
    LinearLayout change_feedback_parent;
    Button send_feedback_btn;
    SessionManager manager;
    EditText feedback_msg,feedback_title;

    String subjectPass,ratingMsg="\n\n\n\nRating : ",feedbackMsg,feedbackTitle;

    RadioButton bugRadio,featureRadio,otherRadio;

    RatingBar ratingBar;
//    EditText feedback_email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        manager = new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(SendFeedback.this));
        }

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.backBtn_feedback), iconFont);

        bugRadio=(RadioButton)findViewById(R.id.bugRadio);
        featureRadio=(RadioButton)findViewById(R.id.featureRadio);
        otherRadio=(RadioButton)findViewById(R.id.otherRadio);

        backButton=(TextView)findViewById(R.id.backBtn_feedback);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        change_feedback_parent=(LinearLayout)findViewById(R.id.change_feedback_parent);
        change_feedback_parent.setBackgroundColor(manager.getColor(SendFeedback.this));

        ratingBar=(RatingBar)findViewById(R.id.rating_setting);

//        feedback_email=(EditText)findViewById(R.id.feedback_email);
        feedback_msg=(EditText)findViewById(R.id.feedback_msg);
        feedback_title=(EditText)findViewById(R.id.feedback_title);

       /* if(UserEmailFetcher.getEmail(SendFeedback.this) != null)
        {
            Log.e("EmailId",UserEmailFetcher.getEmail(SendFeedback.this));
            feedback_email.setText(UserEmailFetcher.getEmail(SendFeedback.this));
        }*/

        send_feedback_btn=(Button)findViewById(R.id.send_feedback_btn);
        send_feedback_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                feedbackMsg=feedback_msg.getText().toString();
                if(ratingBar.getRating()!=0)
                {
                    ratingMsg+=" "+ratingBar.getRating();
                    feedbackMsg+=ratingMsg;
                }
                if(bugRadio.isChecked())
                {
                    subjectPass="[Bug Report] : ";
                    feedbackTitle=subjectPass+feedback_title.getText().toString();
                }
                else if(featureRadio.isChecked())
                {
                    subjectPass="[Feature Report] : ";
                    feedbackTitle=subjectPass+feedback_title.getText().toString();
                }
                else if(otherRadio.isChecked())
                {
                    subjectPass="[Feedback Report] : ";
                    feedbackTitle=subjectPass+feedback_title.getText().toString();
                }
                if(feedback_title.getText().toString().trim().equals(""))
                {
                    feedback_title.setError("Describe your issue");
                }
                else if(feedback_msg.getText().toString().trim().equals(""))
                {
                    feedback_msg.setError("You need to write a description");
                }
                else
                {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ramij.variance@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, feedbackTitle);
                    i.putExtra(Intent.EXTRA_TEXT   , feedbackMsg);
                    try
                    {
                        startActivity(Intent.createChooser(i, "Send Feedback!"));
                        recreate();
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        Toast.makeText(SendFeedback.this, "There are no email clients installed...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target)
    {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
