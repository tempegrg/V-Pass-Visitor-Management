package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnVisitorEntry, btnGuardEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVisitorEntry = findViewById(R.id.btnVisitorEntry);
        btnGuardEntry = findViewById(R.id.btnGuardEntry);

        // Visitor path
        btnVisitorEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("user_role", "visitor");
            startActivity(intent);
        });

        // Guard path
        btnGuardEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("user_role", "guard");
            startActivity(intent);
        });
    }
}