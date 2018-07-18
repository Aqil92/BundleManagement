package com.sevenrocks.firebasedb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sevenrocks.firebasedb.BeanClasses.LotBean;
import com.sevenrocks.firebasedb.Common.Utils;
import com.sevenrocks.firebasedb.R;

import java.util.ArrayList;
import java.util.Date;



public abstract class LotAdapter extends BaseAdapter {

    private  Context context;

    private Date ct;

    private ArrayList<LotBean> LotList;

    public View.OnClickListener viewDetail;
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public LotAdapter(Context context, ArrayList<LotBean>  alist){
        this.context=context;
        this.LotList =alist;
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
        return LotList.size();
    }

    @Override
    public Object getItem(int position) {
        return LotList.get(position);
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
            vi = mInflater.inflate(R.layout.list_of_lot_data_item, null);
        }

        SetGet( vi, position);

        return vi;
    }

    private void SetGet(View vi,int position) {
        final TextView tvView=(TextView)vi.findViewById(R.id.vc);
        final TextView   tvLotno=(TextView)vi.findViewById(R.id.ln);
        final TextView tvScanStatus=(TextView)vi.findViewById(R.id.ss);
        final TextView tvStyleCode=(TextView)vi.findViewById(R.id.sc);

        LotBean lotBean= LotList.get(position);

        tvLotno.setText(lotBean.getLotno());
        tvScanStatus.setText(Utils.isNullValue(lotBean.getScanStatus()));
        tvStyleCode.setText(Utils.isNullValue(lotBean.getStyleCode()));
        tvView.setTag(R.string.lotno, LotList.get(position).getLotno());
        tvView.setOnClickListener(viewDetail);
    }





  }
