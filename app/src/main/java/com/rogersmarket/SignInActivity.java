package com.rogersmarket;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    EditText signIn_etName, signIn_etUser, signIn_etPassword;
    Button signIn_btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signIn_etName = findViewById(R.id.signIn_etName);
        signIn_etUser = findViewById(R.id.signIn_etUser);
        signIn_etPassword = findViewById(R.id.signIn_etPassword);

        signIn_btnRegister = findViewById(R.id.signIn_btnRegister);

        signIn_btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d(TAG, "Register button clicked");
        final String name = signIn_etName.getText().toString();
        final String username = signIn_etUser.getText().toString();
        final String password = signIn_etPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                Log.e("SignInActivity", "Server Response: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success){
                        Intent intent = new Intent(SignInActivity.this, AuthActivity.class);
                        SignInActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                        builder.setMessage("Error during sign in.")
                                .setNegativeButton("Retry",null)
                                .create().show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Parsing Error", e);
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name, username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        queue.add(registerRequest);
    }
}