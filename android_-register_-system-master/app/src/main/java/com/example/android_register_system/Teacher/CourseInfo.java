package com.example.android_register_system.Teacher;

public class CourseInfo {
    private String courseId;
    private String courseNum;
    private String courseName;
    private String teacherNum;
    private int studentNum;

    @Override
    public String toString() {
        return "CourseInfo{" +
                "courseId='" + courseId + '\'' +
                ", courseNum='" + courseNum + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherNum='" + teacherNum + '\'' +
                ", studentNum=" + studentNum +
                '}';
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

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }
}
