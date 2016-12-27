package com.customCRM;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.customCRM.custom_calendar.CalendarListener;
import com.customCRM.custom_calendar.CustomCalendarView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GroupCalendar_Activity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener
{
    Button btnmonth, btnweek, btnday;

    String[] Rec_ID,Callback_ID;

    ImageView ivNext;
    ImageView ivPrevious;

    TextView tvMonth;

    CustomCalendarView mCustomCalendarView;

    Calendar mCalendarCurrent;

    WeekView mWeekView;

    String mStartTime = "";
    String mEndTime = "";

    LinearLayout llMonthView;

    long lastTime = 0;

    List<WeekViewEvent> eventsList;
    List<WeekViewEvent> eventsListFilter;
//    List<WeekViewEvent> Rec_Id;

    AppointmentAdapter mAppointmentAdapter;
    ListView lvcal;

    TextView warningDisplay;

    ProgressDialog pd;

    String demoMonth="";


    Button currentDateBtn;

    TextView backButton;

    SessionManager manager;

    ImageView setting;


    String viewSelection;

    ArrayList userList;

    Dialog dialog;
    String UserIdSt,DateViewSt,GCSessionSt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        manager = new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(GroupCalendar_Activity.this));
        }

        userList=new ArrayList();

        if(manager.getFontStyle(GroupCalendar_Activity.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_groupCal_month_view), mainFont);
        }
        else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_groupCal_month_view), mainFont);
        }
        else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_groupCal_month_view), mainFont);
        }
        else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.groupCal_linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_groupCal_month_view), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.groupCalLL), iconFont);

        Toolbar toolbar = (Toolbar) findViewById(R.id.groupcalendar_toolbar);
        toolbar.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));


        backButton=(TextView)findViewById(R.id.backBtn_groupCal);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        setting=(ImageView)findViewById(R.id.groupCal_setting);

        setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog = new Dialog(GroupCalendar_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_groupcal_settings);

                GetUsers getUsers=new GetUsers();
                getUsers.execute();

                Button dialogButton_Cancel = (Button) dialog.findViewById(R.id.groupCal_setting_Cancel);
                Button dialogButton_Clear = (Button) dialog.findViewById(R.id.groupCal_setting_CLEAR);

                if(manager.getGroupCalUser(GroupCalendar_Activity.this).equals(""))
                {
                    Log.e("Cancel","If");
                    dialogButton_Clear.setVisibility(View.GONE);
                }
                else
                {
                    Log.e("Cancel","Else");
                    dialogButton_Clear.setVisibility(View.VISIBLE);
                }

                final Button dialogButton_OK = (Button) dialog.findViewById(R.id.groupCal_setting_OK);


                final TextView displaySession=(TextView)dialog.findViewById(R.id.select_session_display);

                final Spinner groupCal_view=(Spinner) dialog.findViewById(R.id.groupCal_view_spinner);
                final Spinner groupCal_user=(Spinner) dialog.findViewById(R.id.groupCal_user);
                final Spinner groupCal_session=(Spinner) dialog.findViewById(R.id.groupCal_session);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupCalendar_Activity.this, android.R.layout.simple_spinner_item, userList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupCal_user.setAdapter(dataAdapter);

                if(! manager.getGroupCalUser(GroupCalendar_Activity.this).equals(""))
                {
                    for (int i = 0; i < groupCal_view.getCount(); i++) {
                        Log.e("groupCalView", groupCal_view.getItemAtPosition(i).toString());
                        if (groupCal_view.getItemAtPosition(i).toString().equalsIgnoreCase(manager.getGroupCalDateView(GroupCalendar_Activity.this)))
                        {
                            Log.e("groupCalView", groupCal_view.getItemAtPosition(i).toString());
                            groupCal_view.setSelection(i);
//                            break;
                        }
                    }
                }

                groupCal_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        Log.e("Selected",groupCal_view.getSelectedItem().toString());
                        if(groupCal_view.getSelectedItem().toString().equals("Week"))
                        {
                            GCSessionSt=parent.getSelectedItem().toString();
                            GCSessionSt=GCSessionSt.substring(8);
                            Log.e("Week",GCSessionSt);
                        }
                        else if(groupCal_view.getSelectedItem().equals("Month"))
                        {
                            Month_Data data= (Month_Data) parent.getSelectedItem();
                            GCSessionSt=data.getMonthId().toString();
                            Log.e("MonthId",GCSessionSt);
                        }
                        else
                        {
                            GCSessionSt=parent.getSelectedItem().toString();
                        }
                        Log.e("Session",GCSessionSt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });

                groupCal_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        GroupCalendarUser_Data data = (GroupCalendarUser_Data) parent.getSelectedItem();
                        UserIdSt=data.getLogonId();
                        Log.e("UserId",UserIdSt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                groupCal_view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        viewSelection = parent.getItemAtPosition(position).toString();

                        if(viewSelection.equals("Day"))
                        {
//                            Toast.makeText(getApplicationContext(),"Day",Toast.LENGTH_LONG).show();

                            DateViewSt="day";

                            displaySession.setVisibility(View.VISIBLE);
                            groupCal_session.setVisibility(View.VISIBLE);
                            dialogButton_OK.setEnabled(true);
                            dialogButton_OK.setVisibility(View.VISIBLE);
//                            dialogButton_Cancel.setEnabled(true);

                            List<String> day_container = new ArrayList<String>();

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                            for (int i = 0; i < 7; i++)
                            {
                                Log.e("dateTag", sdf.format(cal.getTime()));
                                day_container.add(sdf.format(cal.getTime()));
                                cal.add(Calendar.DAY_OF_WEEK, 1);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupCalendar_Activity.this, android.R.layout.simple_spinner_item, day_container);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            groupCal_session.setAdapter(dataAdapter);

                            Calendar compareDate=Calendar.getInstance();
                            Log.e("Today",sdf.format(compareDate.getTime()));

                            if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("day"))
                            {
                                for(int i=0;i<day_container.size();i++)
                                {
                                    if(day_container.get(i).equals(manager.getGroupCalSession(GroupCalendar_Activity.this)))
                                    {
                                        groupCal_session.setSelection(i);
                                    }
                                }
                            }
                            else
                            {
                                for(int i=0;i<day_container.size();i++)
                                {
                                    Log.e("Today",i+":"+day_container.get(i));
                                    if(day_container.get(i).equals(sdf.format(compareDate.getTime())))
                                    {
                                        groupCal_session.setSelection(i);
                                    }
                                }
                            }
                        }

                        if(viewSelection.equals("Week"))
                        {
                            DateViewSt="week";

                            displaySession.setVisibility(View.VISIBLE);
                            groupCal_session.setVisibility(View.VISIBLE);
                            dialogButton_OK.setEnabled(true);
                            dialogButton_OK.setVisibility(View.VISIBLE);
                            
                            List<String> week_container = new ArrayList<String>();

                            Calendar c=Calendar.getInstance();
                            c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                            DateFormat df=new SimpleDateFormat("MM-dd-yyyy");

                            c.add(Calendar.DATE,-49);

                            for(int i=0;i<15;i++)
                            {
                                week_container.add("Week of "+df.format(c.getTime()));
                                c.add(Calendar.DATE,7);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupCalendar_Activity.this, android.R.layout.simple_spinner_item, week_container);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            groupCal_session.setAdapter(dataAdapter);

                            if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("week"))
                            {
                                for(int i=0;i<week_container.size();i++)
                                {
                                    if(week_container.get(i).substring(8).equals(manager.getGroupCalSession(GroupCalendar_Activity.this)))
                                    {
                                        groupCal_session.setSelection(i);
                                    }
                                }
                            }
                            else
                            {
                                groupCal_session.setSelection(7);
                            }
                        }
                        if(viewSelection.equals("Month"))
                        {

                            DateViewSt="month";
                            displaySession.setVisibility(View.VISIBLE);
                            groupCal_session.setVisibility(View.VISIBLE);
                            dialogButton_OK.setEnabled(true);
                            dialogButton_OK.setVisibility(View.VISIBLE);

                            ArrayList month_container = new ArrayList();

                            Calendar cal = Calendar.getInstance();
                            Calendar calForMonth = Calendar.getInstance();

                            cal.add(Calendar.MONTH,-1);
                            calForMonth.add(Calendar.MONTH,-1);
                            for(int i=0;i<3;i++)
                            {
                                Month_Data month_data=new Month_Data();

                                DateFormat df=new SimpleDateFormat("MM-dd-yyyy");
                                calForMonth.set(Calendar.DAY_OF_MONTH, calForMonth.getActualMinimum(Calendar.DAY_OF_MONTH));

                                Log.e("Month",cal.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.US));
//                                Log.e("Month",String.valueOf(calForMonth.get(Calendar.MONTH)+1));
                                Log.e("MonthYear",String.valueOf(df.format(calForMonth.getTime())));

                                month_data.setMonthName(cal.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.US));
                                month_data.setMonthId(String.valueOf(df.format(calForMonth.getTime())));

                                cal.add(Calendar.MONTH,+1);
                                calForMonth.add(Calendar.MONTH,+1);

                                month_container.add(month_data);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupCalendar_Activity.this, android.R.layout.simple_spinner_item, month_container);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            groupCal_session.setAdapter(dataAdapter);


                            if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("month"))
                            {
                                for(int i=0;i<groupCal_session.getCount();i++)
                                {
                                    Month_Data data2 = (Month_Data)groupCal_session.getItemAtPosition(i);
                                    Log.e("Month",data2.getMonthName());
                                    if (data2.getMonthId().equals(manager.getGroupCalSession(GroupCalendar_Activity.this)))
                                    {
                                        Log.e("Month",data2.getMonthName());
                                        groupCal_session.setSelection(i);
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                groupCal_session.setSelection(1);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });

                dialogButton_OK.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        manager.setGoupUserData(GroupCalendar_Activity.this,DateViewSt,UserIdSt,GCSessionSt);
                        dialog.dismiss();
                        recreate();
                    }
                });

                dialogButton_Clear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.e("Cancel","Called");
                        manager.clearGroupCal(GroupCalendar_Activity.this);
                        dialog.dismiss();
                        recreate();
                    }
                });

                dialogButton_Cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        currentDateBtn=(Button)findViewById(R.id.groupCal_current_date_btn);

        btnmonth = (Button) findViewById(R.id.groupCal_btnmonth);
        btnmonth.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));

        btnweek = (Button) findViewById(R.id.groupCal_btnweek);

        btnday = (Button) findViewById(R.id.groupCal_btnday);

        lvcal = (ListView) findViewById(R.id.groupCal_listview);
        warningDisplay=(TextView)findViewById(R.id.Display_GroupCalendar_Warning);

        llMonthView=(LinearLayout)findViewById(R.id.linearlayout_groupCal_month_view);

        mCustomCalendarView = (CustomCalendarView) findViewById(R.id.groupCal_view);

        View divider = (View)findViewById(R.id.Divider_GroupCal);
        divider.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));

        tvMonth=(TextView)findViewById(R.id.groupCal_calendar_date_display);

        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM yyyy");
        tvMonth.setText(timeFormat.format(new Date()));

        GradientDrawable bgShape = (GradientDrawable)currentDateBtn.getBackground();
        bgShape.setColor(manager.getColor(GroupCalendar_Activity.this));

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
        currentDateBtn.setText(currentDateFormat.format(new Date()));

        currentDateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                recreate();
            }
        });

        getDateRange(new Date());

        mWeekView = (WeekView)findViewById(R.id.groupCal_weekView);

        ivNext=(ImageView)findViewById(R.id.groupCal_calendar_next_button);
        ivPrevious=(ImageView)findViewById(R.id.groupCal_calendar_prev_button);

