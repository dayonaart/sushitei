package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.cranium.sushiteiapps.model.PointHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by groun on 1/15/2018.
 */

public class WhatsNewPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<Article> articleResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public WhatsNewPaginationAdapter(Context context) {
        this.context = context;
        articleResults = new ArrayList<>();
    }

    public List<Article> getMovies() {
        return articleResults;
    }

    public void setArticles(List<Article> movieResults) {
        this.articleResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.whats_new_list, parent, false);
        viewHolder = new ArticleVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        switch (getItemViewType(position)) {
            case ITEM:
                final ArticleVH articleVH = (ArticleVH) holder;

                Article article = articleResults.get(position);

                articleVH.title.setText(article.getName());
                articleVH.date.setText(article.getFormattedArticleDate());
                articleVH.description.setText(article.getShortMetaDescription());

                String image = App.URL + "files/articles/list/" + article.getImage();
                Picasso.with(context).load(image).error(R.drawable.image_315x315).into(((ArticleVH) holder).image);

                /**
                 * Using Glide to handle image loading.
                 * Learn more about Glide here:
                 * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
                 */

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return articleResults == null ? 0 : articleResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == articleResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Article r) {
        articleResults.add(r);
        notifyItemInserted(articleResults.size() - 1);
    }

    public void addAll(List<Article> articleResults) {
        for (Article result : articleResults) {
            add(result);
        }
    }

    public void remove(Article r) {
        int position = articleResults.indexOf(r);
        if (position > -1) {
            articleResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Article());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = articleResults.size() - 1;
        Article result = getItem(position);

        if (result != null) {
            articleResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Article getItem(int position) {
        return articleResults.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ArticleVH extends RecyclerView.ViewHolder {
        public TextView title, date, description;
        public ImageView image;

        public ArticleVH(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title_text);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.image);
            description = (TextView) view.findViewById(R.id.description);


            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(context, WhatsNewDetailActivity.class);
                    Article article = articleResults.get(getAdapterPosition());
                    intent.putExtra("article", article);
                    context.startActivity(intent);
                }
            });
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
