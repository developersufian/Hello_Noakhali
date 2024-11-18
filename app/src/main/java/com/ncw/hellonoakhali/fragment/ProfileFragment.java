package com.ncw.hellonoakhali.fragment;

import android.app.Dialog;
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
import com.ncw.hellonoakhali.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView tvName, tvEmail;
    private ImageView ivProfilePicture;
    private LinearLayout layContent;
    private Button btnRegDonor;

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

        loadUserInfo();

        if (user != null) {
            checkIfUserExistsInDatabase(user.getUid());
        }

        btnRegDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto register activity with user id name email and photo url

            }
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

        //id`, `user_id`, `name`, `email`, `age`, `blood_group`, `location`, `registration_date`, `phone`, `facebook`, `last_donation_date`, `total_donation_time`
        String id = userData.getString("id");
        String name = userData.getString("name");
        String email = userData.getString("email");
        String bloodGroup = userData.getString("blood_group");  // Note: key from the database
        String location = userData.getString("location");
        String registrationDate = userData.getString("registration_date");
        String phone = userData.getString("phone");
        String facebook = userData.getString("facebook");
        String lastDonationDate = userData.getString("last_donation_date");
        String totalDonationTime = userData.getString("total_donation_time");
        String age = userData.getString("age");
        String userId = userData.getString("user_id");

        //Toast.makeText(getContext(), "User found: Name " + name + ", Email " + email + " Age " + age + " Blood Group " + bloodGroup + " Location " + location + "", Toast.LENGTH_SHORT).show();

        //tvName.setText(name);
        //tvEmail.setText(email);


        // ... (Use the data to populate your UI elements) ...

        layContent.setVisibility(View.VISIBLE);
        btnRegDonor.setVisibility(View.GONE);
        Log.d(TAG, "User found: " + name + ", " + email + ", etc.");  // Log or display the data as needed

    }


    private void handleUserNotFound() {
        layContent.setVisibility(View.GONE);
        btnRegDonor.setVisibility(View.VISIBLE);
        Log.d(TAG, "User not found in database");
    }


// ... rest of your Java code (makeVolleyRequest, handleError, etc.)


// ... rest of your Java code (makeVolleyRequest, handleError, etc.)


    private void makeVolleyRequest(String url, JSONObject jsonObject, com.android.volley.Response.Listener<JSONObject> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                listener,
                error -> handleError(error.getMessage())) {
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
}