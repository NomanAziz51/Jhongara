
package com.propertyeager.realestate.Wrapper.GetADEditText;


import com.google.gson.annotations.SerializedName;


public class Response {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
