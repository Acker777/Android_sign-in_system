package com.example.android_register_system.Teacher;

import java.util.List;

public class RegisterPageInfo {
    private Integer isRegNum;
    private Double regRate;
    private Integer noRegNum;
    private Integer stuNum;
    private List<RegisterStudentInfo> list;

    @Override
    public String toString() {
        return "RegisterPageInfo{" +
                "isRegNum=" + isRegNum +
                ", regRate=" + regRate +
                ", noRegNum=" + noRegNum +
                ", stuNum=" + stuNum +
                ", list=" + list +
                '}';
    }

    public Integer getIsRegNum() {
        return isRegNum;
    }

    public void setIsRegNum(Integer isRegNum) {
        this.isRegNum = isRegNum;
    }

    public Double getRegRate() {
        return regRate;
    }

    public void setRegRate(Double regRate) {
        this.regRate = regRate;
    }

    public Integer getNoRegNum() {
        return noRegNum;
    }

    public void setNoRegNum(Integer noRegNum) {
        this.noRegNum = noRegNum;
    }

    public Integer getStuNum() {
        return stuNum;
    }

    public void setStuNum(Integer stuNum) {
        this.stuNum = stuNum;
    }

    public List<RegisterStudentInfo> getList() {
        return list;
    }

    public void setList(List<RegisterStudentInfo> list) {
        this.list = list;
    }
}
