
package com.propertyeager.realestate.Wrapper.GetCatagoryAds;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Response {

    @SerializedName("result")
    private List<Result> mResult;

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

}
