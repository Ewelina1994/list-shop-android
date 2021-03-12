package com.example.my_list_shop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.R;
import com.example.my_list_shop.recycler_view.RecyclerViewClickInterface;
import com.example.my_list_shop.recycler_view.RecyclerViewItem;
import com.example.my_list_shop.activity.DetailsListActivity;
import com.example.my_list_shop.entity.Item;
import com.example.my_list_shop.service.ListDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoActivityListFragment extends Fragment implements RecyclerViewClickInterface {
    private static final String IS_SORT_DESC = "sort";
    private static final String ITEM_LIST = "itemList";
    View v;
    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    ArrayList<Item> itemList;
    private RecyclerViewItem.ItemAdapter adapter;
    private boolean isSortedDesc = true;

    public NoActivityListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.not_activity_list_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerId);
        adapter = new RecyclerViewItem().setConfig(mRecyclerView, getActivity(), itemList, this);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ListDBHelper(getActivity());
        itemList = (ArrayList<Item>) dbHelper.getListItemArchived();

        if (savedInstanceState != null) {
            itemList = savedInstanceState.getParcelableArrayList(ITEM_LIST);
            savedInstanceState.getBoolean(IS_SORT_DESC);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentDetails = new Intent(getActivity(), DetailsListActivity.class);
        Item item = itemList.get(position);
        intentDetails.putExtra("id_item", item.getId());
        intentDetails.putExtra("name_item_parent", item.getTitle());
        intentDetails.putExtra("is_archived", true);

        startActivity(intentDetails);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void addItemToArchivedListFromAnotherFragment(Item newItem) {
        itemList.add(newItem);
        int position = itemList.indexOf(newItem);
        adapter.notifyItemChanged(position);
    }

    public void sortListByDate() {
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
        outState.putParcelableArrayList(ITEM_LIST, itemList);
        outState.putBoolean(IS_SORT_DESC, isSortedDesc);

    }
}
