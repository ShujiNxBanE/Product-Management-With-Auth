package com.rogersmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText signIn_etName, signIn_etUser, signIn_etPassword;
    Button signIn_btnRegister, signIn_btnCancel;

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
        signIn_btnCancel = findViewById(R.id.signIn_btnCancel);

        signIn_btnRegister.setOnClickListener(this);

        signIn_btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final String name = signIn_etName.getText().toString();
        final String username = signIn_etUser.getText().toString();
        final String password = signIn_etPassword.getText().toString();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = response -> {
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
                e.printStackTrace();
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name, username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        queue.add(registerRequest);
    }
}