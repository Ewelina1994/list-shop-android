package com.example.my_list_shop.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.my_list_shop.db.ListContract;
import com.example.my_list_shop.db.ListDetailsContract;
import com.example.my_list_shop.entity.Item;
import com.example.my_list_shop.entity.ItemDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "list_shoping.db";
    private static final int DATABASE_VERSION = 1;

    private static ListDBHelper instance;
    private SQLiteDatabase db;


    public ListDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static synchronized ListDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ListDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        db.execSQL("PRAGMA Foreign_keys = ON");

        final String SQL_CREATE_ITEM_TABLE = ListContract.SQL_CREATE_ENTRIES;
        final String SQL_CREATE_ITEM_DETAILS_TABLE = ListDetailsContract.SQL_CREATE_ENTRIES;

        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_ITEM_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ListContract.SQL_DELETE_ENTRIES);
        db.execSQL(ListDetailsContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long addItem(Item item) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ListContract.ListTable.COLUMN_TITLE, item.getTitle());
        cv.put(ListContract.ListTable.COLUMN_IS_REMOVED, item.getIsRemoved());
        cv.put(ListContract.ListTable.COLUMN_DATE, String.valueOf(item.getData()));

        db.insert(ListContract.ListTable.TABLE_NAME, null, cv);
        getLastId();
        return getLastId();
    }

    private long getLastId() {
        long index = 0;
        SQLiteDatabase sdb = getReadableDatabase();
        Cursor cursor = sdb.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{ListContract.ListTable.TABLE_NAME},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        return index;
    }

    public boolean updateItem(Item item) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ListContract.ListTable.COLUMN_TITLE, item.getTitle());
        cv.put(ListContract.ListTable.COLUMN_IS_REMOVED, item.getIsRemoved());
        String id = String.valueOf(item.getId());

        return (db.update(ListContract.ListTable.TABLE_NAME, cv, "_id = ?", new String[]{id})) > 0;
    }

    public boolean deleteItem(long id_) {
        String id = String.valueOf(id_);
        ContentValues cv = new ContentValues();
        cv.put(ListContract.ListTable.COLUMN_IS_REMOVED, 1);
        return (db.update(ListContract.ListTable.TABLE_NAME, cv, "_id = ?", new String[]{id})) > 0;
    }

    public List<Item> getListItemActivity() {
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ListContract.ListTable.TABLE_NAME + " WHERE " + ListContract.ListTable.COLUMN_IS_REMOVED + " = " + 0, null);

        return writeDate(cursor);
    }

    public List<Item> getListItemArchived() {
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ListContract.ListTable.TABLE_NAME + " WHERE " + ListContract.ListTable.COLUMN_IS_REMOVED + " = " + 1, null);

        return writeDate(cursor);
    }

    public List<Item> writeDate(Cursor cursor) {
        List<Item> itemlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Item newItem = new Item();
                newItem.setId(cursor.getInt(cursor.getColumnIndex(ListContract.ListTable._ID)));
                newItem.setTitle(cursor.getString(cursor.getColumnIndex(ListContract.ListTable.COLUMN_TITLE)));
                newItem.setIsRemoved(cursor.getInt(cursor.getColumnIndex(ListContract.ListTable.COLUMN_IS_REMOVED)));
                String s = cursor.getString(cursor.getColumnIndex(ListContract.ListTable.COLUMN_DATE));
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy:mm:dd HH:mm:ss");
                Date d = new Date();
                try {
                    if (s != null) {
                        d = sdf2.parse(s);
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                newItem.setData(d);
                itemlist.add(newItem);
            } while ((cursor.moveToNext()));
        }
        return itemlist;
    }

    public boolean addItemDetails(ItemDetails item) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_LIST_ID, item.getItem_id());
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_TITLE, item.getTitle());

        return (db.insert(ListDetailsContract.ListDetailsTable.TABLE_NAME, null, cv)) > 0;
    }

    public boolean updateItemDetails(ItemDetails item) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_LIST_ID, item.getItem_id());
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_TITLE, item.getTitle());
        String id = String.valueOf(item.getId());

        return (db.update(ListDetailsContract.ListDetailsTable.TABLE_NAME, cv, "_id = ?", new String[]{id})) > 0;
    }

    public boolean deleteItemDetails(long id_) {
        db = getWritableDatabase();
        String id = String.valueOf(id_);
        ContentValues cv = new ContentValues();
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_IS_REMOVED, 1);
        return (db.update(ListDetailsContract.ListDetailsTable.TABLE_NAME, cv, "_id = ?", new String[]{id})) > 0;
    }

    public boolean noDeleteItemDetails(long id_) {
        db = getWritableDatabase();
        String id = String.valueOf(id_);
        ContentValues cv = new ContentValues();
        cv.put(ListDetailsContract.ListDetailsTable.COLUMN_IS_REMOVED, 0);
        return (db.update(ListDetailsContract.ListDetailsTable.TABLE_NAME, cv, "_id = ?", new String[]{id})) > 0;
    }

    public List<ItemDetails> getAllItemDetailsListByItemID(long id) {
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ListDetailsContract.ListDetailsTable.TABLE_NAME + " WHERE " + ListDetailsContract.ListDetailsTable.COLUMN_LIST_ID + " = " + id, null);

        List<ItemDetails> listItemDetails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ItemDetails newItem = new ItemDetails();
                newItem.setId(cursor.getInt(cursor.getColumnIndex(ListDetailsContract.ListDetailsTable._ID)));
                newItem.setTitle(cursor.getString(cursor.getColumnIndex(ListDetailsContract.ListDetailsTable.COLUMN_TITLE)));
                newItem.setItem_id(cursor.getInt(cursor.getColumnIndex(ListDetailsContract.ListDetailsTable.COLUMN_LIST_ID)));
                newItem.setIs_removed(cursor.getInt(cursor.getColumnIndex(ListDetailsContract.ListDetailsTable.COLUMN_IS_REMOVED)));
                String s = cursor.getString(cursor.getColumnIndex(ListDetailsContract.ListDetailsTable.COLUMN_DATE));
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                Date d = new Date();
                try {
                    if (s != null) {
                        d = sdf2.parse(s);
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                newItem.setDate(d);
                listItemDetails.add(newItem);
            } while ((cursor.moveToNext()));
        }
        cursor.close();
        return listItemDetails;
    }
}
