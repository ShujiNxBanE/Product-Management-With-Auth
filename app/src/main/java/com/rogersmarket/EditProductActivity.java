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
        // Validate and update product
        existingProduct.setName(etName.getText().toString());
        existingProduct.setAmount(etAmount.getText().toString());
        existingProduct.setPrice(etPrice.getText().toString());
        existingProduct.setType(etType.getText().toString());
        existingProduct.setProvider(etProvider.getText().toString());

        new EditProduct().execute(existingProduct);
    }

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
