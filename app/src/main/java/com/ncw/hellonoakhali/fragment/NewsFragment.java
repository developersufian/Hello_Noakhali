package com.ncw.hellonoakhali.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.adapters.NewsAdapter;
import com.ncw.hellonoakhali.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements NewsAdapter.OnNewsItemClickListener {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private RequestQueue requestQueue;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(newsList, this);  // Pass the fragment as the listener
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getContext());
        loadNews();

        return view;
    }

    private void loadNews() {
        String url = getString(R.string.web_url) + "news.php"; // Your API URL here

        // Create a JsonArrayRequest to fetch data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Clear the existing list to prevent duplication
                        newsList.clear();

                        // Loop through the response and parse each news article
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            int id = jsonObject.getInt("id");
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("content");
                            String author = jsonObject.getString("author");
                            String category = jsonObject.getString("category");
                            String imageUrl = jsonObject.getString("image_url");
                            String publishedAt = jsonObject.getString("published_at");
                            String updatedAt = jsonObject.getString("updated_at");
                            String status = jsonObject.getString("status");
                            String tags = jsonObject.getString("tags");
                            int views = jsonObject.getInt("views");
                            int isFeatured = jsonObject.getInt("is_featured");

                            // Create a News object and add it to the list
                            News news = new News(id, title, content, author, category, imageUrl,
                                    publishedAt, updatedAt, status, tags, views, isFeatured);
                            newsList.add(news);
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to parse response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    Log.e("Volley Error", error.getMessage());
                });

        // Add the request to the Volley queue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onNewsItemClick(String newsId) {
        // Handle the click event and pass the ID
        Bundle bundle = new Bundle();
        bundle.putString("newsId", newsId);  // Pass the news ID
        NewsDetailFragment detailFragment = new NewsDetailFragment();
        detailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
