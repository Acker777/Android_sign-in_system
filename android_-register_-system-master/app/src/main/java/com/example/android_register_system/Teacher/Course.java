package com.example.android_register_system.Teacher;

public class Course {

    private String courseId;
    private String courseNum;
    private String courseName;
    private String teacherNum;

    private String teacherName;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Course() {
    }

//    public Course(String courseId, String courseNum, String courseName, String teacherNum) {
//        this.courseId = courseId;
//        this.courseNum = courseNum;
//        this.courseName = courseName;
//        this.teacherNum = teacherNum;
//    }


    public Course(String courseId, String courseNum, String courseName, String teacherNum, String teacherName) {
        this.courseId = courseId;
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.teacherNum = teacherNum;
        this.teacherName = teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(String teacherNum) {
        this.teacherNum = teacherNum;
    }
}
