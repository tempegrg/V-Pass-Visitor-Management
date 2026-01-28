package com.example.v_pass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EntryLogAdapter extends RecyclerView.Adapter<EntryLogAdapter.LogViewHolder> {

    private ArrayList<EntryLog> logList;

    public EntryLogAdapter(ArrayList<EntryLog> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // PASTIKAN nama fail XML ini betul: item_entry_log
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        EntryLog log = logList.get(position);
        holder.tvName.setText("Visitor: " + log.name);
        holder.tvVehicle.setText("Vehicle: " + log.vehicle);

        // Formatkan timestamp ke tarikh yang boleh dibaca
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvTime.setText(sdf.format(new Date(log.timestamp)));
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvVehicle, tvTime;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            // PASTIKAN ID ini wujud dalam item_entry_log.xml
            tvName = itemView.findViewById(R.id.tvLogName);
            tvVehicle = itemView.findViewById(R.id.tvLogVehicle);
            tvTime = itemView.findViewById(R.id.tvLogTime);
        }
    }
}