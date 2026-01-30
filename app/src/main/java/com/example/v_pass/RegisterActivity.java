package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // Menggunakan TextInputEditText untuk match dengan XML Premium
    private TextInputEditText etName, etIC, etPhone, etVehicle, etPurpose;
    private MaterialButton btnGenerateQR;
    private TextView btnLogout;
    private DatabaseReference visitorRef;

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

        String dbUrl = "https://v-pass-d85c7-default-rtdb.firebaseio.com/";
        visitorRef = FirebaseDatabase.getInstance(dbUrl).getReference("visitors");

        btnGenerateQR.setOnClickListener(view -> registerVisitor());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Returning to Home", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    private void registerVisitor() {
        // Ambil data dari input
        String name = etName.getText().toString().trim();
        String ic = etIC.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String vehicle = etVehicle.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();

        // Validasi input
        if (name.isEmpty() || ic.isEmpty() || phone.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Feedback visual: Matikan button buat sementara
        btnGenerateQR.setEnabled(false);
        btnGenerateQR.setText("Registering...");

        // Generate ID unik
        String qrData = "VP-" + System.currentTimeMillis();
        long timestamp = System.currentTimeMillis();

        Visitor visitor = new Visitor(name, ic, phone, vehicle, purpose, qrData, "ACTIVE", timestamp);

        // Simpan ke Firebase
        visitorRef.child(qrData).setValue(visitor)
                .addOnSuccessListener(unused -> {
                    Log.d("FIREBASE_SUCCESS", "Data saved for: " + name);

                    // PINDAH KE QR ACTIVITY
                    Intent intent = new Intent(RegisterActivity.this, QRActivity.class);
                    intent.putExtra("qrData", qrData);
                    intent.putExtra("visitorName", name);

                    // Gunakan FLAG ini untuk pastikan skrin bertukar dengan lancar
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish(); // Tutup RegisterActivity
                })
                .addOnFailureListener(e -> {
                    // Jika gagal, aktifkan balik button
                    btnGenerateQR.setEnabled(true);
                    btnGenerateQR.setText("GENERATE VISITOR QR");
                    Toast.makeText(this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("FIREBASE_ERROR", e.getMessage());
                });
    }
}