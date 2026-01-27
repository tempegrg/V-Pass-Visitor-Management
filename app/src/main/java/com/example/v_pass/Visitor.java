package com.example.v_pass;

public class Visitor {
    public String name, ic, phone, vehicle, purpose, qrData, status;
    public long timestamp;

    // WAJIB: Constructor kosong
    public Visitor() {}

    public Visitor(String name, String ic, String phone, String vehicle, String purpose, String qrData, String status, long timestamp) {
        this.name = name;
        this.ic = ic;
        this.phone = phone;
        this.vehicle = vehicle;
        this.purpose = purpose;
        this.qrData = qrData;
        this.status = status;
        this.timestamp = timestamp;
    }
}