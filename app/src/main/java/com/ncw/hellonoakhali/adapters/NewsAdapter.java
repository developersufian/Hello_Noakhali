package com.ncw.hellonoakhali.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.model.News;

import java.util.List;
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private OnNewsItemClickListener mListener;

    // Constructor
    public NewsAdapter(List<News> newsList, OnNewsItemClickListener listener) {
        this.newsList = newsList;
        this.mListener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.author.setText("Author: " + news.getAuthor());
        holder.views.setText("Views: " + news.getViews());
        holder.publishedAt.setText("Published at: " + news.getPublishedAt());

        // Use Glide to load the image
        Glide.with(holder.itemView.getContext())
                .load(news.getImageUrl())
                .into(holder.image);

        // Handle item click and pass the ID
        holder.itemView.setOnClickListener(v -> mListener.onNewsItemClick(news.getId()));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, views, publishedAt;
        ImageView image;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            views = itemView.findViewById(R.id.views);
            image = itemView.findViewById(R.id.image);
            publishedAt = itemView.findViewById(R.id.publishedAt);
        }
    }

    // Interface to handle item click
    public interface OnNewsItemClickListener {
        void onNewsItemClick(String newsId);  // Pass only the ID
    }
}
