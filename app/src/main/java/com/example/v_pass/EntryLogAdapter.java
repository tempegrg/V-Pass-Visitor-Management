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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        EntryLog log = logList.get(position);

        // Set name
        holder.tvName.setText(log.name);

        // Set vehicle
        String vehicleText = log.vehicle != null && !log.vehicle.isEmpty() ? log.vehicle : "No Vehicle";
        holder.tvVehicle.setText(vehicleText);

        // Format time and date
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Date date = new Date(log.timestamp);
        String time = timeFormat.format(date);
        String fullDate = dateFormat.format(date);
        String day = dayFormat.format(date);

        // Set time
        holder.tvTime.setText(time);

        // Set date (Today/Yesterday or date)
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        String logDay = simpleDateFormat.format(date);
        String currentDay = simpleDateFormat.format(today);

        if (logDay.equals(currentDay)) {
            holder.tvDate.setText("Today, " + fullDate);
        } else {
            holder.tvDate.setText(day + ", " + fullDate);
        }
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvVehicle, tvTime, tvDate;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvLogName);
            tvVehicle = itemView.findViewById(R.id.tvLogVehicle);
            tvTime = itemView.findViewById(R.id.tvLogTime);
            tvDate = itemView.findViewById(R.id.tvLogDate);
        }
    }
}