package model;

import java.time.LocalDate;

public class Transaction {
    private String id;
    private String judul;
    private double nominal;
    private String kategori;
    private String tipe;
    private LocalDate tanggal;
    private String keterangan;


    public Transaction (String id, String judul, double nominal,String kategori,String tipe,LocalDate tanggal,String keterangan) {
        this.id = id;
        this.judul = judul;
        this.nominal = nominal;
        this.kategori = kategori;
        this.tipe = tipe;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public String getId() {return id;}
    public String getJudul() {return judul;}
    public double getNominal() {return nominal;}
    public String getKategori() {return kategori;}
    public String getTipe() {return tipe;}
    public LocalDate getTanggal() {return tanggal;}
    public String getKeterangan() {return keterangan;}

    public void setJudul (String judul) {this.judul = judul;}
    public void setNominal (double nominal) {this.nominal = nominal;}
    public void setKategori (String kategori) {this.kategori = kategori;}
    public void setTipe (String tipe) {this.tipe = tipe;}
    public void setTanggal (LocalDate tanggal) {this.tanggal = tanggal;}
    public void setKeterangan (String keterangan) {this.keterangan = keterangan;}

    @Override
    public String toString() {
        return "[" + tipe + "] " + judul + " - Rp " + nominal + " | " + kategori + " | " + tanggal;
    }
}
