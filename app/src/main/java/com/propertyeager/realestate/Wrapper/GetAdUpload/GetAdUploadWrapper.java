
package com.propertyeager.realestate.Wrapper.GetAdUpload;


import com.google.gson.annotations.SerializedName;


public class GetAdUploadWrapper {

    @SerializedName("result")
    private Result mResult;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}
