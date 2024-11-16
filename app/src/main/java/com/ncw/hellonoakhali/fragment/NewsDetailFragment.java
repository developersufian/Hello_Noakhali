package com.ncw.hellonoakhali.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.model.News;

import org.json.JSONException;
import org.json.JSONObject;


public class NewsDetailFragment extends Fragment {

    private TextView titleTextView, contentTextView, authorTextView, categoryTextView, tagsTextView, viewsTextView;
    private ImageView newsImageView;
    private RequestQueue requestQueue;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.titleTextView);
        contentTextView = view.findViewById(R.id.contentTextView);
        authorTextView = view.findViewById(R.id.authorTextView);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        tagsTextView = view.findViewById(R.id.tagsTextView);
        viewsTextView = view.findViewById(R.id.viewsTextView);
        newsImageView = view.findViewById(R.id.newsImageView);

        // Retrieve the news ID passed via the Bundle
        String newsId = getArguments().getString("newsId");

        // Initialize the RequestQueue for making network requests
        requestQueue = Volley.newRequestQueue(getContext());
        loadNewsDetails(newsId);

        return view;
    }

    private void loadNewsDetails(String newsId) {
        String url = "http://192.168.0.105/Hello_Noakhali/news_id.php?id=" + newsId;  // Use the news ID in the URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");

                            // Extract data from the response
                            String title = data.getString("title");
                            String content = data.getString("content");
                            String author = data.getString("author");
                            String category = data.getString("category");
                            String tags = data.getString("tags");
                            String views = data.getString("views");
                            String imageUrl = data.getString("image_url");

                            // Set the data to the views
                            titleTextView.setText(title);
                            contentTextView.setText(content);
                            authorTextView.setText("Author: " + author);
                            categoryTextView.setText("Category: " + category);
                            tagsTextView.setText("Tags: " + tags);
                            viewsTextView.setText("Views: " + views);

                            // Load the image using Glide
                            Glide.with(getContext())
                                    .load(imageUrl)
                                    .into(newsImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the Volley request queue
        requestQueue.add(request);
    }
}
