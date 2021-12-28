package com.example.android_register_system.constant;

public class Constants {
    public Constants() {
    }

    final static public String baseUrl = "http://192.168.1.109:8080/";

    //登陆请求
    final static public String loginUrl = baseUrl + "login";
    final static public String registerUrl = baseUrl + "registerUser";


    public static String getLoginUrl(String username, String password) {
        return loginUrl + "?" + "username=" + username + "&password=" + password;
    }

    public static String getRegisterUrl(String userNum, String password, String username, int isStudent) {
        return registerUrl + "?" + "userNum=" + userNum + "&userPassword=" + password + "&userName=" + username + "&isStudent=" + isStudent;
    }


}
