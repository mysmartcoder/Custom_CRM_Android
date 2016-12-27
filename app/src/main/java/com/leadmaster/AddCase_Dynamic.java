package com.leadmaster;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class AddCase_Dynamic extends AppCompatActivity
{

    ProgressDialog pd;

    TextView backButton;

    SessionManager manager=new SessionManager();

    TextView displayTest1_name;

    ArrayList list;

    LinearLayout parent,successParent;

    TextView[] partitionNameTV,spinnerTitle,spinnerType,spinnerID,editTextID,spinnerDatatype,editTextDatatType,
            spinnerRestriction,editTextRestriction;
    EditText[] fieldET;
    Spinner[] fieldSpinner;
    int[] count;
    LinearLayout[] partitionCardView;
    LinearLayout[] childContainer;
    TextInputLayout[] fieldTextInputLayout;
    Button addLeadButton;
    int countSpinner=0,countET=0;
    int indexSpinner=0,indexET=0;

    TextView addLeadWarning,addLeadRefresh;
    TextView addMoreRecord;
    TextView successToast;

    String[] keyContactSpinnerArray;
    HashMap<String,String> keyContactSpinnerMap = new HashMap<String, String>();

    HashMap<String,String> spinnerMap = new HashMap<String, String>();
    HashMap<String,String> fieldKeyValue = new HashMap<String, String>();
    HashMap<String,Integer> fieldKeyValueInt = new HashMap<String, Integer>();
    HashMap<String,Float> fieldKeyValueFloat = new HashMap<String, Float>();

//    HashMap<String,String> spinnerMap;

    String[] spinnerArray;

    int getMandatory=0;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    Spinner key_contact_spinner;
    TextView key_contact_display,record_id_hidden_addOpp,key_contact_restriction_addOpp,key_contact_fieldId_addOpp;
    AutoCompleteTextView searchCompany;
    AutoCompleteAdapter autoCompleteAdapter;

    LinearLayout getRecord;
    TextView partitionName_textview_addOpp,change_serach_result_addopp;
    TextInputLayout accountSearchHint_addOpp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case__dynamic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(manager.getStatusColor(AddCase_Dynamic.this));
        }

        if(manager.getFontStyle(AddCase_Dynamic.this).equals("open-sans"))
        {
            Log.e("font","1");
            Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT1);
            CustomFont.markAsIconContainer(findViewById(R.id.getRecord_addCase), mainFont);
        }
        else if(manager.getFontStyle(AddCase_Dynamic.this).equals("pt-sans"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT2);
            CustomFont.markAsIconContainer(findViewById(R.id.getRecord_addCase), mainFont);
        }
        else if(manager.getFontStyle(AddCase_Dynamic.this).equals("Lora-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT3);
            CustomFont.markAsIconContainer(findViewById(R.id.getRecord_addCase), mainFont);
        }
        else if(manager.getFontStyle(AddCase_Dynamic.this).equals("DroidSerif-Regular"))
        {
            Log.e("font","2");
            Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT4);
            CustomFont.markAsIconContainer(findViewById(R.id.getRecord_addCase), mainFont);
        }
        
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.addCaseLL), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.addCase_refresh), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.success_thumb_addCase), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.change_serach_result_addcase), iconFont);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addCase_toolbar);
        toolbar.setBackgroundColor(manager.getColor(AddCase_Dynamic.this));

        list=new ArrayList();

        addLeadWarning=(TextView)findViewById(R.id.addCase_warning);
        addMoreRecord=(TextView)findViewById(R.id.AddMoreCase);
        successToast=(TextView)findViewById(R.id.addCase_success_toast);

        addLeadRefresh=(TextView)findViewById(R.id.addCase_refresh);
        addLeadRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        getRecord=(LinearLayout)findViewById(R.id.getRecord_addCase);
        partitionName_textview_addOpp=(TextView)findViewById(R.id.partitionName_textview_addCase);
        change_serach_result_addopp=(TextView)findViewById(R.id.change_serach_result_addcase);
        accountSearchHint_addOpp=(TextInputLayout) findViewById(R.id.accountSearchHint_addCase);
        key_contact_display=(TextView)findViewById(R.id.key_contact_display_addCase);
        key_contact_spinner=(Spinner)findViewById(R.id.key_contact_spinner_addCase);
        searchCompany=(AutoCompleteTextView)findViewById(R.id.accountSearch_addCase);
        record_id_hidden_addOpp=(TextView)findViewById(R.id.record_id_hidden_addCase);
        key_contact_restriction_addOpp=(TextView)findViewById(R.id.key_contact_restriction_addCase);
        key_contact_fieldId_addOpp=(TextView)findViewById(R.id.key_contact_fieldId_addCase);

        partitionName_textview_addOpp.setText(manager.getCustomLabel(AddCase_Dynamic.this,"Record")+" Details");
        accountSearchHint_addOpp.setHint("Search "+manager.getCustomLabel(AddCase_Dynamic.this,"Banner - Accounts"));

        autoCompleteAdapter = new AutoCompleteAdapter(AddCase_Dynamic.this, android.R.layout.simple_dropdown_item_1line);
        searchCompany.setAdapter(autoCompleteAdapter);

        //when autocomplete is clicked
        searchCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                change_serach_result_addopp.setVisibility(View.VISIBLE);
                searchCompany.setEnabled(false);
                String countryName = autoCompleteAdapter.getItem(position).getName();
                String recordId = autoCompleteAdapter.getItem(position).getRecordId();

                searchCompany.setText(countryName);
                record_id_hidden_addOpp.setText(recordId);

                GetKeyContact getKeyContact=new GetKeyContact(recordId);
                getKeyContact.execute();
            }
        });

        change_serach_result_addopp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                change_serach_result_addopp.setVisibility(View.GONE);
                searchCompany.setText("");
                record_id_hidden_addOpp.setText("");
                searchCompany.setEnabled(true);
            }
        });

        addLeadButton=(Button)findViewById(R.id.addCaseButton);
        addLeadButton.setText("\tAdd new "+manager.getCustomLabel(AddCase_Dynamic.this,"Case"));
        addLeadButton.setBackgroundColor(manager.getColor(AddCase_Dynamic.this));
        addLeadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("Count",countET-1+","+countSpinner);

                for(int i=0;i<countET-1;i++)
                {
                    Log.e("ET",editTextID[i].getText().toString());
                    Log.e("RS",editTextRestriction[i].getText().toString());
                    Log.e("For"+i,editTextID[i].getText().toString()+" : "+editTextRestriction[i].getText().toString()+"");
                    if(fieldET[i].getText().toString().trim().equals("") && editTextRestriction[i].getText().toString().equals("true"))
                    {
                        Log.e("Mandatory","Edittext=>"+fieldET[i].getText().toString().trim().equals(""));

                        fieldET[i].setError("Mandatory Field");
                        fieldET[i].requestFocus();
                        getMandatory=1;
                    }

                    if(! fieldET[i].getText().toString().trim().equals(""))
                    {
                        if(editTextID[i].getText().toString().equals("Email"))
                        {
                            Log.e("CheckEmail",fieldET[i].getText().toString()+" : "+isValidEmail(fieldET[i].getText().toString())+"");
                            if(! isValidEmail(fieldET[i].getText().toString()))
                            {
                                fieldET[i].setError("Incorrect Email Id");
                                fieldET[i].requestFocus();
                                getMandatory=2;
                            }
                        }

                        if(editTextDatatType[i].getText().toString().equals("int") || editTextDatatType[i].getText().toString().equals("numeric") || editTextDatatType[i].getText().toString().equals("real"))
                        {
                            if(fieldET[i].getText().toString().contains("."))
                            {
                                Log.e("Pass","fieldKeyValueFloat");
                                Log.e("Value : ","EditText= "+editTextID[i].getText().toString()+" : "+fieldET[i].getText());
                                fieldKeyValueFloat.put(editTextID[i].getText().toString(), Float.valueOf(fieldET[i].getText().toString()));
                            }
                            else
                            {
                                Log.e("Pass","fieldKeyValueInt");
                                Log.e("Value : ","EditText= "+editTextID[i].getText().toString()+" : "+fieldET[i].getText());
                                fieldKeyValueInt.put(editTextID[i].getText().toString(), Integer.valueOf(fieldET[i].getText().toString()));
                            }
                        }
                        else
                        {
                            Log.e("Pass","fieldKeyValue");
                            Log.e("Value : ","EditText= "+editTextID[i].getText().toString()+" : "+fieldET[i].getText());
                            fieldKeyValue.put(editTextID[i].getText().toString(),fieldET[i].getText().toString());
                        }
                    }
                }
                for(int j=0;j<countSpinner;j++)
                {
                    Log.e("For"+j,spinnerID[j].getText().toString()+" : "+spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())+" : "+spinnerRestriction[j].getText().toString()+"");

                    if(spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())== null && spinnerRestriction[j].getText().toString().equals("true"))
                    {
                        Log.e("Mandatory","Spinner");
                        ((TextView)fieldSpinner[j].getSelectedView()).setError("Mandatory Field");
                        fieldSpinner[j].setFocusable(true);
                        fieldSpinner[j].setFocusableInTouchMode(true);
                        fieldSpinner[j].requestFocus();
                        getMandatory=1;
                    }

                    if(spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())!= null)
                    {
                        if(spinnerDatatype[j].getText().toString().equals("int") || spinnerDatatype[j].getText().toString().equals("numeric") || spinnerDatatype[j].getText().toString().equals("real"))
                        {
                            if(fieldSpinner[j].getSelectedItem().toString().contains("."))
                            {
                                Log.e("Pass","fieldKeyValueFloat");
                                Log.e("Value : ","Spinner= "+spinnerID[j].getText().toString()+" : "+spinnerMap.get(fieldSpinner[j].getSelectedItem().toString()));
                                fieldKeyValueFloat.put(spinnerID[j].getText().toString(), Float.valueOf(spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())));
                            }
                            else
                            {
                                Log.e("Pass","fieldKeyValueInt");
                                Log.e("Value : ","Spinner= "+spinnerID[j].getText().toString()+" : "+spinnerMap.get(fieldSpinner[j].getSelectedItem().toString()));
                                fieldKeyValueInt.put(spinnerID[j].getText().toString(), Integer.valueOf(spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())));
                            }
                        }
                        else
                        {
                            Log.e("Pass","fieldKeyValue");
                            Log.e("Value : ","Spinner= "+spinnerID[j].getText().toString()+" : "+spinnerMap.get(fieldSpinner[j].getSelectedItem().toString()));
                            fieldKeyValue.put(spinnerID[j].getText().toString(),spinnerMap.get(fieldSpinner[j].getSelectedItem().toString()));
                        }
                    }
                }

