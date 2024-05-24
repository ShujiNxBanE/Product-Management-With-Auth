package com.rogersmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Product> items;
    private final int layout;

    public ProductAdapter(Context context, int layout, ArrayList<Product> items) {
        this.context = context;
        this.items = items;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Product getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.textViewName = convertView.findViewById(R.id.tvName);
            holder.textViewAmount = convertView.findViewById(R.id.tvAmount);
            holder.textViewPrice = convertView.findViewById(R.id.tvPrice);
            holder.textViewType = convertView.findViewById(R.id.tvType);
            holder.textViewProvider = convertView.findViewById(R.id.tvProvider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);

        holder.textViewName.setText(product.getName());
        holder.textViewAmount.setText(product.getAmount());
        holder.textViewPrice.setText(product.getPrice());
        holder.textViewType.setText(product.getType());
        holder.textViewProvider.setText(product.getProvider());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewAmount;
        TextView textViewPrice;
        TextView textViewType;
        TextView textViewProvider;
    }
}
