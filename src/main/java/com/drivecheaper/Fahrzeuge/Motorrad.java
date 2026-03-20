package com.drivecheaper.Fahrzeuge;

public class Motorrad  extends Fahrzeug {

    private String motorradType;

    public Motorrad(int fahrzeug_id, String hersteller, String modell, String kennzeichen, int baujahr, double kilometerstand, double tageskosten, boolean status, double tankfuellung, double kaution, KraftstoffArt kraftstoffArt, String motorradType) {
        super(fahrzeug_id, hersteller, modell, kennzeichen, baujahr, kilometerstand, tageskosten, status, tankfuellung, kaution, kraftstoffArt);
        this.motorradType = motorradType;
    }

    public String getMotorradType() {
        return motorradType;
    }

    public void setMotorradType(String motorradType) {
        this.motorradType = motorradType;
    }

    @Override
    public String getDetails() {
        return getHersteller() + " " + getModell() + " (Motorrad Type: " + motorradType + ")";
    }
}
