package com.ncw.hellonoakhali.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.R;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.ncw.hellonoakhali.model.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private List<SliderItem> sliderItemList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the SliderView
        sliderView = view.findViewById(R.id.imageSlider);

        // Initialize the slider item list
        sliderItemList = new ArrayList<>();
        sliderItemList.add(new SliderItem("https://cdn.britannica.com/84/73184-050-05ED59CB/Sunflower-field-Fargo-North-Dakota.jpg", "Description 1"));
        sliderItemList.add(new SliderItem("https://cdn.mos.cms.futurecdn.net/XDYmyY2eSMZxXUtTfNMExR-1200-80.jpg", "Description 2"));
        sliderItemList.add(new SliderItem("https://i.pinimg.com/736x/7b/f4/55/7bf455dfc054e1ed0185cc3894824771.jpg", "Description 3"));

        // Initialize and set the adapter
        sliderAdapter = new SliderAdapter(getContext(), sliderItemList);
        sliderView.setSliderAdapter(sliderAdapter);

        return view;
    }

    private static class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderItemViewHolder> {

        private Context context;
        private List<SliderItem> sliderItemList;

        public SliderAdapter(Context context, List<SliderItem> sliderItemList) {
            this.context = context;
            this.sliderItemList = sliderItemList;
        }

        @Override
        public SliderItemViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent, false);
            return new SliderItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SliderItemViewHolder viewHolder, int position) {
            SliderItem sliderItem = sliderItemList.get(position);

            viewHolder.textViewDescription.setText(sliderItem.getDescription());
            viewHolder.textViewDescription.setTextSize(16);
            viewHolder.textViewDescription.setTextColor(Color.WHITE);

            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.sufian)
                    .into(viewHolder.imageViewBackground);

            /*
            viewHolder.itemView.setOnClickListener(v ->
                    Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show()
            );

             */
        }

        @Override
        public int getCount() {
            return sliderItemList.size();
        }

        public static class SliderItemViewHolder extends SliderViewAdapter.ViewHolder {
            ImageView imageViewBackground;
            TextView textViewDescription;

            public SliderItemViewHolder(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            }
        }
    }
}
