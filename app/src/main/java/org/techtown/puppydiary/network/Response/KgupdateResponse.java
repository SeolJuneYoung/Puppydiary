package org.techtown.puppydiary.network.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KgupdateResponse {

    private int status;
    private boolean success;
    private String message;
    private JsonArray data;

    private int kgIdx;
    private int userIdx;
    private int year;

    private String jan;
    private String feb;
    private String mar;
    private String apr;
    private String may;
    private String jun;
    private String jul;
    private String aug;
    private String sep;
    private String oct;
    private String nov;
    private String dec;

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
