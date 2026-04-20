package com.drivecheaper.model;

import java.time.LocalDate;

public abstract class Kunde {
    private int kunden_id;
    String vorname;
    String nachname;
    LocalDate geburtsDatum;
    private String adresse;
    String hausnummer;
   private String email;
    private String telefonnummer;
    private boolean gesperrt;
    int ort_id;

    public Kunde(int kunden_id, String vorname, String nachname, LocalDate geburtsDatum, String adresse, String hausnummer, String email, String telefonnummer, boolean gesperrt, int ort_id) {
        this.kunden_id = kunden_id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsDatum = geburtsDatum;
        this.adresse = adresse;
        this.hausnummer = hausnummer;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.gesperrt = gesperrt;
        this.ort_id = ort_id;
    }

    public int getKunden_id() {
        return kunden_id;
    }

    public void setKunden_id(int kunden_id) {
        this.kunden_id = kunden_id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public LocalDate getGeburtsDatum() {
        return geburtsDatum;
    }

    public void setGeburtsDatum(LocalDate geburtsDatum) {
        this.geburtsDatum = geburtsDatum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
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

    public int getOrt_id() {
        return ort_id;
    }

    public void setOrt_id(int ort_id) {
        this.ort_id = ort_id;
    }
}

