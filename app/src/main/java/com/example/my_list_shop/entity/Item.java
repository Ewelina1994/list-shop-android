package com.example.my_list_shop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.Date;

public class Item implements Comparable<Item>, Parcelable {
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

    protected Item(Parcel in) {
        id = in.readLong();
        title = in.readString();
        isRemoved = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    @Override
    public int compareTo(Item o) {
        if (getData() == null || o.getData() == null) {
            return 0;
        }
        return getData().compareTo(o.getData());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(isRemoved);
    }
}
