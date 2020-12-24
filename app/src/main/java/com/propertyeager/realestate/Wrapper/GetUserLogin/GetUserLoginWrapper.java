
package com.propertyeager.realestate.Wrapper.GetUserLogin;


import com.google.gson.annotations.SerializedName;


public class GetUserLoginWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
