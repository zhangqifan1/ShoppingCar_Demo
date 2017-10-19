package com.bawei.shoppingcar_demo;

/**
 * Created by 张祺钒
 * on2017/10/19.
 *
 * 用于购物车 价钱 和 购物数量 存取的Bean类
 */

public class Bean {
    String price;
    String number;

    public Bean(String price, String number) {
        this.price = price;
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
