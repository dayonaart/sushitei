package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.OutletCity;

import java.util.List;

/**
 * Created by hendrigunwan on 6/7/17.
 */

public class OutletCitySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<OutletCity> outletCities;

    LayoutInflater flater;

    public OutletCitySpinnerAdapter(Context context, List<OutletCity> outletCities) {
        this.outletCities = outletCities;
        activity = context;
        flater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return outletCities.size();
    }

    public Object getItem(int i)
    {
        return outletCities.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        OutletCity outletCity = outletCities.get(position);
        txt.setText(outletCity.getName());
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        OutletCity outletCity = outletCities.get(i);
        txt.setText(outletCity.getName());
        return rowview;
    }
}