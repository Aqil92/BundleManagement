package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sevenrocks.firebasedb.Common.AnimatedListView.ListViewExt;
import com.sevenrocks.firebasedb.Common.Constants;
import com.sevenrocks.firebasedb.Common.Preferences;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sbd)
    EditText etBeginDate;

    @BindView(R.id.eed)
    EditText etEndDate;

    @BindView(R.id.tverror)
    TextView tverror;

    @BindView(R.id.cpr)
    TextView tvGo;

    private Preferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        ButterKnife.bind(this);
        init();

    }

    private  void init(){
        pref=new Preferences(this);
        setUpToolbar();
        setListener();
        setValue();

    }

    private void setValue() {
        etEndDate.setText(pref.get(Constants.endDatedtext));
        etBeginDate.setText(pref.get(Constants.beginDatetext));

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        pref.set(Constants.beginDate,"");
        pref.set(Constants.endDate,"");
        pref.set(Constants.endDatedtext,"");
        pref.set(Constants.beginDatetext,"");
        pref.commit();

        Utils.navigate(this,MainActivity.class,false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private  void setListener(){

        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(0);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etBeginDate.getText().toString().equalsIgnoreCase(""))
                    selectDate(1);
                else{
                    tverror.setVisibility(View.VISIBLE);
                    tverror.setText("Please Select beginDate than select endDate!");
                }
            }
        });

        PushDownAnim.setOnTouchPushDownAnim( tvGo )
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(etBeginDate.getText().toString().equalsIgnoreCase("") || etEndDate.getText().toString().equalsIgnoreCase("")){
                            tverror.setVisibility(View.VISIBLE);
                            tverror.setText("Please Select begin And endDate!");
                        }else{
                            Utils.navigate(SelectDateActivity.this,WorkersRatingActivity.class,true);
                        }
                    }

                } );
    }

    private void selectDate(final int i) {
        tverror.setVisibility(View.GONE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String current_Date=dayOfMonth+"-"+(monthOfYear + 1) + "-" + year;
                        Log.v("onSuccess",current_Date);
                        onDateChanged(current_Date,i);

                    }
                },
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void setUpToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Select Date Range");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onDateChanged(String current_Date,int i){
        Log.v("onSuccess","ONAqil");
        Date date=null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try{
            date = sdf.parse(current_Date);
        }catch (Exception e){

        }
        if(i==0){
            etBeginDate.setText(current_Date);
            pref.set(Constants.beginDate,""+date.getTime());
            pref.set(Constants.beginDatetext,current_Date);
            pref.commit();
       }
        else {
            pref.set(Constants.endDate,""+date.getTime());
            pref.set(Constants.endDatedtext,current_Date);
            pref.commit();
            etEndDate.setText(current_Date);
        }
        comparetowDates();
    }

    private void comparetowDates()  {
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //For declaring values in new date objects. use same date format when creating dates
            Date date1 = sdf.parse(etBeginDate.getText().toString());
            Date date2 = sdf.parse(etEndDate.getText().toString());

            if(date2.before(date1)){
                tverror.setVisibility(View.VISIBLE);
                tverror.setText("endDate should be  greater than beginDate!");
                etEndDate.setText("");
            }

        }catch (Exception e){
            Log.v("comparetowDates",e.toString());
        }

    }
}
