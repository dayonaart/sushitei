package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.WhatsNewDetailActivity;
import com.cranium.sushiteiapps.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dayona on 5/28/17.
 */

public class WhatsNewListAdapter extends RecyclerView.Adapter<WhatsNewListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Article> articleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, description;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.image);
            description = (TextView) view.findViewById(R.id.description);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(mContext, WhatsNewDetailActivity.class);
                    Article article = articleList.get(getAdapterPosition());
                    intent.putExtra("article", article);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public WhatsNewListAdapter(Context mContext, List<Article> articleList) {
        this.mContext = mContext;
        this.articleList = articleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.whats_new_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.title.setText(article.getName());
        holder.date.setText(article.getFormattedArticleDate());
        holder.description.setText(article.getShortMetaDescription());

        String image = App.URL + "files/articles/list/" + article.getImage();
        Picasso.with(mContext).load(image).error(R.drawable.image_315x315).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        articleList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Article> list) {
        articleList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Article article) {
        articleList.add(article);
        notifyDataSetChanged();
    }
}
