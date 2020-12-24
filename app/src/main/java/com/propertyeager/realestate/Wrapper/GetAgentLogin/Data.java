
package com.propertyeager.realestate.Wrapper.GetAgentLogin;


import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("agency_logo")
    private String mAgencyLogo;
    @SerializedName("agency_name")
    private String mAgencyName;
    @SerializedName("discription")
    private String mDiscription;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("number")
    private String mNumber;
    @SerializedName("password")
    private String mPassword;

    public String getAgencyLogo() {
        return mAgencyLogo;
    }

    public void setAgencyLogo(String agencyLogo) {
        mAgencyLogo = agencyLogo;
    }

    public String getAgencyName() {
        return mAgencyName;
    }

    public void setAgencyName(String agencyName) {
        mAgencyName = agencyName;
    }

    public String getDiscription() {
        return mDiscription;
    }

    public void setDiscription(String discription) {
        mDiscription = discription;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}
