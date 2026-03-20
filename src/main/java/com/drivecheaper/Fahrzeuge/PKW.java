package com.drivecheaper.Fahrzeuge;

public class PKW extends Fahrzeug {

    private String farbe;
    private int sitzPlaetze;

    public PKW(int fahrzeug_id, String hersteller, String modell, String kennzeichen, int baujahr, double kilometerstand, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt, String farbe, int sitzPlaetze) {
        super(fahrzeug_id, hersteller, modell, kennzeichen, baujahr, kilometerstand, tageskosten, status, tankfuellung, kaution, kraftstoffArt);
        this.farbe = farbe;
        this.sitzPlaetze = sitzPlaetze;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public int getSitzPlaetze() {
        return sitzPlaetze;
    }

    public void setSitzPlaetze(int sitzPlaetze) {
        this.sitzPlaetze = sitzPlaetze;
    }

    @Override
    public String getDetails() {
        return getHersteller() + " " + getModell() + " (Farbe: " + farbe + ")" + " (Sitz Platze: " + sitzPlaetze + ")";
    }


}
