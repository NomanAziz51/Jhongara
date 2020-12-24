
package com.propertyeager.realestate.Wrapper.GetAgentLogin;


import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("data")
    private Data mData;
    @SerializedName("flag")
    private Boolean mFlag;
    @SerializedName("msg")
    private String mMsg;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public Boolean getFlag() {
        return mFlag;
    }

    public void setFlag(Boolean flag) {
        mFlag = flag;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

}
