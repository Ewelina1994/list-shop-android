package com.example.my_list_shop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ItemDetails implements Comparable<ItemDetails>, Parcelable {
    private long id;
    private long item_id;
    private String title;
    private int is_removed;
    private Date date;

    public ItemDetails(long item_id, String title, int is_removed, Date date) {
        this.item_id = item_id;
        this.title = title;
        this.is_removed = is_removed;
        this.date = date;
    }

    public ItemDetails() {

    }

    protected ItemDetails(Parcel in) {
        id = in.readLong();
        item_id = in.readLong();
        title = in.readString();
        is_removed = in.readInt();
    }

    public static final Creator<ItemDetails> CREATOR = new Creator<ItemDetails>() {
        @Override
        public ItemDetails createFromParcel(Parcel in) {
            return new ItemDetails(in);
        }

        @Override
        public ItemDetails[] newArray(int size) {
            return new ItemDetails[size];
        }
    };

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

    @Override
    public int compareTo(ItemDetails o) {
        if (getDate() == null || o.getDate() == null)
            return 0;
        return getDate().compareTo(o.getDate());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(item_id);
        dest.writeString(title);
        dest.writeInt(is_removed);
    }
}