//                if(spinnerMap.get(fieldSpinner[j].getSelectedItem().toString())== null && spinnerRestriction[j].getText().toString().equals("true"))
                if(keyContactSpinnerMap.get(key_contact_spinner.getSelectedItem().toString())== null && key_contact_restriction_addOpp.getText().toString().equals("true"))
                {
                    ((TextView)key_contact_spinner.getSelectedView()).setError("Mandatory Field");
                    key_contact_spinner.setFocusable(true);
                    key_contact_spinner.setFocusableInTouchMode(true);
                    key_contact_spinner.requestFocus();
                    getMandatory=1;
                }
                if(keyContactSpinnerMap.get(key_contact_spinner.getSelectedItem().toString())!= null)
                {
                    Log.e("KeyContact",key_contact_fieldId_addOpp.getText().toString()+" : "+keyContactSpinnerMap.get(key_contact_spinner.getSelectedItem().toString()));

                    fieldKeyValue.put(key_contact_fieldId_addOpp.getText().toString(),keyContactSpinnerMap.get(key_contact_spinner.getSelectedItem().toString()));
                }

                if(record_id_hidden_addOpp.getText().toString().equals(""))
                {
                    searchCompany.setError("Select company from list");
                    searchCompany.requestFocus();
                    getMandatory=4;
                }

                if(searchCompany.getText().toString().equals(""))
                {
                    searchCompany.setError("Mandatory Field");
                    searchCompany.requestFocus();
                    getMandatory=3;
                }

                Log.e("Mandatory",getMandatory+"");
                if(getMandatory==1)
                {
                    Toast.makeText(getApplicationContext(),"Please fill mandatory field.",Toast.LENGTH_LONG).show();
                    getMandatory=0;
                }
                else if(getMandatory==2)
                {
                    Toast.makeText(getApplicationContext(),"Please check email id.",Toast.LENGTH_LONG).show();
                    getMandatory=0;
                }
                else if(getMandatory==3)
                {
                    Toast.makeText(getApplicationContext(),"Require to select "+manager.getCustomLabel(AddCase_Dynamic.this,"Record"),Toast.LENGTH_LONG).show();
                    getMandatory=0;
                }
                else if(getMandatory==4)
                {
                    Toast.makeText(getApplicationContext(),"Require to select "+manager.getCustomLabel(AddCase_Dynamic.this,"Record")+" from list",Toast.LENGTH_LONG).show();
                    getMandatory=0;
                }
                else
                {
                    SaveAddCaseRecords saveAddCaseRecords=new SaveAddCaseRecords(record_id_hidden_addOpp.getText().toString());
                    saveAddCaseRecords.execute();
                }
            }
        });

        backButton=(TextView)findViewById(R.id.backBtn_addCase);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        displayTest1_name=(TextView)findViewById(R.id.addCase_name);
        displayTest1_name.setText("Add new "+manager.getCustomLabel(AddCase_Dynamic.this,"Case"));
        displayTest1_name.setBackgroundColor(manager.getColor(AddCase_Dynamic.this));

        DynamicFieldAdd dynamicFieldAdd=new DynamicFieldAdd();
        dynamicFieldAdd.execute();

        parent=(LinearLayout)findViewById(R.id.addCase_parent);
        successParent=(LinearLayout)findViewById(R.id.success_parent_addCase);

    }

    private class DynamicFieldAdd extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("GetData","GetData");
            pd=new ProgressDialog(AddCase_Dynamic.this);
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

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.cancel();

            if(list.size()==0)
            {
                addLeadWarning.setVisibility(View.VISIBLE);
                addLeadRefresh.setVisibility(View.VISIBLE);
                addLeadButton.setVisibility(View.GONE);
                if(manager.getFontStyle(AddCase_Dynamic.this).equals("open-sans"))
                {
                    Log.e("font","1");
                    Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT1);
                    CustomFont.markAsIconContainer(addLeadWarning, mainFont);
                }
                else if(manager.getFontStyle(AddCase_Dynamic.this).equals("pt-sans"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT2);
                    CustomFont.markAsIconContainer(addLeadWarning, mainFont);
                }
                else if(manager.getFontStyle(AddCase_Dynamic.this).equals("Lora-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT3);
                    CustomFont.markAsIconContainer(addLeadWarning, mainFont);
                }
                else if(manager.getFontStyle(AddCase_Dynamic.this).equals("DroidSerif-Regular"))
                {
                    Log.e("font","2");
                    Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT4);
                    CustomFont.markAsIconContainer(addLeadWarning, mainFont);
                }
            }
            else
            {
                addLeadWarning.setVisibility(View.GONE);
                addLeadRefresh.setVisibility(View.GONE);
                addLeadButton.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.setMargins(0,0,0,10);

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                String[] unique;
                String[] fieldType=new String[list.size()];

                Log.e("ListSize : ",list.size()+"");
                for(int j=0;j<list.size();j++)
                {
                    AddRecord_Data testing_data=(AddRecord_Data)list.get(j);
                    Log.e("FieldName:FieldType:Partition==>",list.get(j).toString()+":"+testing_data.getFieldType()+":"+testing_data.getPartitionName());
                    fieldType[j]=testing_data.getPartitionName();
                    if(testing_data.getFieldType().equalsIgnoreCase("NONE"))
                    {
                        countET++;
                    }
                    else
                    {
                        countSpinner++;
                    }
                }

                unique = new HashSet<String>(Arrays.asList(fieldType)).toArray(new String[new HashSet<String>(Arrays.asList(fieldType)).size()]);
                Log.e("Unique", Arrays.toString(unique));
                Arrays.sort(unique);
                Log.e("Unique", Arrays.toString(unique));
                Log.e("Unique Size : ", unique.length+"");
                Log.e("Count ET,Spinner : ", countET+""+countSpinner);

                fieldET = new EditText[countET];
                editTextID = new TextView[countET];
                editTextDatatType = new TextView[countET];
                editTextRestriction = new TextView[countET];
                fieldTextInputLayout =new TextInputLayout[countET];

                fieldSpinner = new Spinner[countSpinner];
                spinnerTitle = new TextView[countSpinner];
                spinnerType = new TextView[countSpinner];
                spinnerID = new TextView[countSpinner];
                spinnerDatatype = new TextView[countSpinner];
                spinnerRestriction = new TextView[countSpinner];
                count=new int[countSpinner];

                partitionCardView = new LinearLayout[unique.length];
                partitionNameTV = new TextView[unique.length];
                childContainer = new LinearLayout[unique.length];

                for(int k=0;k<unique.length;k++)
                {
                    partitionCardView[k] = new LinearLayout(AddCase_Dynamic.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,25);
                    partitionCardView[k].setLayoutParams(params);
                    partitionCardView[k].setBackgroundResource(R.drawable.custom_shadow);

                    partitionNameTV[k]=new TextView(AddCase_Dynamic.this);
                    LinearLayout.LayoutParams partitionName_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    partitionName_params.setMargins(10,10,10,10);
                    partitionNameTV[k].setLayoutParams(partitionName_params);
                    partitionNameTV[k].setPadding(10,10,10,10);

                    String checkVisibility="";

                    if(unique[k].equals("additional_information"))
                    {
                        partitionNameTV[k].setText(manager.getCustomLabel(AddCase_Dynamic.this,"Case Additional Information"));
                        checkVisibility=manager.getCustomLabel(AddCase_Dynamic.this,"Case Additional Information");
                    }
                    else if(unique[k].equals("casedetail"))
                    {
                        partitionNameTV[k].setText(manager.getCustomLabel(AddCase_Dynamic.this,"Case Details"));
                        checkVisibility=manager.getCustomLabel(AddCase_Dynamic.this,"Case Details");
                    }

                    partitionNameTV[k].setTextSize(20);
                    partitionNameTV[k].setTypeface(null,Typeface.BOLD);
                    partitionNameTV[k].setTextColor(Color.parseColor("#ffffff"));
                    partitionNameTV[k].setBackgroundColor(Color.parseColor("#898989"));

                    childContainer[k]=new LinearLayout(AddCase_Dynamic.this);
                    childContainer[k].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    childContainer[k].setOrientation(LinearLayout.VERTICAL);
                    childContainer[k].addView(partitionNameTV[k]);

                    for(int i=0;i<list.size();i++)
                    {
                        AddRecord_Data testing_data=(AddRecord_Data)list.get(i);

                        Log.e("GetNone",testing_data.getFieldType());
                        if(! testing_data.getFieldID().equals("ContactID"))
                        {
                            if(testing_data.getFieldType().equals("NONE"))
                            {
                                if(testing_data.getPartitionName().equals(unique[k]))
                                {
                                    Log.e("ETIndex",indexET+""+list.get(i).toString());

                                    editTextID[indexET]=new TextView(AddCase_Dynamic.this);
                                    editTextID[indexET].setText(testing_data.getFieldID());
                                    editTextID[indexET].setVisibility(View.GONE);
                                /*Datatype*/
                                    editTextDatatType[indexET]=new TextView(AddCase_Dynamic.this);
                                    editTextDatatType[indexET].setText(testing_data.getFieldDataType());
                                    editTextDatatType[indexET].setVisibility(View.GONE);
                                /**/

                                /*Restriction*/
                                    editTextRestriction[indexET]=new TextView(AddCase_Dynamic.this);
                                    editTextRestriction[indexET].setText(testing_data.getFieldRestriction());
                                    editTextRestriction[indexET].setVisibility(View.GONE);
                                /**/

                                    if(testing_data.getFieldDataType().equals("int") || testing_data.getFieldDataType().equals("numeric") || testing_data.getFieldDataType().equals("real"))
                                    {
                                        Log.e("Datatype",list.get(i).toString()+",numeric");
                                        fieldTextInputLayout[indexET] =new TextInputLayout(AddCase_Dynamic.this);
                                        fieldTextInputLayout[indexET].setLayoutParams(params1);
                                        fieldTextInputLayout[indexET].setHint(list.get(i).toString());

                                        fieldET[indexET]=new EditText(AddCase_Dynamic.this);
                                        fieldET[indexET].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        fieldET[indexET].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        fieldTextInputLayout[indexET].addView(fieldET[indexET],params2);
                                    }
                                    else if(testing_data.getFieldDataType().equals("smalldatetime") || testing_data.getFieldDataType().equals("datetime") || testing_data.getFieldDataType().equals("date"))
                                    {
                                        Log.e("Datatype",list.get(i).toString()+",date");
                                        fieldTextInputLayout[indexET] =new TextInputLayout(AddCase_Dynamic.this);
                                        fieldTextInputLayout[indexET].setLayoutParams(params1);
                                        fieldTextInputLayout[indexET].setHint(list.get(i).toString()+" [ MM/dd/yyyy ]");

                                        fieldET[indexET]=new EditText(AddCase_Dynamic.this);
                                        fieldET[indexET].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        fieldET[indexET].setInputType(InputType.TYPE_NULL);
                                        fieldET[indexET].setFocusable(false);

                                        final int finalIndexET = indexET;
                                        fieldET[finalIndexET].setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                                Calendar newCalendar = Calendar.getInstance();
                                                datePickerDialog=new DatePickerDialog(AddCase_Dynamic.this, new DatePickerDialog.OnDateSetListener()
                                                {
                                                    @Override
                                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                                    {
                                                        Calendar newDate = Calendar.getInstance();
                                                        newDate.set(year, monthOfYear, dayOfMonth);
                                                        Log.e("Date",""+dateFormatter.format(newDate.getTime()));
                                                        fieldET[finalIndexET].setText(dateFormatter.format(newDate.getTime()));
                                                    }
                                                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                                                datePickerDialog.show();
                                            }
                                        });
                                    /*fieldTextInputLayout[finalIndexET].setOnTouchListener(new View.OnTouchListener()
                                    {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event)
                                        {

                                            return false;
                                        }
                                    });*/

                                        fieldTextInputLayout[indexET].addView(fieldET[indexET],params2);
                                    }
                                    else
                                    {
                                        Log.e("Datatype",list.get(i).toString()+",string");
                                        fieldTextInputLayout[indexET] =new TextInputLayout(AddCase_Dynamic.this);
                                        fieldTextInputLayout[indexET].setLayoutParams(params1);
                                        fieldTextInputLayout[indexET].setHint(list.get(i).toString());

                                        fieldET[indexET]=new EditText(AddCase_Dynamic.this);
                                        fieldET[indexET].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        fieldTextInputLayout[indexET].addView(fieldET[indexET],params2);
                                    }

                                    childContainer[k].addView(editTextID[indexET]);
                                    childContainer[k].addView(editTextDatatType[indexET]);
                                    childContainer[k].addView(editTextRestriction[indexET]);
                                    childContainer[k].addView(fieldTextInputLayout[indexET]);
                                    indexET++;
                                }

                            }
                            else
                            {
                                if(testing_data.getPartitionName().equals(unique[k]))
                                {
                                    Log.e("SpinnerIndex",indexSpinner+""+list.get(i).toString());

                                    spinnerTitle[indexSpinner]=new TextView(AddCase_Dynamic.this);
                                    LinearLayout.LayoutParams spinnertitle_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    spinnertitle_params.setMargins(10,10,10,10);
                                    spinnerTitle[indexSpinner].setLayoutParams(spinnertitle_params);
                                    spinnerTitle[indexSpinner].setPadding(10,10,10,10);
                                    spinnerTitle[indexSpinner].setBackgroundColor(Color.parseColor("#E0E0E0"));
                                    spinnerTitle[indexSpinner].setText(testing_data.getFieldName());
                                    spinnerTitle[indexSpinner].setTextSize(17);
                                    spinnerTitle[indexSpinner].setTextColor(Color.parseColor("#575757"));
                                    spinnerTitle[indexSpinner].setTypeface(null,Typeface.BOLD);

                                    spinnerType[indexSpinner]=new TextView(AddCase_Dynamic.this);
                                    spinnerType[indexSpinner].setText(testing_data.getFieldType());
                                    spinnerType[indexSpinner].setVisibility(View.GONE);

                                    spinnerID[indexSpinner]=new TextView(AddCase_Dynamic.this);
                                    spinnerID[indexSpinner].setText(testing_data.getFieldID());
                                    spinnerID[indexSpinner].setVisibility(View.GONE);
                                /*Datatype*/
                                    spinnerDatatype[indexSpinner]=new TextView(AddCase_Dynamic.this);
                                    spinnerDatatype[indexSpinner].setText(testing_data.getFieldDataType());
                                    spinnerDatatype[indexSpinner].setVisibility(View.GONE);
                                /**/

                                /*Restriction*/
                                    spinnerRestriction[indexSpinner]=new TextView(AddCase_Dynamic.this);
                                    spinnerRestriction[indexSpinner].setText(testing_data.getFieldRestriction());
                                    spinnerRestriction[indexSpinner].setVisibility(View.GONE);
                                /**/

                                    fieldSpinner[indexSpinner]=new Spinner(AddCase_Dynamic.this);
                                    count[indexSpinner]=0;
                                    ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(AddCase_Dynamic.this,
                                            R.array.displayDefaultSpinner, android.R.layout.simple_list_item_1);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    fieldSpinner[indexSpinner].setAdapter(adapter);

                                    final int finalIndexSpinner = indexSpinner;
                                    fieldSpinner[finalIndexSpinner].setOnTouchListener(new View.OnTouchListener()
                                    {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event)
                                        {
                                            Log.e("TableName",spinnerType[finalIndexSpinner].getText().toString());
                                            Log.e("FieldID",spinnerID[finalIndexSpinner].getText().toString());
                                            Log.e("Checked",fieldSpinner[finalIndexSpinner].getSelectedItem().toString());
//                                        if(fieldSpinner[finalIndexSpinner].getSelectedItem().equals("Select option :"))
                                            if(count[finalIndexSpinner]==0)
                                            {
                                                GetSpinnerItems getSpinnerItems=new GetSpinnerItems(spinnerType[finalIndexSpinner].getText().toString(),finalIndexSpinner);
                                                getSpinnerItems.execute();
                                                count[finalIndexSpinner]=1;
                                            }
                                            return false;
                                        }
                                    });

                                    fieldSpinner[finalIndexSpinner].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                    {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                        {

                                            Log.e("Key : value",spinnerMap.get(fieldSpinner[finalIndexSpinner].getSelectedItem().toString())+" : "+fieldSpinner[finalIndexSpinner].getSelectedItem().toString());
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent)
                                        {

                                        }
                                    });
                                    childContainer[k].addView(spinnerTitle[indexSpinner]);
                                    childContainer[k].addView(spinnerType[indexSpinner]);
                                    childContainer[k].addView(spinnerID[indexSpinner]);
                                    childContainer[k].addView(spinnerDatatype[indexSpinner]);
                                    childContainer[k].addView(spinnerRestriction[indexSpinner]);
                                    childContainer[k].addView(fieldSpinner[indexSpinner]);
                                    indexSpinner++;
                                }
                            }
                        }
                        else
                        {
                            Log.e("KeyContactId","KeyContactId : "+testing_data.getFieldID());
                            key_contact_fieldId_addOpp.setText(testing_data.getFieldID());
                            key_contact_restriction_addOpp.setText(testing_data.getFieldRestriction());
                            key_contact_display.setText(testing_data.getFieldName());
//                            key_contact_display.setTextSize(15);
//                            key_contact_display.setTextColor(Color.parseColor("#000000"));
//                            key_contact_display.setPaintFlags(key_contact_display.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        }
                    }
                    if(! checkVisibility.equals(""))
                    {
                        partitionCardView[k].addView(childContainer[k]);
                        parent.addView(partitionCardView[k]);
                    }

                    if(manager.getFontStyle(AddCase_Dynamic.this).equals("open-sans"))
                    {
                        Log.e("font","1");
                        Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT1);
                        CustomFont.markAsIconContainer(childContainer[k], mainFont);
                        CustomFont.markAsIconContainer(partitionCardView[k], mainFont);
                        CustomFont.markAsIconContainer(findViewById(R.id.addCase_name), mainFont);
                    }
                    else if(manager.getFontStyle(AddCase_Dynamic.this).equals("pt-sans"))
                    {
                        Log.e("font","2");
                        Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT2);
                        CustomFont.markAsIconContainer(childContainer[k], mainFont);
                        CustomFont.markAsIconContainer(partitionCardView[k], mainFont);
                        CustomFont.markAsIconContainer(findViewById(R.id.addCase_name), mainFont);
                    }
                    else if(manager.getFontStyle(AddCase_Dynamic.this).equals("Lora-Regular"))
                    {
                        Log.e("font","2");
                        Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT3);
                        CustomFont.markAsIconContainer(childContainer[k], mainFont);
                        CustomFont.markAsIconContainer(partitionCardView[k], mainFont);
                        CustomFont.markAsIconContainer(findViewById(R.id.addCase_name), mainFont);
                    }
                    else if(manager.getFontStyle(AddCase_Dynamic.this).equals("DroidSerif-Regular"))
                    {
                        Log.e("font","2");
                        Typeface mainFont = CustomFont.getTypeface(AddCase_Dynamic.this.getApplicationContext(), CustomFont.FONT4);
                        CustomFont.markAsIconContainer(childContainer[k], mainFont);
                        CustomFont.markAsIconContainer(partitionCardView[k], mainFont);
                        CustomFont.markAsIconContainer(findViewById(R.id.addCase_name), mainFont);
                    }

                }
            }
        }
    }

    private void GetData()
    {
        SoapObject resultRequestSOAP = null;
        String SOAP_ACTION = "LMServiceNamespace/GetMobileInsertDataFields";
        String METHOD_NAME = "GetMobileInsertDataFields";
        String NAMESPACE = "LMServiceNamespace";
        String URL = manager.getUrl();

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            Request.addProperty("LogonID", manager.getUserId(AddCase_Dynamic.this));
            Request.addProperty("CompanyID", manager.getWG(AddCase_Dynamic.this));
            Request.addProperty("PageName", "addcase");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL,30000);

            transport.call(SOAP_ACTION, soapEnvelope);

            resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

            for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
            {
                SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);

                String fieldID = TypeEventData.getProperty("FieldID").toString();
                String fieldName = TypeEventData.getProperty("FieldName").toString();
                String fieldValue = TypeEventData.getProperty("FieldValue").toString();
                String fieldInputType = TypeEventData.getProperty("FieldDataType").toString();
                String fieldRestriction = TypeEventData.getProperty("FieldRequired").toString();

                String part[]=fieldValue.split(":");

                AddRecord_Data testing_data=new AddRecord_Data();

                testing_data.setFieldID(fieldID);
                testing_data.setFieldName(fieldName);
                testing_data.setPartitionName(part[0]);
                testing_data.setFieldType(part[2]);
                testing_data.setFieldDataType(fieldInputType);
                testing_data.setFieldRestriction(fieldRestriction);

                list.add(testing_data);
            }
            Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);

        }
        catch (Exception ex)
        {
            Log.e("Error",""+ex);
        }
        Log.e("Url==>",URL);
        Log.e("check", "Result SECOND API: " + resultRequestSOAP);
    }

    private class GetSpinnerItems extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog progressDialog;

        String tableName;
        int index;

        public GetSpinnerItems(String s, int finalIndexSpinner)
        {
            tableName=s;
            index=finalIndexSpinner;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog=new ProgressDialog(AddCase_Dynamic.this);
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetReferenceTableData";
            String METHOD_NAME = "GetReferenceTableData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

                Request.addProperty("CompanyID", manager.getWG(AddCase_Dynamic.this));
                Request.addProperty("TableName", tableName);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);
                //list=new ArrayList();


                resultRequestSOAP    = (SoapObject) soapEnvelope.getResponse();

                spinnerArray = new String[resultRequestSOAP.getPropertyCount()+1];
                Log.e("resultCount",resultRequestSOAP.getPropertyCount()+"");

