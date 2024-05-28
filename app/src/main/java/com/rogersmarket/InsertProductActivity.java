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

public class InsertProductActivity extends AppCompatActivity {

    EditText etName, etAmount, etPrice, etType, etProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etAmount = findViewById(R.id.etAmount);
        etPrice = findViewById(R.id.etPrice);
        etType = findViewById(R.id.etType);
        etProvider = findViewById(R.id.etProvider);

    }

    public void btnAccept_Click(View v) {

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

        Product product = new Product();
        product.setName(name);
        product.setAmount(String.valueOf(amount));
        product.setPrice(String.valueOf(price));
        product.setType(type);
        product.setProvider(provider);

        new InsertProduct().execute(product);
    }


    public void btnCancel_Click(View v){
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertProduct extends AsyncTask<Object, Object, Boolean>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(InsertProductActivity.this, "", "Sending data...", true);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            Product product = (Product) params[0];
            if(product == null ) return false;
            try {
                return  WSJsonProducts.insertProduct(product);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if(result) {
                finish();
            }
            else{
                Toast.makeText(InsertProductActivity.this, "Error sending data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}