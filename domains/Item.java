package com.thoughtworks.pos.domains;

/**
 * Created by Administrator on 2014/12/28.
 */
public class Item {
    private String barcode;
    private String name;
    private String unit;
    private double price;
    private double discount = 1;
    private double vipDiscount = 1;
    private boolean promotion = false;

    public Item() {
    }

    public Item(String barcode, String name, String unit, double price) {
        this.setBarcode(barcode);
        this.setName(name);
        this.setUnit(unit);
        this.setPrice(price);
    }

    public Item(String barcode, String name, String unit, double price, double discount) {
        this(barcode, name, unit, price);
        this.setDiscount(discount);
    }
    public Item(String barcode, String name, String unit, double price, boolean discount) {
        this(barcode, name, unit, price);
        this.setPromotion(discount);
    }

    public Item(String barcode, String name, String unit, double price, double discount, boolean promotion, double vipDiscount) {
        this(barcode, name, unit, price, discount, promotion);
        this.setVipDiscount(vipDiscount);
    }

    public Item(String barcode, String name, String unit, double price, double discount, boolean promotion) {
        this(barcode, name, unit, price, discount);
        if (this.discount == 1) {
            this.setPromotion(promotion);
        }
    }

    public Item(double vipDiscount, String barcode, String name, String unit, double price) {
        this.setBarcode(barcode);
        this.setName(name);
        this.setUnit(unit);
        this.setPrice(price);
        this.setVipDiscount(vipDiscount);
    }


    public Item(double vipDiscount, String barcode, String name, String unit, double price, double discount) {
        this(barcode, name, unit, price);
        this.setDiscount(discount);
        this.setVipDiscount(vipDiscount);
    }

    public Item(double vipDiscount, String barcode, String name, String unit, double price, boolean promotion) {
        this(vipDiscount, barcode, name, unit, price);
        if (this.vipDiscount == 1) {
            this.setPromotion(promotion);
        }
    }

    public Item(double vipDiscount, String barcode, String name, String unit, double price, double discount, boolean promotion) {
        this(vipDiscount, barcode, name, unit, price, discount);
        this.setPromotion(promotion);
    }


    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
    }

    public double getDiscount() {
        if (discount == 0.00)
            return 1.00;
        return discount;
    }

    public boolean getPromotion() {
        if (promotion == false)
            return false;
        return true;
    }

    public double getVipDiscount() {
        if (vipDiscount == 0.00)
            return 1.00;
        return vipDiscount;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        if (discount >= 0 && discount < 1) {
            this.discount = discount;
            this.promotion = false;
        }
    }

    public void setPromotion(boolean promotion) {
        if (promotion) {
            this.discount = 1;
            this.vipDiscount = 1;
        }
        this.promotion = promotion;
    }

    public void setVipDiscount(double vipDiscount) {
        if (vipDiscount >= 0 && vipDiscount < 1) {
            this.vipDiscount = vipDiscount;
            this.promotion = false;
        }
    }
}
