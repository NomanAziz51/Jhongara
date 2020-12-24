
package com.propertyeager.realestate.Wrapper.GetUserSignup;


import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("flag")
    private Boolean mFlag;
    @SerializedName("msg")
    private String mMsg;

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
