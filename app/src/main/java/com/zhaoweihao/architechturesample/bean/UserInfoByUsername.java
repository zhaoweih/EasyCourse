package com.zhaoweihao.architechturesample.bean;

import java.io.Serializable;

/**
*@description 通过username获取用户信息
*@author 
*@time 2019/3/19 17:52
*/
public class UserInfoByUsername implements Serializable {
    private int id;//the id here is received from backend ,which will be added into the litepal database as UserId
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
    private String question;
    private String answer;
    private String descrition;
    private String avatar;

    public UserInfoByUsername() {
    }

    public UserInfoByUsername(int id, String username, String password, String studentId, String teacherId, String classId, String department, Integer education, String date, String school, Integer sex, String name, String phone, String question, String answer, String descrition, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.classId = classId;
        this.department = department;
        this.education = education;
        this.date = date;
        this.school = school;
        this.sex = sex;
        this.name = name;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
        this.descrition = descrition;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "UserInfoByUsername{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", studentId='" + studentId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", classId='" + classId + '\'' +
                ", department='" + department + '\'' +
                ", education=" + education +
                ", date='" + date + '\'' +
                ", school='" + school + '\'' +
                ", sex=" + sex +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", descrition='" + descrition + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
