package com.rogersmarket;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView listProducts;
    TextView tvName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.Name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null) {
            tvName.setText("Welcome " + name + "!");
        } else {
            tvName.setText("Welcome!");
        }

        listProducts = findViewById(R.id.listViewProducts);
        new GetProducts().execute();

        listProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selectedProduct = ((ProductAdapter) parent.getAdapter()).getItem(position);
            Intent intent2 = new Intent(MainActivity.this, EditProductActivity.class);
            intent2.putExtra("product", selectedProduct);
            startActivity(intent2);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.createNewProduct){
            Intent intent = new Intent(this,InsertProductActivity.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.closeSession){
            // Clean the state of session
            SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            // Redirect to login
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new GetProducts().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetProducts extends AsyncTask<Void, Void, Products> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading data...", true);
        }

        @Override
        protected Products doInBackground(Void... voids) {
            try {

                return WSJsonProducts.getProducts();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Products result) {
            super.onPostExecute(result);

            if (result != null) {
                ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.product, result);
                listProducts.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Error loading data...", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
