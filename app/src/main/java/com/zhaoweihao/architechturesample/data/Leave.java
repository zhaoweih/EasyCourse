package com.zhaoweihao.architechturesample.data;

public class Leave {

    /**
     * id : 1
     * studentId : 2015191054
     * teacherId : 20151120
     * stuId : 20
     * tecId : 25
     * content : 5月20日请假
     * status : 1
     * tecAdvise : 申请通过
     * startDate:开始申请时间
     * endDate:请假结束时间
     * startNum:开始请假的节数
     * endNum:节数请假的节数
     */

    private int id;
    private String studentId;
    private String teacherId;
    private int stuId;
    private int tecId;
    private String content;
    private int status;
    private String tecAdvise;

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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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
}
