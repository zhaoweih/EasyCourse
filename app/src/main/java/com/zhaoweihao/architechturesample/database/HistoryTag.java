package com.zhaoweihao.architechturesample.database;

import org.litepal.crud.DataSupport;

public class HistoryTag extends DataSupport {

    /*
    tagTag备注:
    "home_search_all",该标签用于在首页搜索:HomeSearchAllResourceActivity.java）.
    "note_search_all",该标签用于在笔记首页搜索:NoteSearchAllResourceActivity.java）.
    "message_search_all",该标签用于在消息首页搜索找人:MessageSearchAllPeopleActivity.java）.
    "course_search_all",该标签用于在课程搜索搜索并选课:HomeCourseSearchQueryCourseActivity.java）.
    "note_search_personal_all",该标签用于在笔记本里搜索自己的笔记:NoteNoteBookSearchPersonalActivity.java）.
    */

    private Integer id;
    private String tagTag;
    private String tagContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagTag() {
        return tagTag;
    }

    public void setTagTag(String tagTag) {
        this.tagTag = tagTag;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}
