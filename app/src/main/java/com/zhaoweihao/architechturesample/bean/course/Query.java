package com.zhaoweihao.architechturesample.bean.course;
/**
 *@description 用于查询老师课程信息之后，展示课程信息
 */
public class Query {


    /**
     * id : 2
     * tecId : 26
     * teacherId : 20151120
     * courseName : 大学语文
     * courseNum : 2
     * teacherName : null
     * password :
     * description : null
     * class_image:课程图片
     */

    private int id;
    private int tecId;
    private String teacherId;
    private String courseName;
    private int courseNum;
    private String password;
    private String teacherName;
    private String description;
    private String class_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClass_image() {
        return class_image;
    }

    public void setClass_image(String class_image) {
        this.class_image = class_image;
    }
}
