
package com.propertyeager.realestate.Wrapper.GetAdDetails;


import com.google.gson.annotations.SerializedName;


public class GetAdDetailWrapper {

    @SerializedName("msg")
    private String mMsg;
    @SerializedName("response")
    private Response mResponse;
    @SerializedName("status")
    private int mStatus;

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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