//        if(manager.getGroupCalUser(GroupCalendar_Activity.this).equals(""))
//        {
            mCalendarCurrent = Calendar.getInstance();
//        }
        /*else
        {
            Log.e("Date",manager.getGroupCalSession(GroupCalendar_Activity.this));
            String datePart[]=manager.getGroupCalSession(GroupCalendar_Activity.this).split("-");
        *//*Risk*//*
            mCalendarCurrent = Calendar.getInstance();
            mCalendarCurrent.set(Calendar.MONTH, Integer.parseInt(datePart[0])-1);
            mCalendarCurrent.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datePart[1]));
            mCalendarCurrent.set(Calendar.YEAR, Integer.parseInt(datePart[2]));
        *//**//*
            Log.e("DatePart",datePart.length+"");
            Log.e("DatePart Month",Integer.parseInt(datePart[0])-1+"");
            Log.e("DatePart Date",datePart[1]);
            Log.e("DatePart Year",datePart[2]);

        }*/

        setupDateTimeInterpreter(false);

        ivNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCustomCalendarView.nextMonth();
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCustomCalendarView.previousMonth();
            }
        });

        mCustomCalendarView.setCalendarListener(new CalendarListener()
        {
            @Override
            public void onDateSelected(Date date)
            {
                if(Rec_ID == null || Rec_ID.length==0)
                {
//                    Toast.makeText(MyCalendar_Activity.this,"No any Event available...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    filterAppointmentByDate(date);
                }

            }

            @Override
            public void onMonthChanged(Date date)
            {
                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM yyyy");
                tvMonth.setText(timeFormat.format(date));
                mCalendarCurrent.setTimeInMillis(date.getTime());
                Log.e("Called","Month");
                mWeekView.goToDate(mCalendarCurrent);
                getDateRange(date);
            }
        });

        btnmonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                llMonthView.setVisibility(View.VISIBLE);
                mWeekView.setVisibility(View.GONE);

                btnmonth.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                btnmonth.setTextColor(Color.parseColor("#ffffff"));

                btnweek.setBackgroundColor(Color.parseColor("#ffffff"));
                btnweek.setTextColor(Color.parseColor("#000000"));

                btnday.setBackgroundColor(Color.parseColor("#ffffff"));
                btnday.setTextColor(Color.parseColor("#000000"));

                mCustomCalendarView.refreshCalendar(mCalendarCurrent);
            }
        });

        btnweek.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                btnweek.setBackgroundColor(Color.parseColor("#0051A1"));
                btnweek.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                btnweek.setTextColor(Color.parseColor("#ffffff"));

                btnmonth.setBackgroundColor(Color.parseColor("#ffffff"));
                btnmonth.setTextColor(Color.parseColor("#000000"));

                btnday.setBackgroundColor(Color.parseColor("#ffffff"));
                btnday.setTextColor(Color.parseColor("#000000"));

                llMonthView.setVisibility(View.GONE);
                mWeekView.setVisibility(View.VISIBLE);
                mWeekView.setNumberOfVisibleDays(7);

                Log.e("Called","Week");
                mWeekView.goToDate(mCalendarCurrent);


            }
        });

        btnday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                btnday.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                btnday.setTextColor(Color.parseColor("#ffffff"));

                btnmonth.setBackgroundColor(Color.parseColor("#ffffff"));
                btnmonth.setTextColor(Color.parseColor("#000000"));

                btnweek.setBackgroundColor(Color.parseColor("#ffffff"));
                btnweek.setTextColor(Color.parseColor("#000000"));

                llMonthView.setVisibility(View.GONE);
                mWeekView.setVisibility(View.VISIBLE);

                mWeekView.setNumberOfVisibleDays(1);
                mWeekView.goToDate(mCalendarCurrent);
                Log.e("Called","day");
            }
        });


        mWeekView.setOnEventClickListener(this);
