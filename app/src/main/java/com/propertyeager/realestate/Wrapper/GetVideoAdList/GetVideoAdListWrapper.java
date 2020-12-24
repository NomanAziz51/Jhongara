
package com.propertyeager.realestate.Wrapper.GetVideoAdList;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class GetVideoAdListWrapper {

    @SerializedName("msg")
    private String mMsg;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
