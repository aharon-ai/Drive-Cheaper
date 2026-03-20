package com.drivecheaper.Fahrzeuge;

public abstract class Fahrzeug {
    private int fahrzeug_id;
    private String hersteller;
    private String modell;
    private String kennzeichen;
    private int baujahr;
    private double kilometerstand;
    private double tageskosten;
    private boolean status;
    private double tankfuellung; // in Prozent
    private double kaution;
    private KraftstoffArt kraftstoffArt;

    public Fahrzeug(int fahrzeug_id, String hersteller, String modell, String kennzeichen, int baujahr, double kilometerstand, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt) {
        this.fahrzeug_id = fahrzeug_id;
        this.hersteller = hersteller;
        this.modell = modell;
        this.kennzeichen = kennzeichen;
        this.baujahr = baujahr;
        this.kilometerstand = kilometerstand;
        this.tageskosten = tageskosten;
        this.status = status;
        this.tankfuellung = tankfuellung;
        this.kaution = kaution;
        this.kraftstoffArt = kraftstoffArt;
    }

    public abstract String getDetails();

    public int getFahrzeug_id() {
        return fahrzeug_id;
    }

    public void setFahrzeug_id(int fahrzeug_id) {
        this.fahrzeug_id = fahrzeug_id;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public double getKilometerstand() {
        return kilometerstand;
    }

    public void setKilometerstand(double kilometerstand) {
        this.kilometerstand = kilometerstand;
    }

    public double getTageskosten() {
        return tageskosten;
    }

    public void setTageskosten(double tageskosten) {
        this.tageskosten = tageskosten;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getTankfuellung() {
        return tankfuellung;
    }

    public void setTankfuellung(double tankfuellung) {
        this.tankfuellung = tankfuellung;
    }

    public double getKaution() {
        return kaution;
    }

    public void setKaution(double kaution) {
        this.kaution = kaution;
    }

    public KraftstoffArt getKraftstoffArt() {
        return kraftstoffArt;
    }

    public void setKraftstoffArt(KraftstoffArt kraftstoffArt) {
        this.kraftstoffArt = kraftstoffArt;
    }

    @Override
    public String toString() {
        return "Fahrzeug{" +
                "fahrzeug_id=" + fahrzeug_id +
                ", hersteller='" + hersteller + '\'' +
                ", modell='" + modell + '\'' +
                ", kennzeichen='" + kennzeichen + '\'' +
                ", baujahr=" + baujahr +
                ", kilometerstand=" + kilometerstand +
                ", tageskosten=" + tageskosten +
                ", status=" + status +
                ", tankfuellung=" + tankfuellung +
                ", kaution=" + kaution +
                ", kraftstoffArt=" + kraftstoffArt +
                '}';
    }
}






