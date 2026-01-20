package com.example.vpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etIC, etPhone, etVehicle, etPurpose;
    Button btnGenerateQR;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Link UI
        etName = findViewById(R.id.etVisitorName);
        etIC = findViewById(R.id.etVisitorIC);
        etPhone = findViewById(R.id.etPhone);
        etVehicle = findViewById(R.id.etVehicle);
        etPurpose = findViewById(R.id.etPurpose);
        btnGenerateQR = findViewById(R.id.btnGenerateQR);

        dbHelper = new DBHelper(this);

        // Button click
        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVisitor();
            }
        });
    }

    private void registerVisitor() {
        // Ambil input
        String name = etName.getText().toString().trim();
        String ic = etIC.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String vehicle = etVehicle.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();

        // Simple validation
        if (name.isEmpty() || ic.isEmpty() || phone.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate QR data (simple string)
        String qrData = "VP-" + System.currentTimeMillis();

        // Timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Insert to DB
        boolean success = dbHelper.insertVisitor(name, ic, phone, vehicle, purpose, qrData, timestamp);

        if (success) {
            Toast.makeText(this, "Visitor Registered!", Toast.LENGTH_SHORT).show();

            // Move to QRActivity
            Intent intent = new Intent(RegisterActivity.this, QRActivity.class);
            intent.putExtra("qrData", qrData);
            intent.putExtra("visitorName", name);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
