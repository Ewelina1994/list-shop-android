package com.example.my_list_shop.recycler_view;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.R;
import com.example.my_list_shop.entity.ItemDetails;

import java.util.List;

public class RecyclerViewDetails {
    private Context mContext;
    private RecyclerViewDetails.ItemAdapter itemDetailsAdapter;

    public RecyclerViewDetails.ItemAdapter setConfig(RecyclerView recyclerView, Context context, List<ItemDetails> list, RecyclerViewClickInterface recyclerInterface, boolean isArchived) {
        mContext = context;
        itemDetailsAdapter = new RecyclerViewDetails.ItemAdapter(list, mContext, recyclerInterface, isArchived);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(itemDetailsAdapter);
        return itemDetailsAdapter;
    }

    class ItemView extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView check;

        public ItemView(ViewGroup parent, boolean isArchived) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_list_details_adapter, parent, false));
            title = itemView.findViewById(R.id.nameItem);
            check = itemView.findViewById(R.id.btnCheck);
            if (isArchived) {
                check.setEnabled(true);
            }
        }

        public void bind(ItemDetails item) {
            title.setText(item.getTitle());
            if (item.getIs_removed() == 0) {
                check.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
            } else {
                check.setImageResource(R.drawable.ic_check_circle_black_24dp);
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<RecyclerViewDetails.ItemView> {
        private List<ItemDetails> itemDetailsList;
        private Context context;
        private RecyclerViewClickInterface recyclerViewClickInterface;
        private boolean isArchived;

        public ItemAdapter(List<ItemDetails> item, Context context, RecyclerViewClickInterface recycler, boolean isArchived) {
            this.itemDetailsList = item;
            this.context = context;
            this.recyclerViewClickInterface = recycler;
            this.isArchived = isArchived;
        }

        @NonNull
        @Override
        public RecyclerViewDetails.ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewDetails.ItemView(parent, isArchived);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewDetails.ItemView holder, final int position) {
            holder.bind(itemDetailsList.get(position));
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClickInterface.onLongItemClick(position);
                    return true;
                }
            });
        }


        @Override
        public int getItemCount() {
            return itemDetailsList.size();
        }

    }
}
