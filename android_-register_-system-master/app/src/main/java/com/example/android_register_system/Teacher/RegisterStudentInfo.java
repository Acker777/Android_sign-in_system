package com.example.android_register_system.Teacher;

import java.util.Date;

public class RegisterStudentInfo {


    private String register_id;
    private String studentNum;
    private Date studentRegisterTime;
    private String userName;

    @Override
    public String toString() {
        return "RegisterStudentInfo{" +
                "register_id='" + register_id + '\'' +
                ", studentNum='" + studentNum + '\'' +
                ", studentRegisterTime=" + studentRegisterTime +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getRegister_id() {
        return register_id;
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public Date getStudentRegisterTime() {
        return studentRegisterTime;
    }

    public void setStudentRegisterTime(Date studentRegisterTime) {
        this.studentRegisterTime = studentRegisterTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
