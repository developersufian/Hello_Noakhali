package com.ncw.hellonoakhali.model;

public class BloodRequest {

    private int id;
    private String patientIssue;
    private String bloodGroup;
    private String requiredAmount;
    private String requestDate;
    private String hospitalName;
    private String contactInfo;

    private String status;
    private String urgency;
    private String additionalNotes;
    private String reference;
    private String patientAge;
    private String patientGender;
    private String hemoglobinLevel;
    private String dateAndTime;


    // Constructor
    public BloodRequest(int id, String patientIssue, String bloodGroup, String requiredAmount, String requestDate, String hospitalName, String contactInfo, String status, String urgency, String additionalNotes, String reference, String patientAge, String patientGender, String hemoglobinLevel, String dateAndTime) {
        this.id = id;
        this.patientIssue = patientIssue;
        this.bloodGroup = bloodGroup;
        this.requiredAmount = requiredAmount;
        this.requestDate = requestDate;
        this.hospitalName = hospitalName;
        this.contactInfo = contactInfo;
        this.status = status;
        this.urgency = urgency;
        this.additionalNotes = additionalNotes;
        this.reference = reference;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.hemoglobinLevel = hemoglobinLevel;
        this.dateAndTime = dateAndTime;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientIssue() {
        return patientIssue;
    }

    public void setPatientIssue(String patientIssue) {
        this.patientIssue = patientIssue;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(String requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getHemoglobinLevel() {
        return hemoglobinLevel;
    }

    public void setHemoglobinLevel(String hemoglobinLevel) {
        this.hemoglobinLevel = hemoglobinLevel;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
