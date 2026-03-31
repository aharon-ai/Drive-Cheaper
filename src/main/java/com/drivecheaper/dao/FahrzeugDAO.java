package com.drivecheaper.dao;

import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.PKW;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.control.CheckBox;

import java.sql.*;

public class FahrzeugDAO {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static boolean autoHinzufuegen(String hersteller, String modell, String kennzeichen, String baujahr, String kilometerstand,
                                          String tagesKosten, String tankfuellung, String kaution, String kraftstoffArt) {
        String insertQuery = """
                INSERT INTO fahrzeug ( status,  hersteller, modell, kennzeichen, baujahr,
                                      kilometerstand, tagesKosten, tankfuellung, kaution, kraftstoffArt)
                VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
            preparedStatement.setString(4, baujahr);
            preparedStatement.setString(5, kilometerstand);
            preparedStatement.setString(6, tagesKosten);
            preparedStatement.setString(7, tankfuellung);
            preparedStatement.setString(8, kaution);
            preparedStatement.setString(9, kraftstoffArt);



            // 4. Den fertigen Befehl an die Datenbank abschicken(Update statt Query)
            preparedStatement.executeUpdate();
            return true; // Es hat geklapt
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Es gab einen Fahler
        }
    }

    public static boolean autoLoeschen(int id){

        String deleteQuery = """
                DELETE FROM fahrzeug WHERE fahrzeug_id = ?""";

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            // Das erste (und einzige) Fragezeichen (1) bekommt die übergebene id
            preparedStatement.setInt(1, id);


            // Den fertigen Befehl an die Datenbank abschicken(Update statt Query)
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