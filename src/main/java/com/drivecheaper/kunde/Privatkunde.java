package com.drivecheaper.kunde;

import java.time.LocalDate;

public class Privatkunde extends Kunde {
    private String vorname;
    private String nachname;
    private LocalDate geburtstag;


    public Privatkunde(int kunden_id, String vorname, String nachname, LocalDate gebutstag,  String adresse, int plz, String ort, String email, String telefonnummer, boolean gesperrt) {
        super(kunden_id, adresse, plz, ort, email, telefonnummer, gesperrt);
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtstag = gebutstag;
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

    public LocalDate getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(LocalDate gebutstag) {
        this.geburtstag = geburtstag;
    }
}
