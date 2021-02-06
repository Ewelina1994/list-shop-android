package com.example.my_list_shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.activity.DetailsListActivity;
import com.example.my_list_shop.entity.Item;

import java.util.List;

public class RecyclerViewConfig {
    private Context mContext;
    private ItemAdapter mQuestionAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Item> list) {
        mContext = context;
        mQuestionAdapter = new ItemAdapter(list, mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mQuestionAdapter);
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

        class ItemAdapter extends RecyclerView.Adapter<ItemView> {
            private List<Item> items;
            Context context;

            public ItemAdapter(List<Item> item, Context context) {
                this.items = item;
                this.context=context;
            }

            @NonNull
            @Override
            public RecyclerViewConfig.ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ItemView(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull final RecyclerViewConfig.ItemView holder, final int position) {
                holder.bind(items.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentDetails = new Intent(context, DetailsListActivity.class);
                        intentDetails.putExtra("id_item", items.get(position).getId());
                        if(items.get(position).getIsRemoved()==1){
                            intentDetails.putExtra("is_archived", true);
                        }
                        else if(items.get(position).getIsRemoved()==0){
                            intentDetails.putExtra("is_archived", false);
                        }
                        context.startActivity(intentDetails);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        }

}
