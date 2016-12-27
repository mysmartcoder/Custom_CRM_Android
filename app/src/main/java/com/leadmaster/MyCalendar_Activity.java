package com.leadmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.leadmaster.custom_calendar.CalendarListener;
import com.leadmaster.custom_calendar.CustomCalendarView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyCalendar_Activity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener
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

    SessionManager manager;

    int totalRcordDIsplay;

    TextView backButton;

    Button currentDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        manager = new SessionManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(MyCalendar_Activity.this));
        }

        if(manager.getFontStyle(MyCalendar_Activity.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.mycal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_calendar_month_view), mainFont);
        }
        else if(manager.getFontStyle(MyCalendar_Activity.this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.mycal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_calendar_month_view), mainFont);
        }
        else if(manager.getFontStyle(MyCalendar_Activity.this).equals("Lora-Regular"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.mycal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_calendar_month_view), mainFont);
        }
        else if(manager.getFontStyle(MyCalendar_Activity.this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.mycal_header), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linear_day_option), mainFont);
            CustomFont.markAsIconContainer(findViewById(R.id.linearlayout_calendar_month_view), mainFont);
        }

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.myCalLL), iconFont);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mycalendar_toolbar);
        toolbar.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));

        currentDateBtn=(Button)findViewById(R.id.current_date_btn);

        backButton = (TextView)findViewById(R.id.backBtn_myCal);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnmonth = (Button) findViewById(R.id.btnmonth);
        btnmonth.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));

        btnweek = (Button) findViewById(R.id.btnweek);

        btnday = (Button) findViewById(R.id.btnday);

        lvcal = (ListView) findViewById(R.id.calendar_listview);
        warningDisplay=(TextView)findViewById(R.id.Display_MyCalendar_Warning);

        llMonthView=(LinearLayout)findViewById(R.id.linearlayout_calendar_month_view);

        mCustomCalendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        View divider = (View)findViewById(R.id.Divider_MyCal);
        divider.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));

        tvMonth=(TextView)findViewById(R.id.calendar_date_display);

        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM yyyy");
        tvMonth.setText(timeFormat.format(new Date()));


        GradientDrawable bgShape = (GradientDrawable)currentDateBtn.getBackground();
        bgShape.setColor(manager.getColor(MyCalendar_Activity.this));

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
        currentDateBtn.setText(currentDateFormat.format(new Date()));

        currentDateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                getDateRange(new Date());
                recreate();
            }
        });

//        demoMonth=tvMonth.getText().toString();

        getDateRange(new Date());

        mWeekView = (WeekView)findViewById(R.id.weekView);

        ivNext=(ImageView)findViewById(R.id.calendar_next_button);
        ivPrevious=(ImageView)findViewById(R.id.calendar_prev_button);

        mCalendarCurrent = Calendar.getInstance();

        setupDateTimeInterpreter(false);

        ivNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCustomCalendarView.nextMonth();
//                demoMonth=tvMonth.getText().toString();
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCustomCalendarView.previousMonth();
//                demoMonth=tvMonth.getText().toString();
            }
        });

        mCustomCalendarView.setCalendarListener(new CalendarListener()
        {
            @Override
            public void onDateSelected(Date date)
            {
                if(totalRcordDIsplay==0)
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

                btnmonth.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));
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
                btnweek.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));
                btnweek.setTextColor(Color.parseColor("#ffffff"));

                btnmonth.setBackgroundColor(Color.parseColor("#ffffff"));
                btnmonth.setTextColor(Color.parseColor("#000000"));

                btnday.setBackgroundColor(Color.parseColor("#ffffff"));
                btnday.setTextColor(Color.parseColor("#000000"));

                llMonthView.setVisibility(View.GONE);
                mWeekView.setVisibility(View.VISIBLE);
                mWeekView.setNumberOfVisibleDays(7);

                Log.e("Called","setNumberOfVisibleDays");

                mWeekView.goToDate(mCalendarCurrent);

                Log.e("Called","goToDate");

            }
        });

        btnday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                btnday.setBackgroundColor(manager.getColor(MyCalendar_Activity.this));
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
        });


        mWeekView.setOnEventClickListener(this);
