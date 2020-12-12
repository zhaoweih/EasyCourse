package com.zhaoweihao.architechturesample.bean;

public class ModifyPassword {
    /**
     * @newPassword 登录需要的用户名
     * @oldPassword 登录需要的密码
     * @id 数据库中的id
     */
    private String newPassword;
    private String oldPassword;
    private Integer id;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
