package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnVisitorEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVisitorEntry = findViewById(R.id.btnVisitorEntry);

        btnVisitorEntry.setOnClickListener(v -> {
            // First step in the flow: Go to Login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}