package com.drivecheaper.controllers;

import com.drivecheaper.dao.MitarbeiterDAO;
import com.drivecheaper.model.Mitarbeiter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

public class MitarbeiterController {
    @FXML private TableView<Mitarbeiter> tabelleMitarbeiter;
    @FXML private TableColumn<Mitarbeiter, Integer> colId;
    @FXML private TableColumn<Mitarbeiter, String> colVorname;
    @FXML private TableColumn<Mitarbeiter, String> colNachname;
    @FXML private TableColumn<Mitarbeiter, String> colEmail;
    @FXML private TableColumn<Mitarbeiter, String> colTelefon;

    @FXML private TextField txtVorname;
    @FXML private TextField txtNachname;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;

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
        // Property-Namen muessen exakt zu den Getter-Methoden im Modell passen.
        colId.setCellValueFactory(new PropertyValueFactory<>("mitarbeiter_id"));
        colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<>("telefonnummer"));

        tabelleMitarbeiter.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> {
            if (neu != null) {
                // Beim Tabellenklick Formular vorbelegen, damit Edit/Delete direkt moeglich ist.
                txtVorname.setText(neu.getVorname());
                txtNachname.setText(neu.getNachname());
                txtEmail.setText(neu.getEmail());
                txtTelefon.setText(neu.getTelefonnummer());
            }
        });

        ladeMitarbeiter();
    }

    @FXML
    public void onHinzufuegen() {
        if (!eingabeValidieren()) {
            return;
        }

        boolean erfolg = MitarbeiterDAO.mitarbeiterHinzufuegen(
                txtVorname.getText(), txtNachname.getText(), txtEmail.getText(), txtTelefon.getText()
        );
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Der Mitarbeiter wurde erfolgreich gespeichert!");
            ladeMitarbeiter();
            felderLeeren();
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mitarbeiter konnte nicht gespeichert werden.");
        }
    }

    @FXML
    public void onBearbeiten() {
        Mitarbeiter ausgewaehlt = tabelleMitarbeiter.getSelectionModel().getSelectedItem();
        if (ausgewaehlt == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mitarbeiter aus der Tabelle aus.");
            return;
        }
        if (!eingabeValidieren()) {
            return;
        }

        ausgewaehlt.setVorname(txtVorname.getText());
        ausgewaehlt.setNachname(txtNachname.getText());
        ausgewaehlt.setEmail(txtEmail.getText());
        ausgewaehlt.setTelefonnummer(txtTelefon.getText());

        boolean erfolg = MitarbeiterDAO.mitarbeiterBearbeiten(ausgewaehlt);
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Der Mitarbeiter wurde erfolgreich bearbeitet!");
            ladeMitarbeiter();
            felderLeeren();
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mitarbeiter konnte nicht bearbeitet werden.");
        }
    }

    @FXML
    public void onLoeschen() {
        Mitarbeiter ausgewaehlt = tabelleMitarbeiter.getSelectionModel().getSelectedItem();
        if (ausgewaehlt == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mitarbeiter zum Löschen aus.");
            return;
        }

        boolean erfolg = MitarbeiterDAO.mitarbeiterLoeschen(ausgewaehlt.getMitarbeiter_id());
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Der Mitarbeiter wurde erfolgreich gelöscht!");
            ladeMitarbeiter();
            felderLeeren();
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mitarbeiter konnte nicht gelöscht werden (Hat er evtl. noch Mietverträge?).");
        }
    }

    @FXML
    public void onAbbrechen() {
        felderLeeren();
        tabelleMitarbeiter.getSelectionModel().clearSelection();
    }

    @FXML
    public void onAktualisieren() {
        ladeMitarbeiter();
    }

    private boolean eingabeValidieren() {
        // Schlanke Pflichtfeldpruefung auf Controller-Ebene.
        if (txtVorname.getText().isBlank() || txtNachname.getText().isBlank() ||
                txtEmail.getText().isBlank() || txtTelefon.getText().isBlank()) {
            zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Bitte fülle alle Textfelder aus.");
            return false;
        }
        return true;
    }

    private void ladeMitarbeiter() {
        tabelleMitarbeiter.setItems(MitarbeiterDAO.ladeAlleMitarbeiter());
    }

    private void felderLeeren() {
        txtVorname.clear();
        txtNachname.clear();
        txtEmail.clear();
        txtTelefon.clear();
    }

    private void zeigeMeldung(Alert.AlertType type, String titel, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

}