//        mWeekView.setMonthChangeListener(this);
        mWeekView.setScrollListener(new WeekView.ScrollListener()
        {
            @Override
            public void onFirstVisibleDayChanged(Calendar calendar, Calendar calendar1)
            {
                /*if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("month"))
                {
                    String old_format=manager.getGroupCalSession(GroupCalendar_Activity.this);
                    Log.e("Entered",old_format);

                    // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
                    SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
                    Date date1 = null;
                    try
                    {
                        date1 = dt.parse(old_format);
                        // *** same for the format String below
                        SimpleDateFormat dt1 = new SimpleDateFormat("MMM yyyy");
//                        System.out.println(dt1.format(date1));
                        Log.e("Entered",dt1.format(date1));
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                }*/
                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM yyyy");
                String currentMonth = tvMonth.getText().toString().trim();
                String nextMonth = timeFormat.format(calendar.getTime());
                if (!currentMonth.equalsIgnoreCase(nextMonth))
                {
                    tvMonth.setText(nextMonth);
                    mCalendarCurrent.setTimeInMillis(calendar.getTimeInMillis());
                    getDateRange(calendar.getTime());
                }
            }
        });

    }

    public void filterAppointmentByDate(Date mDate)
    {
        Log.e("filterAppointmentByDate","Called");
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(mDate.getTime());
        String selectedDate = getFormateFromCalendar(mCalendar, "dd/MM/yyyy");
        eventsListFilter = new ArrayList<WeekViewEvent>();
        for (int i=0; i < eventsList.size(); i++)
        {
            String date = getFormateFromCalendar(eventsList.get(i).getStartTime(), "dd/MM/yyyy");
            if(date.equalsIgnoreCase(selectedDate))
                eventsListFilter.add(eventsList.get(i));
        }

        mAppointmentAdapter.notifyDataSetChanged();
    }

    public void getDateRange(Date mDate)
    {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(mDate.getTime());
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        mStartTime = timeFormat.format(mCalendar.getTime());

        mCalendar.add(Calendar.MONTH, 1);
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        mEndTime = timeFormat.format(mCalendar.getTime());

        Log.e("GetUserId",manager.getGroupCalUser(GroupCalendar_Activity.this));

        if(! manager.getGroupCalUser(GroupCalendar_Activity.this).equals(""))
        {
            GroupCalendarUserDetails groupCalendarUserDetails=new GroupCalendarUserDetails();
            groupCalendarUserDetails.execute();

//            SearchCountCallback searchTotalCallback=new SearchCountCallback();
//            searchTotalCallback.execute();

            lvcal.setVisibility(View.VISIBLE);
            warningDisplay.setVisibility(View.GONE);
        }
        else
        {
            lvcal.setVisibility(View.GONE);
            warningDisplay.setVisibility(View.VISIBLE);

//            Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
//            FontManager.markAsIconContainer(findViewById(R.id.Display_GroupCalendar_Warning), iconFont);

            warningDisplay.setText("Please select user...");
//            warningDisplay.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_account_circle_black,0);

            btnweek.setEnabled(false);
            btnweek.setTextColor(Color.parseColor("#9E9E9E"));
            btnweek.setPaintFlags(btnweek.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            btnday.setEnabled(false);
            btnday.setTextColor(Color.parseColor("#9E9E9E"));
            btnday.setPaintFlags(btnday.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public String getFormateFromCalendar(Calendar calendar, String format)
    {
        String formatedDate = "";
        Date mDate = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(format, new Locale("en"));
        formatedDate = sdf.format(mDate);
        return formatedDate;
    }

    private void setupDateTimeInterpreter(final boolean shortDate)
    {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter()
        {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("d MMM", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
//                return weekday.toUpperCase() + format.format(date.getTime());
                return format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect)
    {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth)
    {
        Log.e("Called","onMonthChange");
        if(lastTime == 0)
        {
            Log.e("Called","lastTimeIf");
            if (eventsList == null)
            {
                Log.e("Called","eventsList");
                eventsList = new ArrayList<WeekViewEvent>();
//                Rec_Id=new ArrayList<WeekViewEvent>();
            }
            lastTime = Calendar.getInstance().getTimeInMillis();
            return eventsList;
        }
        else
        {
            if(Calendar.getInstance().getTimeInMillis() - lastTime > 5000)
            {
                if (eventsList == null)
                    eventsList = new ArrayList<WeekViewEvent>();
//                    Rec_Id = new ArrayList<WeekViewEvent>();

                lastTime = Calendar.getInstance().getTimeInMillis();
                return eventsList;
            }
        }
        return new ArrayList<WeekViewEvent>();
    }

    private class GetUsers extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetData","GetData");

            pd=new ProgressDialog(GroupCalendar_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            GetData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            pd.cancel();

            Spinner groupCal_user=(Spinner) dialog.findViewById(R.id.groupCal_user);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GroupCalendar_Activity.this, android.R.layout.simple_spinner_item, userList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupCal_user.setAdapter(dataAdapter);


            if(! manager.getGroupCalUser(GroupCalendar_Activity.this).equals(""))
            {
                for(int i=0;i<groupCal_user.getCount();i++)
                {
                    GroupCalendarUser_Data data2 = (GroupCalendarUser_Data)groupCal_user.getItemAtPosition(i);
                    Log.e("User",data2.getLogonId());
                    if (data2.getLogonId().equals(manager.getGroupCalUser(GroupCalendar_Activity.this)))
                    {
                        Log.e("SelectedUser",data2.getLogonId());
                        groupCal_user.setSelection(i);
                        break;
                    }
                }
            }
        }
    }
    private void GetData()
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/GetGroupCalendarUserDetail";
        String METHOD_NAME = "GetGroupCalendarUserDetail";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("LogonID", manager.getUserId(GroupCalendar_Activity.this));
            Request.addProperty("CompanyID", manager.getWG(GroupCalendar_Activity.this));

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,30000);

            transport.call(SOAP_ACTION, soapEnvelope);
            //list=new ArrayList();

            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
            // resultString = (SoapPrimitive) soapEnvelope.getResponse();
            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
            {
                SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                String logonID = TypeEventData.getProperty("LogonID").toString();
                String fullName = TypeEventData.getProperty("FullName").toString();
                String businessType = TypeEventData.getProperty("BusinessType").toString();

                GroupCalendarUser_Data  callbacktaskData=new GroupCalendarUser_Data();

                if(logonID.equals("anyType{}"))
                {
                    logonID="none";
                }else
                {
                    logonID=logonID+"";
                }

                if(fullName.equals("anyType{}"))
                {
                    fullName="none";
                }else
                {
                    fullName=fullName+"";
                }

                if(businessType.equals("anyType{}"))
                {
                    businessType="none";
                }else
                {
                    businessType=businessType+"";
                }


                callbacktaskData.setLogonId(logonID);
                callbacktaskData.setFullName(fullName);
                callbacktaskData.setBusinessType(businessType);

                userList.add(callbacktaskData);

                Log.e("logonID",logonID);
                Log.e("fullName",fullName);

            }
            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
        }
        catch (Exception ex)
        {
           Log.e("", "Error: " + ex.getMessage());
        }
        Log.e("1", "Result SECOND API: " + resultRequestSOAP);
    }

    private class GroupCalendarUserDetails extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            Log.e("DemoMonth",tvMonth.getText().toString());
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            eventsList = new ArrayList<WeekViewEvent>();
//            Rec_Id = new ArrayList<WeekViewEvent>();
            eventsListFilter = new ArrayList<WeekViewEvent>();

            pd=new ProgressDialog(GroupCalendar_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetGroupCalendarCallBackEvent";
            String METHOD_NAME = "GetGroupCalendarCallBackEvent";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Log.e("LogonId",manager.getGroupCalUser(GroupCalendar_Activity.this));
                Log.e("CompanyID", manager.getWG(GroupCalendar_Activity.this));
                Log.e("Duration", manager.getGroupCalDateView(GroupCalendar_Activity.this));
                Log.e("DurationDate", manager.getGroupCalSession(GroupCalendar_Activity.this));

                Request.addProperty("LogonID", manager.getGroupCalUser(GroupCalendar_Activity.this));
                Request.addProperty("CompanyID", manager.getWG(GroupCalendar_Activity.this));
                Request.addProperty("Duration", manager.getGroupCalDateView(GroupCalendar_Activity.this));
                Request.addProperty("DurationDate", manager.getGroupCalSession(GroupCalendar_Activity.this));

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();

                Log.e("Result", String.valueOf(soapEnvelope.getResponse()));
                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();
                Log.e("Result",resultRequestSOAP+"");
                Rec_ID = new String[resultRequestSOAP.getPropertyCount()];
                Callback_ID = new String[resultRequestSOAP.getPropertyCount()];
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String account = TypeEventData.getProperty("Company").toString();
                    String callback = TypeEventData.getProperty("EventName").toString();
                    String eventType = TypeEventData.getProperty("EventType").toString();
                    String startTime = TypeEventData.getProperty("StartTime").toString();
                    String endTime = TypeEventData.getProperty("EndTime").toString();
                    String eventstatus = TypeEventData.getProperty("EventStatus").toString();

                    String rec_id = TypeEventData.getProperty("RECDNO").toString();
                    String callbak_id = TypeEventData.getProperty("CallBackID").toString();

                    Rec_ID[i]=rec_id;
                    Callback_ID[i]=callbak_id;

                    String passName;

                    if(! callback.equals("anyType{}"))
                    {
                        passName=callback;
                    }
                    else if(! eventType.equals("anyType{}"))
                    {
                        passName=eventType;
                    }
                    else if(! account.equals("anyType{}"))
                    {
                        passName=account;
                    }
                    else
                    {
                        passName="Callback Event";
                    }

                    WeekViewEvent recWeekViewEvent=new WeekViewEvent();

                    recWeekViewEvent.setId(Long.parseLong(rec_id));

                    WeekViewEvent mWeekViewEvent = new WeekViewEvent();
                    mWeekViewEvent.setId(Long.parseLong(callbak_id));
                    mWeekViewEvent.setName(passName);
                    mWeekViewEvent.setStartTime(getTimeFromString(startTime, "yyyy-MM-dd HH:mm:ss"));
                    mWeekViewEvent.setEndTime(getTimeFromString(endTime, "yyyy-MM-dd HH:mm:ss"));
                    mWeekViewEvent.setColor(Color.parseColor("#0051A1"));
                    eventsList.add(mWeekViewEvent);

                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d("Post","Execute");

            Log.e("EventList",eventsList.size()+"");
//            if(Rec_ID.length != 0)
            if(eventsList.size() != 0)
            {
                warningDisplay.setVisibility(View.GONE);
                lvcal.setVisibility(View.VISIBLE);

                eventsListFilter = eventsList;

                mAppointmentAdapter = new AppointmentAdapter();
                lvcal.setAdapter(mAppointmentAdapter);

                mCustomCalendarView.setAppointment(eventsList);
                mCustomCalendarView.refreshCalendar(mCustomCalendarView.getCurrentCalendar());

                mWeekView.setMonthChangeListener(GroupCalendar_Activity.this);
                mWeekView.notifyDatasetChanged();

                pd.dismiss();


                /*if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("week"))
                {
                    btnweek.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                    btnweek.setTextColor(Color.parseColor("#ffffff"));

                    btnmonth.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnmonth.setTextColor(Color.parseColor("#000000"));

                    btnday.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnday.setTextColor(Color.parseColor("#000000"));

                    llMonthView.setVisibility(View.GONE);
                    mWeekView.setVisibility(View.VISIBLE);
                    mWeekView.setNumberOfVisibleDays(7);

                    mWeekView.goToDate(mCalendarCurrent);
                }
                else if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("day"))
                {
                    btnday.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                    btnday.setTextColor(Color.parseColor("#ffffff"));

                    btnmonth.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnmonth.setTextColor(Color.parseColor("#000000"));

                    btnweek.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnweek.setTextColor(Color.parseColor("#000000"));

                    llMonthView.setVisibility(View.GONE);
                    mWeekView.setVisibility(View.VISIBLE);

                    mWeekView.setNumberOfVisibleDays(1);
                    mWeekView.goToDate(mCalendarCurrent);
                }
                else if(manager.getGroupCalDateView(GroupCalendar_Activity.this).equals("month"))
                {
                    llMonthView.setVisibility(View.VISIBLE);
                    mWeekView.setVisibility(View.GONE);

                    btnmonth.setBackgroundColor(manager.getColor(GroupCalendar_Activity.this));
                    btnmonth.setTextColor(Color.parseColor("#ffffff"));

                    btnweek.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnweek.setTextColor(Color.parseColor("#000000"));

                    btnday.setBackgroundColor(Color.parseColor("#ffffff"));
                    btnday.setTextColor(Color.parseColor("#000000"));

                    mCustomCalendarView.refreshCalendar(mCalendarCurrent);
                }*/
            }
            else
            {
                pd.dismiss();

                lvcal.setVisibility(View.GONE);
                warningDisplay.setVisibility(View.VISIBLE);
                warningDisplay.setText("No any event available");

                btnweek.setEnabled(false);
                btnweek.setTextColor(Color.parseColor("#9E9E9E"));
                btnweek.setPaintFlags(btnweek.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                btnday.setEnabled(false);
                btnday.setTextColor(Color.parseColor("#9E9E9E"));
                btnday.setPaintFlags(btnday.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }


    /**
     * BaseAdapter class for load data into listview
     */
    public class AppointmentAdapter extends BaseAdapter
    {
        ViewHolder mViewHolder;

        @Override
        public int getCount() {
            return eventsListFilter.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertview, ViewGroup arg2)
        {
            if (convertview == null)
            {
                convertview = getLayoutInflater().inflate(R.layout.row_appoinment_group_cal, null);

                mViewHolder = new ViewHolder();

                mViewHolder.mTextViewName = (TextView) convertview.findViewById(R.id.row_appointment_group_textview_name);
                mViewHolder.mTextViewDate = (TextView) convertview.findViewById(R.id.row_appointment_group_textview_date);
                mViewHolder.mTextViewTime = (TextView) convertview.findViewById(R.id.row_appointment_group_textview_time);

                mViewHolder.row_groupcalendar_listview_parent = (RelativeLayout)convertview.findViewById(R.id.row_groupcalendar_listview_parent);

                convertview.setTag(mViewHolder);

            }
            else
            {
                mViewHolder = (ViewHolder) convertview.getTag();
            }

            Log.e("EventeName : ",eventsListFilter.get(position).getName());
            Log.e("EventDate : ",getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "dd, MMM"));
            Log.e("EventTime : ",getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "hh:mm a"));

            if(manager.getFontStyle(GroupCalendar_Activity.this).equals("open-sans"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_linear_date), mainFont);
            }
            else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("pt-sans"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_linear_date), mainFont);
            }
            else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("Lora-Regular"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_linear_date), mainFont);
            }
            else if(manager.getFontStyle(GroupCalendar_Activity.this).equals("DroidSerif-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_group_linear_date), mainFont);
            }

            Log.e("EventListFIlter",eventsListFilter.get(position).getName());
            mViewHolder.mTextViewName.setText(eventsListFilter.get(position).getName());
            mViewHolder.mTextViewDate.setText(getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "dd, MMM"));
            mViewHolder.mTextViewTime.setText(getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "hh:mm a"));

            mViewHolder.row_groupcalendar_listview_parent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

