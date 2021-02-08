package com.example.my_list_shop.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.R;
import com.example.my_list_shop.recycler_view.RecyclerViewClickInterface;
import com.example.my_list_shop.recycler_view.RecyclerViewItem;
import com.example.my_list_shop.activity.DetailsListActivity;
import com.example.my_list_shop.entity.Item;
import com.example.my_list_shop.service.ListDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentActivityList extends Fragment implements RecyclerViewClickInterface {
    View v;
    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    private FloatingActionButton addItem;
    List<Item> itemList;
    private String newItemText;
    private RecyclerViewItem.ItemAdapter adapter;
    ComunicateBetweenFragments sendItem;

    public FragmentActivityList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.list_activity_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerId);
        adapter = new RecyclerViewItem().setConfig(mRecyclerView, getActivity(), itemList, this);
        addItem = (FloatingActionButton) v.findViewById(R.id.fab);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWindow();
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ListDBHelper(getActivity());
        itemList = dbHelper.getListItemActivity();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendItem = (ComunicateBetweenFragments) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    private void addNewItemList() throws ParseException {
        SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Item newItem = new Item(newItemText, 0, sdf2.parse(sdf2.format(new Date())));
        long lastdIdFromDatabase = dbHelper.addItem(newItem);
        newItem.setId(lastdIdFromDatabase);
        itemList.add(newItem);
        adapter.notifyItemChanged(itemList.indexOf(newItem));

    }

    private void showDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.name_item);

        final EditText input = new EditText(getActivity());
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

    @Override
    public void onItemClick(int position) {
        Intent intentDetails = new Intent(getActivity(), DetailsListActivity.class);
        Item item=itemList.get(position);
        intentDetails.putExtra("id_item", item.getId());
        intentDetails.putExtra("name_item_parent", item.getTitle());
        intentDetails.putExtra("is_archived", false);

        startActivity(intentDetails);
    }

    @Override
    public void onLongItemClick(int position) {
        showDialogSetArchived(position);

    }
    private void showDialogSetArchived(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getText(R.string.are_you_sure_you_want_archived) + " "+itemList.get(position).getTitle());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             archive_list(position);
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

    private boolean archive_list(int position) {
        if(position<itemList.size()){
            Item item=itemList.get(position);
            item.setIsRemoved(1);
            dbHelper.updateItem(item);
            itemList.remove(item);
            adapter.notifyDataSetChanged();
            //send Item Archive to Second Fragment
            sendItem.sendItemToArchive(item);
            return true;
        }
        return false;
    }
}
