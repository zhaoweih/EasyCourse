package com.zhaoweihao.architechturesample.bean;

public class UpdateChapter {
    private Integer id;
    private Integer course_id;
    private Integer unit_id;
    private String title;
    private String res_list;
    private Integer test_id;
    private String sql2o;
    private String whereValues;
    private String saveOrUpdateProperties;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRes_list() {
        return res_list;
    }

    public void setRes_list(String res_list) {
        this.res_list = res_list;
    }

    public Integer getTest_id() {
        return test_id;
    }

    public void setTest_id(Integer test_id) {
        this.test_id = test_id;
    }

    public String getSql2o() {
        return sql2o;
    }

    public void setSql2o(String sql2o) {
        this.sql2o = sql2o;
    }

    public String getWhereValues() {
        return whereValues;
    }

    public void setWhereValues(String whereValues) {
        this.whereValues = whereValues;
    }

    public String getSaveOrUpdateProperties() {
        return saveOrUpdateProperties;
    }

    public void setSaveOrUpdateProperties(String saveOrUpdateProperties) {
        this.saveOrUpdateProperties = saveOrUpdateProperties;
    }
}
