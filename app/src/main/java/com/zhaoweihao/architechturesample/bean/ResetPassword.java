package com.zhaoweihao.architechturesample.bean;

/**
 * @author
 * @description 用于重置密码，添加了问题和答案，相对于修改密码来说
 * @time 2019/3/25 15:53
 * "username": "zhaowei0",
 * "question": null,
 * "answer": "黑猫",
 * "new_password": "abc12300"
 */
public class ResetPassword {
    private String username;
    private String question;
    private String answer;
    private String new_password;

    public ResetPassword() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
