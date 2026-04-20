package com.drivecheaper.controllers;

import com.drivecheaper.dao.FahrzeugDAO;
import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.KraftstoffArt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class FahrzeugController {

    //wir brauchen eine Verbindung zur Fahrzeug
    //private Fahrzeug api = new Fahrzeug();


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

    ObservableList<Fahrzeug> fahrzeugListe = FXCollections.observableArrayList();


            @FXML
            public void initialize() {

                //wir legen fest, welche Eigenschaft vom Objekt artikel in welcher TableColumn angezeigt wird
                colId.setCellValueFactory(new PropertyValueFactory<>("fahrzeug_id"));
                colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
                colHersteller.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
                colModell.setCellValueFactory(new PropertyValueFactory<>("modell"));
                colKennzeichen.setCellValueFactory(new PropertyValueFactory<>("kennzeichen"));
                cbKraftstoffArt.getItems().addAll(KraftstoffArt.values());

                // 1. Liste fullen
                fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();

                // 2. Liste an die Tabelle binden
                tabelleFahrzeuge.setItems(fahrzeugListe);


                System.out.println("Gefundene Autos: " + fahrzeugListe.size());


                // Boolean fur die Tabvelle definieren als Verfügbar oder nicht verfügbar
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
                        // Textfelder mit den Daten des angeklickten Autos füllen
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

            }
            @FXML
            public void onHizufugenKlicken(){
                    // 1. Texte aus den Eingabefaldern auslesen
                    String eingabeHersteller = txtHersteller.getText();
                    String eingabeModell = txtModell.getText();
                    String eingabeKennzeichen = txtKennzeichen.getText();
                    String eingabeBaujahr = txtBaujahr.getText();
                    String eingabeKilometerstand = txtKilometerstand.getText();
                    String eingabeTageskosten = txtTageskosten.getText();
                    String eingabeTankfuellung = txtTankfuellung.getText();
                    String eingabeKaution = txtKaution.getText();

                    // 1. Text holen
                    String eingabeKraftstoffArt = String.valueOf(cbKraftstoffArt.getValue());
                    eingabeKraftstoffArt = eingabeKraftstoffArt.substring(0, 1).toUpperCase() + eingabeKraftstoffArt.substring(1).toLowerCase();

                // WICHTIG: Hier wandeln wir das UI-Element in einen Datenwert (boolean) um!
                boolean eingabeStatus = isStatus.isSelected();

                // Kleiner Sicherheits-Check: Sind die Felder leer?
                if (eingabeHersteller.isEmpty() || eingabeModell.isEmpty() || eingabeKennzeichen.isEmpty()) {
                    System.out.println("Achtung: Die Felder mussen ausgefuhlt sein");
                    return; // Bricht die methode ab
                }

                // 2. Die Daten an die Datenbank schicken
                boolean erfolg = FahrzeugDAO.autoHinzufuegen(
                        eingabeHersteller, eingabeModell, eingabeKennzeichen,
                        eingabeBaujahr, eingabeKilometerstand, eingabeTageskosten,
                        eingabeTankfuellung, eingabeKaution, eingabeKraftstoffArt
                );

                // 3. Das Specher ist erfolgreich: Tabelle update und Fehler leeren
                if (erfolg) {
                    System.out.println("Das Auto ist erfolgreich gespeichert!");

                    // Tabelle neue laden
                    fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();
                    tabelleFahrzeuge.setItems(fahrzeugListe);

                    // Textfelder wieder leer machen fur das nachste Auto hinzufugen
                    // Felder leeren
                    txtHersteller.clear();
                    txtModell.clear();
                    txtKennzeichen.clear();
                    txtBaujahr.clear();
                    txtKilometerstand.clear();
                    txtTageskosten.clear();
                    txtKaution.clear();
                    cbKraftstoffArt.getSelectionModel().clearSelection();
                    txtTankfuellung.clear(); // Auch dieses leeren

                    // Die Checkbox können wir ignorieren, da der Status ja eh in der DB auf 1 gesetzt wurde.

                } else {

                    System.out.println("Fahler beim Speichern in der Datenbank.");

                }

            }


            ;
            @FXML
            public void onBearbeitenKlicken() {
                // 1. Das markierte Auto aus der Tabelle holen
                Fahrzeug geaendertesAuto = tabelleFahrzeuge.getSelectionModel().getSelectedItem();
                // 2. Prüfen, ob wirklich ein Auto ausgewählt wurde
                if (geaendertesAuto != null) {
                    // Wir holen uns die ID dieses Autos, denn die brauchen wir für die Datenbank!

                    int zieleBearbeiten = geaendertesAuto.getFahrzeug_id();

                        System.out.println("Das Auto ist erfolgreich bearbeitet!");

                        geaendertesAuto.setHersteller(txtHersteller.getText());
                        geaendertesAuto.setModell(txtModell.getText());
                        geaendertesAuto.setKennzeichen(txtKennzeichen.getText());
                        geaendertesAuto.setStatus(isStatus.isSelected());

                        // Bei Zahlen wandeln wir den Text direkt um:
                        geaendertesAuto.setBaujahr(Integer.parseInt(txtBaujahr.getText()));
                        geaendertesAuto.setKilometerstand(Double.parseDouble(txtKilometerstand.getText()));
                        geaendertesAuto.setTageskosten(Double.parseDouble(txtTageskosten.getText()));
                        geaendertesAuto.setTankfuellung(Double.parseDouble(txtTankfuellung.getText()));
                        geaendertesAuto.setKaution(Double.parseDouble(txtKaution.getText()));
                        geaendertesAuto.setKraftstoffArt(cbKraftstoffArt.getValue());

                        // 2. Das fertige Paket an die Datenbank schicken!
                        boolean erfolg = FahrzeugDAO.autoBearbeiten(geaendertesAuto);

                    if (erfolg) {
                        System.out.println("Das Auto ist erfolgreich bearbeitet!");
                        // Tabelle neue laden
                        fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();
                        tabelleFahrzeuge.setItems(fahrzeugListe);
                    }
                } else {
                    System.out.println("Bitte wähle zuerst ein Auto aus der Tabelle aus!");
                }

            }

            @FXML
            public void onLoeschenKlicken(){
                // 1. Das markierte Auto aus der Tabelle holen
                Fahrzeug ausgewaehltesAuto = tabelleFahrzeuge.getSelectionModel().getSelectedItem();
                // 2. Prüfen, ob wirklich ein Auto ausgewählt wurde
                if (ausgewaehltesAuto != null) {
                    // Wir holen uns die ID dieses Autos, denn die brauchen wir für die Datenbank!
                    int zieleLoeschen = ausgewaehltesAuto.getFahrzeug_id();

                    // 1. Das Auto in der Datenbank löschen
                    boolean erfolg = FahrzeugDAO.autoLoeschen(zieleLoeschen);

                    if (erfolg) {
                        System.out.println("Das Auto ist erfolgreich gelöscht!");

                        // Tabelle neue laden
                        fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();
                        tabelleFahrzeuge.setItems(fahrzeugListe);

                        // Textfelder wieder leer machen fur das nachste Auto hinzufugen
                        txtHersteller.clear();
                        txtModell.clear();
                        txtKennzeichen.clear();
                        txtBaujahr.clear();
                        txtKilometerstand.clear();
                        txtTageskosten.clear();
                        txtKaution.clear();
                        cbKraftstoffArt.getSelectionModel().clearSelection();

                    }
                } else {
                    System.out.println("Bitte wähle zuerst ein Auto aus der Tabelle aus!");
                }

            }

            @FXML
            public void onAbbrechenKlicken(){
                txtHersteller.clear();
                txtModell.clear();
                txtKennzeichen.clear();

            }

    }

