package com.drivecheaper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DriveCheaperApp extends Application {
    public static void main(String[] args) {launch(args);


    }

    //jede Anwendung verwendet für den Start diese Methode
    //Vererbung und überschreiben notwendig
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Mitarbeiter-wiev.fxml"));

        //Laden der FXML-Datei, zuerst Erstellen eines Objekt der Klasse DriveController , dann initialize()
        Parent root = fxmlLoader.load();

        //Anzeigefläche im Fenster festlegen -- wo soll das fxml angezeigt werden
        Scene scene = new Scene(root, 720, 550);

        //Anzeigefläche einem Fenster zuweisen -- jetzt hat es events Minimieren, maximieren, Schließen, Titelleiste
        stage.setScene(scene);

        //titel festlegen
        stage.setTitle("Drive Cheaper!");

        //Anzeigen des Fensters
        stage.show();
    }
}
