package com.ncw.hellonoakhali;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DonorRegisterActivity extends AppCompatActivity {

    private ImageView userPhoto;
    private EditText nameField, phoneNumber, address, date_of_birth, last_date_of_donation, total_donated, facebook;
    private Spinner bloodGroup;
    private Button registerButton;
    private String userId;
    private String name;
    private String email;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donor_register);


        initializeViews();
        setupBloodGroupSpinner();

        // Get the intent data
        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        photoUrl = intent.getStringExtra("PHOTO_URL");

        Log.d("DonorRegister", "User ID: " + userId);
        Log.d("DonorRegister", "Name: " + name);
        Log.d("DonorRegister", "Email: " + email);
        Log.d("DonorRegister", "Photo URL: " + photoUrl);

        nameField.setText(name);


        // Load photo using a library like Glide
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this).load(photoUrl).circleCrop().into(userPhoto);
        }
        date_of_birth.setFocusable(false);

        // Show DatePickerDialog when EditText is clicked
        date_of_birth.setOnClickListener(v -> showDatePicker("date_of_birth"));
        last_date_of_donation.setFocusable(false);
        last_date_of_donation.setOnClickListener(v -> showDatePicker("last_donation"));

        registerButton.setOnClickListener(v -> registerDonor(userId));


    }


    private void initializeViews() {
        userPhoto = findViewById(R.id.userPhoto);
        nameField = findViewById(R.id.nameField);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        bloodGroup = findViewById(R.id.bloodGroup);
        registerButton = findViewById(R.id.registerButton);
        date_of_birth = findViewById(R.id.date_of_birth);
        last_date_of_donation = findViewById(R.id.last_date_of_donation);
        total_donated = findViewById(R.id.total_donated);
        facebook = findViewById(R.id.facebook);

    }

    private void registerDonor(String userId) {
        // Validate inputs
        if (validateInputs()) {
            String selectedBloodGroup = bloodGroup.getSelectedItem().toString();
            String dateOfBirth = date_of_birth.getText().toString();
            String lastDonationDate = last_date_of_donation.getText().toString();
            String totalDonated = total_donated.getText().toString();
            String facebookLink = facebook.getText().toString();
            String phone = phoneNumber.getText().toString();
            String addres = address.getText().toString();
            String name = nameField.getText().toString();


            Map<String, Object> donorData = new HashMap<>();

            donorData.put("name", name);
            donorData.put("blood_group", selectedBloodGroup);
            donorData.put("date_of_birth", dateOfBirth);
            donorData.put("last_donation_date", lastDonationDate);
            donorData.put("total_donated", totalDonated);
            donorData.put("facebook_link", facebookLink);
            donorData.put("phone_number", phone);
            donorData.put("address", addres);


            // mysql database


            String url = getString(R.string.web_url) + "register_donor.php";

// Create JSON object for POST data
            JSONObject postData = new JSONObject();
            try {
                postData.put("user_id", userId);
                postData.put("name", name);
                postData.put("blood_group", selectedBloodGroup);
                postData.put("date_of_birth", dateOfBirth);
                postData.put("last_donation_date", lastDonationDate);
                postData.put("total_donated", totalDonated);
                postData.put("facebook_link", facebookLink);
                postData.put("phone_number", phoneNumber);
                postData.put("address", address);
                postData.put("email", email);
                postData.put("photo_url", photoUrl);

                // Creating the request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        postData,
                        response -> {
                            try {
                                boolean success = response.getBoolean("success");
                                String message = response.getString("message");
                                if (success) {
                                    Toast.makeText(DonorRegisterActivity.this, "Donor registered successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("DonorRegister", "Success: " + message);
                                    finish();
                                } else {
                                    Toast.makeText(DonorRegisterActivity.this, "Registration failed: " + message, Toast.LENGTH_SHORT).show();
                                    Log.e("DonorRegister", "Failed: " + message);
                                }
                            } catch (JSONException e) {
                                Log.e("DonorRegister", "JSON Parsing Error: " + e.getMessage());
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            String errorMessage = "An error occurred.";
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                errorMessage = new String(error.networkResponse.data);
                            } else if (error.getMessage() != null) {
                                errorMessage = error.getMessage();
                            }
                            Log.e("DonorRegisterError", "Volley Error: " + errorMessage);
                            Toast.makeText(DonorRegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );

                // Add request to RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                Log.e("DonorRegister", "JSON Creation Error: " + e.getMessage());
                e.printStackTrace();
            }

             
        }


    }

    private void showDatePicker(String date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Format the date
            String selectedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear);
            if (date.equals("date_of_birth")) {
                date_of_birth.setText(selectedDate);
                date_of_birth.setError(null);
            } else if (date.equals("last_donation")) {
                last_date_of_donation.setText(selectedDate);
                last_date_of_donation.setError(null);
            }

        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupBloodGroupSpinner() {
        String[] bloodGroups = new String[]{"Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        bloodGroup.setAdapter(adapter);
    }

    private boolean validateInputs() {
        if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError("Phone number is required");
            return false;
        }
        if (address.getText().toString().isEmpty()) {
            address.setError("Address is required");
            return false;
        }
        if (bloodGroup.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date_of_birth.getText().toString().isEmpty()) {
            date_of_birth.setError("Date of birth is required");
            return false;
        }

        if (nameField.getText().toString().isEmpty()) {
            nameField.setError("Name is required");
            return false;
        }


        return true;
    }
}