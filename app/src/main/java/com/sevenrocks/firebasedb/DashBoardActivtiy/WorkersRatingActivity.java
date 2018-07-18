package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sevenrocks.firebasedb.Adapters.LotAdapter;
import com.sevenrocks.firebasedb.Adapters.WorkAdapter;
import com.sevenrocks.firebasedb.BeanClasses.WorkerModel;
import com.sevenrocks.firebasedb.Common.AnimatedListView.ListViewExt;
import com.sevenrocks.firebasedb.Common.Constants;
import com.sevenrocks.firebasedb.Common.MDToast;
import com.sevenrocks.firebasedb.Common.Preferences;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkersRatingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llList)
    ListViewExt llList;

     @BindView(R.id.tch)
    TextView tvChooseDate;

    private DB db;

    private Preferences pref;

    WorkAdapter workAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_rating);

        ButterKnife.bind(this);
        init();
    }


    private  void init(){

        db=new DB(this);
        pref=new Preferences(this);
        setUpToolbar();
        getOverLockName();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Utils.navigate(this,SelectDateActivity.class,false);

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
        toolbar.setTitle("Workers Rating!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getOverLockName(){
        ArrayList<String> overlockList=new ArrayList();
        String query="select overlock from p_index where CAST(scanDate AS integer) between CAST('"+pref.get(Constants.beginDate)+"' AS integer) and CAST('"+pref.get(Constants.endDate)+"' AS integer) group by overlock";
        Cursor cur=db.findCursor(query);
        Log.v("CalculateValue",""+cur.getCount());
        if(cur!=null && cur.moveToNext()){
            for(int i=0;i<cur.getCount();i++){
                String overlock=cur.getString(cur.getColumnIndex("overlock"));
                if(overlock!=null && !overlock.equalsIgnoreCase("\"\"") && !overlock.equalsIgnoreCase("")){
                    overlockList.add(cur.getString(cur.getColumnIndex("overlock")));
                }
                cur.moveToNext();
            }
            cur.close();
        }
        Log.v("CalculateValue123","overlockList "+overlockList);
        CalculateRanking(overlockList);



    }

    private void CalculateRanking( ArrayList<String> overlockList) {
        db.truncate("dashboard");
        for(int i=0;i<overlockList.size();i++){
            HashMap<String ,String > map=new HashMap();
            String overlock=overlockList.get(i);
            map.put("overlock",overlock);
            int noOfChestValueGreen=Utils.getNoOfValue(pref.get(Constants.beginDate),pref.get(Constants.endDate),"\"Green\"",overlock,pref,db);
            int noOfChestValueYellow=Utils.getNoOfValue(pref.get(Constants.beginDate),pref.get(Constants.endDate),"\"Yellow\"",overlock,pref,db);
            int noOfChestValueRed=Utils.getNoOfValue(pref.get(Constants.beginDate),pref.get(Constants.endDate),"\"Red\"",overlock,pref,db);
            int TotalNoOfPiece=Utils.getTotalNo(pref.get(Constants.beginDate),pref.get(Constants.endDate),overlock,pref,db);
            float pi= (float) ((1*noOfChestValueGreen+0.7*noOfChestValueYellow-1*noOfChestValueRed)/TotalNoOfPiece*100);
            map.put("pi",Utils.roundTwoDecimals(pi)+"%");
            db.insert("dashboard",map);
        }
     //   getDashboardValue();
        setAdaper();
    }



    private ArrayList<WorkerModel> getDashboardValue(){
         ArrayList<WorkerModel> workerLlist=new ArrayList();
        Cursor cur=db.findCursor("Select * from dashboard order by pi DESC");
        if(cur!=null && cur.moveToNext()){
            for(int i=0;i<cur.getCount();i++){
                int j=i+1;
                WorkerModel workerModel=new WorkerModel();
                workerModel.setRank(String.valueOf(j));
                workerModel.setPi(cur.getString(cur.getColumnIndex("pi")));
                workerModel.setOverlock(cur.getString(cur.getColumnIndex("overlock")));
                workerLlist.add(workerModel);
                cur.moveToNext();
            }
            cur.close();
        }
        Log.v("CalculateValue542","overlockList "+workerLlist);
        if(workerLlist.size()==0)
            tvChooseDate.setVisibility(View.VISIBLE);
        else
            tvChooseDate.setVisibility(View.GONE);

        return  workerLlist;
    }

    private  void setAdaper(){
        workAdaper=new WorkAdapter(WorkersRatingActivity.this,getDashboardValue()) {
            @Override
            protected void onViewClick(View v, String workername) {
                pref.set(Constants.workers,workername);
                pref.commit();
                 Utils.navigate(WorkersRatingActivity.this,WorkerDetailedActivity.class,true);
            }
        };
        llList.setAdapter(workAdaper);

    }
}
