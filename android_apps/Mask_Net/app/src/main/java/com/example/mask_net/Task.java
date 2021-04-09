package com.example.mask_net;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//
//public class Task {
//
//    @SerializedName("user_name")
//    public String user_name;
//
////    public Task(String user_name) {
////        user_name = user_name.substring(1, user_name.length() - 1);
////        this.user_name = user_name;
////    }
//
//    public String getUser() {
//        return user_name;
//    }
//    @SerializedName("id")
//    public String id;
//    @SerializedName("date")
//    public String date;
//    @SerializedName("location")
//    public String location;
//    @SerializedName("img")
//    public String img;
//
//    public Task(String id, String date, String location, String img)
//    {
//        this.id=id;
//        this.date=date;
//        this.location=location;
//        this.img=img;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//}
public class Task {
    @SerializedName("id") public String id;
    @SerializedName("date") public String date;
    @SerializedName("location") public String location;
    @SerializedName("img") public String img;

    public Task(String id, String date, String location, String img) {
        this.id=id;
        this.date=date;
        this.location=location;
        this.img=img;
    }

    public String getId(){ return this.id; }
    public String getDate(){ return this.date; }
    public String getLocation(){ return this.location; }
    public String getImg(){ return this.img; }
}