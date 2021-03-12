package com.example.my_list_shop.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FragmentActivityList extends Fragment implements RecyclerViewClickInterface {
    public static final String ITEM_LIST = "itemList";
    private static final String IS_SORT_DESC = "sort";
    private View v;
    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    private FloatingActionButton addItem;
    private ArrayList<Item> itemList;
    private String newItemText;
    private RecyclerViewItem.ItemAdapter adapter;
    private CommunicateBetweenFragments sendItem;
    private boolean isSortedDesc = true;

    public FragmentActivityList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_activity_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerId);
        adapter = new RecyclerViewItem().setConfig(mRecyclerView, getActivity(), itemList, this);
        addItem = v.findViewById(R.id.fab);
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
        itemList = (ArrayList<Item>) dbHelper.getListItemActivity();

        if (savedInstanceState != null) {
            itemList = savedInstanceState.getParcelableArrayList(ITEM_LIST);
            savedInstanceState.getBoolean(IS_SORT_DESC);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendItem = (CommunicateBetweenFragments) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    private void addNewItemList() throws ParseException {
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Item newItem = new Item(newItemText, 0, sdf2.parse(sdf2.format(new Date())));
        long lastIdFromDatabase = dbHelper.addItem(newItem);
        newItem.setId(lastIdFromDatabase);
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
        Item item = itemList.get(position);
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
        builder.setTitle(getText(R.string.are_you_sure_you_want_archived) + " " + itemList.get(position).getTitle());

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
        if (position < itemList.size()) {
            Item item = itemList.get(position);
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

    public void sortList() {
        if (!isSortedDesc) {
            Collections.sort(itemList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getData().compareTo(o2.getData());
                }
            });
            isSortedDesc = true;
        } else {
            Collections.reverse(itemList);
            System.out.println("sort odwrotne");
            isSortedDesc = false;
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SORT_DESC, isSortedDesc);
        outState.putParcelableArrayList(ITEM_LIST, (ArrayList<? extends Parcelable>) itemList);

    }
}
