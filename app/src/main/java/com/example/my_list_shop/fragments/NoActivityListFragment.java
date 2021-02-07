package com.example.my_list_shop.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class NoActivityListFragment extends Fragment implements RecyclerViewClickInterface {
    View v;
    private RecyclerView mRecyclerView;
    private ListDBHelper dbHelper;
    List<Item> itemList;
    private RecyclerViewItem.ItemAdapter adapter;
    public NoActivityListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.not_activity_list_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerId);
        adapter = new RecyclerViewItem().setConfig(mRecyclerView, getActivity(), itemList, this);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ListDBHelper(getActivity());
        itemList = dbHelper.getListItemArchived();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        itemList = dbHelper.getListItemArchived();
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onItemClick(int position) {
        Intent intentDetails = new Intent(getActivity(), DetailsListActivity.class);
        Item item=itemList.get(position);
        intentDetails.putExtra("id_item", item.getId());

        intentDetails.putExtra("is_archived", true);

        startActivity(intentDetails);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void addItemToArchivedListFromAnotcherFragment(Item newItem) {
        itemList.add(newItem);
        int position = itemList.indexOf(newItem);
        adapter.notifyItemChanged(position);
    }
}
