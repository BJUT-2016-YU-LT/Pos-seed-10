package com.thoughtworks.pos.domains;

/**
 * Created by Administrator on 2016/6/30.
 */
public class User {
    private String number;
    private String name;
    private boolean isVip;
    private int score = 0;

    public User() {
    }

    public User(String number) {
        this.isVip = false;
        this.name = "null";
        this.number = number;
    }

    public User(String number, String name, boolean isVip) {
        this.isVip = isVip;
        this.name = name;
        this.number = number;
    }

    public User(String user, String name, boolean isVip, int score) {
        this(user, name, isVip);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNumber() {
        return this.number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getIsVip() {
        return this.isVip;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean addScore(int score) {
        if (this.isVip) {
            this.score += score;
            return true;
        }
        return false;
    }
}
