package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etIC, etPhone, etVehicle, etPurpose;
    Button btnGenerateQR, btnLogout;
    DatabaseReference visitorRef;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etVisitorName);
        etIC = findViewById(R.id.etVisitorIC);
        etPhone = findViewById(R.id.etPhone);
        etVehicle = findViewById(R.id.etVehicle);
        etPurpose = findViewById(R.id.etPurpose);
        btnGenerateQR = findViewById(R.id.btnGenerateQR);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DBHelper(this);
        String dbUrl = "https://v-pass-d85c7-default-rtdb.firebaseio.com/";
        visitorRef = FirebaseDatabase.getInstance(dbUrl).getReference("visitors");

        btnGenerateQR.setOnClickListener(view -> registerVisitor());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    private void registerVisitor() {
        String name = etName.getText().toString().trim();
        String ic = etIC.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String vehicle = etVehicle.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();

        if (name.isEmpty() || ic.isEmpty() || phone.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String qrData = "VP-" + System.currentTimeMillis();
        long timestamp = System.currentTimeMillis();
        Visitor visitor = new Visitor(name, ic, phone, vehicle, purpose, qrData, "ACTIVE", timestamp);

        // Save Online
        visitorRef.child(qrData).setValue(visitor).addOnSuccessListener(unused -> {
            // Save Offline
            dbHelper.insertVisitor(name, ic, phone, vehicle, purpose, qrData, String.valueOf(timestamp));

            Intent intent = new Intent(RegisterActivity.this, QRActivity.class);
            intent.putExtra("qrData", qrData);
            intent.putExtra("visitorName", name);
            startActivity(intent);
        }).addOnFailureListener(e -> Log.e("FIREBASE_ERROR", e.getMessage()));
    }
}