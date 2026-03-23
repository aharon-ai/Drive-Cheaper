package com.drivecheaper.kunde;

public class Firmenkunde extends Kunde {

    private String firmenName;
    private String ansprechpartner;
    private double rabatt; // in prozent


    public Firmenkunde(int kunden_id, String firmenName, String ansprechpartner, double rabatt, String adresse, int plz, String ort, String email, String telefonnummer, boolean gesperrt) {
        super(kunden_id, adresse, plz, ort, email, telefonnummer, gesperrt);
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