//        mWeekView.setMonthChangeListener(this);
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar calendar, Calendar calendar1) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM yyyy");
                String currentMonth = tvMonth.getText().toString().trim();
                String nextMonth = timeFormat.format(calendar.getTime());
                if (!currentMonth.equalsIgnoreCase(nextMonth)) {
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

        SearchCountCallback searchCountCallback=new SearchCountCallback();
        searchCountCallback.execute();
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
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

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


    private class SearchTotalCallback extends AsyncTask<Void, Void, Void>
    {

        int total;

        public SearchTotalCallback(int totalRcordDIsplay)
        {
            total=totalRcordDIsplay;
        }

        @Override
        protected void onPreExecute()
        {
            Log.e("DemoMonth",tvMonth.getText().toString());
            super.onPreExecute();
            Log.d("TotalData","TotalData");
            eventsList = new ArrayList<WeekViewEvent>();
//            Rec_Id = new ArrayList<WeekViewEvent>();
            eventsListFilter = new ArrayList<WeekViewEvent>();


//            pd=new ProgressDialog(MyCalendar_Activity.this);
//            pd.setMessage("Wait...");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SearchCallBackEvent";
            String METHOD_NAME = "SearchCallBackEvent";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(MyCalendar_Activity.this));
                Request.addProperty("pwd", manager.getUserPass(MyCalendar_Activity.this));
                Request.addProperty("company_id", manager.getWG(MyCalendar_Activity.this));
                Request.addProperty("eventname", "");
                Request.addProperty("startindex", 1);
                Request.addProperty("endindex", total);
                Request.addProperty("eventdate", "");


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
//                    Log.e("Combination",""+Long.parseLong(callbak_id+" "+rec_id));
                    mWeekViewEvent.setId(Long.parseLong(callbak_id));
//                    mWeekViewEvent.setName(passName+","+rec_id);
                    mWeekViewEvent.setName(passName);
                    mWeekViewEvent.setStartTime(getTimeFromString(startTime, "yyyy-MM-dd HH:mm:ss"));
                    mWeekViewEvent.setEndTime(getTimeFromString(endTime, "yyyy-MM-dd HH:mm:ss"));
//                    mWeekViewEvent.setLocation(eventstatus);
//                    mWeekViewEvent.setColor(Color.parseColor("#17309A"));
                    mWeekViewEvent.setColor(Color.parseColor("#0051A1"));
                    eventsList.add(mWeekViewEvent);

//                    Rec_Id.add(recWeekViewEvent);
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

            eventsListFilter = eventsList;

            mAppointmentAdapter = new AppointmentAdapter();
            lvcal.setAdapter(mAppointmentAdapter);

            mCustomCalendarView.setAppointment(eventsList);
            mCustomCalendarView.refreshCalendar(mCustomCalendarView.getCurrentCalendar());

            mWeekView.setMonthChangeListener(MyCalendar_Activity.this);
            mWeekView.notifyDatasetChanged();

            pd.dismiss();
        }
    }

    private class SearchCountCallback extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
//            Log.e("DemoMonth",tvMonth.getText().toString());
            super.onPreExecute();
//            Log.d("TotalData","TotalData");
//            eventsList = new ArrayList<WeekViewEvent>();
//            eventsListFilter = new ArrayList<WeekViewEvent>();

            pd=new ProgressDialog(MyCalendar_Activity.this);
            pd.setMessage("Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/SearchCallBackCount";
            String METHOD_NAME = "SearchCallBackCount";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("username", manager.getUserName(MyCalendar_Activity.this));
                Request.addProperty("pwd", manager.getUserPass(MyCalendar_Activity.this));
                Request.addProperty("company_id", manager.getWG(MyCalendar_Activity.this));
                Request.addProperty("eventname", "");
                Request.addProperty("eventdate", "");


                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);


                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();

                resultRequestSOAP    = (SoapObject) soapEnvelope.bodyIn;
