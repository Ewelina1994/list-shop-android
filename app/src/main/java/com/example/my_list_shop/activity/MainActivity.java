package com.example.my_list_shop.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.my_list_shop.R;
import com.example.my_list_shop.RecyclerViewConfig;
import com.example.my_list_shop.entity.Item;
import com.example.my_list_shop.service.ListDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    private FloatingActionButton addItem;
    List<Item> itemList;
    String newItemText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerId);
        addItem = (FloatingActionButton) findViewById(R.id.fab);
        addItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    showDialogWindow();
                }
        });
//        if (savedInstanceState == null) {
        dbHelper = new ListDBHelper(this);
        itemList=dbHelper.getAllItemList();

        new RecyclerViewConfig().setConfig(mRecyclerView, MainActivity.this, itemList);
    }

    private void addNewItemList() throws ParseException {
        SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Item newItem = new Item(newItemText, 1, sdf2.parse(sdf2.format(new Date())));
        dbHelper.addItem(newItem);
        itemList.add(newItem);
        //new RecyclerViewConfig().setConfig(mRecyclerView, MainActivity.this, itemList);
    }

    private void showDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.name_item);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newItemText = input.getText().toString();
                if (newItemText.equals("")) return;
                try {
                    addNewItemList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

}
