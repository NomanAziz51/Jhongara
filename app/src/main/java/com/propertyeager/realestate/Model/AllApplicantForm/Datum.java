
package com.propertyeager.realestate.Model.AllApplicantForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("branch_id")
    @Expose
    private String branchId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("son_of")
    @Expose
    private Object sonOf;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("cnic")
    @Expose
    private Object cnic;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("phoneno")
    @Expose
    private Object phoneno;
    @SerializedName("officeno")
    @Expose
    private Object officeno;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("profession")
    @Expose
    private Object profession;
    @SerializedName("passport_no")
    @Expose
    private Object passportNo;
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
    private Object alternateAddress;
    @SerializedName("alternate_address_city")
    @Expose
    private Object alternateAddressCity;
    @SerializedName("alternate_address_country")
    @Expose
    private Object alternateAddressCountry;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("applicant_signature_img")
    @Expose
    private Object applicantSignatureImg;
    @SerializedName("as_cnic_front_img")
    @Expose
    private Object asCnicFrontImg;
    @SerializedName("as_cnic_back_img")
    @Expose
    private Object asCnicBackImg;
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
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Object getSonOf() {
        return sonOf;
    }

    public void setSonOf(Object sonOf) {
        this.sonOf = sonOf;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getCnic() {
        return cnic;
    }

    public void setCnic(Object cnic) {
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

    public void setPhoneno(Object phoneno) {
        this.phoneno = phoneno;
    }

    public Object getOfficeno() {
        return officeno;
    }

    public void setOfficeno(Object officeno) {
        this.officeno = officeno;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getProfession() {
        return profession;
    }

    public void setProfession(Object profession) {
        this.profession = profession;
    }

    public Object getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(Object passportNo) {
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

    public void setAlternateAddress(Object alternateAddress) {
        this.alternateAddress = alternateAddress;
    }

    public Object getAlternateAddressCity() {
        return alternateAddressCity;
    }

    public void setAlternateAddressCity(Object alternateAddressCity) {
        this.alternateAddressCity = alternateAddressCity;
    }

    public Object getAlternateAddressCountry() {
        return alternateAddressCountry;
    }

    public void setAlternateAddressCountry(Object alternateAddressCountry) {
        this.alternateAddressCountry = alternateAddressCountry;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getApplicantSignatureImg() {
        return applicantSignatureImg;
    }

    public void setApplicantSignatureImg(Object applicantSignatureImg) {
        this.applicantSignatureImg = applicantSignatureImg;
    }

    public Object getAsCnicFrontImg() {
        return asCnicFrontImg;
    }

    public void setAsCnicFrontImg(Object asCnicFrontImg) {
        this.asCnicFrontImg = asCnicFrontImg;
    }

    public Object getAsCnicBackImg() {
        return asCnicBackImg;
    }

    public void setAsCnicBackImg(Object asCnicBackImg) {
        this.asCnicBackImg = asCnicBackImg;
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

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}
