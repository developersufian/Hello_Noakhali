package com.ncw.hellonoakhali.model;

public class BloodDonor {
    private int id;
    private String userId;
    private String name;
    private String email;
    private String bloodGroup;
    private String dob;
    private String lastDonationDate;
    private int totalDonated;
    private String facebook;
    private String phone;
    private String address;
    private String photoUrl;
    private String registrationDate;

    // Constructor
    public BloodDonor(int id, String userId, String name, String email, String bloodGroup, String dob,
                      String lastDonationDate, int totalDonated, String facebook, String phone,
                      String address, String photoUrl, String registrationDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.dob = dob;
        this.lastDonationDate = lastDonationDate;
        this.totalDonated = totalDonated;
        this.facebook = facebook;
        this.phone = phone;
        this.address = address;
        this.photoUrl = photoUrl;
        this.registrationDate = registrationDate;
    }

    // Getter and setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public int getTotalDonated() {
        return totalDonated;
    }

    public void setTotalDonated(int totalDonated) {
        this.totalDonated = totalDonated;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    // ...
}

