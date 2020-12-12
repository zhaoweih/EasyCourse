package com.zhaoweihao.architechturesample.bean;

public class ValidationMesgEvent {
    private int MesgNum;

    public ValidationMesgEvent(int mesgNum) {
        this.MesgNum = mesgNum;
    }

    public int getMesgNum() {
        return this.MesgNum;
    }

    public void setMesgNum(int mesgNum) {
        this.MesgNum = mesgNum;
    }
}
