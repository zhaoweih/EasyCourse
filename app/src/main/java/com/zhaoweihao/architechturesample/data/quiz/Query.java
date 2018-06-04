package com.zhaoweihao.architechturesample.data.quiz;

public class Query {

    /**
     * id : 3
     * courseId : 4
     * teacherId : 20151120
     * tecId : null
     * quizNum : 1
     * studentId : 2015191054
     * stuId : null
     * studentName : 赵威豪
     */

    private int id;
    private int courseId;
    private String teacherId;
    private Object tecId;
    private int quizNum;
    private String studentId;
    private Object stuId;
    private String studentName;

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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Object getTecId() {
        return tecId;
    }

    public void setTecId(Object tecId) {
        this.tecId = tecId;
    }

    public int getQuizNum() {
        return quizNum;
    }

    public void setQuizNum(int quizNum) {
        this.quizNum = quizNum;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Object getStuId() {
        return stuId;
    }

    public void setStuId(Object stuId) {
        this.stuId = stuId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
