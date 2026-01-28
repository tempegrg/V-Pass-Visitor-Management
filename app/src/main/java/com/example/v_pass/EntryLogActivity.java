package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class EntryLogActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EntryLogAdapter adapter;
    ArrayList<EntryLog> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_log);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logList = new ArrayList<>();
        adapter = new EntryLogAdapter(logList);
        recyclerView.setAdapter(adapter);

        // Gunakan Query untuk limit 10 data terakhir
        Query logQuery = FirebaseDatabase.getInstance()
                .getReference("entry_logs")
                .limitToLast(10);

        logQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                logList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    EntryLog log = data.getValue(EntryLog.class);
                    if (log != null) { // Tambah check null di sini
                        logList.add(0, log);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log ralat jika ada
            }
        });
    }
}