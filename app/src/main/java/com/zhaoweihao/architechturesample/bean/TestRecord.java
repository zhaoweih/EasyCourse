package com.zhaoweihao.architechturesample.bean;

import com.zhaoweihao.architechturesample.bean.vote.AddRec;

import java.util.List;

public class TestRecord {
    private Integer id;
    private String studentId;
    private Integer stuId;
    private Integer voteId;
    private String date;
    private List<AddRec> rec_json;

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

    public List<AddRec> getRec_json() {
        return rec_json;
    }

    public void setRec_json(List<AddRec> rec_json) {
        this.rec_json = rec_json;
    }
}