//            SoapObject soapObjectGetProperty = (SoapObject) resultRequestSOAP.getProperty("SearchAccountCountResult");

                totalRcordDIsplay=Integer.parseInt(resultRequestSOAP.getProperty("SearchCallBackCountResult").toString());
                // resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.e("TotalRecord", ""+totalRcordDIsplay);
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

            if(totalRcordDIsplay != 0)
            {
                warningDisplay.setVisibility(View.GONE);
                lvcal.setVisibility(View.VISIBLE);

                SearchTotalCallback searchTotalCallback=new SearchTotalCallback(totalRcordDIsplay);
                searchTotalCallback.execute();
            }
            else
            {
                pd.dismiss();

                lvcal.setVisibility(View.GONE);
                warningDisplay.setVisibility(View.VISIBLE);

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
                convertview = getLayoutInflater().inflate(R.layout.row_appoinment, null);

                mViewHolder = new ViewHolder();

                mViewHolder.mTextViewName = (TextView) convertview.findViewById(R.id.row_appointment_textview_name);
                mViewHolder.mTextViewDate = (TextView) convertview.findViewById(R.id.row_appointment_textview_date);
                mViewHolder.mTextViewTime = (TextView) convertview.findViewById(R.id.row_appointment_textview_time);

                mViewHolder.row_mycalendar_listview_parent = (RelativeLayout)convertview.findViewById(R.id.row_mycalendar_listview_parent);

                convertview.setTag(mViewHolder);

            }
            else
            {
                mViewHolder = (ViewHolder) convertview.getTag();
            }

            Log.e("EventeName : ",eventsListFilter.get(position).getName());
            Log.e("EventDate : ",getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "dd, MMM"));
            Log.e("EventTime : ",getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "hh:mm a"));

            if(manager.getFontStyle(MyCalendar_Activity.this).equals("open-sans"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT1);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_linear_date), mainFont);
            }
            else if(manager.getFontStyle(MyCalendar_Activity.this).equals("pt-sans"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT2);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_linear_date), mainFont);
            }
            else if(manager.getFontStyle(MyCalendar_Activity.this).equals("Lora-Regular"))
            {
                Log.e("font","1");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT3);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_linear_date), mainFont);
            }
            else if(manager.getFontStyle(MyCalendar_Activity.this).equals("DroidSerif-Regular"))
            {
                Log.e("font","2");
                Typeface mainFont = CustomFont.getTypeface(getApplicationContext(), CustomFont.FONT4);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_textview_name), mainFont);
                CustomFont.markAsIconContainer(findViewById(R.id.row_appointment_linear_date), mainFont);
            }

//            String name=eventsListFilter.get(position).getName();
//            mViewHolder.mTextViewName.setText(name.substring(0,name.lastIndexOf(",")));
            mViewHolder.mTextViewName.setText(eventsListFilter.get(position).getName());
            mViewHolder.mTextViewDate.setText(getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "dd, MMM"));
            mViewHolder.mTextViewTime.setText(getFormateFromCalendar(eventsListFilter.get(position).getStartTime(), "hh:mm a"));

            mViewHolder.row_mycalendar_listview_parent.setOnClickListener(new View.OnClickListener()
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
                            String url =manager.getMainUrl(MyCalendar_Activity.this) + "/mobile_auth.asp?key=" + encp + "&topage=mobile_cbViewEvent.asp&RECDNO=" + RecordID + "&CompanyID=" + manager.getWG(MyCalendar_Activity.this) + "&CallBackID=" + eventsListFilter.get(position).getId() + "&appkeyword=&pagetype=callback";

                            Log.e("URL",url);

                            Intent intent = new Intent(MyCalendar_Activity.this,CommonWebView.class);
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

        RelativeLayout row_mycalendar_listview_parent;
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

        String userName=manager.getUserName(MyCalendar_Activity.this);
        String pass=manager.getUserPass(MyCalendar_Activity.this);
        String dbId=manager.getDB(MyCalendar_Activity.this);
        String wgId=manager.getWG(MyCalendar_Activity.this);


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