package com.example.my_list_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.entity.Item;

import java.util.List;

public class RecyclerViewItem {
    private Context mContext;
    private ItemAdapter mQuestionAdapter;

    public ItemAdapter setConfig(RecyclerView recyclerView, Context context, List<Item> list, RecyclerViewClickInterface recyclerInterface) {
        mContext = context;
        mQuestionAdapter = new ItemAdapter(list, mContext, recyclerInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mQuestionAdapter);
        return mQuestionAdapter;
    }

    class ItemView extends RecyclerView.ViewHolder {
        private TextView title;

        public ItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_list_adapter, parent, false));

            title = itemView.findViewById(R.id
                    .nameItem);
        }

        public void bind(Item item) {
            title.setText(item.getTitle());
        }
    }

        public class ItemAdapter extends RecyclerView.Adapter<ItemView> {
            private List<Item> items;
            Context context;
            RecyclerViewClickInterface recyclerViewClickInterface;

            public ItemAdapter(List<Item> item, Context context, RecyclerViewClickInterface recycler) {
                this.items = item;
                this.context=context;
                recyclerViewClickInterface=recycler;
            }

            @NonNull
            @Override
            public RecyclerViewItem.ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ItemView(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull final RecyclerViewItem.ItemView holder, final int position) {
                holder.bind(items.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewClickInterface.onItemClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        recyclerViewClickInterface.onLongItemClick(position);
                        return false;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        }

}
