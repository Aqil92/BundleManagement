package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sevenrocks.firebasedb.Common.MDToast;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ln)
    TextView tvLotNo;

    @BindView(R.id.pi)
    TextView tvPerIndex;

    private int exitCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpToolbar();
        setAnimatedClick();
    }

    private void setAnimatedClick(){
        PushDownAnim.setOnTouchPushDownAnim( tvLotNo )
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Utils.navigate(MainActivity.this,ListOfLotActivity.class,true);
                    }

                } ); 
        
        PushDownAnim.setOnTouchPushDownAnim( tvPerIndex )
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Utils.navigate(MainActivity.this,SelectDateActivity.class,true);
                    }

                } );
        
    }

    private void setUpToolbar(){
         toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("SevenRoks!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onBackPressed() {


        if (exitCheck == 0) {
            MDToast.makeText(this,"Press again to exit",MDToast.LENGTH_LONG,MDToast.TYPE_WARNING);
            exitCheck = 1;
        } else {

            finish();

        }
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds

                    sleep(1 * 3000);
                    exitCheck = 0;
                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();


    }



}