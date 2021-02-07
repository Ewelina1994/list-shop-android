package com.example.my_list_shop;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_list_shop.entity.ItemDetails;
import com.example.my_list_shop.service.ListDBHelper;

import java.util.List;

public class RecyclerViewDetails {
    private Context mContext;
    private RecyclerViewDetails.ItemAdapter itemDetailsAdapter;

    public RecyclerViewDetails.ItemAdapter setConfig(RecyclerView recyclerView, Context context, List<ItemDetails> list, RecyclerViewClickInterface recyclerInterface) {
        mContext = context;
        itemDetailsAdapter = new RecyclerViewDetails.ItemAdapter(list, mContext, recyclerInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(itemDetailsAdapter);
        return itemDetailsAdapter;
    }

     class ItemView extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView check;

        public ItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_list_details_adapter, parent, false));
            title = itemView.findViewById(R.id.nameItem);
            check = itemView.findViewById(R.id.btnCheck);
        }

        public void bind(ItemDetails item) {
            title.setText(item.getTitle());
            if(item.getIs_removed()==0){
                check.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
            }else {
                check.setImageResource(R.drawable.ic_check_circle_black_24dp);
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

     public class ItemAdapter extends RecyclerView.Adapter<RecyclerViewDetails.ItemView> {
         private List<ItemDetails> itemDetailsList;
         private Context context;
         private RecyclerViewClickInterface recyclerViewClickInterface;

         public ItemAdapter(List<ItemDetails> item, Context context, RecyclerViewClickInterface recycler) {
             this.itemDetailsList = item;
             this.context = context;
             this.recyclerViewClickInterface = recycler;
         }

         @NonNull
         @Override
         public RecyclerViewDetails.ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             return new RecyclerViewDetails.ItemView(parent);
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

//         private void deleteItem(ItemDetails item, @NonNull final RecyclerViewDetails.ItemView holder) {
//             if (item.getIs_removed() == 0) {
//                 item.setIs_removed(1);
//                 dbHelper.deleteItemDetails(item.getId());
//                 holder.check.setImageResource(R.drawable.ic_check_circle_black_24dp);
//                 holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//             } else {
//                 item.setItem_id(0);
//                 dbHelper.noDeleteItemDetails(item.getId());
//                 holder.check.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
//                 holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//
//             }


         @Override
         public int getItemCount() {
             return itemDetailsList.size();
         }

     }
}
