package com.zhaoweihao.architechturesample.bean.course;

public class Select {


    /**
     * courseId : 4
     * stuId : 20
     * studentId : 2015191054
     * courseName : 大学语文
     * teacherName : 赵威豪
     * password : 123456
     */

    private int courseId;
    private int stuId;
    private String studentId;
    private String courseName;
    private String teacherName;
    private String password;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
