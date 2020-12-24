
package com.propertyeager.realestate.Wrapper.GetProfileEdit;


import com.google.gson.annotations.SerializedName;


public class GetProfileEditWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
