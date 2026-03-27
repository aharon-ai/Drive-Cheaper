package com.drivecheaper.model;


public class Motorrad extends Fahrzeug {

    private String motorradType;

    public Motorrad(int fahrzeug_id, String hersteller, String modell, String kennzeichen, double kilometerstand, int baujahr, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt) {
        super(fahrzeug_id, hersteller, modell, kennzeichen, kilometerstand, baujahr, tageskosten, status, tankfuellung, kaution, kraftstoffArt);
        this.motorradType = motorradType;
    }

    @Override
    public String getDetails() {
        return getHersteller() + " " + getModell() + " (Motorrad Type: " + motorradType + ")";
    }

}


