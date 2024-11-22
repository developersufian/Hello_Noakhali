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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.adapters.BloodDonorAdapter;
import com.ncw.hellonoakhali.adapters.BloodRequestAdapter;
import com.ncw.hellonoakhali.model.BloodDonor;
import com.ncw.hellonoakhali.model.BloodRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BloodDonorFragment extends Fragment {
    private RecyclerView recyclerView;
    private BloodDonorAdapter adapter;
    private List<BloodDonor> bloodDonorList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blood_donor, container, false);

        recyclerView = view.findViewById(R.id.recyclerView); // Assuming recyclerView is in fragment_blood_request.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the list
        bloodDonorList = new ArrayList<>();

        loadBloodDonor();


        return view;
    }


    private void loadBloodDonor() {
        String url = getString(R.string.web_url) + "blood_donor.php"; // Your API URL here

        // Create a JsonArrayRequest to fetch data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Clear the existing list to prevent duplication
                        bloodDonorList.clear();

                        // Loop through the response and parse each blood donor
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Parse fields from the JSON object
                            int id = jsonObject.getInt("id");
                            String userId = jsonObject.getString("user_id");
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            String bloodGroup = jsonObject.getString("blood_group");
                            String dob = jsonObject.getString("dob"); // Date of Birth
                            String lastDonationDate = jsonObject.getString("last_donation_date");
                            int totalDonated = jsonObject.getInt("total_donated");
                            String facebook = jsonObject.getString("facebook");
                            String phone = jsonObject.getString("phone");
                            String address = jsonObject.getString("address");
                            String photoUrl = jsonObject.getString("photo_url");
                            String registrationDate = jsonObject.getString("registration_date");

                            // Create a BloodDonor object and set the fields
                            BloodDonor bloodDonor = new BloodDonor(id, userId, name, email, bloodGroup, dob,
                                    lastDonationDate, totalDonated, facebook,
                                    phone, address, photoUrl, registrationDate);

                            // Add the BloodDonor object to the list
                            bloodDonorList.add(bloodDonor);
                        }

                        // Initialize the adapter and set it to RecyclerView
                        adapter = new BloodDonorAdapter(bloodDonorList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to parse response", Toast.LENGTH_SHORT).show();
                        Log.e("JSON Parsing Error", e.getMessage());
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    Log.e("Volley Error", error.getMessage());
                });

        // Create a request queue and add the request
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }

}
