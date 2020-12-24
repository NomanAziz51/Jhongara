
package com.propertyeager.realestate.Wrapper.GetLeaderBoard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopleadsAllOfficesTop3 {

    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("agent_picture")
    @Expose
    private String agentPicture;
    @SerializedName("branchmanager")
    @Expose
    private String branchmanager;
    @SerializedName("branchname")
    @Expose
    private String branchname;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentPicture() {
        return agentPicture;
    }

    public void setAgentPicture(String agentPicture) {
        this.agentPicture = agentPicture;
    }

    public String getBranchmanager() {
        return branchmanager;
    }

    public void setBranchmanager(String branchmanager) {
        this.branchmanager = branchmanager;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

}
