package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;

/**
 * Created by cranium-01 on 06/06/17.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater;
    String[] title;

    public SpinnerAdapter(Context context, String[] title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListContent holder;
        View v = convertView;
        //if (v == null) {
            mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.spinner_item, null);
            holder = new ListContent();
            holder.name = (TextView) v.findViewById(R.id.name);

            v.setTag(holder.name);
//        } else {
//            holder = new ListContent();
//            holder.name = (TextView) v.findViewById(R.id.name);
//
//            v.setTag(holder.name);
//        }

        holder.name.setText(title[position]);

        return v;
    }

    protected static class ListContent {
        TextView name;
    }
}
