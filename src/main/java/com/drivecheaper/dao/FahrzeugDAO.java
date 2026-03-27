package com.drivecheaper.dao;

import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.PKW;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class FahrzeugDAO {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static boolean autoHinzufuegen(String hersteller, String modell, String kennzeichen) {
        String insertQuery = """
                INSERT INTO fahrzeug ( status,  hersteller, modell, kennzeichen)
                VALUES (1, ?, ?, ?)
            """;
        // 1. Verbindung herstellen
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             // 2. Das PreparedStatment mit unserem SQL-Text erstellen
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // 3. Die Fragezeichen (?) der Reihe nach mit unseren Variablen füllen!
            // Das erste Fragezeichen (1) bekommt den Text (String) aus der Variable 'hersteller'
            preparedStatement.setString(1, hersteller);
            preparedStatement.setString(2, modell);
            preparedStatement.setString(3, kennzeichen);


            // 4. Den fertigen Befehl an die Datenbank abschicken(Update statt Query)
            preparedStatement.executeUpdate();
            return true; // Es hat geklapt
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Es gab einen Fahler
        }
    }

    public static ObservableList<Fahrzeug> ladeAlleFahrzeuge() {

        ObservableList<Fahrzeug> fahrzeugListe = FXCollections.observableArrayList();
        // 1. Verbindung zur Datenbank "drivecheper" herstellen
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             Statement statement = connection.createStatement();
                                                            // 2. Die SQL-Befehl vorbereiten
             ResultSet rs = statement.executeQuery("SELECT * FROM fahrzeug"))
            {
//            // 1. Verbindung zur Datenbank "drivecheper" herstellen
//            Connection connection = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/drivecheaper",
//                    "root",
//                    "user1234"
//            );
//
//            Statement statement = connection.createStatement();
//
//            // 2. Die SQL-Befehl vorbereiten
//            String query = "SELECT * FROM fahrzeug";
//
//            //Die Fahrzeuge aus der Datenbank in diesem resultSet zwischengespeichert.
//            ResultSet resultSet = statement.executeQuery(query);


                while (rs.next()) {
                    // 1. Daten in Variablen festhalten
                    int fahrzeug_id = rs.getInt("fahrzeug_id");
                    boolean status = rs.getBoolean("status");
                    String hersteller = rs.getString("hersteller");
                    String modell = rs.getString("modell");
                    String kennzeichen = rs.getString("kennzeichen");

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