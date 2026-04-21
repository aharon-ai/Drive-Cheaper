package com.drivecheaper.controllers;

import com.drivecheaper.dao.FahrzeugDAO;
import com.drivecheaper.dao.KundeDAO;
import com.drivecheaper.dao.MietvertragDAO;
import com.drivecheaper.dao.MitarbeiterDAO;
import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.Mitarbeiter;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class MietvertragController {
    @FXML private TableView<MietvertragDAO.MietvertragUebersicht> tabelleMietvertraege;

    // --- NAMEN AN DEINE FXML ANGEPASST ---
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colId;
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colKunde;
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colFahrzeug;
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colMitarbeiter;

    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, LocalDate> colStartdatum;
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, LocalDate> colEndedatum;
    @FXML private TableColumn<MietvertragDAO.MietvertragUebersicht, Double> colGesamtkosten;

    @FXML private ComboBox<KundeDAO.KundeUebersicht> cbKunde;
    @FXML private ComboBox<Fahrzeug> cbFahrzeug;
    @FXML private ComboBox<Mitarbeiter> cbMitarbeiter;

    @FXML private DatePicker dpStartdatum;
    @FXML private DatePicker dpEndedatum;
    @FXML private TextField txtGesamtkosten;

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
        // 1. Listen aus der Datenbank in die ComboBoxen laden (wichtig, dass das ZUERST passiert)
        cbKunde.setItems(KundeDAO.ladeKundenUebersicht());
        cbFahrzeug.setItems(FahrzeugDAO.ladeAlleFahrzeuge());
        cbMitarbeiter.setItems(MitarbeiterDAO.ladeAlleMitarbeiter());

        // 2. Basis-Zuordnung der Spalten
        colId.setCellValueFactory(new PropertyValueFactory<>("mietvertragId"));
        colKunde.setCellValueFactory(new PropertyValueFactory<>("kundeId"));
        colFahrzeug.setCellValueFactory(new PropertyValueFactory<>("fahrzeugId"));
        colMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiterId"));
        colStartdatum.setCellValueFactory(new PropertyValueFactory<>("startdatum"));
        colEndedatum.setCellValueFactory(new PropertyValueFactory<>("endedatum"));
        colGesamtkosten.setCellValueFactory(new PropertyValueFactory<>("gesamtkosten"));

        // --- NEU: IDS IN NAMEN ÜBERSETZEN (IN DER TABELLE) ---

        colKunde.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer kundeId, boolean empty) {
                super.updateItem(kundeId, empty);
                if (empty || kundeId == null) {
                    setText(null);
                } else {
                    String name = "Unbekannt (ID: " + kundeId + ")"; // Falls gelöscht
                    for (KundeDAO.KundeUebersicht k : cbKunde.getItems()) {
                        if (k.getKundeId() == kundeId) {
                            name = k.getVorname() + " " + k.getNachname();
                            break;
                        }
                    }
                    setText(name);
                }
            }
        });

        colFahrzeug.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer fahrzeugId, boolean empty) {
                super.updateItem(fahrzeugId, empty);
                if (empty || fahrzeugId == null) {
                    setText(null);
                } else {
                    String name = "Unbekannt (ID: " + fahrzeugId + ")";
                    for (Fahrzeug f : cbFahrzeug.getItems()) {
                        if (f.getFahrzeug_id() == fahrzeugId) {
                            name = f.getHersteller() + " " + f.getModell() + " (" + f.getKennzeichen() + ")";
                            break;
                        }
                    }
                    setText(name);
                }
            }
        });

        colMitarbeiter.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer mitarbeiterId, boolean empty) {
                super.updateItem(mitarbeiterId, empty);
                if (empty || mitarbeiterId == null) {
                    setText(null);
                } else {
                    String name = "Unbekannt (ID: " + mitarbeiterId + ")";
                    for (Mitarbeiter m : cbMitarbeiter.getItems()) {
                        if (m.getMitarbeiter_id() == mitarbeiterId) {
                            name = m.getVorname() + " " + m.getNachname();
                            break;
                        }
                    }
                    setText(name);
                }
            }
        });

        // 3. Schöne Text-Anzeige für die Dropdowns (ComboBoxen) einstellen
        cbKunde.setConverter(new StringConverter<>() {
            @Override
            public String toString(KundeDAO.KundeUebersicht k) {
                return k == null ? "" : k.getVorname() + " " + k.getNachname() + " (ID: " + k.getKundeId() + ")";
            }
            @Override public KundeDAO.KundeUebersicht fromString(String s) { return null; }
        });

        cbFahrzeug.setConverter(new StringConverter<>() {
            @Override
            public String toString(Fahrzeug f) {
                return f == null ? "" : f.getHersteller() + " " + f.getModell() + " (" + f.getKennzeichen() + ")";
            }
            @Override public Fahrzeug fromString(String s) { return null; }
        });

        cbMitarbeiter.setConverter(new StringConverter<>() {
            @Override
            public String toString(Mitarbeiter m) {
                return m == null ? "" : m.getVorname() + " " + m.getNachname() + " (ID: " + m.getMitarbeiter_id() + ")";
            }
            @Override public Mitarbeiter fromString(String s) { return null; }
        });

        // 4. Zuhörer: Wenn in der Tabelle geklickt wird, fülle das Formular
        tabelleMietvertraege.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> {
            if (neu != null) {
                dpStartdatum.setValue(neu.getStartdatum());
                dpEndedatum.setValue(neu.getEndedatum());
                txtGesamtkosten.setText(String.valueOf(neu.getGesamtkosten()));

                // Passenden Kunden im Dropdown auswählen
                for (KundeDAO.KundeUebersicht k : cbKunde.getItems()) {
                    if (k.getKundeId() == neu.getKundeId()) {
                        cbKunde.setValue(k);
                        break;
                    }
                }
                // Passendes Fahrzeug im Dropdown auswählen
                for (Fahrzeug f : cbFahrzeug.getItems()) {
                    if (f.getFahrzeug_id() == neu.getFahrzeugId()) {
                        cbFahrzeug.setValue(f);
                        break;
                    }
                }
                // Passenden Mitarbeiter im Dropdown auswählen
                for (Mitarbeiter m : cbMitarbeiter.getItems()) {
                    if (m.getMitarbeiter_id() == neu.getMitarbeiterId()) {
                        cbMitarbeiter.setValue(m);
                        break;
                    }
                }
            }
        });

        ladeMietvertraege();
    }

    @FXML
    public void onHinzufuegen() {
        try {
            KundeDAO.KundeUebersicht kunde = cbKunde.getValue();
            Fahrzeug fahrzeug = cbFahrzeug.getValue();
            Mitarbeiter mitarbeiter = cbMitarbeiter.getValue();

            LocalDate start = dpStartdatum.getValue();
            LocalDate ende = dpEndedatum.getValue();

            if (kunde == null || fahrzeug == null || mitarbeiter == null || start == null || ende == null || txtGesamtkosten.getText().isBlank()) {
                zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Bitte wähle Kunde, Fahrzeug, Mitarbeiter und beide Daten aus!");
                return;
            }

            double kosten = Double.parseDouble(txtGesamtkosten.getText().replace(",", "."));

            boolean erfolg = MietvertragDAO.mietvertragHinzufuegen(
                    kunde.getKundeId(), fahrzeug.getFahrzeug_id(), mitarbeiter.getMitarbeiter_id(), start, ende, kosten
            );

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Mietvertrag erfolgreich hinzugefügt!");
                ladeMietvertraege();
                felderLeeren();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mietvertrag konnte nicht gespeichert werden.");
            }
        } catch (NumberFormatException e) {
            zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Die Gesamtkosten müssen eine gültige Zahl sein.");
        }
    }

    @FXML
    public void onBearbeiten() {
        MietvertragDAO.MietvertragUebersicht selected = tabelleMietvertraege.getSelectionModel().getSelectedItem();
        if (selected == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mietvertrag aus der Tabelle aus.");
            return;
        }

        try {
            KundeDAO.KundeUebersicht kunde = cbKunde.getValue();
            Fahrzeug fahrzeug = cbFahrzeug.getValue();
            Mitarbeiter mitarbeiter = cbMitarbeiter.getValue();
            LocalDate start = dpStartdatum.getValue();
            LocalDate ende = dpEndedatum.getValue();

            if (kunde == null || fahrzeug == null || mitarbeiter == null || start == null || ende == null || txtGesamtkosten.getText().isBlank()) {
                zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Bitte fülle alle Felder aus.");
                return;
            }

            double kosten = Double.parseDouble(txtGesamtkosten.getText().replace(",", "."));

            boolean erfolg = MietvertragDAO.mietvertragBearbeiten(
                    selected.getMietvertragId(), kunde.getKundeId(), fahrzeug.getFahrzeug_id(), mitarbeiter.getMitarbeiter_id(), start, ende, kosten
            );

            if (erfolg) {
                zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Mietvertrag erfolgreich aktualisiert!");
                ladeMietvertraege();
                felderLeeren();
            } else {
                zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mietvertrag konnte nicht bearbeitet werden.");
            }
        } catch (NumberFormatException e) {
            zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Die Gesamtkosten müssen eine gültige Zahl sein.");
        }
    }

    @FXML
    public void onLoeschen() {
        MietvertragDAO.MietvertragUebersicht selected = tabelleMietvertraege.getSelectionModel().getSelectedItem();
        if (selected == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mietvertrag zum Löschen aus.");
            return;
        }

        boolean erfolg = MietvertragDAO.mietvertragLoeschen(selected.getMietvertragId());
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Mietvertrag erfolgreich gelöscht!");
            ladeMietvertraege();
            felderLeeren();
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mietvertrag konnte nicht gelöscht werden.");
        }
    }

    @FXML
    public void onAbbrechen() {
        felderLeeren();
        tabelleMietvertraege.getSelectionModel().clearSelection();
    }

    @FXML
    public void onAktualisieren() {
        ladeMietvertraege();
    }

    private void ladeMietvertraege() {
        tabelleMietvertraege.setItems(MietvertragDAO.ladeAlleMietvertraege());
    }

    private void felderLeeren() {
        cbKunde.getSelectionModel().clearSelection();
        cbFahrzeug.getSelectionModel().clearSelection();
        cbMitarbeiter.getSelectionModel().clearSelection();
        dpStartdatum.setValue(null);
        dpEndedatum.setValue(null);
        txtGesamtkosten.clear();
    }

    private void zeigeMeldung(Alert.AlertType type, String titel, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}