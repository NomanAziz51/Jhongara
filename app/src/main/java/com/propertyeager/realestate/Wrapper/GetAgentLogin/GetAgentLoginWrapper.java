
package com.propertyeager.realestate.Wrapper.GetAgentLogin;


import com.google.gson.annotations.SerializedName;


public class GetAgentLoginWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
