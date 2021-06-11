package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.PointHistory;

import java.util.List;

/**
 * Created by Dayona on 5/28/17.
 */

public class PointHistoryListAdapter extends RecyclerView.Adapter<PointHistoryListAdapter.MyViewHolder> {

    private Context mContext;
    private List<PointHistory> pointHistoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView point, outletName, price, balancePoint, expiredPoint ,date, bonusPoint;
        public ImageView imagePlus, imageMinus;

        public MyViewHolder(View view) {
            super(view);
            point = (TextView) view.findViewById(R.id.point);
            outletName = (TextView) view.findViewById(R.id.outlet_name);
            price = (TextView) view.findViewById(R.id.price);
            bonusPoint = (TextView) view.findViewById(R.id.point_bonus);
            balancePoint = (TextView) view.findViewById(R.id.balance_point);
            date = (TextView) view.findViewById(R.id.date);
            imagePlus = (ImageView) view.findViewById(R.id.image_plus);
            imageMinus = (ImageView) view.findViewById(R.id.image_minus);
            expiredPoint = (TextView) view.findViewById(R.id.point_expired);
        }
    }

    public PointHistoryListAdapter(Context mContext, List<PointHistory> pointHistoryList) {
        this.mContext = mContext;
        this.pointHistoryList = pointHistoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.point_history_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PointHistory pointHistory = pointHistoryList.get(position);
        String exp = "";
        if (pointHistory.getOutlet()==null)  {
            exp = "";
            Log.e("point expired","expired point");
//            holder.outletName.setTextColor(Color.RED);
        }
        else {
            exp =pointHistory.getOutlet().getName();
            holder.outletName.setTextColor(Color.BLACK);
        }

        Log.e(App.LOG," point history transtype "+pointHistory.getTransType());

        holder.point.setText(pointHistory.getPoint());
        holder.outletName.setText(exp);
        holder.price.setText("( DINE IN: " + pointHistory.getFormattedPurchaseTotal() + ")");
        holder.balancePoint.setText("Balance Poin: " + pointHistory.getBalancePoint());
        holder.date.setText(pointHistory.getFormattedPurchaseDate());

        Log.e("cranium", position + " "+pointHistory.getFormattedPurchaseDate());
        holder.bonusPoint.setText("Bonus Poin: " + pointHistory.getPointBonus());
        if (pointHistory.getOperation().equals("+") || pointHistory.getIsAdd().equals("1")) {
            holder.imageMinus.setVisibility(View.INVISIBLE);
            holder.imagePlus.setVisibility(View.VISIBLE);
            holder.expiredPoint.setText("Point Expired : -");
        } else {
            holder.imageMinus.setVisibility(View.VISIBLE);
            holder.imagePlus.setVisibility(View.GONE);
            holder.expiredPoint.setText("Point Expired : " + pointHistory.getPoint());
        }

        if(pointHistory.getTransType().equals("Void Points")) holder.expiredPoint.setVisibility(View.VISIBLE);
        else holder.expiredPoint.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return pointHistoryList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        pointHistoryList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<PointHistory> list) {
        pointHistoryList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(PointHistory list) {
        pointHistoryList.add(list);
        notifyDataSetChanged();
    }
}
