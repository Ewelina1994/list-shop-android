package com.example.my_list_shop.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.R;
import com.example.my_list_shop.RecyclerViewClickInterface;
import com.example.my_list_shop.RecyclerViewDetails;
import com.example.my_list_shop.entity.ItemDetails;
import com.example.my_list_shop.service.ListDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class DetailsListActivity extends AppCompatActivity implements RecyclerViewClickInterface {
    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    private FloatingActionButton addItem;
    List<ItemDetails> itemList;
    String newItemText;
    String editNameItem;
    long id_item;
    boolean isArchived;
    RecyclerViewDetails.ItemAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_list_activity);
        mRecyclerView = findViewById(R.id.recyclerId);
        addItem = (FloatingActionButton) findViewById(R.id.fab);
        addItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialogWindowAdd();
            }
        });
        //        if (savedInstanceState == null) {
        dbHelper = new ListDBHelper(this);
        id_item=getIntent().getLongExtra("id_item", -1);
        isArchived=getIntent().getBooleanExtra("is_archived", false);

        itemList=dbHelper.getAllItemDetailsListByItemID(id_item);

        adapter= new RecyclerViewDetails().setConfig(mRecyclerView, this, itemList, this);
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
        ItemDetails newItemDetails = new ItemDetails(id_item, newItemText, 0, new Date());
        dbHelper.addItemDetails(newItemDetails);
        itemList.add(newItemDetails);
    }
    private void editItem(ItemDetails itemDetails, int position) {
        if (!editNameItem.equals("")) {
            itemDetails.setTitle(editNameItem);
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
                editNameItem = input.getText().toString();
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
        } else {
            itemDetails.setItem_id(0);
        }
    }


    @Override
    public void onItemClick(int position) {

        setDeleteItemDetails(position);
        adapter.notifyItemChanged(position);

    }


    @Override
    public void onLongItemClick(int position) {
        showDialogWindowUpdate(itemList.get(position), position);
    }
}