//                spinnerMap.put("Select option :","");
                spinnerArray[0] = "Select option :";
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
//                for (int i = 1; i < resultRequestSOAP.getPropertyCount()+1; i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String key = TypeEventData.getProperty("Key").toString();
                    String value = TypeEventData.getProperty("Value").toString();

                    if(value.equals("anyType{}"))
                    {
                        value="(SELECT)";
                    }else
                    {
                        value=value+"";
                    }

                    spinnerMap.put(value,key);
                    spinnerArray[i+1] = value;
//                    spinnerArray[i] = value;

                    Log.e("Key:Value = ",key+" : "+value);
                }
                Log.e("resultCount",spinnerArray.length+"");
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("check", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

            ArrayAdapter<String> adapter =new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fieldSpinner[index].setAdapter(adapter);
        }
    }


    private class SaveAddCaseRecords extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog saveRecordProgress;
        int countResponse=0;
        int recordId;

        public SaveAddCaseRecords(String s)
        {
            recordId=Integer.parseInt(s);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            saveRecordProgress = new ProgressDialog(AddCase_Dynamic.this);
            saveRecordProgress.setTitle("Wait...");
            saveRecordProgress.setCancelable(false);
            saveRecordProgress.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/AddCase";
            String METHOD_NAME = "AddCase";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("logon_id", manager.getUserId(AddCase_Dynamic.this));
                Request.addProperty("pwd", manager.getUserPass(AddCase_Dynamic.this));
                Request.addProperty("company_id", manager.getWG(AddCase_Dynamic.this));

                SoapObject field = new SoapObject(NAMESPACE,"CaseData");
                Request.addSoapObject(field);

                SoapObject record = new SoapObject(NAMESPACE,"TypeCaseData");
                Log.e("MapSize",""+fieldKeyValue.size());
                Log.e("RecdNo",recordId+"");

                record.addProperty("RECDNO",recordId);

                for (Map.Entry<String, String> entry : fieldKeyValue.entrySet())
                {
                    Log.e("GetStringMap",entry.getKey()+" : "+entry.getValue());
                    record.addProperty(entry.getKey(),entry.getValue());
                }
                for (Map.Entry<String, Integer> entry : fieldKeyValueInt.entrySet())
                {
                    Log.e("GetIntMap",entry.getKey()+" : "+entry.getValue());
                    record.addProperty(entry.getKey(),entry.getValue());
                }
                for (Map.Entry<String, Float> entry : fieldKeyValueFloat.entrySet())
                {
                    Log.e("GetAllMapFloat",entry.getKey()+" : "+entry.getValue());
                    record.addProperty(entry.getKey(),entry.getValue());
                }
                field.addSoapObject(record);
                Log.e("For","Complete");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

                countResponse=resultRequestSOAP.getPropertyCount();
                Log.e("Count",resultRequestSOAP.getPropertyCount()+"");
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String caseID = TypeEventData.getProperty("CaseID").toString();
                    Log.e("CaseID:",caseID);
                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("check", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            saveRecordProgress.dismiss();
            if(countResponse!=0)
            {
//                Toast.makeText(getApplicationContext(),"Successfully Record Save",Toast.LENGTH_LONG).show();
                getRecord.setVisibility(View.GONE);
                parent.setVisibility(View.GONE);
                addLeadButton.setVisibility(View.GONE);
                successParent.setVisibility(View.VISIBLE);
                successToast.setText("Your "+manager.getCustomLabel(AddCase_Dynamic.this,"Case")+"  is successfully add.");
                addMoreRecord.setText("Add More "+manager.getCustomLabel(AddCase_Dynamic.this,"Case"));
                addMoreRecord.setPaintFlags(addMoreRecord.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                addMoreRecord.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        searchCompany.setText("");
                        recreate();
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),manager.getCustomLabel(AddCase_Dynamic.this,"Case")+" not save,Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetKeyContact extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog keyContactPd;
        String recordId;

        public GetKeyContact(String recordId)
        {
            this.recordId=recordId;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            keyContactPd=new ProgressDialog(AddCase_Dynamic.this);
            keyContactPd.setTitle("Wait...");
            keyContactPd.setCancelable(false);
            keyContactPd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            SoapObject resultRequestSOAP = null;
            String SOAP_ACTION = "LMServiceNamespace/GetKeyContactData";
            String METHOD_NAME = "GetKeyContactData";
            String NAMESPACE = "LMServiceNamespace";
            String URL = manager.getUrl();

            try
            {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("CompanyID", manager.getWG(AddCase_Dynamic.this));
                Request.addProperty("RECDNO", recordId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL,30000);

                transport.call(SOAP_ACTION, soapEnvelope);

                resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

                keyContactSpinnerArray = new String[resultRequestSOAP.getPropertyCount()+1];
                keyContactSpinnerArray[0] = "Select option :";
                for (int i = 0; i < resultRequestSOAP.getPropertyCount(); i++)
                {
                    SoapObject TypeEventData = (SoapObject) resultRequestSOAP .getProperty(i);
                    String key = TypeEventData.getProperty("Key").toString();
                    String value = TypeEventData.getProperty("Value").toString();

                    if(value.equals("anyType{}"))
                    {
                        value="(SELECT)";
                    }else
                    {
                        value=value+"";
                    }

                    keyContactSpinnerMap.put(value,key);
                    keyContactSpinnerArray[i+1] = value;
                }
                Log.e("00000000", "Result SECOND API: " + resultRequestSOAP);
            }
            catch (Exception ex)
            {
                Log.e("", "Error: " + ex.getMessage());
            }
            Log.e("Url==>",URL);
            Log.e("check", "Result SECOND API: " + resultRequestSOAP);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            keyContactPd.dismiss();

            ArrayAdapter<String> adapter =new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item, keyContactSpinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            key_contact_spinner.setAdapter(adapter);
        }
    }

    public static boolean isValidEmail(CharSequence target)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}