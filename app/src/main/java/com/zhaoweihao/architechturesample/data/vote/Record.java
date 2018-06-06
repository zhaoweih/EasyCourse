package com.zhaoweihao.architechturesample.data.vote;

import android.content.Intent;

import java.util.List;

public class Record {
    private Integer id;
    private String studentId;
    private Integer stuId;
    private Integer voteId;
    private String date;
    private List<AddRec> recJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<AddRec> getRecJson() {
        return recJson;
    }

    public void setRecJson(List<AddRec> recJson) {
        this.recJson = recJson;
    }
}
