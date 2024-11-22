package com.ncw.hellonoakhali.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.BloodDonorDetailsActivity;
import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.model.BloodDonor;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.BloodDonorViewHolder> {

    private List<BloodDonor> bloodDonorList;

    // Constructor
    public BloodDonorAdapter(List<BloodDonor> bloodDonorList) {
        this.bloodDonorList = bloodDonorList;
    }

    @Override
    public BloodDonorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_donor, parent, false);
        return new BloodDonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BloodDonorViewHolder holder, int position) {
        // Get the blood donor object
        BloodDonor bloodDonor = bloodDonorList.get(position);

        // Bind data to the views in the ViewHolder
        holder.nameTextView.setText(bloodDonor.getName());
        holder.bloodGroupTextView.setText(bloodDonor.getBloodGroup());
        holder.phoneTextView.setText(bloodDonor.getPhone());
        holder.addressTextView.setText(bloodDonor.getAddress());

        // You can use an image loading library like Glide or Picasso to load the profile photo
        Glide.with(holder.itemView.getContext())
                .load(bloodDonor.getPhotoUrl())
                .into(holder.photoImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), BloodDonorDetailsActivity.class);
            intent.putExtra("bloodDonor", bloodDonor.getUserId());
            holder.itemView.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return bloodDonorList.size();
    }

    // ViewHolder class to hold the views
    public static class BloodDonorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, bloodGroupTextView, phoneTextView, addressTextView;
        ImageView photoImageView;

        public BloodDonorViewHolder(View itemView) {
            super(itemView);
            // Find views by ID
            nameTextView = itemView.findViewById(R.id.nameTextView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);

            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}

