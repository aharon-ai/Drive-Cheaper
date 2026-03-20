package com.drivecheaper.kunde;

public abstract class Kunde {
    private int kunden_id;
    private String adresse;
    private int plz;
    private String ort;
    private String email;
    private String telefonnummer;
    private boolean gesperrt;

    public Kunde(int kunden_id, String adresse, int plz, String ort, String email, String telefonnummer, boolean gesperrt) {
        this.kunden_id = kunden_id;
        this.adresse = adresse;
        this.plz = plz;
        this.ort = ort;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.gesperrt = gesperrt;
    }

    public int getKunden_id() {
        return kunden_id;
    }

    public void setKunden_id(int kunden_id) {
        this.kunden_id = kunden_id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public boolean isGesperrt() {
        return gesperrt;
    }

    public void setGesperrt(boolean gesperrt) {
        this.gesperrt = gesperrt;
    }
}
