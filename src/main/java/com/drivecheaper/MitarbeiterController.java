package com.drivecheaper;

import com.drivecheaper.DAO.FahrzeugDAO;
import com.drivecheaper.Fahrzeuge.Fahrzeug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.drivecheaper.DAO.FahrzeugDAO;

import java.sql.*;
import java.util.ArrayList;


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


                // Dieser Block muss verschoben werden! 📦
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


    }

