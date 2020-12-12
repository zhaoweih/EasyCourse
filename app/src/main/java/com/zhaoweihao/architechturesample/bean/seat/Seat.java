package com.zhaoweihao.architechturesample.bean.seat;

public class Seat {

    /**
     * model : 1
     * seated : {"rowNum":15,"columnNum":25,"addRow":[{"row":0,"rowId":"1","columnIds":"N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|N|N","columnTypes":"N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N","columnStates":"N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N"},{"row":1,"rowId":"2","columnIds":"N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|N","columnTypes":"N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N","columnStates":"N|N|N|U|U|U|U|U|U|U|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N"}]}
     */

    private int model;
    private SeatSel seated;

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public SeatSel getSeated() {
        return seated;
    }

    public void setSeated(SeatSel seated) {
        this.seated = seated;
    }
}
