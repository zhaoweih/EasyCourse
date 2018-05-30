package com.zhaoweihao.architechturesample.data.seat;

import java.util.List;

public class SeatSel {


    /**
     * rowNum : 15
     * columnNum : 25
     * addRow : [{"row":0,"rowId":"1","columnIds":"N|N|N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|N|N|19|20|21|22|23|24|25|26|27|28|29|N|N|N","columnTypes":"N|N|N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N|S|S|S|S|S|S|S|S|S|S|S|N|N|N","columnStates":"N|N|N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N|A|A|A|A|A|A|A|A|A|A|A|N|N|N"},{"row":1,"rowId":"2","columnIds":"N|N|N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|N|N|19|20|21|22|23|24|25|26|27|28|29|N|N|N","columnTypes":"N|N|N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N|S|S|S|S|S|S|S|S|S|S|S|N|N|N","columnStates":"N|N|N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N|A|A|A|A|A|A|A|A|A|A|A|N|N|N"}]
     */

    private int rowNum;
    private int columnNum;
    private List<AddRowBean> addRow;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public List<AddRowBean> getAddRow() {
        return addRow;
    }

    public void setAddRow(List<AddRowBean> addRow) {
        this.addRow = addRow;
    }

    public static class AddRowBean {
        /**
         * row : 0
         * rowId : 1
         * columnIds : N|N|N|N|N|N|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|N|N|19|20|21|22|23|24|25|26|27|28|29|N|N|N
         * columnTypes : N|N|N|N|N|N|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|S|N|N|S|S|S|S|S|S|S|S|S|S|S|N|N|N
         * columnStates : N|N|N|N|N|N|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|A|N|N|A|A|A|A|A|A|A|A|A|A|A|N|N|N
         */

        private int row;
        private String rowId;
        private String columnIds;
        private String columnTypes;
        private String columnStates;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getColumnIds() {
            return columnIds;
        }

        public void setColumnIds(String columnIds) {
            this.columnIds = columnIds;
        }

        public String getColumnTypes() {
            return columnTypes;
        }

        public void setColumnTypes(String columnTypes) {
            this.columnTypes = columnTypes;
        }

        public String getColumnStates() {
            return columnStates;
        }

        public void setColumnStates(String columnStates) {
            this.columnStates = columnStates;
        }
    }
}
