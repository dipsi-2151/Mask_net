package com.example.mask_net;

import android.graphics.Bitmap;

public class Penalty {


        Bitmap bmp;
        String date, location;
        String id;

        public Penalty()
        { }


    public Penalty(Bitmap bmp, String date, String location,String id) {
            this.id=id;
            this.bmp=bmp;
        this.date = date;
        this.location = location;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }
}
