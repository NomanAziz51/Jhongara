
package com.propertyeager.realestate.Model.CustomerEditForm;

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
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("present_address")
    @Expose
    private String presentAddress;
    @SerializedName("present_address_city")
    @Expose
    private Object presentAddressCity;
    @SerializedName("present_address_country")
    @Expose
    private Object presentAddressCountry;
    @SerializedName("dealing_status")
    @Expose
    private String dealingStatus;
    @SerializedName("followup_datetime")
    @Expose
    private Object followupDatetime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public Object getPresentAddressCity() {
        return presentAddressCity;
    }

    public void setPresentAddressCity(Object presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public Object getPresentAddressCountry() {
        return presentAddressCountry;
    }

    public void setPresentAddressCountry(Object presentAddressCountry) {
        this.presentAddressCountry = presentAddressCountry;
    }

    public String getDealingStatus() {
        return dealingStatus;
    }

    public void setDealingStatus(String dealingStatus) {
        this.dealingStatus = dealingStatus;
    }

    public Object getFollowupDatetime() {
        return followupDatetime;
    }

    public void setFollowupDatetime(Object followupDatetime) {
        this.followupDatetime = followupDatetime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
