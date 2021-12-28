package com.example.android_register_system.Student;

public class User {
    private String userNum;
    private String userPassword;
    private String userName;
    private int isStudent;

    public User() {
    }

    public User(String userNum, String userPassword, String userName, int isStudent) {
        this.userNum = userNum;
        this.userPassword = userPassword;
        this.userName = userName;
        this.isStudent = isStudent;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(int isStudent) {
        this.isStudent = isStudent;
    }
}
