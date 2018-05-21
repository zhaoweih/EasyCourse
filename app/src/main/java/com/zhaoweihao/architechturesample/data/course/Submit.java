package com.zhaoweihao.architechturesample.data.course;

public class Submit {


    /**
     * tecId : 26
     * teacherId : 20151120
     * courseName : 牛津和爱因斯坦的搏斗
     * teacherName : 赵威豪
     * password : 123456
     * description : 讲述牛津和爱因斯坦的斗争
     */

    private int tecId;
    private String teacherId;
    private String courseName;
    private String teacherName;
    private String password;
    private String description;

    public int getTecId() {
        return tecId;
    }

    public void setTecId(int tecId) {
        this.tecId = tecId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
