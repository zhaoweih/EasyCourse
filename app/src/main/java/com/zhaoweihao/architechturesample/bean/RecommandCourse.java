package com.zhaoweihao.architechturesample.bean;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author "id": 4,
 * "tecId": 26,
 * "teacherId": "20151120",
 * "courseName": "牛津和爱因斯坦的搏斗",
 * "courseNum": 10,
 * "teacherName": "赵威豪君",
 * "password": "123456",
 * "description": "讲述牛津和爱因斯坦的斗争",
 * "class_image": null
 * @description
 * @time 2019/3/18 0:44
 */
public class RecommandCourse {
    private int id;
    @JSONField(name = "id")
    private int course_id;
    private int tecId;
    private String teacherId;
    private String courseName;
    private int courseNum;
    private String password;
    private String teacherName;
    private String description;
    private String class_image;

    public RecommandCourse() {
    }

    public RecommandCourse(int course_id, int tecId, String teacherId, String courseName, int courseNum, String password, String teacherName, String description, String class_image) {
        this.course_id = course_id;
        this.tecId = tecId;
        this.teacherId = teacherId;
        this.courseName = courseName;
        this.courseNum = courseNum;
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

    @JSONField(name = "id")
    public int getCourse_id() {
        return course_id;
    }

    @JSONField(name = "id")
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

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
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
        return "RecommandCourse{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", tecId=" + tecId +
                ", teacherId='" + teacherId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseNum=" + courseNum +
                ", password='" + password + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", description='" + description + '\'' +
                ", class_image='" + class_image + '\'' +
                '}';
    }
}
