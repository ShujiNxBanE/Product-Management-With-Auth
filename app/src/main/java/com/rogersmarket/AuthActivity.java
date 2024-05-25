package com.rogersmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class AuthActivity extends AppCompatActivity {

    TextView auth_btnSignIn;
    EditText register_etUser, register_etPassword;
    Button auth_btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verify the state of session
        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        boolean loggedIn = preferences.getBoolean("logged_in", false);

        if (loggedIn) {
            String name = preferences.getString("name", null);
            String username = preferences.getString("username", null);
            String password = preferences.getString("password", null);

            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("username", username);
            intent.putExtra("password", password);

            AuthActivity.this.startActivity(intent);
            finish();
        }

        register_etUser = findViewById(R.id.register_etUser);
        register_etPassword = findViewById(R.id.register_etPassword);

        auth_btnSignIn = findViewById(R.id.register_btnSignIn);
        auth_btnStart = findViewById(R.id.register_btnStart);

        auth_btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, SignInActivity.class);
            AuthActivity.this.startActivity(intent);
        });

        auth_btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = register_etUser.getText().toString();
                final String password = register_etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success){

                                String name = jsonResponse.getString("name");

                                // Save state of session
                                SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("name", name);
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putBoolean("logged_in", true);
                                editor.apply();

                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);

                                AuthActivity.this.startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                                builder.setMessage("Error during Login.")
                                        .setNegativeButton("Retry",null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(AuthActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}