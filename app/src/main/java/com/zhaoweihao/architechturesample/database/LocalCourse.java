package com.zhaoweihao.architechturesample.database;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
/**
*@description 用于存储首页的课程
*@author
*@time 2019/3/17 9:29
*/
public class LocalCourse extends DataSupport implements Serializable {
    private int id;
    private Integer state;
    private int User_id;
    private int course_id;
    private int tecId;
    private String teacherId;
    private String courseName;
    private int course_Selected_Num;
    private String password;
    private String teacherName;
    private String description;
    private String class_image;

    public LocalCourse() {
    }

    public LocalCourse(Integer state, int user_id, int course_id, int tecId, String teacherId, String courseName, int course_Selected_Num, String password, String teacherName, String description, String class_image) {
        this.state = state;
        User_id = user_id;
        this.course_id = course_id;
        this.tecId = tecId;
        this.teacherId = teacherId;
        this.courseName = courseName;
        this.course_Selected_Num = course_Selected_Num;
        this.password = password;
        this.teacherName = teacherName;
        this.description = description;
        this.class_image = class_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

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

    public int getCourse_Selected_Num() {
        return course_Selected_Num;
    }

    public void setCourse_Selected_Num(int course_Selected_Num) {
        this.course_Selected_Num = course_Selected_Num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClass_image() {
        return class_image;
    }

    public void setClass_image(String class_image) {
        this.class_image = class_image;
    }

    @Override
    public String toString() {
        return "LocalCourse{" +
                "id=" + id +
                ", state=" + state +
                ", User_id=" + User_id +
                ", course_id=" + course_id +
                ", tecId=" + tecId +
                ", teacherId='" + teacherId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", course_Selected_Num=" + course_Selected_Num +
                ", password='" + password + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", description='" + description + '\'' +
                ", class_image='" + class_image + '\'' +
                '}';
    }
}
