package org.techtown.puppydiary.network.Response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    private String jwtToken;
    private JsonObject data;

    public int getCode() {
        return code;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getJwtToken() {
        jwtToken = data.getAsJsonObject().get("jwtToken").getAsString();
        return jwtToken;
    }
}