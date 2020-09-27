package org.techtown.puppydiary.network.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KgupdateResponse {

    private int status;
    private boolean success;
    private String message;
    private JsonArray data;

    private String month;
    private Double kg;


    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public JsonArray getData(){ return data; }

    public String getMonth(){
        month = data.getAsJsonObject().get("month").getAsString();
        return month;
    }

    public Double getKg(){
        kg = data.getAsJsonObject().get("kg").getAsDouble();
        return kg;
    }


}
