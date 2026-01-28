package com.example.v_pass; // Fixed package name to match your other files

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.util.HashMap;

public class GuardScanActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private DatabaseReference visitorRef, logRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_scan);

        barcodeView = findViewById(R.id.barcodeScanner);

        // Standardizing the database URL
        String dbUrl = "https://v-pass-d85c7-default-rtdb.firebaseio.com/";
        visitorRef = FirebaseDatabase.getInstance(dbUrl).getReference("visitors");
        logRef = FirebaseDatabase.getInstance(dbUrl).getReference("entry_logs");

        barcodeView.decodeContinuous(callback);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.pause(); // Pause to prevent multiple scans
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

            // Check if QR is still active
            if (!"ACTIVE".equals(status)) {
                showFailDialog("Access Denied: QR already used or expired.");
                return;
            }

            String name = snapshot.child("name").getValue(String.class);

            // FIXED: Changed "plate" to "vehicle" to match your RegisterActivity
            String vehicle = snapshot.child("vehicle").getValue(String.class);

            showSuccessDialog(qrData, name, vehicle);

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
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
        // 1. Update status to CHECKED_IN (QR cannot be used again)
        visitorRef.child(qrId).child("status").setValue("CHECKED_IN");

        // 2. Create entry log for EntryLogActivity
        HashMap<String, Object> log = new HashMap<>();
        log.put("name", name);
        log.put("vehicle", vehicle);
        log.put("timestamp", System.currentTimeMillis());

        logRef.push().setValue(log)
                .addOnSuccessListener(aVoid -> {
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