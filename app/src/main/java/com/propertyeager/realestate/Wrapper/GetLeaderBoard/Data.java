
package com.propertyeager.realestate.Wrapper.GetLeaderBoard;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("branch_name")
    @Expose
    private List<BranchName> branchName = null;
    @SerializedName("topleads_branch_wise_first")
    @Expose
    private List<Object> topleadsBranchWiseFirst = null;
    @SerializedName("topleads_branch_wise")
    @Expose
    private List<Object> topleadsBranchWise = null;
    @SerializedName("count_branch_wise")
    @Expose
    private Integer countBranchWise;
    @SerializedName("topleads_all_offices")
    @Expose
    private List<Object> topleadsAllOffices = null;
    @SerializedName("topleads_all_offices_top3")
    @Expose
    private List<TopleadsAllOfficesTop3> topleadsAllOfficesTop3 = null;
    @SerializedName("count_all_offices")
    @Expose
    private Integer countAllOffices;

    public List<BranchName> getBranchName() {
        return branchName;
    }

    public void setBranchName(List<BranchName> branchName) {
        this.branchName = branchName;
    }

    public List<Object> getTopleadsBranchWiseFirst() {
        return topleadsBranchWiseFirst;
    }

    public void setTopleadsBranchWiseFirst(List<Object> topleadsBranchWiseFirst) {
        this.topleadsBranchWiseFirst = topleadsBranchWiseFirst;
    }

    public List<Object> getTopleadsBranchWise() {
        return topleadsBranchWise;
    }

    public void setTopleadsBranchWise(List<Object> topleadsBranchWise) {
        this.topleadsBranchWise = topleadsBranchWise;
    }

    public Integer getCountBranchWise() {
        return countBranchWise;
    }

    public void setCountBranchWise(Integer countBranchWise) {
        this.countBranchWise = countBranchWise;
    }

    public List<Object> getTopleadsAllOffices() {
        return topleadsAllOffices;
    }

    public void setTopleadsAllOffices(List<Object> topleadsAllOffices) {
        this.topleadsAllOffices = topleadsAllOffices;
    }

    public List<TopleadsAllOfficesTop3> getTopleadsAllOfficesTop3() {
        return topleadsAllOfficesTop3;
    }

    public void setTopleadsAllOfficesTop3(List<TopleadsAllOfficesTop3> topleadsAllOfficesTop3) {
        this.topleadsAllOfficesTop3 = topleadsAllOfficesTop3;
    }

    public Integer getCountAllOffices() {
        return countAllOffices;
    }

    public void setCountAllOffices(Integer countAllOffices) {
        this.countAllOffices = countAllOffices;
    }

}
