package com.ncw.hellonoakhali;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.model.News;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        News news = (News) getIntent().getSerializableExtra("news");

        TextView title = findViewById(R.id.detail_title);
        TextView content = findViewById(R.id.detail_content);
        ImageView image = findViewById(R.id.detail_image);

        title.setText(news.getTitle());
        content.setText(news.getContent());

        Glide.with(this)
                .load(news.getImageUrl())
                .into(image);

    }
}
