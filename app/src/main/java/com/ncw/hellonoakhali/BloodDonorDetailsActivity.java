package com.ncw.hellonoakhali;

import static com.smarteist.autoimageslider.SliderView.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ncw.hellonoakhali.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BloodDonorDetailsActivity extends AppCompatActivity {
    private String bloodDonorId;

    private TextView tvName, tvEmail;
    private ImageView ivProfilePicture;
    DateCalculator dateCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor_details);

        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        ivProfilePicture = findViewById(R.id.img_profile);


        Intent intent = getIntent();
        bloodDonorId = intent.getStringExtra("bloodDonor");


        loadUserInfo(bloodDonorId);

    }

    private void loadUserInfo(String bloodDonorId) {
        String url = getString(R.string.web_url) + "check_user.php";
        JSONObject jsonObject = new JSONObject(new HashMap<String, String>() {{
            put("user_id", bloodDonorId);
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

        String name = userData.getString("name");
        String email = userData.getString("email");
        String bloodGroup = userData.getString("blood_group");  // Note: key from the database
        String address = userData.getString("address");
        String registrationDate = userData.getString("registration_date");
        String phone = userData.getString("phone");
        String facebook = userData.getString("facebook");
        String lastDonationDate = userData.getString("last_donation_date");
        String totalDonationTime = userData.getString("total_donated");
        String dob = userData.getString("dob");
        String userId = userData.getString("user_id");
        String photoUrl = userData.getString("photo_url");

        dateCalculator = new DateCalculator(dob, lastDonationDate);


        //tvName.setText(name);
        //tvEmail.setText(email);

        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).into(ivProfilePicture);


        Log.d("UserData", "User: " + userId);
        Log.d("UserData", "Name: " + name);
        Log.d("UserData", "Email: " + email);
        Log.d("UserData", "Blood Group: " + bloodGroup);
        Log.d("UserData", "Address: " + address);
        Log.d("UserData", "Registration Date: " + registrationDate);
        Log.d("UserData", "Phone: " + phone);
        Log.d("UserData", "Facebook: " + facebook);
        Log.d("UserData", "Last Donation Date: " + lastDonationDate);
        Log.d("UserData", "Total Donation Time: " + totalDonationTime);
        Log.d("UserData", "DOB: " + dob);
        Log.d("UserData", "Photo URL: " + photoUrl);


        //Last Donation Date: 19-11-2024 to today, total days
        //DOB: 30-07-2005 to today total years

        Log.d("UserData", "Days since last donation: " + dateCalculator.getDaysSinceLastDonation());
        Log.d("UserData", "Age: " + dateCalculator.getAge());


    }


    private void handleUserNotFound() {
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
        Volley.newRequestQueue(this).add(request);
    }

    private void handleError(String errorMessage) {
        Log.e(TAG, errorMessage);
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
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