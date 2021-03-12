package com.example.my_list_shop.db;

import android.provider.BaseColumns;

public class ListDetailsContract {
    public ListDetailsContract() {
    }

    public static class ListDetailsTable implements BaseColumns {
        public static final String TABLE_NAME = "list_details_item";
        public static final String COLUMN_LIST_ID = "list_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IS_REMOVED = "is_remove";
        public static final String COLUMN_DATE = "date";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ListDetailsTable.TABLE_NAME + " (" +
                    ListDetailsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ListDetailsTable.COLUMN_LIST_ID + " INTEGER, " +
                    ListDetailsTable.COLUMN_TITLE + " TEXT, " +
                    ListDetailsTable.COLUMN_IS_REMOVED + " INTEGER, " +
                    ListDetailsTable.COLUMN_DATE + " TEXT, " +
                    "FOREIGN KEY (list_id) REFERENCES item_list (_id));";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ListDetailsTable.TABLE_NAME + ";";

}
