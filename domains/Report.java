package com.thoughtworks.pos.domains;

import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class Report {
    private List<ItemGroup> itemGroupies;
    private int level;
    private int score = 0;

    public Report(List<ItemGroup> itemGroupies) {
        this.itemGroupies = itemGroupies;
    }

    public List<ItemGroup> getItemGroupies() {
        return itemGroupies;
    }

    public double getTotal() {
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies)
            result += itemGroup.subTotal();
        return result;
    }

    public double getSaving() {
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies)
            result += itemGroup.saving();
        return result;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int scoreType) {
        this.level = scoreType;
    }

    public int getScore() {
        return (int) getTotal() / 5 * level;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
