package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.Wishlist;

import java.util.List;

/**
 * Created by hendrigunwan on 6/7/17.
 */

public class WishlistSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<Wishlist> wishlists;

    LayoutInflater flater;

    public WishlistSpinnerAdapter(Context context, List<Wishlist> wishlists) {
        this.wishlists = wishlists;
        activity = context;
        flater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return wishlists.size();
    }

    public Object getItem(int i)
    {
        return wishlists.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        txt.setBackgroundColor(Color.TRANSPARENT);
        Wishlist wishlist = wishlists.get(position);
        txt.setText(wishlist.getName());
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        View rowview = flater.inflate(R.layout.spinner_item, null,true);
        TextView txt = (TextView) rowview.findViewById(R.id.name);
        txt.setBackgroundColor(Color.TRANSPARENT);
        Wishlist wishlist = wishlists.get(i);
        txt.setText(wishlist.getName());
        return rowview;
    }
}