package com.drivecheaper.controllers;

import com.drivecheaper.dao.FahrzeugDAO;
import com.drivecheaper.dao.KundeDAO;
import com.drivecheaper.dao.MietvertragDAO;
import com.drivecheaper.dao.MitarbeiterDAO;
import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.Mitarbeiter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class MietvertragController {
    @FXML
    private TableView<MietvertragDAO.MietvertragUebersicht> tabelleMietvertraege;


    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colId;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colKunde;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colFahrzeug;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Integer> colMitarbeiter;


    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Double> colMietTage;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Double> colTageskosten;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Double> colRabatt;


    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, Double> colGesamtkosten;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, LocalDate> colStartdatum;
    @FXML
    private TableColumn<MietvertragDAO.MietvertragUebersicht, LocalDate> colEndedatum;


    @FXML
    private ComboBox<KundeDAO.KundeUebersicht> cbKunde;
    @FXML
    private ComboBox<Fahrzeug> cbFahrzeug;
    @FXML
    private ComboBox<Mitarbeiter> cbMitarbeiter;


    @FXML
    private DatePicker dpStartdatum;
    @FXML
    private DatePicker dpEndedatum;


    // Suchen
    @FXML private TextField txtSuche;

    // Versteckte Listen als Wörterbuch für die Tabelle
    private ObservableList<KundeDAO.KundeUebersicht> alleKundenKatalog;
    private ObservableList<Fahrzeug> alleFahrzeugeKatalog;
    private ObservableList<Mitarbeiter> alleMitarbeiterKatalog;

    @FXML
    public void onHomeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "HomeView.fxml"); }
    @FXML
    public void onFahrzeugeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "FahrzeugView.fxml"); }
    @FXML
    public void onMitarbeiterClick(ActionEvent event) {
        ViewNavigator.navigate(event, "MitarbeiterView.fxml"); }
    @FXML
    public void onKundeClick(ActionEvent event) {
        ViewNavigator.navigate(event, "KundeView.fxml"); }
    @FXML
    public void onMietvertragClick(ActionEvent event) {
        ViewNavigator.navigate(event, "MietvertragView.fxml"); }


    @FXML
    public void initialize() {
        // 1. Kataloge komplett laden (auch mit vermieteten Autos!)
        alleKundenKatalog = KundeDAO.ladeKundenUebersicht();
        alleFahrzeugeKatalog = FahrzeugDAO.ladeAlleFahrzeuge();
        alleMitarbeiterKatalog = MitarbeiterDAO.ladeAlleMitarbeiter();

        // 2. Dropdowns befüllen
        cbKunde.setItems(alleKundenKatalog);
        cbMitarbeiter.setItems(alleMitarbeiterKatalog);

        // Nur verfügbare Fahrzeuge in das Dropdown-Menü laden
        ObservableList<Fahrzeug> verfuegbareFahrzeuge = FXCollections.observableArrayList();
        for (Fahrzeug f : alleFahrzeugeKatalog) {
            if (f.isStatus() == true) {
                verfuegbareFahrzeuge.add(f);
            }
        }
        cbFahrzeug.setItems(verfuegbareFahrzeuge);

        colId.setCellValueFactory(new PropertyValueFactory<>("mietvertragId"));
        colKunde.setCellValueFactory(new PropertyValueFactory<>("kundeId"));
        colFahrzeug.setCellValueFactory(new PropertyValueFactory<>("fahrzeugId"));
        colMitarbeiter.setCellValueFactory(new PropertyValueFactory<>("mitarbeiterId"));
        colMietTage.setCellValueFactory(new PropertyValueFactory<>("mietTage"));
        colTageskosten.setCellValueFactory(new PropertyValueFactory<>("tageskosten"));
        colRabatt.setCellValueFactory(new PropertyValueFactory<>("rabatt"));
        colStartdatum.setCellValueFactory(new PropertyValueFactory<>("startdatum"));
        colEndedatum.setCellValueFactory(new PropertyValueFactory<>("endedatum"));
        colGesamtkosten.setCellValueFactory(new PropertyValueFactory<>("gesamtkosten"));

        // --- NEU: Die Tabelle schaut ab jetzt im Kometten Katalog nach ---
        colKunde.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Integer kundeId, boolean empty) {
                super.updateItem(kundeId, empty);
                if (empty || kundeId == null) setText(null);
                else {
                    String name = "ID: " + kundeId;
                    for (KundeDAO.KundeUebersicht k : alleKundenKatalog) {
                        if (k.getKundeId() == kundeId) { name = k.getVorname() + " " + k.getNachname(); break; }
                    }
                    setText(name);
                }
            }
        });

        colFahrzeug.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Integer fahrzeugId, boolean empty) {
                super.updateItem(fahrzeugId, empty);
                if (empty || fahrzeugId == null) setText(null);
                else {
                    String name = "ID: " + fahrzeugId;
                    for (Fahrzeug f : alleFahrzeugeKatalog) {
                        if (f.getFahrzeug_id() == fahrzeugId) {
                            // Hersteller, Modell und Kennzeichen!
                            name = f.getHersteller() + " " + f.getModell() + " (" + f.getKennzeichen() + ")";
                            break;
                        }
                    }
                    setText(name);
                }
            }
        });

        colMitarbeiter.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Integer mitarbeiterId, boolean empty) {
                super.updateItem(mitarbeiterId, empty);
                if (empty || mitarbeiterId == null) setText(null);
                else {
                    String name = "ID: " + mitarbeiterId;
                    for (Mitarbeiter m : alleMitarbeiterKatalog) {
                        if (m.getMitarbeiter_id() == mitarbeiterId) { name = m.getVorname() + " " + m.getNachname(); break; }
                    }
                    setText(name);
                }
            }
        });

        // --- NEU: Runden der Gesamtkosten auf 2 Nachkommastellen ---
        colGesamtkosten.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(Double kosten, boolean empty) {
                super.updateItem(kosten, empty);
                if (empty || kosten == null) setText(null);
                else {
                    setText(String.format("%.2f €", kosten));
                }
            }
        });

        // Schöne Text-Anzeige für die Dropdowns
        cbKunde.setConverter(new StringConverter<>() {
            @Override public String toString(KundeDAO.KundeUebersicht k) { return k == null ? "" : k.getVorname() + " " + k.getNachname(); }
            @Override public KundeDAO.KundeUebersicht fromString(String s) { return null; }
        });

        cbFahrzeug.setConverter(new StringConverter<>() {
            @Override public String toString(Fahrzeug f) { return f == null ? "" : f.getHersteller() + " " + f.getModell() + " (" + f.getKennzeichen() + ")"; }
            @Override public Fahrzeug fromString(String s) { return null; }
        });

        cbMitarbeiter.setConverter(new StringConverter<>() {
            @Override public String toString(Mitarbeiter m) { return m == null ? "" : m.getVorname() + " " + m.getNachname(); }
            @Override public Mitarbeiter fromString(String s) { return null; }
        });

        tabelleMietvertraege.getSelectionModel().selectedItemProperty().addListener((obs, alt, neu) -> {
            if (neu != null) {
                dpStartdatum.setValue(neu.getStartdatum());
                dpEndedatum.setValue(neu.getEndedatum());
                for (KundeDAO.KundeUebersicht k : cbKunde.getItems()) if (k.getKundeId() == neu.getKundeId()) cbKunde.setValue(k);

                // Fahrzeug Dropdown: Nur setzen, wenn es noch in der verfügbaren Liste ist
                cbFahrzeug.getSelectionModel().clearSelection();
                for (Fahrzeug f : cbFahrzeug.getItems()) {
                    if (f.getFahrzeug_id() == neu.getFahrzeugId()) {
                        cbFahrzeug.setValue(f);
                        break;
                    }
                }

                for (Mitarbeiter m : cbMitarbeiter.getItems()) if (m.getMitarbeiter_id() == neu.getMitarbeiterId()) cbMitarbeiter.setValue(m);
            }
        });

        // --- NEU: Auswahl aufheben, wenn man ins Leere klickt ---
        tabelleMietvertraege.setOnMouseClicked(event -> {
            if (tabelleMietvertraege.getSelectionModel().getSelectedItem() == null) {
                onAbbrechen(); // Setzt alle Felder unten zurück
            }
        });

        ladeMietvertraege();
    }

    private double[] berechneAlles(LocalDate start, LocalDate ende, Fahrzeug fahrzeug, KundeDAO.KundeUebersicht kunde) {
        long tage = ChronoUnit.DAYS.between(start, ende);
        if (tage <= 0) tage = 1;

        double tagesPreis = fahrzeug.getTageskosten();
        double rabatt = KundeDAO.getFirmenRabatt(kunde.getKundeId());
        double gesamtPreis = (tage * tagesPreis) * (1.0 - (rabatt / 100.0));

        return new double[]{tage, tagesPreis, rabatt, gesamtPreis};
    }

    @FXML
    public void onHinzufuegen() {
        KundeDAO.KundeUebersicht kunde = cbKunde.getValue();
        Fahrzeug fahrzeug = cbFahrzeug.getValue();
        Mitarbeiter mitarbeiter = cbMitarbeiter.getValue();
        LocalDate start = dpStartdatum.getValue();
        LocalDate ende = dpEndedatum.getValue();

        if (kunde == null || fahrzeug == null || mitarbeiter == null || start == null || ende == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Bitte fülle alle Dropdowns und Daten aus!");
            return;
        }

        double[] werte = berechneAlles(start, ende, fahrzeug, kunde);

        boolean erfolg = MietvertragDAO.mietvertragHinzufuegen(
                kunde.getKundeId(), fahrzeug.getFahrzeug_id(), mitarbeiter.getMitarbeiter_id(),
                start, ende, werte[0], werte[1], werte[2], werte[3]
        );

        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Vertrag erfolgreich erstellt!\nPreis: " + String.format("%.2f €", werte[3]));
            ladeMietvertraege();
            onAbbrechen(); // Formular automatisch leeren
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mietvertrag konnte nicht gespeichert werden.");
        }
    }

    @FXML
    public void onBearbeiten() {
        MietvertragDAO.MietvertragUebersicht selected = tabelleMietvertraege.getSelectionModel().getSelectedItem();
        if (selected == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mietvertrag aus!");
            return;
        }

        KundeDAO.KundeUebersicht kunde = cbKunde.getValue();
        Fahrzeug fahrzeug = cbFahrzeug.getValue();
        Mitarbeiter mitarbeiter = cbMitarbeiter.getValue();
        LocalDate start = dpStartdatum.getValue();
        LocalDate ende = dpEndedatum.getValue();

        if (kunde == null || fahrzeug == null || mitarbeiter == null || start == null || ende == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Eingabefehler", "Bitte fülle alle Felder aus.");
            return;
        }

        double[] werte = berechneAlles(start, ende, fahrzeug, kunde);

        boolean erfolg = MietvertragDAO.mietvertragBearbeiten(
                selected.getMietvertragId(), kunde.getKundeId(), fahrzeug.getFahrzeug_id(), mitarbeiter.getMitarbeiter_id(),
                start, ende, werte[0], werte[1], werte[2], werte[3]
        );

        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Vertrag erfolgreich aktualisiert!");
            ladeMietvertraege();
            onAbbrechen(); // Formular automatisch leeren
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Mietvertrag konnte nicht bearbeitet werden.");
        }
    }

    @FXML
    public void onLoeschen() {
        MietvertragDAO.MietvertragUebersicht selected = tabelleMietvertraege.getSelectionModel().getSelectedItem();
        if (selected == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle einen Mietvertrag zum Löschen aus.");
            return;
        }
        boolean erfolg = MietvertragDAO.mietvertragLoeschen(selected.getMietvertragId());
        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Mietvertrag gelöscht!");
            ladeMietvertraege();
            onAbbrechen(); // Formular automatisch leeren
        }
    }

    @FXML
    public void onAutoZurueckgeben() {
        MietvertragDAO.MietvertragUebersicht selected = tabelleMietvertraege.getSelectionModel().getSelectedItem();

        if (selected == null) {
            zeigeMeldung(Alert.AlertType.WARNING, "Hinweis", "Bitte wähle zuerst einen Mietvertrag aus der Tabelle aus!");
            return;
        }

        boolean erfolg = MietvertragDAO.autoWiederVerfuegbarMachen(selected.getFahrzeugId());

        if (erfolg) {
            zeigeMeldung(Alert.AlertType.INFORMATION, "Erfolg", "Vertrag beendet! Das Auto ist ab sofort wieder verfügbar.");
            ladeMietvertraege();
            onAbbrechen(); // Formular automatisch leeren
        } else {
            zeigeMeldung(Alert.AlertType.ERROR, "Fehler", "Das Auto konnte nicht freigegeben werden.");
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
        onAbbrechen(); // --- NEU: Auswahl aufheben beim Klicken auf Aktualisieren ---
    }

    private void ladeMietvertraege() {
        // 1. Daten aus der Datenbank holen
        ObservableList<MietvertragDAO.MietvertragUebersicht> vertraege = MietvertragDAO.ladeAlleMietvertraege();

        // Fahrzeug-Dropdown aktualisieren
        alleFahrzeugeKatalog = FahrzeugDAO.ladeAlleFahrzeuge();
        ObservableList<Fahrzeug> verfuegbareFahrzeuge = FXCollections.observableArrayList();
        for (Fahrzeug f : alleFahrzeugeKatalog) {
            if (f.isStatus() == true) verfuegbareFahrzeuge.add(f);
        }
        cbFahrzeug.setItems(verfuegbareFahrzeuge);

        // --- NEUE SUCHE ---

        // 2. Die Daten in eine "gefilterte Liste" packen
        javafx.collections.transformation.FilteredList<MietvertragDAO.MietvertragUebersicht> filteredData =
                new javafx.collections.transformation.FilteredList<>(vertraege, b -> true);

        // 3. Zuhören, was der Nutzer in das Suchfeld tippt
        if (txtSuche != null) {
            txtSuche.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(vertrag -> {
                    // Wenn das Suchfeld leer ist, zeige alle Verträge
                    if (newValue == null || newValue.isBlank()) return true;

                    // Suchwort in Kleinbuchstaben umwandeln (für bessere Suche)
                    String suchWort = newValue.toLowerCase();

                    // Namen aus dem Katalog zusammensuchen, um darin suchen zu können
                    String kundeName = "";
                    for (KundeDAO.KundeUebersicht k : alleKundenKatalog) {
                        if (k.getKundeId() == vertrag.getKundeId()) kundeName = k.getVorname() + " " + k.getNachname();
                    }
                    String autoName = "";
                    for (Fahrzeug f : alleFahrzeugeKatalog) {
                        if (f.getFahrzeug_id() == vertrag.getFahrzeugId()) autoName = f.getKennzeichen() + " " + f.getModell();
                    }

                    // Schauen, ob das Suchwort irgendwo drinsteckt
                    if (String.valueOf(vertrag.getMietvertragId()).contains(suchWort)) return true; // Suche nach ID
                    if (kundeName.toLowerCase().contains(suchWort)) return true; // Suche nach Kundenname
                    if (autoName.toLowerCase().contains(suchWort)) return true; // Suche nach Kennzeichen/Auto

                    return false; // Nichts gefunden -> Ausblenden!
                });
            });
        }

        // 4. Damit man die Spalten trotzdem noch durch Klicken sortieren kann:
        javafx.collections.transformation.SortedList<MietvertragDAO.MietvertragUebersicht> sortedData =
                new javafx.collections.transformation.SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabelleMietvertraege.comparatorProperty());

        // 5. Die gefilterte Liste in die Tabelle packen!
        tabelleMietvertraege.setItems(sortedData);
    }

    private void felderLeeren() {
        cbKunde.getSelectionModel().clearSelection();
        cbFahrzeug.getSelectionModel().clearSelection();
        cbMitarbeiter.getSelectionModel().clearSelection();
        dpStartdatum.setValue(null);
        dpEndedatum.setValue(null);
    }

    private void zeigeMeldung(Alert.AlertType type, String titel, String text) {
        Alert alert = new Alert(type); alert.setTitle(titel); alert.setHeaderText(null); alert.setContentText(text); alert.showAndWait();
    }
}