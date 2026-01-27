package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etIC, etPhone, etVehicle, etPurpose;
    Button btnGenerateQR;

    DatabaseReference visitorRef;

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

        // Firebase reference
        visitorRef = FirebaseDatabase.getInstance().getReference("visitors");

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVisitor();
            }
        });
    }

    private void registerVisitor() {

        String name = etName.getText().toString().trim();
        String ic = etIC.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String vehicle = etVehicle.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();

        if (name.isEmpty() || ic.isEmpty() || phone.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate QR data
        String qrData = "VP-" + System.currentTimeMillis();
        long timestamp = System.currentTimeMillis();

        // Create object
        Visitor visitor = new Visitor(
                name,
                ic,
                phone,
                vehicle,
                purpose,
                qrData,
                "ACTIVE",
                timestamp
        );

        // Save to Firebase
        visitorRef.child(qrData).setValue(visitor)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this, "Visitor Registered!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, QRActivity.class);
                    intent.putExtra("qrData", qrData);
                    intent.putExtra("visitorName", name);
                    startActivity(intent);

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                );
    }
}
