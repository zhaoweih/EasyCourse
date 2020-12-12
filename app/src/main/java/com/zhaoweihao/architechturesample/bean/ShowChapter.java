package com.zhaoweihao.architechturesample.bean;
/**
*@description
 *               "id": 2,
 * 				"course_id": 12,
 * 				"unit_id": 12,
 * 				"title": "川普麦当劳理论",
 * 				"res_list": "[1,2,3,4]",
 * 				"test_id": null
*@author
*@time 2019/2/11 15:18
*/
public class ShowChapter {
    private Integer  id;
    private Integer course_id;
    private Integer unit_id;
    private String title;
    private String res_list;
    private Integer test_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourse_id() {
        return course_id;
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
}
