
package com.propertyeager.realestate.Wrapper.GetVideoAdList;


import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("Address")
    private String mAddress;
    @SerializedName("Area")
    private String mArea;
    @SerializedName("Baths")
    private String mBaths;
    @SerializedName("Discription")
    private String mDiscription;
    @SerializedName("Price")
    private String mPrice;
    @SerializedName("Rooms")
    private String mRooms;
    @SerializedName("Stories")
    private String mStories;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("video_id")
    private String mVideoId;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
    }

    public String getBaths() {
        return mBaths;
    }

    public void setBaths(String baths) {
        mBaths = baths;
    }

    public String getDiscription() {
        return mDiscription;
    }

    public void setDiscription(String discription) {
        mDiscription = discription;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getRooms() {
        return mRooms;
    }

    public void setRooms(String rooms) {
        mRooms = rooms;
    }

    public String getStories() {
        return mStories;
    }

    public void setStories(String stories) {
        mStories = stories;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

}
