package com.drivecheaper.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public final class ViewNavigator {

    private ViewNavigator() {
    }

    public static void navigate(ActionEvent event, String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewNavigator.class.getResource("/com/drivecheaper/" + fxmlName));
            Parent root = loader.load();

            // OPTIMIERUNG 1: Node statt Button (funktioniert jetzt mit allen klickbaren Elementen)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // OPTIMIERUNG 2: Fenstergröße beibehalten!
            // Nur wenn es noch gar keine Szene gibt, machen wir eine neue.
            // Ansonsten tauschen wir einfach den Inhalt (Root) aus.
            if (stage.getScene() == null) {
                stage.setScene(new Scene(root, 1000, 816));
            } else {
                stage.getScene().setRoot(root);
            }

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Systemfehler");
            alert.setHeaderText("Die Ansicht '" + fxmlName + "' konnte nicht geladen werden!");

            Throwable cause = e.getCause();
            if (cause != null) {
                alert.setContentText("Ursache: " + cause.toString());
            } else {
                alert.setContentText("Fehler: " + e.getMessage());
            }
            alert.showAndWait();
        }
    }
}