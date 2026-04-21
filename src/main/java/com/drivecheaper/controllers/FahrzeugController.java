package com.drivecheaper.controllers;

import com.drivecheaper.dao.FahrzeugDAO;
import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.KraftstoffArt;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FahrzeugController {

    @FXML
    private TableView<Fahrzeug> tabelleFahrzeuge;
    @FXML
    private TableColumn<Fahrzeug, Integer> colId;
    @FXML
    private TableColumn<Fahrzeug, Boolean> colStatus;
    @FXML
    private TableColumn<Fahrzeug, String> colHersteller;
    @FXML
    private TableColumn<Fahrzeug, String> colModell;
    @FXML
    private TableColumn<Fahrzeug, String> colKennzeichen;

    @FXML
    private TextField txtHersteller;
    @FXML
    private TextField txtModell;
    @FXML
    private TextField txtKennzeichen;
    @FXML
    private TextField txtBaujahr;
    @FXML
    private TextField txtKilometerstand;
    @FXML
    private TextField txtTageskosten;
    @FXML
    private CheckBox isStatus;
    @FXML
    private TextField txtKaution;
    @FXML
    private TextField txtFahrzeug_id;
    @FXML
    private ComboBox<KraftstoffArt> cbKraftstoffArt;
    @FXML
    private TextField txtTankfuellung;

    // Das Suchfeld
    @FXML
    private TextField txtSuche;

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

    // Wir brauchen diese Liste als "Original-Speicher" für die Suche
    private ObservableList<Fahrzeug> fahrzeugListe = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Wir legen fest, welche Eigenschaft vom Objekt fahrzeug in welcher TableColumn angezeigt wird
        colId.setCellValueFactory(new PropertyValueFactory<>("fahrzeug_id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colHersteller.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
        colModell.setCellValueFactory(new PropertyValueFactory<>("modell"));
        colKennzeichen.setCellValueFactory(new PropertyValueFactory<>("kennzeichen"));
        cbKraftstoffArt.getItems().addAll(KraftstoffArt.values());

        // Boolean für die Tabelle definieren als Verfügbar oder nicht verfügbar
        colStatus.setCellFactory(column -> {
            return new TableCell<Fahrzeug, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item) {
                            setText("✅ Verfügbar");
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        } else {
                            setText("❌ Vermietet");
                            setStyle("-fx-text-fill: red;");
                        }
                    }
                }
            };
        });

        // Zuhörer für Klicks in der Tabelle hinzufügen
        tabelleFahrzeuge.getSelectionModel().selectedItemProperty().addListener((obs, altesAuto, neuesAuto) -> {
            if (neuesAuto != null) {
                txtHersteller.setText(neuesAuto.getHersteller());
                txtModell.setText(neuesAuto.getModell());
                txtKennzeichen.setText(neuesAuto.getKennzeichen());
                txtBaujahr.setText(String.valueOf(neuesAuto.getBaujahr()));
                txtKilometerstand.setText(String.valueOf(neuesAuto.getKilometerstand()));
                isStatus.setSelected(neuesAuto.isStatus());
                txtTageskosten.setText(String.valueOf(neuesAuto.getTageskosten()));
                txtTankfuellung.setText(String.valueOf(neuesAuto.getTankfuellung()));
                txtKaution.setText(String.valueOf(neuesAuto.getKaution()));
                cbKraftstoffArt.getSelectionModel().select(neuesAuto.getKraftstoffArt());
            }
        });

        // Damit das Formular geleert wird, wenn man ins "Leere" klickt
        tabelleFahrzeuge.setOnMouseClicked(event -> {
            if (tabelleFahrzeuge.getSelectionModel().getSelectedItem() == null) {
                felderLeeren();
            }
        });

        ladeFahrzeuge();
    }

    // --- NEUE HILFSMETHODE FÜR DIE SUCHE UND DAS LADEN ---
    private void ladeFahrzeuge() {
        fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();

        // Die Daten in eine "gefilterte Liste" packen
        javafx.collections.transformation.FilteredList<Fahrzeug> filteredData =
                new javafx.collections.transformation.FilteredList<>(fahrzeugListe, b -> true);

        // Zuhören, was der Nutzer in das Suchfeld tippt
        if (txtSuche != null) {
            txtSuche.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(auto -> {
                    // Wenn das Suchfeld leer ist, zeige alle Autos
                    if (newValue == null || newValue.isBlank()) return true;

                    String suchWort = newValue.toLowerCase();

                    // Suchen nach Hersteller, Modell oder Kennzeichen
                    if (auto.getHersteller() != null && auto.getHersteller().toLowerCase().contains(suchWort)) return true;
                    if (auto.getModell() != null && auto.getModell().toLowerCase().contains(suchWort)) return true;
                    if (auto.getKennzeichen() != null && auto.getKennzeichen().toLowerCase().contains(suchWort)) return true;

                    return false; // Nichts gefunden -> Ausblenden!
                });
            });
        }

        // Sortierung beibehalten
        javafx.collections.transformation.SortedList<Fahrzeug> sortedData =
                new javafx.collections.transformation.SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabelleFahrzeuge.comparatorProperty());

        // Die gefilterte Liste in die Tabelle packen
        tabelleFahrzeuge.setItems(sortedData);
    }

    @FXML
    public void onHizufugenKlicken() {
        try {
            String eingabeHersteller = txtHersteller.getText();
            String eingabeModell = txtModell.getText();
            String eingabeKennzeichen = txtKennzeichen.getText();
            String eingabeBaujahr = txtBaujahr.getText();
            String eingabeKilometerstand = txtKilometerstand.getText();
            String eingabeTageskosten = txtTageskosten.getText();
            String eingabeTankfuellung = txtTankfuellung.getText();
            String eingabeKaution = txtKaution.getText();

            if (eingabeHersteller.isEmpty() || eingabeModell.isEmpty() || eingabeKennzeichen.isEmpty() || cbKraftstoffArt.getValue() == null) {
                zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Achtung: Bitte fülle alle wichtigen Felder aus!");
                return;
            }

            String eingabeKraftstoffArt = String.valueOf(cbKraftstoffArt.getValue());
            eingabeKraftstoffArt = eingabeKraftstoffArt.substring(0, 1).toUpperCase() + eingabeKraftstoffArt.substring(1).toLowerCase();

            boolean erfolg = FahrzeugDAO.autoHinzufuegen(
                    eingabeHersteller, eingabeModell, eingabeKennzeichen,
                    eingabeBaujahr, eingabeKilometerstand, eingabeTageskosten,
                    eingabeTankfuellung, eingabeKaution, eingabeKraftstoffArt
            );

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Das Fahrzeug wurde erfolgreich gespeichert!");
                ladeFahrzeuge();
                felderLeeren();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern in der Datenbank.");
            }
        } catch (Exception e) {
            zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler", "Bitte überprüfe deine Eingaben auf korrekte Zahlenwerte!");
        }
    }

    @FXML
    public void onBearbeitenKlicken() {
        try {
            Fahrzeug geaendertesAuto = tabelleFahrzeuge.getSelectionModel().getSelectedItem();

            if (geaendertesAuto != null) {
                geaendertesAuto.setHersteller(txtHersteller.getText());
                geaendertesAuto.setModell(txtModell.getText());
                geaendertesAuto.setKennzeichen(txtKennzeichen.getText());
                geaendertesAuto.setStatus(isStatus.isSelected());

                geaendertesAuto.setBaujahr(Integer.parseInt(txtBaujahr.getText()));
                geaendertesAuto.setKilometerstand(Double.parseDouble(txtKilometerstand.getText()));
                geaendertesAuto.setTageskosten(Double.parseDouble(txtTageskosten.getText()));
                geaendertesAuto.setTankfuellung(Double.parseDouble(txtTankfuellung.getText()));
                geaendertesAuto.setKaution(Double.parseDouble(txtKaution.getText()));
                geaendertesAuto.setKraftstoffArt(cbKraftstoffArt.getValue());

                boolean erfolg = FahrzeugDAO.autoBearbeiten(geaendertesAuto);

                if (erfolg) {
                    zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Das Fahrzeug wurde erfolgreich bearbeitet!");
                    ladeFahrzeuge();
                    felderLeeren();
                } else {
                    zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Fehler beim Aktualisieren in der Datenbank.");
                }
            } else {
                zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst ein Fahrzeug aus der Tabelle aus!");
            }
        } catch (NumberFormatException e) {
            zeigeMeldung(Alert.AlertType.ERROR, "Eingabefehler", "Bitte überprüfe deine Eingaben bei den Zahlen (Kilometer, Tageskosten, etc.).");
        }
    }

    @FXML
    public void onLoeschenKlicken() {
        Fahrzeug ausgewaehltesAuto = tabelleFahrzeuge.getSelectionModel().getSelectedItem();

        if (ausgewaehltesAuto != null) {
            int zieleLoeschen = ausgewaehltesAuto.getFahrzeug_id();
            boolean erfolg = FahrzeugDAO.autoLoeschen(zieleLoeschen);

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Das Fahrzeug wurde erfolgreich gelöscht!");
                ladeFahrzeuge();
                felderLeeren();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Das Fahrzeug konnte nicht gelöscht werden (hat es evtl. noch Mietverträge?).");
            }
        } else {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst ein Fahrzeug zum Löschen aus der Tabelle aus!");
        }
    }

    @FXML
    public void onAbbrechenKlicken() {
        felderLeeren();
        tabelleFahrzeuge.getSelectionModel().clearSelection();
    }

    @FXML
    public void onAktualisierenKlicken() {
        ladeFahrzeuge();
        onAbbrechenKlicken();
    }

    private void felderLeeren() {
        txtHersteller.clear();
        txtModell.clear();
        txtKennzeichen.clear();
        txtBaujahr.clear();
        txtKilometerstand.clear();
        txtTageskosten.clear();
        txtKaution.clear();
        txtTankfuellung.clear();
        cbKraftstoffArt.getSelectionModel().clearSelection();
        isStatus.setSelected(false);
    }

    private void zeigeMeldung(Alert.AlertType alertType, String titel, String nachricht) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(nachricht);
        alert.showAndWait();
    }
}