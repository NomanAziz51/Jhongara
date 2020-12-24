
package com.propertyeager.realestate.Wrapper.GetADEditText;


import com.google.gson.annotations.SerializedName;


public class GetADEditTextWrapper {

    @SerializedName("msg")
    private String mMsg;
    @SerializedName("response")
    private Response mResponse;
    @SerializedName("status")
    private Long mStatus;

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
