package com.zhaoweihao.architechturesample.bean.course;

public class QueryComment {
    /**
     "discussId": 24,
     "content": "我对这个保持我的看法",
     "studentId": "2015191054",
     "stuId": null,
     "date": "2018-05-27"
     */
    private int discussId;
    private String content;
    private String studentId;
    private int stuId;
    private String date;
    public int getDiscussId() {
        return discussId;
    }

    public void setDiscussId(int discussId) {
        this.discussId = discussId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
