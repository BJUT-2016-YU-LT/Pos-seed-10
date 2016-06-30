package com.thoughtworks.pos.services.services;

import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ItemGroup;
import com.thoughtworks.pos.domains.Report;
import com.thoughtworks.pos.domains.ShoppingChart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class ReportDataGenerator {
    private ShoppingChart shoppingChart;

    public ReportDataGenerator(ShoppingChart shoppingChart) {
        this.shoppingChart = shoppingChart;
    }

    public Report generate()  {
        ArrayList<Item> items = shoppingChart.getItems();

        List<ItemGroup> itemGroups = GetItemGroups(items);
        Report report = new Report(itemGroups);

        if(shoppingChart.getUser().getNumber()!=null && shoppingChart.getUser().getIsVip()) { // 判断现有积分情况，决定消费金额和积分的比率
            if (shoppingChart.getUser().getScore() >= 0 && shoppingChart.getUser().getScore() <= 200) {
                report.setLevel(1);
            } else if (shoppingChart.getUser().getScore() > 200 && shoppingChart.getUser().getScore() <= 500) {
                report.setLevel(3);
            } else if (shoppingChart.getUser().getScore() > 500) {
                report.setLevel(5);
            }
        }
        shoppingChart.getUser().addScore(report.getScore()); // 增加积分
        return report;
    }

    private List<ItemGroup> GetItemGroups(ArrayList<Item> items) {
        List<ItemGroup> itemGroupies = new LinkedList<ItemGroup>();
        for (ItemGroup group: groupByItemBarCode(items).values())
            itemGroupies.add(group);
        return itemGroupies;
    }

    private LinkedHashMap<String, ItemGroup> groupByItemBarCode(ArrayList<Item> items) {
        LinkedHashMap<String, ItemGroup> map = new LinkedHashMap<String, ItemGroup>();
        for (Item item : items) {
            String itemBarCode = item.getBarcode();
            if (!map.containsKey(itemBarCode)) {
                map.put(itemBarCode, new ItemGroup(item));
                map.get(itemBarCode).setUser(shoppingChart.getUser());
            }
            map.get(itemBarCode).addOne();
        }
        return map;
    }
}