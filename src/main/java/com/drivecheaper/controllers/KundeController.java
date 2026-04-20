package com.drivecheaper.controllers;

import com.drivecheaper.dao.KundenDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.time.LocalDate;

public class KundeController {

    // 1. Hier sind die Verknüpfungen zu deiner FXML-Datei.
    // ACHTUNG: Die Namen (z.B. txtVorname) müssen exakt mit der fx:id im Scene Builder übereinstimmen!
    @FXML private TextField txtVorname;
    @FXML private TextField txtNachname;
    @FXML private DatePicker dpGeburtsDatum; // Für das Datum nutzen wir ein DatePicker-Feld!
    @FXML private TextField txtAdresse;
    @FXML private TextField txtHausnummer;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtOrtId; // Hier trägt man vorerst die Zahl für den Ort ein (z.B. 1)

    // 2. Diese Methode wird ausgeführt, wenn du auf "Kunde Speichern" klickst
    @FXML
    public void onKundeSpeichernKlicken() {
        try {
            // Werte aus den Feldern auslesen
            String vorname = txtVorname.getText();
            String nachname = txtNachname.getText();
            LocalDate geburtsDatum = dpGeburtsDatum.getValue();
            String adresse = txtAdresse.getText();
            String hausnummer = txtHausnummer.getText();
            String email = txtEmail.getText();
            String telefon = txtTelefon.getText();

            // Die Ort-ID ist ein Textfeld, muss aber in eine Zahl (int) verwandelt werden
            int ortId = Integer.parseInt(txtOrtId.getText());

            // DAO aufrufen, um den Kunden in der Datenbank zu speichern
            boolean erfolg = KundenDAO.kundeHinzufuegen(
                    vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefon, ortId
            );

            if (erfolg) {
                zeigeMeldung("Erfolg", "Der Kunde wurde erfolgreich angelegt!");
                // Optional: Felder danach leeren
            } else {
                zeigeMeldung("Fehler", "Der Kunde konnte nicht gespeichert werden.");
            }

        } catch (NumberFormatException e) {
            zeigeMeldung("Eingabefehler", "Bitte gib bei der Ort-ID eine gültige Zahl ein.");
        } catch (Exception e) {
            zeigeMeldung("Fehler", "Bitte fülle alle Pflichtfelder korrekt aus.");
        }
    }

    // Hilfsmethode für Pop-Up Fenster
    private void zeigeMeldung(String titel, String nachricht) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(nachricht);
        alert.showAndWait();
    }
}