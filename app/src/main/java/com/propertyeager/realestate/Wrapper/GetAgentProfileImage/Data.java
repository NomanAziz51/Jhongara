
package com.propertyeager.realestate.Wrapper.GetAgentProfileImage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("agent_picture")
    @Expose
    private String agentPicture;

    public String getAgentPicture() {
        return agentPicture;
    }

    public void setAgentPicture(String agentPicture) {
        this.agentPicture = agentPicture;
    }

}
