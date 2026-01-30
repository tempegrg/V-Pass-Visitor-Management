package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr); // Ensure file is activity_qr.xml

        ImageView ivQR = findViewById(R.id.ivQRCode);
        TextView tvName = findViewById(R.id.tvShowName);
        TextView btnLogout = findViewById(R.id.btnLogout);

        String qrData = getIntent().getStringExtra("qrData");
        String visitorName = getIntent().getStringExtra("visitorName");

        tvName.setText("Visitor Pass: " + visitorName);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Returning to Home", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(QRActivity.this, MainActivity.class));
            finish();
        });

        try {
            if (qrData != null && !qrData.isEmpty()) {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 500, 500);
                ivQR.setImageBitmap(bitmap);
            } else {
                tvName.setText("Error: No Data Received");
                Toast.makeText(this, "QR Data is empty!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("QR_ERROR", "QR Generation Failed:" + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
        }
    }
}