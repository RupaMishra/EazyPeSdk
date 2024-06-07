package com.example.easypepg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Map.Entry<String,Object>> itemList;

    public MyAdapter( List<Map.Entry<String,Object>> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciept_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Map.Entry<String,Object> item = itemList.get(position);
        holder.key.setText(item.getKey());
        holder.value.setText(String.valueOf(item.getValue()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView key;
        TextView value;

        MyViewHolder(View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);
        }
    }
}
