package com.drivecheaper.controllers;

import com.drivecheaper.dao.MitarbeiterDAO;
import com.drivecheaper.model.Mitarbeiter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

public class MitarbeiterController {



    @FXML
    private TableView<Mitarbeiter> tabelleMitarbeiter;
    @FXML
    private TableColumn<Mitarbeiter, Integer> colId;
    @FXML
    private TableColumn<Mitarbeiter, String> colVorname;
    @FXML
    private TableColumn<Mitarbeiter, String> colNachname;
    @FXML
    private TableColumn<Mitarbeiter, String> colEmail;
    @FXML
    private TableColumn<Mitarbeiter, String> colTelefon;


    @FXML
    private TextField txtVorname;
    @FXML
    private TextField txtNachname;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefon;


    // Das Suchfeld
    @FXML private TextField txtSuche;

    // Speichert die Original-Liste für die Suche
    private ObservableList<Mitarbeiter> mitarbeiterListeOriginal;

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

        // Damit das Formular geleert wird, wenn man ins "Leere" klickt ---
        tabelleMitarbeiter.setOnMouseClicked(event -> {
            if (tabelleMitarbeiter.getSelectionModel().getSelectedItem() == null) {
                onAbbrechen();
            }
        });

        ladeMitarbeiter();
    }

    // Hifsmethode mit Suchen
    private void ladeMitarbeiter() {
        // 1. Originaldaten laden
        mitarbeiterListeOriginal = MitarbeiterDAO.ladeAlleMitarbeiter();

        // 2. Die Daten in eine "gefilterte Liste" packen
        javafx.collections.transformation.FilteredList<Mitarbeiter> filteredData =
                new javafx.collections.transformation.FilteredList<>(mitarbeiterListeOriginal, b -> true);

        // 3. Zuhören, was der Nutzer in das Suchfeld tippt
        if (txtSuche != null) {
            txtSuche.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(mitarbeiter -> {
                    // Wenn das Suchfeld leer ist, zeige alle Mitarbeiter
                    if (newValue == null || newValue.isBlank()) return true;

                    String suchWort = newValue.toLowerCase();

                    // Suchen nach Vorname, Nachname, Email oder Telefon
                    if (mitarbeiter.getVorname() != null && mitarbeiter.getVorname().toLowerCase().contains(suchWort)) return true;
                    if (mitarbeiter.getNachname() != null && mitarbeiter.getNachname().toLowerCase().contains(suchWort)) return true;
                    if (mitarbeiter.getEmail() != null && mitarbeiter.getEmail().toLowerCase().contains(suchWort)) return true;
                    if (mitarbeiter.getTelefonnummer() != null && mitarbeiter.getTelefonnummer().toLowerCase().contains(suchWort)) return true;

                    return false; // Nichts gefunden -> Ausblenden!
                });
            });
        }

        // 4. Sortierung beibehalten
        javafx.collections.transformation.SortedList<Mitarbeiter> sortedData =
                new javafx.collections.transformation.SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabelleMitarbeiter.comparatorProperty());

        // 5. Die gefilterte Liste in die Tabelle packen
        tabelleMitarbeiter.setItems(sortedData);
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
            onAbbrechen(); // Felder automatisch leeren
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
            onAbbrechen(); // Felder automatisch leeren
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
            onAbbrechen(); // Felder automatisch leeren
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
        onAbbrechen(); // --- NEU: Löscht die Auswahl beim Klicken auf Aktualisieren ---
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