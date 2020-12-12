package com.zhaoweihao.architechturesample.bean;

import android.net.Uri;

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
     * @avatar 头像的url
     */
   /*
    ***the data We use before :
   private Integer id;
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
    private String name;*/
    private Integer id;//the id here is received from backend ,which will be added into the litepal database as UserId
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
    private String phone;
    private String descrition;
    private String avatar;
    private String question;
    private String answer;
    private String md5_password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMd5_password() {
        return md5_password;
    }

    public void setMd5_password(String md5_password) {
        this.md5_password = md5_password;
    }
}
