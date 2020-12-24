
package com.propertyeager.realestate.Model.CustomerForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("branch_id")
    @Expose
    private String branchId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("son_of")
    @Expose
    private String sonOf;
    @SerializedName("cnic")
    @Expose
    private String cnic;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("phoneno")
    @Expose
    private String phoneno;
    @SerializedName("officeno")
    @Expose
    private String officeno;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("passport_no")
    @Expose
    private String passportNo;
    @SerializedName("present_address")
    @Expose
    private String presentAddress;
    @SerializedName("present_address_city")
    @Expose
    private String presentAddressCity;
    @SerializedName("present_address_country")
    @Expose
    private String presentAddressCountry;
    @SerializedName("alternate_address")
    @Expose
    private String alternateAddress;
    @SerializedName("alternate_address_city")
    @Expose
    private String alternateAddressCity;
    @SerializedName("alternate_address_country")
    @Expose
    private String alternateAddressCountry;
    @SerializedName("dealing_status")
    @Expose
    private String dealingStatus;
    @SerializedName("reason_of_notinterested")
    @Expose
    private String reasonOfNotinterested;
    @SerializedName("interested_in")
    @Expose
    private String interestedIn;
    @SerializedName("followup_datetime")
    @Expose
    private String followupDatetime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getSonOf() {
        return sonOf;
    }

    public void setSonOf(String sonOf) {
        this.sonOf = sonOf;
    }

    public Object getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Object getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public Object getOfficeno() {
        return officeno;
    }

    public void setOfficeno(String officeno) {
        this.officeno = officeno;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Object getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPresentAddressCity() {
        return presentAddressCity;
    }

    public void setPresentAddressCity(String presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public String getPresentAddressCountry() {
        return presentAddressCountry;
    }

    public void setPresentAddressCountry(String presentAddressCountry) {
        this.presentAddressCountry = presentAddressCountry;
    }

    public Object getAlternateAddress() {
        return alternateAddress;
    }

    public void setAlternateAddress(String alternateAddress) {
        this.alternateAddress = alternateAddress;
    }

    public Object getAlternateAddressCity() {
        return alternateAddressCity;
    }

    public void setAlternateAddressCity(String alternateAddressCity) {
        this.alternateAddressCity = alternateAddressCity;
    }

    public Object getAlternateAddressCountry() {
        return alternateAddressCountry;
    }

    public void setAlternateAddressCountry(String alternateAddressCountry) {
        this.alternateAddressCountry = alternateAddressCountry;
    }

    public String getDealingStatus() {
        return dealingStatus;
    }

    public void setDealingStatus(String dealingStatus) {
        this.dealingStatus = dealingStatus;
    }

    public String getReasonOfNotinterested() {
        return reasonOfNotinterested;
    }

    public void setReasonOfNotinterested(String reasonOfNotinterested) {
        this.reasonOfNotinterested = reasonOfNotinterested;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getFollowupDatetime() {
        return followupDatetime;
    }

    public void setFollowupDatetime(String followupDatetime) {
        this.followupDatetime = followupDatetime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
