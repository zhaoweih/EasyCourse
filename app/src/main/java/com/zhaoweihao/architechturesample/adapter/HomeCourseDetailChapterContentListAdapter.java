package com.zhaoweihao.architechturesample.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.GetUnit;
import com.zhaoweihao.architechturesample.bean.ShowChapter;

import java.util.List;

/**
 * @author
 * @description 首页-课程-详细界面-课程章节-展示单元-章节内容列表
 * @time 2019/2/11 15:16
 */
public class HomeCourseDetailChapterContentListAdapter extends BaseQuickAdapter<ShowChapter, BaseViewHolder> {
    private String[] chapterTitle;
    private String[] chapterVideoUrl;
    private int[] chapterId;
    private int[] unitId;
    private int[] courseId;

    public HomeCourseDetailChapterContentListAdapter(@Nullable List<ShowChapter> data) {
        super(R.layout.layout_home_course_detail_chapter_content, data);
        chapterTitle = new String[data.size()];
        chapterVideoUrl = new String[data.size()];
        unitId=new int[data.size()];
        courseId=new int[data.size()];
        chapterId = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            chapterTitle[i] = data.get(i).getTitle();
            chapterVideoUrl[i] = data.get(i).getRes_list();
            chapterId[i] = data.get(i).getId();
            unitId[i]=data.get(i).getUnit_id();
            courseId[i]=data.get(i).getCourse_id();
        }
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ShowChapter showChapter) {
        Log.v("tanxiiii", showChapter.getTitle());
        viewHolder.setText(R.id.lhchcc_title, showChapter.getTitle());
    }

    public String[] getChapterTitle() {
        return chapterTitle;
    }

    public String[] getChapterVideoUrl() {
        return chapterVideoUrl;
    }

    public int[] getChapterId(){
        return chapterId;
    }

    public int[] getCourseId() {
        return courseId;
    }

    public int[] getUnitId() {
        return unitId;
    }
}
