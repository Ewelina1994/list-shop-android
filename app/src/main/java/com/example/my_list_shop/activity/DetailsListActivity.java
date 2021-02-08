package com.example.my_list_shop.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.R;
import com.example.my_list_shop.recycler_view.RecyclerViewClickInterface;
import com.example.my_list_shop.recycler_view.RecyclerViewDetails;
import com.example.my_list_shop.entity.ItemDetails;
import com.example.my_list_shop.service.ListDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DetailsListActivity extends AppCompatActivity implements RecyclerViewClickInterface {
    public static final String ITEM_LIST = "itemList";
    public static final String NEW_ITEM_NAME = "newItemName";
    public static final String ID_PARENT = "idParent";
    public static final String NAME_PARENT = "nameParent";
    public static final String IS_ARCHIVED = "isArchived";
    public static final String IS_SORT_DESC = "isSortedDesc";

    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    private FloatingActionButton addItem;
    ArrayList<ItemDetails> itemList;
    String newItemText;
    long id_parent;
    String nameParent;
    boolean isArchived;
    boolean isSortedDesc =true;
    RecyclerViewDetails.ItemAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_list_activity);
        mRecyclerView = findViewById(R.id.recyclerId);
        addItem = (FloatingActionButton) findViewById(R.id.fab);
        dbHelper = new ListDBHelper(this);


            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogWindowAdd();
                }
            });
            id_parent = getIntent().getLongExtra("id_item", -1);
            nameParent = getIntent().getStringExtra("name_item_parent");
            isArchived = getIntent().getBooleanExtra("is_archived", false);

            itemList = (ArrayList<ItemDetails>) dbHelper.getAllItemDetailsListByItemID(id_parent);

            if (savedInstanceState != null){
            itemList= savedInstanceState.getParcelableArrayList(ITEM_LIST);
            savedInstanceState.getString(NEW_ITEM_NAME);
            savedInstanceState.getLong(ID_PARENT);
            savedInstanceState.getString(NAME_PARENT);
            savedInstanceState.getBoolean(IS_ARCHIVED);
            savedInstanceState.getBoolean(IS_SORT_DESC);
        }
            setTitle(getText(R.string.list_details) + " "+ nameParent);
        adapter = new RecyclerViewDetails().setConfig(mRecyclerView, this, itemList, this);
        if (isArchived) {
            addItem.setVisibility(View.GONE);
        }
    }

    private void showDialogWindowAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.name_item_details);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newItemText = input.getText().toString();
                if (newItemText.equals("")) return;
                addNewItemList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addNewItemList() {
        ItemDetails newItemDetails = new ItemDetails(id_parent, newItemText, 0, new Date());
        dbHelper.addItemDetails(newItemDetails);
        itemList.add(newItemDetails);
    }
    private void editItem(ItemDetails itemDetails, int position) {
        if (!newItemText.equals("")) {
            itemDetails.setTitle(newItemText);
            dbHelper.updateItemDetails(itemDetails);
            adapter.notifyItemChanged(position);
        }
    }

    private void showDialogWindowUpdate(final ItemDetails item, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_item);

        final EditText input = new EditText(this);
        input.setText(item.getTitle());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newItemText = input.getText().toString();
                editItem(item, position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setDeleteItemDetails(int position) {
        ItemDetails itemDetails = itemList.get(position);
        if (itemDetails.getIs_removed() == 0) {
            itemDetails.setIs_removed(1);
            dbHelper.deleteItemDetails(itemDetails.getId());

            adapter.notifyItemChanged(position);

        } else {
            itemDetails.setIs_removed(0);
            dbHelper.noDeleteItemDetails(itemDetails.getId());
            adapter.notifyItemChanged(position);
        }
    }


    @Override
    public void onItemClick(int position) {

        if(!isArchived){
            setDeleteItemDetails(position);
        }

    }


    @Override
    public void onLongItemClick(int position) {
        if(!isArchived) {
            showDialogWindowUpdate(itemList.get(position), position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.id_sort) {
            sortList(itemList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortList(List<ItemDetails> itemList) {

        if(!isSortedDesc){
            Collections.sort(itemList, new Comparator<ItemDetails>() {
                public int compare(ItemDetails o1, ItemDetails o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            isSortedDesc =true;
        }else {
            Collections.reverse(itemList);
            System.out.println("sort odwrotne");
            isSortedDesc =false;
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(ITEM_LIST, itemList);
        savedInstanceState.putString(NEW_ITEM_NAME, newItemText);
        savedInstanceState.putLong(ID_PARENT, id_parent);
        savedInstanceState.putString(NAME_PARENT, nameParent);
        savedInstanceState.putBoolean(IS_ARCHIVED, isArchived);
        savedInstanceState.putBoolean(IS_SORT_DESC, isSortedDesc);

        //declare values before saving the state
        super.onSaveInstanceState(savedInstanceState);
    }
}
