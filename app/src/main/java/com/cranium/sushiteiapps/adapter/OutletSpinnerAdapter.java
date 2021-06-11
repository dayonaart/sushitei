package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.Outlet;

import java.util.List;

/**
 * Created by hendrigunwan on 6/7/17.
 */

public class OutletSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<Outlet> outlets;

    LayoutInflater flater;

    public OutletSpinnerAdapter(Context context, List<Outlet> outlets) {
        this.outlets = outlets;
        activity = context;
        flater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return outlets.size();
    }

    public Object getItem(int i)
    {
        return outlets.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        Outlet outlet = outlets.get(position);
        txt.setText(outlet.getName());
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        Outlet outlet = outlets.get(i);
        txt.setText(outlet.getName());
        return rowview;
    }
}