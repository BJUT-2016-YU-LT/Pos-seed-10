package com.thoughtworks.pos.domains;

import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.services.services.ReportDataGenerator;

/**
 * Created by Administrator on 2014/12/28.
 */
public class Pos {
    public String getShoppingList(ShoppingChart shoppingChart) throws EmptyShoppingCartException {

        Report report = new ReportDataGenerator(shoppingChart).generate();

        StringBuilder shoppingListBuilder = new StringBuilder()
                .append("***商店购物清单***\n");

        for (ItemGroup itemGroup : report.getItemGroupies()) {
            boolean promtion = false;
            if(itemGroup.groupPromotion()==true){
                promtion = true;
            }
            shoppingListBuilder.append(
                    new StringBuilder()
                            .append("名称：").append(itemGroup.groupName()).append("，")
                            .append("数量：").append(itemGroup.groupSize()).append(itemGroup.groupUnit()).append("，")
                            .append("单价：").append(String.format("%.2f", itemGroup.groupPrice())).append("(元)").append("，")
                            .append("小计：").append(String.format("%.2f", itemGroup.subTotal())).append("(元)").append("\n")
                            .toString());
            if (promtion==true && itemGroup.groupSize()>2){
                shoppingListBuilder.append(
                        new StringBuilder()
                                .append("----------------------\n")
                                .append("挥泪赠送商品：\n")
                                .append("名称：").append(itemGroup.groupName()).append("，")
                                .append("数量：").append(itemGroup.groupPromotionSize()).append(itemGroup.groupUnit()).append("\n")
                                .toString());
            }
        }

        StringBuilder subStringBuilder = shoppingListBuilder
                .append("----------------------\n")
                .append("总计：").append(String.format("%.2f", report.getTotal())).append("(元)").append("\n");

        double saving = report.getSaving();
        if (saving == 0) {
            return subStringBuilder
                    .append("**********************\n")
                    .toString();
        }
        return subStringBuilder
                .append("节省：").append(String.format("%.2f", saving)).append("(元)").append("\n")
                .append("**********************\n")
                .toString();
    }
}
