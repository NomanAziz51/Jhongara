
package com.propertyeager.realestate.Model.BookingForm;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Errors {

    @SerializedName("name")
    @Expose
    private List<String> name = null;
    @SerializedName("son_of")
    @Expose
    private List<String> sonOf = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("cnic")
    @Expose
    private List<String> cnic = null;
    @SerializedName("mobileno")
    @Expose
    private List<String> mobileno = null;
    @SerializedName("date_of_birth")
    @Expose
    private List<String> dateOfBirth = null;
    @SerializedName("present_address")
    @Expose
    private List<String> presentAddress = null;
    @SerializedName("present_address_city")
    @Expose
    private List<String> presentAddressCity = null;
    @SerializedName("present_address_country")
    @Expose
    private List<String> presentAddressCountry = null;
    @SerializedName("alternate_address")
    @Expose
    private List<String> alternateAddress = null;
    @SerializedName("alternate_address_city")
    @Expose
    private List<String> alternateAddressCity = null;
    @SerializedName("alternate_address_country")
    @Expose
    private List<String> alternateAddressCountry = null;
    @SerializedName("nok_name")
    @Expose
    private List<String> nokName = null;
    @SerializedName("nok_son_of")
    @Expose
    private List<String> nokSonOf = null;
    @SerializedName("nok_relationship")
    @Expose
    private List<String> nokRelationship = null;
    @SerializedName("nok_cnic")
    @Expose
    private List<String> nokCnic = null;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getSonOf() {
        return sonOf;
    }

    public void setSonOf(List<String> sonOf) {
        this.sonOf = sonOf;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getCnic() {
        return cnic;
    }

    public void setCnic(List<String> cnic) {
        this.cnic = cnic;
    }

    public List<String> getMobileno() {
        return mobileno;
    }

    public void setMobileno(List<String> mobileno) {
        this.mobileno = mobileno;
    }

    public List<String> getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(List<String> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(List<String> presentAddress) {
        this.presentAddress = presentAddress;
    }

    public List<String> getPresentAddressCity() {
        return presentAddressCity;
    }

    public void setPresentAddressCity(List<String> presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public List<String> getPresentAddressCountry() {
        return presentAddressCountry;
    }

    public void setPresentAddressCountry(List<String> presentAddressCountry) {
        this.presentAddressCountry = presentAddressCountry;
    }

    public List<String> getAlternateAddress() {
        return alternateAddress;
    }

    public void setAlternateAddress(List<String> alternateAddress) {
        this.alternateAddress = alternateAddress;
    }

    public List<String> getAlternateAddressCity() {
        return alternateAddressCity;
    }

    public void setAlternateAddressCity(List<String> alternateAddressCity) {
        this.alternateAddressCity = alternateAddressCity;
    }

    public List<String> getAlternateAddressCountry() {
        return alternateAddressCountry;
    }

    public void setAlternateAddressCountry(List<String> alternateAddressCountry) {
        this.alternateAddressCountry = alternateAddressCountry;
    }

    public List<String> getNokName() {
        return nokName;
    }

    public void setNokName(List<String> nokName) {
        this.nokName = nokName;
    }

    public List<String> getNokSonOf() {
        return nokSonOf;
    }

    public void setNokSonOf(List<String> nokSonOf) {
        this.nokSonOf = nokSonOf;
    }

    public List<String> getNokRelationship() {
        return nokRelationship;
    }

    public void setNokRelationship(List<String> nokRelationship) {
        this.nokRelationship = nokRelationship;
    }

    public List<String> getNokCnic() {
        return nokCnic;
    }

    public void setNokCnic(List<String> nokCnic) {
        this.nokCnic = nokCnic;
    }

}
