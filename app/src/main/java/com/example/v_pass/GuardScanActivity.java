package com.example.v_pass;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings; // IMPORT BARU
import java.util.HashMap;

public class GuardScanActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private DatabaseReference visitorRef, logRef;
    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_scan);

        barcodeView = findViewById(R.id.barcodeScanner);
        btnBack = findViewById(R.id.btnBack);

        // --- TAMBAHAN UNTUK LAJUKAN SCAN ---
        CameraSettings settings = new CameraSettings();
        settings.setRequestedCameraId(0); // Guna kamera belakang
        settings.setAutoFocusEnabled(true); // Paksa auto-focus sentiasa ON
        barcodeView.getBarcodeView().setCameraSettings(settings);
        // ------------------------------------

        String dbUrl = "https://v-pass-d85c7-default-rtdb.firebaseio.com/";
        visitorRef = FirebaseDatabase.getInstance(dbUrl).getReference("visitors");
        logRef = FirebaseDatabase.getInstance(dbUrl).getReference("entry_logs");

        btnBack.setOnClickListener(v -> {
            barcodeView.pause();
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                barcodeView.pause();
                finish();
            }
        });

        barcodeView.decodeContinuous(callback);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            // Kita pause supaya dia tak scan berkali-kali masa tengah proses
            barcodeView.pause();
            verifyQR(result.getText());
        }
    };

    private void verifyQR(String qrData) {
        visitorRef.child(qrData).get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) {
                showFailDialog("Invalid QR: Record not found.");
                return;
            }

            String status = snapshot.child("status").getValue(String.class);

            if (!"ACTIVE".equals(status)) {
                showFailDialog("Access Denied: QR already used or expired.");
                return;
            }

            String name = snapshot.child("name").getValue(String.class);
            String vehicle = snapshot.child("vehicle").getValue(String.class);

            showSuccessDialog(qrData, name, vehicle);

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            barcodeView.resume();
        });
    }

    private void showSuccessDialog(String qrId, String name, String vehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ACCESS GRANTED");
        builder.setMessage(
                "Visitor: " + name +
                        "\nVehicle: " + (vehicle != null ? vehicle : "No Vehicle") +
                        "\n\nProceed with Check-In?"
        );

        builder.setPositiveButton("CHECK-IN", (dialog, which) -> {
            checkInVisitor(qrId, name, vehicle);
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            dialog.dismiss();
            barcodeView.resume();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void showFailDialog(String reason) {
        NotificationHelper.showNotification(this, "⚠️ SECURITY ALERT", reason);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ACCESS DENIED");
        builder.setMessage(reason);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            barcodeView.resume();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void checkInVisitor(String qrId, String name, String vehicle) {
        visitorRef.child(qrId).child("status").setValue("CHECKED_IN");

        HashMap<String, Object> log = new HashMap<>();
        log.put("name", name);
        log.put("vehicle", vehicle);
        log.put("timestamp", System.currentTimeMillis());

        logRef.push().setValue(log)
                .addOnSuccessListener(aVoid -> {
                    NotificationHelper.showNotification(this, "✅ CHECK-IN SUCCESS", "Visitor: " + name + " has entered.");
                    Toast.makeText(this, "Check-in Successful", Toast.LENGTH_SHORT).show();
                    barcodeView.resume();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Log Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    barcodeView.resume();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}