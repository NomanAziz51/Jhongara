
package com.propertyeager.realestate.Wrapper.GetAgentProfileData;


import com.google.gson.annotations.SerializedName;

public class GetAgentProfileDataWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
