package com.zhaoweihao.architechturesample.bean;

public class Course {

    /**
     * id : 2
     * courseId : 10
     * stuId : 20
     * studentId : 2015191054
     * courseName : 大学语文
     * teacherName : 赵威豪
     * password : null
     */

    private int id;
    private int courseId;
    private int stuId;
    private String studentId;
    private String courseName;
    private String teacherName;
    private Object password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }
}
