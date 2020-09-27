package org.techtown.puppydiary.network.Response.account;

import com.google.gson.JsonArray;

import java.util.List;

public class ShowAccountResponse {

    private int status;
    private boolean success;
    private String message;
    private List<ShowAccount> data;


    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public List<ShowAccount> getData(){ return data; }

    public class ShowAccount{
        private int idaccount;
        private int userIdx;
        private int year;
        private int month;
        private int date;
        private String item;
        private int price;

        public int getIdaccount(){
            return idaccount;
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

        public String getItem(){
            return item;
        }

        public int getPrice(){
            return price;
        }
    }
}
