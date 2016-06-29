package com.thoughtworks.pos.domains;

import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class ItemGroup {
    private List<Item> items;

    public ItemGroup(List<Item> items) {
        this.items = items;
    }

    public String groupName() {
        return items.get(0).getName();
    }

    public int groupSize() {
        return items.size();
    }

    public String groupUnit() {
        return items.get(0).getUnit();
    }

    public double groupPrice() {
        return items.get(0).getPrice();
    }

    public boolean groupPromotion(){ return items.get(0).getPromotion();}

    public int groupPromotionSize(){ return items.size() / 3;}

    public double subTotal() {
        double result = 0.00;
        for (Item item : items) {
            if (item.getPromotion() == true) {
                result = item.getPrice() * ((items.size() / 3)*2+items.size()%3);
            } else {
                result += item.getPrice() * item.getDiscount();
            }
        }
        return result;
    }

    public double saving() {
        double result = 0.00;
        for (Item item : items){
           if (item.getPromotion()==true){
               result =item.getPrice()*(items.size()/3);
           }
            else{
               result += item.getPrice() * (1 - item.getDiscount());
           }
        }
        return result;
    }
}
