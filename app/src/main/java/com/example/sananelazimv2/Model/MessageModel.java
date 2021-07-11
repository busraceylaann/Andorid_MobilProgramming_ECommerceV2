package com.example.sananelazimv2.Model;

public class MessageModel {

    private String mesaj,from,to,zaman;

    public MessageModel(String mesaj, String from, String to, String zaman) {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.zaman=zaman;
    }

    public MessageModel() {
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
