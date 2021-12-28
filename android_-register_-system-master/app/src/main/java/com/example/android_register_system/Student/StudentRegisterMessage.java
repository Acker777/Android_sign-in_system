package com.example.android_register_system.Student;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StudentRegisterMessage {
    @SerializedName("registerId")
    String registerId;
    @SerializedName("registerName")
    String registerName;
    @SerializedName("registerDateTime")
    Date registerDateTime;
    @SerializedName("isregister")
    int isregister;

    public StudentRegisterMessage(String registerId, String registerName, Date registerDateTime, int isregister) {
        this.registerId = registerId;
        this.registerName = registerName;
        this.registerDateTime = registerDateTime;
        this.isregister = isregister;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getRegisterDateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(this.registerDateTime);
    }

    public void setRegisterDateTime(String registerDateTime) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.registerDateTime=simpleDateFormat.parse(registerDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getIsregister() {
        return isregister;
    }

    public void setIsregister(int isregister) {
        this.isregister = isregister;
    }
}
