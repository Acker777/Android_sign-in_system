package com.example.android_register_system.Student;

import com.google.gson.annotations.SerializedName;

public class CourseMessage {
    @SerializedName("courseNum")
    String courseId;
    String courseName;
    //String teacherName;

    public CourseMessage(String courseId, String courseName, String teacherName) {
        this.courseId = courseId;
        this.courseName = courseName;
        //this.teacherName = teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

//    public String getTeacherName() {
//        return teacherName;
//    }
//
//    public void setTeacherName(String teacherName) {
//        this.teacherName = teacherName;
//    }
}
