package com.example.my_list_shop.entity;

import java.util.Date;

public class Item {
    private long id;
    private String title;
    private int isRemoved;
    private Date data;

    public Item(String title, int isRemoved, Date data) {
        this.title = title;
        this.isRemoved = isRemoved;
        this.data = data;
    }

    public Item(long id, String title, int isRemoved, Date date) {
        this.id = id;
        this.title = title;
        this.isRemoved = isRemoved;
        this.data = date;
    }

    public Item() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(int isRemoved) {
        this.isRemoved = isRemoved;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
