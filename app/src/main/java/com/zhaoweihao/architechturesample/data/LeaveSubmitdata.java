package com.zhaoweihao.architechturesample.data;

import java.io.Serializable;

public class LeaveSubmitdata implements Serializable{

    /**
     * id : 1
     * studentId : 2015191054
     * teacherId : 20151120
     * stuId : 20
     * tecId : 25
     * content : 5月20日请假
     * status : 1
     * tecAdvise : 申请通过
     * startDate:开始申请时间 类型为date
     * endDate:请假结束时间 类型为date
     * startNum:开始请假的节数  int
     * endNum:节数请假的节数  int
     */

    private int id;
    private String studentId;
    private int courseId;
    private int stuId;
    private int tecId;
    private String content;
    private int status;
    private String tecAdvise;
    private String startDate;
    private String endDate;
    private int startNum;
    private int endNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public int getTecId() {
        return tecId;
    }

    public void setTecId(int tecId) {
        this.tecId = tecId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTecAdvise() {
        return tecAdvise;
    }

    public void setTecAdvise(String tecAdvise) {
        this.tecAdvise = tecAdvise;
    }

    public String getStartDate() {
        return  startDate;
    }

    public void setStartDate(String startDate)  {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return  endDate;
    }

    public void setEndDate(String endDate)  {
        this.endDate =endDate;
    }

    public int getStartNum(){
        return  startNum;
    }
    public void setStartNum(int startNum){
        this.startNum=startNum;
    }
    public int getEndNum(){
        return endNum;
    }
    public void setEndNum(int endNum){
        this.endNum=endNum;
    }



}
