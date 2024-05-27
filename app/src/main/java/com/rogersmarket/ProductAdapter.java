package com.rogersmarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;

import java.io.IOException;
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
            holder.btnEditProduct = convertView.findViewById(R.id.btnEditProduct);
            holder.btnDeleteProduct = convertView.findViewById(R.id.btnDeleteProduct);
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

        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

        holder.btnDeleteProduct.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> new DeleteProductTask(product).execute())
                .setNegativeButton(android.R.string.no, null)
                .show());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewName;
        TextView textViewAmount;
        TextView textViewPrice;
        TextView textViewType;
        TextView textViewProvider;
        Button btnEditProduct;
        Button btnDeleteProduct;
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteProductTask extends AsyncTask<Void, Void, Boolean> {
        private final Product product;

        DeleteProductTask(Product product) {
            this.product = product;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return WSJsonProducts.deleteProduct(product.getId());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                items.remove(product);
                notifyDataSetChanged();
                Toast.makeText(context, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error deleting product...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
