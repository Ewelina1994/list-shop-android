package com.example.my_list_shop.db;

import android.provider.BaseColumns;

public class ListContract {
    public ListContract() {
    }
    public static class ListTable implements BaseColumns {
        public static final String TABLE_NAME="item_list";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_IS_REMOVED ="is_activity";
        public static final String COLUMN_DATE="date";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ListContract.ListTable.TABLE_NAME + " (" +
                    ListTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ListTable.COLUMN_TITLE + " TEXT, "+
                    ListTable.COLUMN_IS_REMOVED + " INTEGER, "+
                    ListTable.COLUMN_DATE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ListTable.TABLE_NAME + ";";
}
