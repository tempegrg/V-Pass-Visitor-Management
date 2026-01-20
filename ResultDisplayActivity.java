package com.example.vpass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultDisplayActivity extends AppCompatActivity {

    private TextView tvVisitorID;
    private Button btnConfirmAction;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        // 1. Initialize Firebase Reference (Must match the node name in your console)
        mDatabase = FirebaseDatabase.getInstance().getReference("Visitors");

        // 2. Link UI Components
        tvVisitorID = findViewById(R.id.tvVisitorName);
        btnConfirmAction = findViewById(R.id.btnConfirmAction);

        // 3. Get the Scanned Data from the Intent
        String scannedID = getIntent().getStringExtra("SCAN_DATA");

        if (scannedID != null) {
            tvVisitorID.setText("Scanned ID: " + scannedID);
        } else {
            tvVisitorID.setText("No Data Scanned");
        }

        // 4. Set Click Listener for the Check-Out Button
        btnConfirmAction.setOnClickListener(v -> {
            if (scannedID != null) {
                performCheckOut(scannedID);
            } else {
                Toast.makeText(this, "Error: No ID found to check out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performCheckOut(String visitorId) {
        // First, check if this Visitor ID actually exists in your Firebase
        mDatabase.child(visitorId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {

                    // ID is valid! Now create the timestamp
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    // Update the status and exit time in Firebase
                    mDatabase.child(visitorId).child("status").setValue("Checked-Out");
                    mDatabase.child(visitorId).child("exitTime").setValue(currentTime)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ResultDisplayActivity.this, "Check-out Successful!", Toast.LENGTH_LONG).show();
                                finish(); // Returns user to the Scanner or Dashboard
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ResultDisplayActivity.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // This happens if the QR scanned isn't in your "Visitors" database
                    Toast.makeText(ResultDisplayActivity.this, "Error: Visitor ID not found in system", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}