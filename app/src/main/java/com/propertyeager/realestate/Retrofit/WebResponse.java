package com.propertyeager.realestate.Retrofit;



import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class WebResponse<T> {

    @Expose
    public int status_code;

    @Nullable
    @Expose
    public String message = "";

    @Nullable
    @Expose
    public String error_code;

    @Nullable
    @Expose
    public boolean error;

    @Nullable
    @Expose
    public T body;

    public boolean isSuccess() {
        return (status_code >= 200 && status_code <= 204);
    }

    public boolean isExpired() {
        try {
            if (error_code == null || !error)
                return false;
            if (status_code == 406 && error_code.equals("oauth_invalid_token"))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
