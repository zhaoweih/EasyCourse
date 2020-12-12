package com.zhaoweihao.architechturesample.bean;

import java.io.Serializable;

/**
*@description 添加和显示活动
*@author
 * {
 * 	"id": null,
 * 	"start_time": 1549090809897,
 * 	"end_time": 1549177209897,
 * 	"title": "测试活动标题",
 * 	"img_url": "http://upload-images.jianshu.io/upload_images/5796527-2829fb39b6b86721.png",
 * 	"type": 1,
 * 	"tags": "课余活动",
 * 	"time": null,
 * 	"sender_id": null,
 * 	"content_url": null,
 * 	"content": "测试活动",
 * 	"views": null,
 * 	"sql2o": null,
 * 	"whereValues": [],
 * 	"saveOrUpdateProperties": []
 * }
*@time 2019/2/23 19:54
*/
public class AddAndShowActivity implements Serializable {
    private int id;
    private long start_time;
    private long end_time;
    private String title;
    private String img_url;
    private String type;
    private String tags;
    private long time;
    private Integer sender_id;
    private String content_url;
    private String content;
    private String views;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getSender_id() {
        return sender_id;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}
