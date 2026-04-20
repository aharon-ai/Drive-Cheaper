package com.drivecheaper.model;

import java.time.LocalDate;

public class Firmenkunde extends Kunde {

    private String firmenName;
    private String ansprechpartner;
    private double rabatt; // in Prozent

    // Der Konstruktor
    public Firmenkunde(int kunden_id, String vorname, String nachname, LocalDate geburtsDatum,
                       String adresse, String hausnummer, String email, String telefonnummer,
                       boolean gesperrt, int ort_id,
                       String firmenName, String ansprechpartner, double rabatt) {

        // super() schickt die Basis-Daten an die übergeordnete Klasse "Kunde"
        super(kunden_id, vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefonnummer, gesperrt, ort_id);
        this.firmenName = firmenName;
        this.ansprechpartner = ansprechpartner;
        this.rabatt = rabatt;
    }

    public String getFirmenName() {
        return firmenName;
    }

    public void setFirmenName(String firmenName) {
        this.firmenName = firmenName;
    }

    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    public void setAnsprechpartner(String ansprechpartner) {
        this.ansprechpartner = ansprechpartner;
    }

    public double getRabatt() {
        return rabatt;
    }

    public void setRabatt(double rabatt) {
        this.rabatt = rabatt;
    }
}