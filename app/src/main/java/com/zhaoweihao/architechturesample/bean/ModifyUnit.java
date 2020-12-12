package com.zhaoweihao.architechturesample.bean;

public class ModifyUnit {
    private Number course_id;
    private Number user_id;
    private String teacher_id;
    private String title;
    private String whereValues;
    private String saveOrUpdateProperties;

    public Number getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Number course_id) {
        this.course_id = course_id;
    }

    public Number getUser_id() {
        return user_id;
    }

    public void setUser_id(Number user_id) {
        this.user_id = user_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhereValues() {
        return whereValues;
    }

    public void setWhereValues(String whereValues) {
        this.whereValues = whereValues;
    }

    public String getSaveOrUpdateProperties() {
        return saveOrUpdateProperties;
    }

    public void setSaveOrUpdateProperties(String saveOrUpdateProperties) {
        this.saveOrUpdateProperties = saveOrUpdateProperties;
    }
}
