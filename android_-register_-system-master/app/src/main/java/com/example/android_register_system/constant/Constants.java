package com.example.android_register_system.constant;

import com.example.android_register_system.Student.User;
import com.example.android_register_system.Teacher.Course;

import java.util.List;

public class Constants {
    public Constants() {
    }

    final static public String baseUrl = "http://10.70.142.116:8080/";

    //登陆请求
    final static public String loginUrl = baseUrl + "login";
    final static public String registerUrl = baseUrl + "registernewuser";
    final static public String queryCourse = baseUrl + "queryCourse";


    public static String getLoginUrl(String username, String password) {
        return loginUrl + "?" + "username=" + username + "&password=" + password;
    }

    public static String getRegisterUrl(String userNum, String password, String username, int isStudent) {
        return registerUrl + "?" + "userNum=" + userNum + "&userPassword=" + password + "&userName=" + username + "&isStudent=" + isStudent;
    }

    public static String queryCourse() {
        return queryCourse ;
    }

}
