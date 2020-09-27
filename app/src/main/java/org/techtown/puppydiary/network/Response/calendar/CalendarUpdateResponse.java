package org.techtown.puppydiary.network.Response.calendar;

import com.google.gson.JsonArray;

import java.util.List;

public class CalendarUpdateResponse {

    private int status;
    private boolean success;
    private String message;
    private List<CalendarUpdate> data;

    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public List<CalendarUpdate> getData(){ return data; }

    public class CalendarUpdate {
        private int idcalendar;
        private int userIdx;
        private int year;
        private int month;
        private int date;
        private String memo;
        private int inject;
        private int water;
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
