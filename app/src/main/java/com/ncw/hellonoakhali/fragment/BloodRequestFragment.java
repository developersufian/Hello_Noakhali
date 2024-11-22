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
import com.ncw.hellonoakhali.adapters.BloodRequestAdapter;
import com.ncw.hellonoakhali.model.BloodRequest;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BloodRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private BloodRequestAdapter adapter;
    private List<BloodRequest> bloodRequestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blood_request, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView); // Assuming recyclerView is in fragment_blood_request.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the list
        bloodRequestList = new ArrayList<>();

        // Load blood request data
        loadRequest();

        return view;
    }

    private void loadRequest() {
        String url = getString(R.string.web_url) + "last_week_request.php"; // Your API URL here

        // Create a JsonArrayRequest to fetch data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Clear the existing list to prevent duplication
                        bloodRequestList.clear();

                        // Loop through the response and parse each blood request
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            int id = jsonObject.getInt("request_id");
                            String patientIssue = jsonObject.getString("patient_issue");
                            String bloodGroup = jsonObject.getString("blood_group");
                            String requiredAmount = jsonObject.getString("required_amount");
                            String requestDate = jsonObject.getString("request_date");
                            String hospitalName = jsonObject.getString("hospital_name");
                            String contactInfo = jsonObject.getString("contact_info");
                            String status = jsonObject.getString("status");
                            String urgency = jsonObject.getString("urgency");
                            String additionalNotes = jsonObject.getString("additional_notes");
                            String reference = jsonObject.getString("reference");
                            String patientAge = jsonObject.getString("age");
                            String patientGender = jsonObject.getString("gender");
                            String hemoglobinLevel = jsonObject.getString("hemoglobin_level");
                            String dateAndTime = jsonObject.getString("donation_date_time");


                            // Create a BloodRequest object and add it to the list
                            BloodRequest bloodRequest = new BloodRequest(id, patientIssue, bloodGroup, requiredAmount, requestDate, hospitalName, contactInfo, status, urgency, additionalNotes, reference, patientAge, patientGender, hemoglobinLevel, dateAndTime);
                            bloodRequestList.add(bloodRequest);
                        }

                        // Initialize the adapter and set it to RecyclerView
                        adapter = new BloodRequestAdapter(bloodRequestList);
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
