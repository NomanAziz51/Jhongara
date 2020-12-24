
package com.propertyeager.realestate.Wrapper.GetAgentSignup;


import com.google.gson.annotations.SerializedName;


public class GetAgentSignupWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
