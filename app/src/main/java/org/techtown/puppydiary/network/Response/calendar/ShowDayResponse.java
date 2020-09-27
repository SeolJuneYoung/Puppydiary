package org.techtown.puppydiary.network.Response.calendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowDayResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ShowDay> data;

    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public List<ShowDay> getData(){
        return data;
    }

    public class ShowDay {
        @SerializedName("idcalendar")
        private int idcalendar;

        @SerializedName("userIdx")
        private int userIdx;

        @SerializedName("year")
        private int year;

        @SerializedName("month")
        private int month;

        @SerializedName("date")
        private int date;

        @SerializedName("memo")
        private String memo;

        @SerializedName("inject")
        private int inject;

        @SerializedName("water")
        private int water;

        @SerializedName("photo")
        private String photo;

        public int getIdcalendar(){
            return idcalendar;
        }

        public int getUserIdx(){
            return userIdx;
        }

        public int getYear(){
            return year;
        }

        public int getMonth(){
            return month;
        }

        public int getDate(){
            return date;
        }

        public String getMemo(){
            return memo;
        }

        public int getInject(){
            return inject;
        }

        public int getWater(){
            return water;
        }

        public String getPhoto(){
            return photo;
        }
    }
}