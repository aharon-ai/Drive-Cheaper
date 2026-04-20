package com.drivecheaper.dao;

import com.drivecheaper.model.Fahrzeug;
import com.drivecheaper.model.KraftstoffArt;
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
                    int baujahr = rs.getInt("baujahr");
                    double kilometerstand = rs.getDouble("kilometerstand");
                    double tagesKosten = rs.getDouble("tagesKosten");
                    double tankfuellung = rs.getDouble("tankfuellung");
                    double kaution = rs.getDouble("kaution");
                    String dbKraftstoff = rs.getString("kraftstoffArt"); // z.B. "Benzin"

                    // 2. Daraus ein neues Fahrzeug bauen (passe das an deinen echten Fahrzeug-Konstruktor an!)
                    Fahrzeug neuesAuto = new PKW(fahrzeug_id, status, hersteller, modell, kennzeichen);


                    neuesAuto.setBaujahr(baujahr);
                    neuesAuto.setKilometerstand(kilometerstand);
                    neuesAuto.setTageskosten(tagesKosten);
                    neuesAuto.setTankfuellung(tankfuellung);
                    neuesAuto.setKaution(kaution);

                    // Kraftstoff zurück in ein Enum verwandeln (Aus "Benzin" wird wieder "BENZIN")
                    if (dbKraftstoff != null) {
                        neuesAuto.setKraftstoffArt(KraftstoffArt.valueOf(dbKraftstoff.toUpperCase()));

                    }
                    // 3. Das fertige Auto in unsere Liste packen
                    fahrzeugListe.add(neuesAuto);
                }


            } catch (SQLException e) {
                System.out.println("DA IST DER FEHLER: " + e.getMessage());
                e.printStackTrace();
            }

            return fahrzeugListe;
    }
    public static boolean autoBearbeiten(Fahrzeug geaendertesAuto) {
        // 1. Der SQL-Befehl mit Lücken (?) für jedes Feld und dem WHERE ganz am Ende
        String updateQuery = """
            UPDATE fahrzeug 
            SET hersteller = ?, modell = ?, kennzeichen = ?, baujahr = ?, 
                kilometerstand = ?, tageskosten = ?, status = ?, 
                tankfuellung = ?, kaution = ?, kraftstoffArt = ? 
            WHERE fahrzeug_id = ?""";

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // 2. Die Lücken der Reihe nach mit den Daten aus dem Objekt füllen
            preparedStatement.setString(1, geaendertesAuto.getHersteller());
            preparedStatement.setString(2, geaendertesAuto.getModell());
            preparedStatement.setString(3, geaendertesAuto.getKennzeichen());
            preparedStatement.setInt(4, geaendertesAuto.getBaujahr());
            preparedStatement.setDouble(5, geaendertesAuto.getKilometerstand());
            preparedStatement.setDouble(6, geaendertesAuto.getTageskosten());
            preparedStatement.setBoolean(7, geaendertesAuto.isStatus());
            preparedStatement.setDouble(8, geaendertesAuto.getTankfuellung());
            preparedStatement.setDouble(9, geaendertesAuto.getKaution());
            preparedStatement.setString(10, String.valueOf(geaendertesAuto.getKraftstoffArt()));

            // Das 11. Fragezeichen gehört zur WHERE-Bedingung (welches Auto soll geändert werden?)
            preparedStatement.setInt(11, geaendertesAuto.getFahrzeug_id());

            // 3. Befehl ausführen und prüfen, ob Zeilen geändert wurden
            int betroffeneZeilen = preparedStatement.executeUpdate();
            return betroffeneZeilen > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}