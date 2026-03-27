package com.drivecheaper.controllers;

import com.drivecheaper.dao.FahrzeugDAO;
import com.drivecheaper.model.Fahrzeug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class MitarbeiterController {

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

    ObservableList<Fahrzeug> fahrzeugListe = FXCollections.observableArrayList();


            @FXML
            public void initialize() {

                //wir legen fest, welche Eigenschaft vom Objekt artikel in welcher TableColumn angezeigt wird
                colId.setCellValueFactory(new PropertyValueFactory<>("fahrzeug_id"));
                colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
                colHersteller.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
                colModell.setCellValueFactory(new PropertyValueFactory<>("modell"));
                colKennzeichen.setCellValueFactory(new PropertyValueFactory<>("kennzeichen"));

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

            }
            @FXML
            public void onHizufugenKlicken(){
                // 1. Texte aus den Eingabefaldern auslesen
                String eingabeHersteller = txtHersteller.getText();
                String eingabeModell = txtModell.getText();
                String eingabeKennzeichen = txtKennzeichen.getText();

                // Kleiner Sicherheits-Check: Sind die Felder leer?
                if (eingabeHersteller.isEmpty() || eingabeModell.isEmpty() || eingabeKennzeichen.isEmpty()) {
                    System.out.println("Achtung: Die Felder mussen ausgefuhlt sein");
                    return; // Bricht die methode ab
                }

                // 2. Die Daten an die Datenbank schicken
                boolean erfolg = FahrzeugDAO.autoHinzufuegen(eingabeHersteller, eingabeModell, eingabeKennzeichen);

                // 3. Das Specher ist erfolgreich: Tabelle update und Fehler leeren
                if (erfolg) {
                    System.out.println("Das Auto ist erfolgreich gespeichert!");

                    // Tabelle neue laden
                    fahrzeugListe = FahrzeugDAO.ladeAlleFahrzeuge();
                    tabelleFahrzeuge.setItems(fahrzeugListe);

                    // Textfelder wieder leer machen fur das nachste Auto hinzufugen
                    txtHersteller.clear();
                    txtModell.clear();
                    txtKennzeichen.clear();
                } else {
                    System.out.println("Fahler beim Speichern in der Datenbank.");
                }

            }
            @FXML
            public void onBearbeitenKlicken(){

            }

            @FXML
            public void onLoeschenKlicken(){

            }

            @FXML
            public void onAbbrechenKlicken(){
                txtHersteller.clear();
                txtModell.clear();
                txtKennzeichen.clear();

            }

    }

