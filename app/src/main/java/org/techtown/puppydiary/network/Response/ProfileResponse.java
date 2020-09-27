package org.techtown.puppydiary.network.Response;

import org.techtown.puppydiary.network.Response.calendar.CalendarPhotoResponse;

import java.util.List;

public class ProfileResponse {

    private int status;
    private boolean success;
    private String message;
    private List<CalendarPhotoResponse.CalendarPhoto> data;

    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public List<CalendarPhotoResponse.CalendarPhoto> getData(){
        return data;
    }

    public class Profile {
        private int useridx;
        private String photo;
        private String profile;

        public int getUseridx() {
            return useridx;
        }

        public String getProfile() {
            return profile;
        }

        public String getPhoto() {
            return photo;
        }
    }

}