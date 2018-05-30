package com.zhaoweihao.architechturesample.data.seat;

public class Create {
    private String classCode;
    private SeatSel seatSel;

    public SeatSel getSeatSel() {
        return seatSel;
    }

    public void setSeatSel(SeatSel seatSel) {
        this.seatSel = seatSel;
    }

    public String getClassCode() {

        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
