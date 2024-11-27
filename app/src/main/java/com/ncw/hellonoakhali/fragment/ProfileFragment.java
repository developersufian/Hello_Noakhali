package com.ncw.hellonoakhali.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ncw.hellonoakhali.DonorRegisterActivity;
import com.ncw.hellonoakhali.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView tvName, tvEmail, tvBloodGroup, tvAddress, tvPhone, tvLastDonationDate, tvTotalDonationTime, tvAge, tvHeight, tvWeight;
    private ImageView ivProfilePicture;
    private LinearLayout layContent;
    private Button btnRegDonor;

    DateCalculator dateCalculator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);
        ivProfilePicture = view.findViewById(R.id.img_profile);
        layContent = view.findViewById(R.id.lay_content);
        btnRegDonor = view.findViewById(R.id.btn_reg_donor);
        tvBloodGroup = view.findViewById(R.id.blood_group);
        tvAddress = view.findViewById(R.id.address);
        tvPhone = view.findViewById(R.id.phone);
        tvLastDonationDate = view.findViewById(R.id.last_donation);
        tvTotalDonationTime = view.findViewById(R.id.total_donations);
        tvAge = view.findViewById(R.id.age);
        tvHeight = view.findViewById(R.id.height);
        tvWeight = view.findViewById(R.id.weight);


        loadUserInfo();

        if (user != null) {
            checkIfUserExistsInDatabase(user.getUid());
        }

        btnRegDonor.setOnClickListener(v -> {
            //goto donor register activity with user id name email and photo url as intent extra
            // Inside your current activity
            Intent intent = new Intent(getContext(), DonorRegisterActivity.class);

            // Add data to intent
            intent.putExtra("USER_ID", user.getUid());
            intent.putExtra("NAME", user.getDisplayName());
            intent.putExtra("EMAIL", user.getEmail());

            if (user.getPhotoUrl() != null) {
                intent.putExtra("PHOTO_URL", user.getPhotoUrl().toString());
                Log.d("PHOTO_URL", "User photo URL: " + user.getPhotoUrl());
            } else {
                Log.e("PHOTO_URL", "User photo URL is null");
            }


            // Start the activity
            startActivity(intent);


        });


        return view;
    }


    private void loadUserInfo() {
        if (user != null) {
            tvName.setText(user.getDisplayName() != null ? user.getDisplayName() : "No Name");
            tvEmail.setText(user.getEmail() != null ? user.getEmail() : "No Email");

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(ivProfilePicture);
            } else {
                ivProfilePicture.setImageResource(R.drawable.sufian);
            }
        } else {
            Log.e(TAG, "User not found");
        }
    }

    private void checkIfUserExistsInDatabase(String userId) {
        String url = getString(R.string.web_url) + "check_user.php";
        JSONObject jsonObject = new JSONObject(new HashMap<String, String>() {{
            put("user_id", userId);
        }});
        makeVolleyRequest(url, jsonObject, response -> {
            try {
                if (response.getBoolean("success")) {
                    if (response.getBoolean("found")) {
                        JSONObject userData = response.getJSONObject("user_data"); // Get user data
                        handleUserFound(userData);  // Pass user data to the handler
                    } else {
                        handleUserNotFound();
                    }
                } else {
                    handleError(response.getString("error"));
                }
            } catch (JSONException e) {
                handleError("JSON Parsing Error: " + e.getMessage());
            }
        });
    }


    private void handleUserFound(JSONObject userData) throws JSONException {
        // Access and display user data

        //String name = userData.getString("name");
        //String email = userData.getString("email");
        String bloodGroup = userData.getString("blood_group");  // Note: key from the database
        String address = userData.getString("address");
        //String registrationDate = userData.getString("registration_date");
        String phone = userData.getString("phone");
        //String facebook = userData.getString("facebook");
        String lastDonationDate = userData.getString("last_donation_date");
        String totalDonationTime = userData.getString("total_donated");
        String dob = userData.getString("dob");
        String height = userData.getString("height");
        String weight = userData.getString("weight");
        //String userId = userData.getString("user_id");
        //String photoUrl = userData.getString("photo_url");

        dateCalculator = new DateCalculator(dob, lastDonationDate);

        tvBloodGroup.setText(bloodGroup);
        tvAddress.setText(address);
        tvPhone.setText(phone);
        tvLastDonationDate.setText(dateCalculator.getDaysSinceLastDonation() + " Days ago");
        tvTotalDonationTime.setText(totalDonationTime + " Times");
        tvAge.setText(dateCalculator.getAge() + " Years");
        tvHeight.setText(height);
        tvWeight.setText(weight+" Kg");

        tvPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        });

        layContent.setVisibility(View.VISIBLE);
        btnRegDonor.setVisibility(View.GONE);

    }


    private void handleUserNotFound() {
        layContent.setVisibility(View.GONE);
        btnRegDonor.setVisibility(View.VISIBLE);
        Log.d(TAG, "User not found in database");
    }


    private void makeVolleyRequest(String url, JSONObject jsonObject, com.android.volley.Response.Listener<JSONObject> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, listener, error -> handleError(error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<String, String>() {{
                    put("Content-Type", "application/json");
                }};
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void handleError(String errorMessage) {
        Log.e(TAG, errorMessage);
        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    private static class DateCalculator {
        private final String birthDate;
        private final String donationDate;

        public DateCalculator(String birthDate, String donationDate) {
            this.birthDate = birthDate;
            this.donationDate = donationDate;
        }

        public long getDaysSinceLastDonation() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date lastDonationDate = sdf.parse(donationDate);
                Date currentDate = new Date();

                long diffInMillis = currentDate.getTime() - lastDonationDate.getTime();
                return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        public int getAge() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date birthDate = sdf.parse(this.birthDate);

                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                dob.setTime(birthDate);

                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                return age;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}