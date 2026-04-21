package com.drivecheaper.controllers;

import com.drivecheaper.dao.KundeDAO;
import com.drivecheaper.dao.OrtDAO;
import com.drivecheaper.model.Ort;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class KundeController {
    @FXML private TextField txtVorname;
    // Speichert die ID des Kunden, den wir gerade angeklickt haben (-1 bedeutet: Niemand ist ausgewählt)
    private int ausgewaehlterKundeId = -1;
    @FXML private TextField txtNachname;
    @FXML private DatePicker dpGeburtsDatum;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtHausnummer;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private ComboBox<Ort> cbOrt;

    @FXML private CheckBox chkFirmenkunde;
    @FXML private TextField txtFirmenname;
    @FXML private TextField txtAnsprechpartner;
    @FXML private TextField txtRabatt;

    @FXML private TableView<KundeDAO.KundeUebersicht> tabelleKunden;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, Integer> colId;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, String> colVorname;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, String> colNachname;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, LocalDate> colGeburtsdatum;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, String> colKundentyp;
    @FXML private TableColumn<KundeDAO.KundeUebersicht, String> colFirmenname;

    @FXML
    public void onHomeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "HomeView.fxml");
    }

    @FXML
    public void onFahrzeugeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "FahrzeugView.fxml");
    }

    @FXML
    public void onMitarbeiterClick(ActionEvent event) {
        ViewNavigator.navigate(event, "MitarbeiterView.fxml");
    }

    @FXML
    public void onKundeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "KundeView.fxml");
    }

    @FXML
    public void onMietvertragClick(ActionEvent event) {
        ViewNavigator.navigate(event, "MietvertragView.fxml");
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("kundeId"));
        colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colGeburtsdatum.setCellValueFactory(new PropertyValueFactory<>("geburtsdatum"));
        colKundentyp.setCellValueFactory(new PropertyValueFactory<>("kundentyp"));
        colFirmenname.setCellValueFactory(new PropertyValueFactory<>("firmenname"));

        cbOrt.setItems(OrtDAO.ladeAlleOrte());
        ladeKundenInTabelle();
        umschaltenFirmenkundenFelder(false);

        // Wartet auf Klicks in der Tabelle
        tabelleKunden.getSelectionModel().selectedItemProperty().addListener((obs, alterWert, neuerWert) -> {
            if (neuerWert != null) {
                kundeInFelderLaden(neuerWert);
            }
        });
    }

    @FXML
    public void onFirmenkundeToggle() {
        umschaltenFirmenkundenFelder(chkFirmenkunde.isSelected());
    }

    @FXML
    public void onKundeSpeichernKlicken() {
        try {
            String vorname = txtVorname.getText();
            String nachname = txtNachname.getText();
            LocalDate geburtsDatum = dpGeburtsDatum.getValue();
            String adresse = txtAdresse.getText();
            String hausnummer = txtHausnummer.getText();
            String email = txtEmail.getText();
            String telefon = txtTelefon.getText();
            Ort ort = cbOrt.getValue();

            if (ort == null) {
                zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler", "Bitte wähle einen Ort aus der Liste aus.");
                return; // Bricht das Speichern ab, wenn kein Ort gewählt wurde
            }
            int ortId = ort.getOrtId();

            if (vorname.isBlank() || nachname.isBlank() || geburtsDatum == null || adresse.isBlank()
                    || hausnummer.isBlank() || email.isBlank() || telefon.isBlank()) {
                zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler", "Bitte alle Pflichtfelder ausfuellen.");
                return;
            }

            boolean erfolg;
            if (chkFirmenkunde.isSelected()) {
                String firmenname = txtFirmenname.getText();
                String ansprechpartner = txtAnsprechpartner.getText();
                double rabatt = Double.parseDouble(txtRabatt.getText());

                if (firmenname.isBlank() || ansprechpartner.isBlank()) {
                    zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler",
                            "Bitte Firmenname und Ansprechpartner ausfuellen.");
                    return;
                }

                erfolg = KundeDAO.firmenkundeHinzufuegen(
                        vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefon, ortId,
                        firmenname, ansprechpartner, rabatt
                );
            } else {
                erfolg = KundeDAO.kundeHinzufuegen(
                        vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefon, ortId
                );
            }

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Der Kunde wurde erfolgreich gespeichert.");
                felderLeeren();
                ladeKundenInTabelle();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Kunde konnte nicht gespeichert werden.");
            }
        } catch (NumberFormatException e) {
            zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler", "Rabatt und Ort-ID muessen gueltige Zahlen sein.");
        }
    }

    @FXML
    public void onKundeBearbeitenKlicken() {
        if (ausgewaehlterKundeId == -1) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Kunden aus der Tabelle aus!");
            return;
        }

        try {
            Ort ort = cbOrt.getValue();
            if (ort == null) {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Bitte einen Ort auswählen.");
                return;
            }

            boolean erfolg = KundeDAO.kundeAktualisieren(
                    ausgewaehlterKundeId,
                    txtVorname.getText(), txtNachname.getText(), dpGeburtsDatum.getValue(),
                    txtAdresse.getText(), txtHausnummer.getText(), txtEmail.getText(), txtTelefon.getText(),
                    ort.getOrtId(), chkFirmenkunde.isSelected(),
                    txtFirmenname.getText(), txtAnsprechpartner.getText(),
                    txtRabatt.getText().isBlank() ? 0 : Double.parseDouble(txtRabatt.getText())
            );

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Kunde erfolgreich aktualisiert!");
                felderLeeren();
                ladeKundenInTabelle();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Kunde konnte nicht aktualisiert werden.");
            }
        } catch (Exception e) {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Bitte überprüfe deine Eingaben.");
        }
    }

    @FXML
    public void onKundeLoeschenKlicken() {
        if (ausgewaehlterKundeId == -1) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Kunden zum Löschen aus!");
            return;
        }

        boolean erfolg = KundeDAO.kundeLoeschen(ausgewaehlterKundeId);
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Der Kunde wurde gelöscht.");
            felderLeeren();
            ladeKundenInTabelle();
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Kunde konnte nicht gelöscht werden (evtl. hat er noch Mietverträge?).");
        }
    }

    @FXML
    public void onAbbrechenKlicken() {
        felderLeeren();
        tabelleKunden.getSelectionModel().clearSelection(); // Optional: Hebt die Markierung in der Tabelle auf
        zeigeMeldung(Alert.AlertType.INFORMATION, "Abgebrochen", "Die Eingabefelder wurden geleert.");
    }

    private void ladeKundenInTabelle() {
        ObservableList<KundeDAO.KundeUebersicht> kundenListe = KundeDAO.ladeKundenUebersicht();
        tabelleKunden.setItems(kundenListe);
    }

    private void umschaltenFirmenkundenFelder(boolean aktiv) {
        txtFirmenname.setDisable(!aktiv);
        txtAnsprechpartner.setDisable(!aktiv);
        txtRabatt.setDisable(!aktiv);

    }



    private void felderLeeren() {
        ausgewaehlterKundeId = -1; // Wieder zurücksetzen!
        txtVorname.clear();
        txtVorname.clear();
        txtNachname.clear();
        dpGeburtsDatum.setValue(null);
        txtAdresse.clear();
        txtHausnummer.clear();
        txtEmail.clear();
        txtTelefon.clear();
        cbOrt.getSelectionModel().clearSelection();
        txtFirmenname.clear();
        txtAnsprechpartner.clear();
        txtRabatt.clear();
        chkFirmenkunde.setSelected(false);
        umschaltenFirmenkundenFelder(false);
    }

    private void kundeInFelderLaden(KundeDAO.KundeUebersicht kunde) {

        // ID merken!
        ausgewaehlterKundeId = kunde.getKundeId();

        // 1. Basis-Daten füllen
        txtVorname.setText(kunde.getVorname());
        txtVorname.setText(kunde.getVorname());
        txtNachname.setText(kunde.getNachname());
        dpGeburtsDatum.setValue(kunde.getGeburtsdatum());
        txtAdresse.setText(kunde.getAdresse());
        txtHausnummer.setText(kunde.getHausnummer());
        txtEmail.setText(kunde.getEmail());
        txtTelefon.setText(kunde.getTelefon());


        // 2. Den richtigen Ort im Dropdown-Menü (ComboBox) auswählen
        for (Ort ort : cbOrt.getItems()) {
            if (ort.getOrtId() == kunde.getOrtId()) {
                cbOrt.setValue(ort);
                break;
            }
        }

        // 3. Firmenkunden-Felder einstellen
        if ("Firmenkunde".equals(kunde.getKundentyp())) {
            chkFirmenkunde.setSelected(true);
            umschaltenFirmenkundenFelder(true);
            txtFirmenname.setText(kunde.getFirmenname());
        } else {
            chkFirmenkunde.setSelected(false);
            umschaltenFirmenkundenFelder(false);
            txtFirmenname.clear();
        }
    }

    private void zeigeMeldung(Alert.AlertType alertType, String titel, String nachricht) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(nachricht);
        alert.showAndWait();
    }


}
