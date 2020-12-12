package com.zhaoweihao.architechturesample.bean.course;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
*@description 用于查询学生课程信息之后，展示课程信息
*/
public class QuerySelect {
    /**
     * id : 2    int
     * courseId: 26  int
     * stuId : 20    int
     * studentId : 2015191036   String
     * courseName: 大学语文     String
     * teacherName : txk        String
     * password:null
     * class_image:课程图片
     */

    private int id;
    private int courseId;
    private int stuId;
    private String studentId;
    private String courseName;
    private String teacherName;
    private String password;
    private Object course;

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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public JsonObject getCourse() {
        return course;
    }

    public void setCourse(JsonObject course) {
        this.course = course;
    }*/

    public Object getCourse() {
        return course;
    }

    public void setCourse(Object course) {
        this.course = course;
    }
}
