package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MenuDetailListActivity;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.squareup.picasso.Picasso;
import com.vlk.multimager.utils.Image;

import java.util.List;

/**
 * Created by Dayona on 5/31/17.
 */

public class CommentDeleteAdapter extends RecyclerView.Adapter<CommentDeleteAdapter.MyViewHolder> {

    private Context mContext;
    private List<Image> imageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.btn_closeupload);

        }
    }

    public CommentDeleteAdapter(Context mContext, List<Image> menuList) {
        this.mContext = mContext;
        this.imageList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.btn_del_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Image hotMenu = imageList.get(position);
        holder.image.setImageResource(R.drawable.close);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        imageList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Image> menuList) {
        menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    public void add(Image menu) {
        imageList.add(menu);
        notifyDataSetChanged();
    }
}
