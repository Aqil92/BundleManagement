package com.sevenrocks.firebasedb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevenrocks.firebasedb.BeanClasses.WorkerModel;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.R;

import java.util.ArrayList;
import java.util.Date;


public abstract class WorkAdapter extends BaseAdapter {

    private  Context context;

    private ArrayList<WorkerModel> workList;

    public View.OnClickListener viewDetail;
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public WorkAdapter(Context context, ArrayList<WorkerModel>  alist){
        this.context=context;
        this.workList =alist;
        viewDetail = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onViewClick(v,String.valueOf(v.getTag(R.string.lotno)));
            }

        };
    }

    protected abstract void onViewClick(View v, String lotno);

    @Override
    public int getCount() {
        return workList.size();
    }

    @Override
    public Object getItem(int position) {
        return workList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi =convertView;

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            vi = mInflater.inflate(R.layout.list_of_work_data_item, null);
        }

        SetGet( vi, position);

        return vi;
    }

    private void SetGet(View vi,int position) {
        final TextView tvView=(TextView)vi.findViewById(R.id.vc);
        final TextView   tvWorker=(TextView)vi.findViewById(R.id.wn);
        final TextView tvRanking=(TextView)vi.findViewById(R.id.r);
        final TextView tvPerformanceIndex=(TextView)vi.findViewById(R.id.pi);

        WorkerModel work= workList.get(position);

        Log.v("CalculateValue902","Adaper "+workList.toString());

        tvWorker.setText(work.getOverlock());
        tvRanking.setText(Utils.isNullValue(work.getRank()));
        tvPerformanceIndex.setText(Utils.isNullValue(work.getPi()));
        tvView.setTag(R.string.lotno, workList.get(position).getOverlock());
        tvView.setOnClickListener(viewDetail);
    }





  }
