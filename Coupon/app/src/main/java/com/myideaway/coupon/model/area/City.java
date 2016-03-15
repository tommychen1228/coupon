package com.myideaway.coupon.model.area;

import java.util.ArrayList;
import java.util.List;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM9:16
 */
public class City {
    private int id;
    private String name;
    private String namePinYin;
    private City parent;
    private List<City> children;

    public City() {
        children = new ArrayList<City>();
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

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }

    public City getParent() {
        return parent;
    }

    public void setParent(City parent) {
        this.parent = parent;
    }

    public List<City> getChildren() {
        return children;
    }

    public void setChildren(List<City> children) {
        this.children = children;
    }
}
