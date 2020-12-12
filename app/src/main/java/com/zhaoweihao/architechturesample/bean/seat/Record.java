package com.zhaoweihao.architechturesample.bean.seat;

public class Record {

    /**
     * id : 1
     * studentId : 2015191054
     * stuId : null
     * classCode : 20780
     * classColumn : 4
     * classRow : 4
     */

    private int id;
    private String studentId;
    private Integer stuId;
    private String classCode;
    private int classColumn;
    private int classRow;

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

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getClassColumn() {
        return classColumn;
    }

    public void setClassColumn(int classColumn) {
        this.classColumn = classColumn;
    }

    public int getClassRow() {
        return classRow;
    }

    public void setClassRow(int classRow) {
        this.classRow = classRow;
    }
}

