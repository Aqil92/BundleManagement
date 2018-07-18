package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.sevenrocks.firebasedb.Common.AnimatedListView.ListViewExt;
import com.sevenrocks.firebasedb.Common.Constants;
import com.sevenrocks.firebasedb.Common.Preferences;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkerDetailedActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DB db;

    private Preferences pref;

    @BindView(R.id.wn)
    TextView tvWorker;

    @BindView(R.id.pi)
    TextView tvPerformanceIndex;

    private String overlock="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_detailed);
        ButterKnife.bind(this);
        init();
    }

    private  void setValueForWorker(){
        tvWorker.setText(overlock);

        String endDate=getEndDate();
        double bgd=Double.parseDouble(endDate)-5097600;

        String beginDate=bgd+"";

        Log. v("setValueForWorker",beginDate+" "+endDate);

        int noOfChestValueGreen=Utils.getNoOfValue(beginDate,endDate,"\"Green\"",overlock,pref,db);
        int noOfChestValueYellow=Utils.getNoOfValue(beginDate,endDate,"\"Yellow\"",overlock,pref,db);
        int noOfChestValueRed=Utils.getNoOfValue(beginDate,endDate,"\"Red\"",overlock,pref,db);
        int TotalNoOfPiece=Utils.getTotalNo(beginDate,endDate,overlock,pref,db);
        float pi= (float) ((1*noOfChestValueGreen+0.7*noOfChestValueYellow-1*noOfChestValueRed)/TotalNoOfPiece*100);

        tvPerformanceIndex.setText(Utils.roundTwoDecimals(pi)+"%");

    }

    private  String getEndDate(){
       String query="select max(cast(scanDate as integer)) as endDate from p_index where overlock='"+overlock+"'";
       Cursor cur= db.findCursor(query);
        if(cur!=null && cur.moveToNext()){
            return cur.getString(cur.getColumnIndex("endDate"));
        }
        return  "";
    }

    private  void init(){
        db=new DB(this);
        pref=new Preferences(this);
        overlock=pref.get(Constants.workers);
        setUpToolbar();
        setValueForWorker();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Utils.navigate(this,WorkersRatingActivity.class,false);

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

    private void setUpToolbar(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(overlock+" Performance!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
