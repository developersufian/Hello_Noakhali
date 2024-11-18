package com.ncw.hellonoakhali.fragment;


import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray newsArray = response.getJSONArray("data");

                        for (int i = 0; i < newsArray.length(); i++) {
                            JSONObject newsJson = newsArray.getJSONObject(i);
                            News news = new News();
                            news.setId(newsJson.getString("id"));
                            news.setTitle(newsJson.getString("title"));
                            news.setContent(newsJson.getString("content"));
                            news.setAuthor(newsJson.getString("author"));
                            news.setCategory(newsJson.getString("category"));
                            news.setImageUrl(newsJson.getString("image_url"));
                            news.setPublishedAt(newsJson.getString("published_at"));
                            news.setViews(newsJson.getString("views"));

                            newsList.add(news);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        requestQueue.add(request);
    }

    @Override
    public void onNewsItemClick(String newsId) {
        // Handle the click event and pass the ID

        // You can open a new fragment or activity and pass the news ID
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
