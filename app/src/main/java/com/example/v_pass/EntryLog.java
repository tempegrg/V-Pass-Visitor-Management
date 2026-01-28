package com.example.v_pass;

public class EntryLog {
    public String name;
    public String vehicle;
    public long timestamp;

    public EntryLog() {} // Perlu ada constructor kosong untuk Firebase

    public EntryLog(String name, String vehicle, long timestamp) {
        this.name = name;
        this.vehicle = vehicle;
        this.timestamp = timestamp;
    }
}