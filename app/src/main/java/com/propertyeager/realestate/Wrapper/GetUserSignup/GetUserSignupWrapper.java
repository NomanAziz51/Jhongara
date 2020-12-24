
package com.propertyeager.realestate.Wrapper.GetUserSignup;


import com.google.gson.annotations.SerializedName;


public class GetUserSignupWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
