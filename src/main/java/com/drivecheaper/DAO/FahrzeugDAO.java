package com.drivecheaper.DAO;

import com.drivecheaper.Fahrzeuge.Fahrzeug;
import com.drivecheaper.Fahrzeuge.PKW;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class FahrzeugDAO {

    public static ObservableList<Fahrzeug> ladeAlleFahrzeuge() {
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement preparedStatement = null;

        ObservableList<Fahrzeug> fahrzeugListe = FXCollections.observableArrayList();

        try {
            // 1. Verbindung zur Datenbank "drivecheper" herstellen
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/drivecheaper",
                    "root",
                    "user1234"
            );

            Statement statement = connection.createStatement();

            // 2. Die SQL-Befehl vorbereiten
            String query = "SELECT * FROM fahrzeug";

            //Die Fahrzeuge aus der Datenbank in diesem resultSet zwischengespeichert.
            ResultSet resultSet = statement.executeQuery(query);


            while (resultSet.next()) {
                // 1. Daten in Variablen festhalten
                int fahrzeug_id = resultSet.getInt("fahrzeug_id");
                boolean status = resultSet.getBoolean("status");
                String hersteller = resultSet.getString("hersteller");
                String modell = resultSet.getString("modell");
                String kennzeichen = resultSet.getString("kennzeichen");


                // 2. Daraus ein neues Fahrzeug bauen (passe das an deinen echten Fahrzeug-Konstruktor an!)
                Fahrzeug neuesAuto = new PKW(fahrzeug_id, status, hersteller, modell, kennzeichen);

                // 3. Das fertige Auto in unsere Liste packen
                fahrzeugListe.add(neuesAuto);
            }

        } catch (SQLException e) {
            System.out.println("DA IST DER FEHLER: " + e.getMessage());
            e.printStackTrace();
        }
        return fahrzeugListe;
    }
}