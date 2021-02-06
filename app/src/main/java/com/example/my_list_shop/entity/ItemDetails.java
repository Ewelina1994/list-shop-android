package com.example.my_list_shop.entity;

import java.util.Date;

public class ItemDetails {
    private long id;
    private long item_id;
    private String title;
    private int is_removed;
    private Date date;

    public ItemDetails(long item_id, String title, int is_removed, Date date) {
        this.item_id = item_id;
        this.title = title;
        this.is_removed=is_removed;
        this.date = date;
    }

    public ItemDetails(long id, long item_id, String title, int is_removed, Date date) {
        this.id = id;
        this.item_id = item_id;
        this.title = title;
        this.is_removed=is_removed;
        this.date = date;
    }

    public ItemDetails() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIs_removed() {
        return is_removed;
    }

    public void setIs_removed(int is_removed) {
        this.is_removed = is_removed;
    }
}
