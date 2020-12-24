
package com.propertyeager.realestate.Model.PaymentForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("customer_form_id")
    @Expose
    private String customerFormId;
    @SerializedName("applied_for")
    @Expose
    private String appliedFor;
    @SerializedName("payment_procedure")
    @Expose
    private String paymentProcedure;
    @SerializedName("floor")
    @Expose
    private String floor;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("down_payment")
    @Expose
    private String downPayment;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("witness_name1")
    @Expose
    private String witnessName1;
    @SerializedName("witness_name2")
    @Expose
    private String witnessName2;

    public String getCustomerFormId() {
        return customerFormId;
    }

    public void setCustomerFormId(String customerFormId) {
        this.customerFormId = customerFormId;
    }

    public String getAppliedFor() {
        return appliedFor;
    }

    public void setAppliedFor(String appliedFor) {
        this.appliedFor = appliedFor;
    }

    public String getPaymentProcedure() {
        return paymentProcedure;
    }

    public void setPaymentProcedure(String paymentProcedure) {
        this.paymentProcedure = paymentProcedure;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getWitnessName1() {
        return witnessName1;
    }

    public void setWitnessName1(String witnessName1) {
        this.witnessName1 = witnessName1;
    }

    public String getWitnessName2() {
        return witnessName2;
    }

    public void setWitnessName2(String witnessName2) {
        this.witnessName2 = witnessName2;
    }

}
