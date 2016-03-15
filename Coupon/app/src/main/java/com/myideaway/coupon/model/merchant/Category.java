package com.myideaway.coupon.model.merchant;

import java.util.ArrayList;
import java.util.List;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM8:59
 */
public class Category {
    private int id;
    private String name;
    private String namePinyin;
    private List<Category> children;
    private Category parent;

    public Category() {
        children = new ArrayList<Category>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
