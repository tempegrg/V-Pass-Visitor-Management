package com.example.vpass;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRActivity extends AppCompatActivity {

    TextView tvVisitorName;
    ImageView imgQRCode;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // Link UI
        tvVisitorName = findViewById(R.id.tvTitle);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data dari intent
        String qrData = getIntent().getStringExtra("qrData");
        String visitorName = getIntent().getStringExtra("visitorName");

        tvVisitorName.setText("Visitor: " + visitorName);

        // Generate QR
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            imgQRCode.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "QR generation failed!", Toast.LENGTH_SHORT).show();
        }

        // Back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // kembali ke RegisterActivity
            }
        });
    }
}
