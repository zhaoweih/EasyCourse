package com.zhaoweihao.architechturesample.data;

public class LeaveShow {
    /**
     * 这个类用来给教师确认学生的请假条，拒绝或者同意学生的请假
     * @leaveId : 请假条id
     * @tecId : 老师在数据库的id,不是教师编号
     * @status : 1是未审批，2是拒绝，3是通过
     * @tecAdvise : 教师留言，如：“申请不通过原因”
     */
    private int leaveId;
    private int tecId;
    private int status;
    private String tecAdvise;

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setTecId(int tecId) {
        this.tecId = tecId;
    }

    public int getTecId() {
        return tecId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setTecAdvise(String tecAdvise) {
        this.tecAdvise = tecAdvise;
    }

    public String getTecAdvise() {
        return tecAdvise;
    }

}
