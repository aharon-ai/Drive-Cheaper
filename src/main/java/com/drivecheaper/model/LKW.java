package com.drivecheaper.model;

public class LKW extends Fahrzeug {



    private int gewichtKapazitat;

    public LKW(int fahrzeug_id, String hersteller, String modell, String kennzeichen, double kilometerstand, int baujahr, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt) {
        super(fahrzeug_id, hersteller, modell, kennzeichen, kilometerstand, baujahr, tageskosten, status, tankfuellung, kaution, kraftstoffArt);
        this.gewichtKapazitat = gewichtKapazitat;
    }

    public int getGewichtKapazitat() {

        return gewichtKapazitat;

    }

    public void setGewichtKapazitat(int gewichtKapazitat) {

        this.gewichtKapazitat = gewichtKapazitat;

    }

    @Override

    public String getDetails() {

        return getHersteller() + " " + getModell() + " (Gewicht Kapazitat: " + gewichtKapazitat + ")";

    }

}