//                    String toSplit = String.valueOf(eventsListFilter.get(position).getName());
//                    String result[] = toSplit.split(",");
//                    String recordId = result[result.length - 1];

                    String RecordID = null;
                    for(int i=0;i<Rec_ID.length;i++)
                    {
                        Log.e("Length "+i,Rec_ID.length+"");
                        Log.e("CallbackId",Callback_ID[i]+"");
                        Log.e("CallbackId",eventsListFilter.get(position).getId()+"");

                        if(Callback_ID[i].equals(""+eventsListFilter.get(position).getId()))
                        {
                            RecordID=Rec_ID[i];
                            Log.e("RecordID 1 : ",RecordID);

                            String encp=encrptVal();
                            String url =manager.getMainUrl(GroupCalendar_Activity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + RecordID + "&CompanyID=" + manager.getWG(GroupCalendar_Activity.this) + "&CallBackID=" + eventsListFilter.get(position).getId() + "&appkeyword=&pagetype=callback";

                            Log.e("URL",url);

                            Intent intent = new Intent(GroupCalendar_Activity.this,CommonWebView.class);
                            intent.putExtra("url", url);
                            intent.putExtra("frg", "callback");
                            startActivity(intent);
                        }
                    }
//                    Log.e("RecordId 2 :",);

                }
            });

            return convertview;
        }
    }

    public class ViewHolder
    {
        TextView mTextViewDate;
        TextView mTextViewTime;
        TextView mTextViewName;

        RelativeLayout row_groupcalendar_listview_parent;
    }

    public Calendar getTimeFromString(String date, String date_formate)
    {
        Log.e("getTimeFromString","Called");

        String str =date;

        // parse the String "29/07/2013" to a java.util.Date object
        Date date2=null;
        try
        {
            date2= new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").parse(str);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        // format the java.util.Date object to the desired format
//        String formattedDate = new SimpleDateFormat(date_formate).format(date);
        String formattedDate = new SimpleDateFormat(date_formate).format(date2);

        Log.e("NewDate : ",formattedDate);

//        SimpleDateFormat dateFormat = new SimpleDateFormat(date_formate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_formate);
        Date myDate = null;
        try
        {
//            myDate = dateFormat.parse(date);
            myDate = dateFormat.parse(formattedDate);
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(myDate.getTime());
        Log.e("getTimeFromString",""+mCalendar);
        return mCalendar;
    }


    //Base64 Encryption...
    private String encrptVal()
    {
        String encrptVal = null;

        String userName=manager.getUserName(GroupCalendar_Activity.this);
        String pass=manager.getUserPass(GroupCalendar_Activity.this);
        String dbId=manager.getDB(GroupCalendar_Activity.this);
        String wgId=manager.getWG(GroupCalendar_Activity.this);


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