package com.sevenrocks.firebasedb.DashBoardActivtiy;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sevenrocks.firebasedb.Adapters.DemoBase;
import com.sevenrocks.firebasedb.Common.Constants;
import com.sevenrocks.firebasedb.Common.Preferences;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PieChartActivity extends DemoBase {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sp)
    TextView tvscanncedPiece;

    @BindView(R.id.usp)
    TextView tvunScannedPiece;

    @BindView(R.id.sut)
    TextView tvscannedUnsannedText;

    @BindView(R.id.pcc)
    PieChart pieChestChart;
    
    @BindView(R.id.pcs)
    PieChart pieShoulderChart;
    
    @BindView(R.id.pcl)
    PieChart pieLenthcChart;

    @BindView(R.id.ctg)
    TextView tvctg;

    @BindView(R.id.cty)
    TextView tvcty;

    @BindView(R.id.ctr)
    TextView tvctr;


    @BindView(R.id.stg)
    TextView tvstg;

    @BindView(R.id.sty)
    TextView tvsty;

    @BindView(R.id.str)
    TextView tvstr;


    @BindView(R.id.ltg)
    TextView tvltg;

    @BindView(R.id.lty)
    TextView tvlty;

    @BindView(R.id.ltr)
    TextView tvltr;


    private Preferences pref;

    private DB db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        ButterKnife.bind(this);
        init();
    }

    private   void init() {
        pref = new Preferences(this);
        db = new DB(this);
        setUpToolbar();
        setSannedAndUnScannedPieceValues();
        setUpPieChart("chest",pieChestChart,"ChestChart");
        setUpPieChart("shoulder",pieShoulderChart,"ShoulderChart");
        setUpPieChart("lengths",pieLenthcChart,"LengthChart");
    }

    private void setUpPieChart(String valueFor,PieChart piChart,String title){

        piChart.setUsePercentValues(true);
        piChart.getDescription().setEnabled(false);
        piChart.setExtraOffsets(5, 10, 5, 5);

        piChart.setDragDecelerationFrictionCoef(0.95f);



        piChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "light.ttf"));
        piChart.setCenterText(generateCenterSpannableText(title));

        piChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        piChart.setDrawHoleEnabled(true);
        piChart.setHoleColor(Color.WHITE);

        piChart.setTransparentCircleColor(Color.WHITE);
        piChart.setTransparentCircleAlpha(110);

        piChart.setHoleRadius(58f);
        piChart.setTransparentCircleRadius(61f);

        piChart.setDrawCenterText(true);

        piChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        piChart.setRotationEnabled(true);
        piChart.setHighlightPerTapEnabled(true);

        // pieShoulderChart.setUnit(" €");
        // pieShoulderChart.setDrawUnitsInChart(true);

        // add a selection listener
     //   pieShoulderChart.setOnChartValueSelectedListener(this);

        setData(valueFor, 100,piChart);

        piChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // pieShoulderChart.spin(2000, 0, 360);

        Legend l = piChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private SpannableString generateCenterSpannableText(String title) {
        int min=title.length();
        int max=title.length()+1;
        SpannableString s = new SpannableString(title+"\ndeveloped by Mohd Aqil");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, min, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), min, s.length() - max, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), min, s.length() - min, 0);
        s.setSpan(new RelativeSizeSpan(.8f), min, s.length() - max, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - min, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 9, s.length(), 0);
        return s;
    }

    private void setData(String Valuefor, float range,PieChart piC) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        String queryForTototal="select * from piece where lotNo='"+pref.get(Constants.lotno)+"'";
        String queeyforGreen=queryForTototal+" and "+Valuefor+" = '\"Green\"'";
        String queeyforYellow=queryForTototal+" and "+Valuefor+" = '\"Yellow\"'";
        String queeyforRed=queryForTototal+" and "+Valuefor+" = '\"Red\"'";

        float totalCount=db.findCursor(queryForTototal).getCount();
        float greenCount=db.findCursor(queeyforGreen).getCount();
        float yellowCount=db.findCursor(queeyforYellow).getCount();
        float redCount=db.findCursor(queeyforRed).getCount();

       /* Log.v("ValueOfCharts","\n"+Valuefor+" "+totalCount+" "+greenCount+" "+yellowCount+" "+redCount);
        Log.v("ValueOfCharts","\n"+queeyforGreen+"\n"+queeyforYellow+"\n"+queeyforRed);*/

        float greenPerCent=greenCount*100/totalCount;
        float yellowPerCent=yellowCount*100/totalCount;
        float RedPerCent=redCount*100/totalCount;


        setAcctualValues(Valuefor,Utils.roundTwoDecimals(greenPerCent),Utils.roundTwoDecimals(yellowPerCent),Utils.roundTwoDecimals(RedPerCent));
/*
        Log.v("ValueOfCharts",Valuefor+" "+greenPerCent+" "+yellowPerCent+" "+RedPerCent+" ");*/

            entries.add(new PieEntry(greenPerCent,"Green"));//green
            entries.add(new PieEntry(yellowPerCent,"Yellow"));//yellow
            entries.add(new PieEntry(RedPerCent,"Red"));//red

        PieDataSet dataSet = new PieDataSet(entries, "Lot Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(56,142,60));

        colors.add(Color.rgb(255,184,5));
        colors.add(Color.rgb(194,0,36));
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
       // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        data.setValueTypeface(mTfLight);
        piC.setData(data);

        // undo all highlights
        piC.highlightValues(null);

        piC.invalidate();
    }

    private void setAcctualValues(String title,double green,double yellow,double red){
        switch (title){
            case "chest":
            tvctg.setText(green+"%");
            tvcty.setText(yellow+"%");
            tvctr.setText(red+"%");break;

            case "shoulder":
            tvstg.setText(green+"%");
            tvsty.setText(yellow+"%");
            tvstr.setText(red+"%");break;

            case "lengths":
            tvltg.setText(green+"%");
            tvlty.setText(yellow+"%");
            tvltr.setText(red+"%");break;
        }
    }

    @Override
    public void onBackPressed(){
      super.onBackPressed();
      Utils.navigate(this,ListOfLotActivity.class,false);
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
        toolbar.setTitle("PieCharts Of lotNo "+pref.get(Constants.lotno));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setSannedAndUnScannedPieceValues(){

        String queryForScanned="select scanStatus from piece where lotNo='"+pref.get(Constants.lotno)+"' and Ltrim(Rtrim(scanStatus))='\"Scanned\"'";
        String queryForUnScanned="select scanStatus from piece where lotNo='"+pref.get(Constants.lotno)+"' and Ltrim(Rtrim(scanStatus))='\"Unscanned\"'";
        tvscanncedPiece.setText(db.findCursor(queryForScanned).getCount()+"");
        tvscannedUnsannedText.setText(">No of scanned and unscanned pieces of in "+pref.get(Constants.lotno)+".");
        tvunScannedPiece.setText(db.findCursor(queryForUnScanned).getCount()+"");

    }

}
