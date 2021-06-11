package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.Seat;

import java.util.List;

/**
 * Created by hendrigunwan on 6/7/17.
 */

public class SeatSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<Seat> seats;

    LayoutInflater flater;

    public SeatSpinnerAdapter(Context context, List<Seat> seats) {
        this.seats = seats;
        activity = context;
        flater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return seats.size();
    }

    public Object getItem(int i)
    {
        return seats.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        Seat seat = seats.get(position);
        txt.setText(seat.getName());
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        Seat seat = seats.get(i);
        txt.setText(seat.getName());
        return rowview;
    }
}