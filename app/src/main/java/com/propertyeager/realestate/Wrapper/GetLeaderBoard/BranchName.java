
package com.propertyeager.realestate.Wrapper.GetLeaderBoard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchName {

    @SerializedName("branchname")
    @Expose
    private String branchname;

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

}
