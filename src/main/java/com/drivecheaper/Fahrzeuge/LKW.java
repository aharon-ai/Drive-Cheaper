package com.drivecheaper.Fahrzeuge;

public class LKW  extends Fahrzeug{

    private int gewichtKapazitat;

    public LKW(int fahrzeug_id, String hersteller, String modell, String kennzeichen, int baujahr, double kilometerstand, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt, int gewichtKapazitat) {
        super(fahrzeug_id, hersteller, modell, kennzeichen, baujahr, kilometerstand, tageskosten, status, tankfuellung, kaution, kraftstoffArt);
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
