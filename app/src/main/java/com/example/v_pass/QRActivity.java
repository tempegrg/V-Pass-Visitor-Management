package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        ImageView ivQR = findViewById(R.id.ivQRCode);
        TextView tvName = findViewById(R.id.tvShowName);

        String qrData = getIntent().getStringExtra("qrData");
        String visitorName = getIntent().getStringExtra("visitorName");

        tvName.setText("Pass for: " + visitorName);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            // Jana Bitmap dari String
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 500, 500);
            ivQR.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}