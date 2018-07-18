package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sevenrocks.firebasedb.Adapters.LotAdapter;
import com.sevenrocks.firebasedb.Common.AnimatedListView.ListViewExt;
import com.sevenrocks.firebasedb.Common.Constants;
import com.sevenrocks.firebasedb.Common.MDToast;
import com.sevenrocks.firebasedb.Common.Preferences;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfLotActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llList)
    ListViewExt llList;

    private LotAdapter lotAdapter;

    private  DB db;

    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lot);
        ButterKnife.bind(this);
        setUpToolbar();

        db =new DB(this);
        pref=new Preferences(this);

        setAdapter();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Utils.navigate(this,MainActivity.class,false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Lot Number's");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    private void setAdapter() {

        lotAdapter=new LotAdapter(this,Utils.getLotData(db)) {
            @Override
            protected void onViewClick(View v, String lotno) {
               // Toast.makeText(ListOfLotActivity.this, lotno, Toast.LENGTH_SHORT).show();
                pref.set(Constants.lotno,lotno);
                pref.commit();
                MDToast.makeText(ListOfLotActivity.this,lotno+" Selected!",MDToast.LENGTH_SHORT,MDToast.TYPE_SUCCESS);
                Utils.navigate(ListOfLotActivity.this,PieChartActivity.class,true);
            }
        };
        llList.setAdapter(lotAdapter);




    }
}
