package com.drivecheaper.controllers;

import com.drivecheaper.dao.HomeDAO;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class HomeController {
    @FXML private Label lblGesamtFahrzeuge;
    @FXML private Label lblVerfuegbar;
    @FXML private Label lblKunden;
    @FXML private Label lblFirmenkunden;
    @FXML private Label lblMitarbeiter;
    @FXML private Label lblMietvertraege;
    @FXML private Label lblUmsatz;

    @FXML private ComboBox<String> cbZeitraum;
    @FXML private DatePicker dpVon;
    @FXML private DatePicker dpBis;

    @FXML private TableView<HomeDAO.BerichtEintrag> tabelleBerichte;
    @FXML private TableColumn<HomeDAO.BerichtEintrag, String> colTitel;
    @FXML private TableColumn<HomeDAO.BerichtEintrag, String> colWert;
    @FXML private TableColumn<HomeDAO.BerichtEintrag, String> colDetails;

    @FXML
    public void initialize() {

        colTitel.setCellValueFactory(new PropertyValueFactory<>("titel"));
        colWert.setCellValueFactory(new PropertyValueFactory<>("wert"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));

        cbZeitraum.setItems(FXCollections.observableArrayList(
                "Gesamt", "Woche", "Monat", "Jahr", "Manuell"
        ));
        cbZeitraum.getSelectionModel().select("Gesamt");
        applyZeitraumDefaults();
        ladeDaten();
    }

    @FXML
    public void onZeitraumGeaendert() {
        applyZeitraumDefaults();
    }

    @FXML
    public void onAktualisieren(ActionEvent event) {
        ladeDaten();
    }

    private void applyZeitraumDefaults() {
        String typ = cbZeitraum.getValue();
        LocalDate heute = LocalDate.now();

        if ("Woche".equals(typ)) {
            dpVon.setValue(heute.minusDays(6));
            dpBis.setValue(heute);
            dpVon.setDisable(true);
            dpBis.setDisable(true);
        } else if ("Monat".equals(typ)) {
            dpVon.setValue(heute.withDayOfMonth(1));
            dpBis.setValue(heute);
            dpVon.setDisable(true);
            dpBis.setDisable(true);
        } else if ("Jahr".equals(typ)) {
            dpVon.setValue(heute.withDayOfYear(1));
            dpBis.setValue(heute);
            dpVon.setDisable(true);
            dpBis.setDisable(true);
        } else if ("Manuell".equals(typ)) {
            dpVon.setDisable(false);
            dpBis.setDisable(false);
            if (dpVon.getValue() == null) {
                dpVon.setValue(heute.withDayOfMonth(1));
            }
            if (dpBis.getValue() == null) {
                dpBis.setValue(heute);
            }
        } else {
            dpVon.setValue(null);
            dpBis.setValue(null);
            dpVon.setDisable(true);
            dpBis.setDisable(true);
        }
    }

    private void ladeDaten() {
        LocalDate von = dpVon.getValue();
        LocalDate bis = dpBis.getValue();

        if ("Gesamt".equals(cbZeitraum.getValue())) {
            von = null;
            bis = null;
        } else if (von != null && bis != null && von.isAfter(bis)) {
            LocalDate temp = von;
            von = bis;
            bis = temp;
            dpVon.setValue(von);
            dpBis.setValue(bis);
        }

        HomeDAO.HomeStatistik statistik = HomeDAO.ladeHomeStatistik(von, bis);
        lblGesamtFahrzeuge.setText(String.valueOf(statistik.getGesamtFahrzeuge()));
        lblVerfuegbar.setText(String.valueOf(statistik.getVerfuegbareFahrzeuge()));
        lblKunden.setText(String.valueOf(statistik.getGesamtKunden()));
        lblFirmenkunden.setText(String.valueOf(statistik.getGesamtFirmenkunden()));
        lblMitarbeiter.setText(String.valueOf(statistik.getGesamtMitarbeiter()));
        lblMietvertraege.setText(String.valueOf(statistik.getAktiveMietvertraege()));
        lblUmsatz.setText(String.format("%.2f EUR", statistik.getUmsatzZeitraum()));

        tabelleBerichte.setItems(HomeDAO.ladeBerichte(von, bis));
    }

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
}
