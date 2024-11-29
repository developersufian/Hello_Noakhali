package com.ncw.hellonoakhali.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ncw.hellonoakhali.R;
import com.ncw.hellonoakhali.model.BloodRequest;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {

    private final List<BloodRequest> bloodRequestList;

    public BloodRequestAdapter(List<BloodRequest> bloodRequestList) {
        this.bloodRequestList = bloodRequestList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BloodRequest bloodRequest = bloodRequestList.get(position);

        holder.patientIssue.setText("রোগীর সমস্যা: " + bloodRequest.getPatientIssue());
        holder.bloodGroup.setText("রক্তের গ্রুপ: " + bloodRequest.getBloodGroup());
        holder.requiredAmount.setText("প্রয়োজনীয় পরিমাণ: " + bloodRequest.getRequiredAmount());
        holder.hospitalName.setText("হাসপাতালের নাম: " + bloodRequest.getHospitalName());
        holder.contactInfo.setText("যোগাযোগের তথ্য: " + bloodRequest.getContactInfo());
        holder.requestDate.setText("প্রকাশের সময়: " + bloodRequest.getRequestDate());
        holder.urgency.setText("জরুরিতা: " + bloodRequest.getUrgency());
        holder.additionalNotes.setText("অতিরিক্ত নোট: " + bloodRequest.getAdditionalNotes());
        holder.reference.setText("রেফারেন্স: " + bloodRequest.getReference());
        holder.patientAge.setText("রোগীর বয়স: " + bloodRequest.getPatientAge());
        holder.patientGender.setText("রোগীর লিঙ্গ: " + bloodRequest.getPatientGender());
        holder.hemoglobinLevel.setText("হিমোগ্লোবিন স্তর: " + bloodRequest.getHemoglobinLevel());
        holder.dateAndTime.setText("তারিখ এবং সময়: " + bloodRequest.getDateAndTime());
        holder.status.setText("অবস্থা: " + bloodRequest.getStatus());



    }

    @Override
    public int getItemCount() {
        return bloodRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientIssue, bloodGroup, requiredAmount, hospitalName, contactInfo, requestDate, urgency, additionalNotes, reference, patientAge, patientGender, hemoglobinLevel, dateAndTime, status;

        public ViewHolder(View itemView) {
            super(itemView);
            patientIssue = itemView.findViewById(R.id.patientIssue);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
            requiredAmount = itemView.findViewById(R.id.requiredAmount);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            contactInfo = itemView.findViewById(R.id.contactInfo);
            requestDate = itemView.findViewById(R.id.requestDate);
            urgency = itemView.findViewById(R.id.urgency);
            additionalNotes = itemView.findViewById(R.id.additionalNotes);
            reference = itemView.findViewById(R.id.reference);
            patientAge = itemView.findViewById(R.id.patientAge);
            patientGender = itemView.findViewById(R.id.patientGender);
            hemoglobinLevel = itemView.findViewById(R.id.hemoglobin_level);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            status = itemView.findViewById(R.id.status);

        }
    }
}
