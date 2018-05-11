package com.zhaoweihao.architechturesample.data;

import com.google.gson.annotations.SerializedName;

public class User {
    /**
     * @id id int (不需要提交，数据库自动生成)
     * @username 用户名
     * @password 密码
     * @studentId 学号
     * @teacherId 教师编号
     * @classId 班级编号
     * @department 学院
     * @education 学历 int
     * @date 入学时间
     * @school 学校
     * @sex 性别 int
     * @name 真实姓名
     */

    private String username;
    private String password;
    private String studentId;
    private String teacherId;
    private String classId;
    private String department;
    private Integer education;
    private String date;
    private String school;
    private Integer sex;
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
