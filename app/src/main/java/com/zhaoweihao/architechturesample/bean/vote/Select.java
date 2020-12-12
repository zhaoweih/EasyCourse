package com.zhaoweihao.architechturesample.bean.vote;

import java.io.Serializable;
import java.net.Inet4Address;

public class Select implements Serializable{

    private Integer id;

    private String title;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;

    private Integer numA = 0;
    private Integer numB = 0;
    private Integer numC = 0;
    private Integer numD = 0;

    private Integer right_choice;

    private Integer user_choice;

    public Integer getUser_choice() {
        return user_choice;
    }

    public void setUser_choice(Integer user_choice) {
        this.user_choice = user_choice;
    }

    public Integer getRight_choice() {
        return right_choice;
    }

    public void setRight_choice(Integer right_choice) {
        this.right_choice = right_choice;
    }

    public Integer getNumA() {
        return numA;
    }

    public void setNumA(Integer numA) {
        this.numA = numA;
    }

    public Integer getNumB() {
        return numB;
    }

    public void setNumB(Integer numB) {
        this.numB = numB;
    }

    public Integer getNumC() {
        return numC;
    }

    public void setNumC(Integer numC) {
        this.numC = numC;
    }

    public Integer getNumD() {
        return numD;
    }

    public void setNumD(Integer numD) {
        this.numD = numD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }
}
