package com.rogersmarket;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.IOException;

public class EditProductActivity extends AppCompatActivity {

    EditText etName, etAmount, etPrice, etType, etProvider;
    Product existingProduct; // Assuming you have a way to get the existing product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_productMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.edit_etName);
        etAmount = findViewById(R.id.edit_etAmount);
        etPrice = findViewById(R.id.edit_etPrice);
        etType = findViewById(R.id.edit_etType);
        etProvider = findViewById(R.id.edit_etProvider);

        // Assuming you pass the product data as intent extras or have another way to retrieve it
        existingProduct = (Product) getIntent().getSerializableExtra("product");

        // Populate fields with existing product data
        if (existingProduct != null) {
            etName.setText(existingProduct.getName());
            etAmount.setText(existingProduct.getAmount());
            etPrice.setText(existingProduct.getPrice());
            etType.setText(existingProduct.getType());
            etProvider.setText(existingProduct.getProvider());
        }
    }

    public void btnEdit_Click(View v) {

        String name = etName.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String provider = etProvider.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty() || priceStr.isEmpty() || type.isEmpty() || provider.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Amount must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        existingProduct.setName(name);
        existingProduct.setAmount(String.valueOf(amount));
        existingProduct.setPrice(String.valueOf(price));
        existingProduct.setType(type);
        existingProduct.setProvider(provider);

        new EditProduct().execute(existingProduct);
    }

    public void btnEdit_CancelClick(View v){ finish(); }

    @SuppressLint("StaticFieldLeak")
    private class EditProduct extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EditProductActivity.this, "", "Updating data...", true);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            Product product = (Product) params[0];
            if (product == null) return false;
            try {
                return WSJsonProducts.updateProduct(product); // Assuming you have this method
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result) {
                finish();
            } else {
                Toast.makeText(EditProductActivity.this, "Error updating data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
