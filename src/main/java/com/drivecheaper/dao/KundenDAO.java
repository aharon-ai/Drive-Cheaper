package com.drivecheaper.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.time.LocalDate;

public class KundenDAO {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static boolean kundeHinzufuegen(String vorname, String nachname, LocalDate geburtsDatum,
                                           String adresse, String hausnummer, String email,
                                           String telefonnummer, int ort_id) {

        String insertQuery = """
                INSERT INTO Kunde (vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefonnummer, gespert, ort_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)
            """;
        // Hinweis: 'gespert' setzen wir direkt im SQL-Befehl auf 0 (FALSE), da ein neuer Kunde ja nicht direkt gesperrt ist.

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, vorname);
            preparedStatement.setString(2, nachname);
            preparedStatement.setDate(3, Date.valueOf(geburtsDatum)); // Hier passiert die Umwandlung!
            preparedStatement.setString(4, adresse);
            preparedStatement.setString(5, hausnummer);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, telefonnummer);
            preparedStatement.setInt(8, ort_id);


            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean firmenkundeHinzufuegen(String vorname, String nachname, LocalDate geburtsDatum,
                                                 String adresse, String hausnummer, String email,
                                                 String telefonnummer, int ort_id,
                                                 String firmenName, String ansprechpartner, double rabatt) {

        String insertKundeQuery = """
                INSERT INTO Kunde (vorname, nachname, geburtsDatum, adresse, hausnummer, email, telefonnummer, gespert, ort_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)
            """;

        String insertFirmaQuery = """
                INSERT INTO Firmenkunde (kunde_id, firmenName, ansprechpartner, rabatt)
                VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {

            // 1. Transaktion starten: Automatische Speicherung kurz anhalten
            connection.setAutoCommit(false);

            // WICHTIG: Wir sagen Java, dass wir die generierte ID nach dem ersten Insert zurückhaben wollen!
            try (PreparedStatement stmtKunde = connection.prepareStatement(insertKundeQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtFirma = connection.prepareStatement(insertFirmaQuery)) {

                // 2. Basis-Kunde befüllen und abschicken
                stmtKunde.setString(1, vorname);
                stmtKunde.setString(2, nachname);
                stmtKunde.setDate(3, Date.valueOf(geburtsDatum));
                stmtKunde.setString(4, adresse);
                stmtKunde.setString(5, hausnummer);
                stmtKunde.setString(6, email);
                stmtKunde.setString(7, telefonnummer);
                stmtKunde.setInt(8, ort_id);
                stmtKunde.executeUpdate();

                // 3. Die neu erstellte Kunden-ID aus der Datenbank fischen
                ResultSet rs = stmtKunde.getGeneratedKeys();
                int neueKundeId = 0;
                if (rs.next()) {
                    neueKundeId = rs.getInt(1);
                } else {
                    connection.rollback(); // Fehler: Keine ID bekommen -> Alles rückgängig machen!
                    return false;
                }

                // 4. Firmen-Daten mit der neuen ID abschicken
                stmtFirma.setInt(1, neueKundeId);
                stmtFirma.setString(2, firmenName);
                stmtFirma.setString(3, ansprechpartner);
                stmtFirma.setDouble(4, rabatt);
                stmtFirma.executeUpdate();

                // 5. Beide Schritte waren erfolgreich -> Jetzt wirklich dauerhaft speichern!
                connection.commit();
                return true;

            } catch (SQLException ex) {
                connection.rollback(); // Wenn irgendwo ein Fehler passiert, machen wir beide Inserts komplett rückgängig
                ex.printStackTrace();
                return false;
            } finally {
                connection.setAutoCommit(true); // Standard-Verhalten wiederherstellen
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
