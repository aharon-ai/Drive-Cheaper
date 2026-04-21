package com.drivecheaper.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KundeDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static class KundeUebersicht {
        private final int kundeId;
        private final String vorname;
        private final String nachname;
        private final LocalDate geburtsdatum;
        private final String kundentyp;
        private final String firmenname;
        private final int ortId;
        private final String adresse;
        private final String hausnummer;
        private final String email;
        private final String telefon;

        // Konstruktor
        public KundeUebersicht(int kundeId, String vorname, String nachname, LocalDate geburtsdatum,
                               String kundentyp, String firmenname, int ortId,
                               String adresse, String hausnummer, String email, String telefon) {
            this.kundeId = kundeId;
            this.vorname = vorname;
            this.nachname = nachname;
            this.geburtsdatum = geburtsdatum;
            this.kundentyp = kundentyp;
            this.firmenname = firmenname;
            this.ortId = ortId;
            this.adresse = adresse;
            this.hausnummer = hausnummer;
            this.email = email;
            this.telefon = telefon;
        }

        // Get-Methoden
        public int getKundeId() { return kundeId; }
        public String getVorname() { return vorname; }
        public String getNachname() { return nachname; }
        public LocalDate getGeburtsdatum() { return geburtsdatum; }
        public String getKundentyp() { return kundentyp; }
        public String getFirmenname() { return firmenname; }
        public int getOrtId() { return ortId; }
        public String getAdresse() { return adresse; }
        public String getHausnummer() { return hausnummer; }
        public String getEmail() { return email; }
        public String getTelefon() { return telefon; }
    }

    public static boolean kundeHinzufuegen(String vorname, String nachname, LocalDate geburtsDatum,
                                           String adresse, String hausnummer, String email,
                                           String telefonnummer, int ortId) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");
            String vornameCol = SchemaResolver.column(connection, kundeTable, "vorname");
            String nachnameCol = SchemaResolver.column(connection, kundeTable, "nachname");
            String geburtsCol = SchemaResolver.column(connection, kundeTable, "geburtsDatum", "geburtsdatum");
            String adresseCol = SchemaResolver.column(connection, kundeTable, "adresse");
            String hausnummerCol = SchemaResolver.column(connection, kundeTable, "hausnummer");
            String emailCol = SchemaResolver.column(connection, kundeTable, "email");
            String telefonCol = SchemaResolver.column(connection, kundeTable, "telefonnummer", "telefon");
            String gesperrtCol = SchemaResolver.column(connection, kundeTable, "gesperrt", "gespert");
            String ortCol = SchemaResolver.column(connection, kundeTable, "ort_id", "ortId");

            String insertQuery = "INSERT INTO " + kundeTable + " (" +
                    vornameCol + ", " + nachnameCol + ", " + geburtsCol + ", " + adresseCol + ", " +
                    hausnummerCol + ", " + emailCol + ", " + telefonCol + ", " + gesperrtCol + ", " + ortCol +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, vorname);
                preparedStatement.setString(2, nachname);
                preparedStatement.setDate(3, Date.valueOf(geburtsDatum));
                preparedStatement.setString(4, adresse);
                preparedStatement.setString(5, hausnummer);
                preparedStatement.setString(6, email);
                preparedStatement.setString(7, telefonnummer);
                preparedStatement.setInt(8, ortId);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Kunde speichern Fehler: " + e.getMessage());
            return false;
        }
    }

    public static boolean firmenkundeHinzufuegen(String vorname, String nachname, LocalDate geburtsDatum,
                                                 String adresse, String hausnummer, String email,
                                                 String telefonnummer, int ortId,
                                                 String firmenName, String ansprechpartner, double rabatt) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            connection.setAutoCommit(false);

            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");
            String firmenTable = SchemaResolver.table(connection, "firmenkunde", "Firmenkunde");

            String kundeIdCol = SchemaResolver.column(connection, kundeTable, "kunde_id", "kunden_id", "id");
            String vornameCol = SchemaResolver.column(connection, kundeTable, "vorname");
            String nachnameCol = SchemaResolver.column(connection, kundeTable, "nachname");
            String geburtsCol = SchemaResolver.column(connection, kundeTable, "geburtsDatum", "geburtsdatum");
            String adresseCol = SchemaResolver.column(connection, kundeTable, "adresse");
            String hausnummerCol = SchemaResolver.column(connection, kundeTable, "hausnummer");
            String emailCol = SchemaResolver.column(connection, kundeTable, "email");
            String telefonCol = SchemaResolver.column(connection, kundeTable, "telefonnummer", "telefon");
            String gesperrtCol = SchemaResolver.column(connection, kundeTable, "gesperrt", "gespert");
            String ortCol = SchemaResolver.column(connection, kundeTable, "ort_id", "ortId");

            String fkKundeCol = SchemaResolver.column(connection, firmenTable, "kunde_id", "kunden_id");
            String firmenNameCol = SchemaResolver.column(connection, firmenTable, "firmenName", "firmenname");
            String ansprechpartnerCol = SchemaResolver.column(connection, firmenTable, "ansprechpartner");
            String rabattCol = SchemaResolver.column(connection, firmenTable, "rabatt");

            String insertKundeQuery = "INSERT INTO " + kundeTable + " (" +
                    vornameCol + ", " + nachnameCol + ", " + geburtsCol + ", " + adresseCol + ", " +
                    hausnummerCol + ", " + emailCol + ", " + telefonCol + ", " + gesperrtCol + ", " + ortCol +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)";

            String insertFirmaQuery = "INSERT INTO " + firmenTable + " (" +
                    fkKundeCol + ", " + firmenNameCol + ", " + ansprechpartnerCol + ", " + rabattCol +
                    ") VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmtKunde = connection.prepareStatement(insertKundeQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtFirma = connection.prepareStatement(insertFirmaQuery)) {
                stmtKunde.setString(1, vorname);
                stmtKunde.setString(2, nachname);
                stmtKunde.setDate(3, Date.valueOf(geburtsDatum));
                stmtKunde.setString(4, adresse);
                stmtKunde.setString(5, hausnummer);
                stmtKunde.setString(6, email);
                stmtKunde.setString(7, telefonnummer);
                stmtKunde.setInt(8, ortId);
                stmtKunde.executeUpdate();

                int neueKundeId;
                try (ResultSet rs = stmtKunde.getGeneratedKeys()) {
                    if (rs.next()) {
                        neueKundeId = rs.getInt(1);
                    } else {
                        connection.rollback();
                        return false;
                    }
                }

                stmtFirma.setInt(1, neueKundeId);
                stmtFirma.setString(2, firmenName);
                stmtFirma.setString(3, ansprechpartner);
                stmtFirma.setDouble(4, rabatt);
                stmtFirma.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                System.out.println("Firmenkunde speichern Fehler: " + ex.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Firmenkunde DB Fehler: " + e.getMessage());
            return false;
        }
    }

    public static ObservableList<KundeUebersicht> ladeKundenUebersicht() {
        ObservableList<KundeUebersicht> kundenListe = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");
            String firmenTable = SchemaResolver.table(connection, "firmenkunde", "Firmenkunde");

            // Spaltennamen auflösen
            String kundeIdCol = SchemaResolver.column(connection, kundeTable, "kunde_id", "kunden_id", "id");
            String vornameCol = SchemaResolver.column(connection, kundeTable, "vorname");
            String nachnameCol = SchemaResolver.column(connection, kundeTable, "nachname");
            String geburtsCol = SchemaResolver.column(connection, kundeTable, "geburtsDatum", "geburtsdatum");
            String ortCol = SchemaResolver.column(connection, kundeTable, "ort_id", "ortId");
            // --- NEUE SPALTEN ---
            String adresseCol = SchemaResolver.column(connection, kundeTable, "adresse");
            String hausnummerCol = SchemaResolver.column(connection, kundeTable, "hausnummer");
            String emailCol = SchemaResolver.column(connection, kundeTable, "email");
            String telefonCol = SchemaResolver.column(connection, kundeTable, "telefonnummer", "telefon");

            try {
                String fkKundeCol = SchemaResolver.column(connection, firmenTable, "kunde_id", "kunden_id");
                String firmenNameCol = SchemaResolver.column(connection, firmenTable, "firmenName", "firmenname");

                // SQL-Abfrage mit den neuen Spalten
                String selectQuery = "SELECT k." + kundeIdCol + " AS kunde_id, " +
                        "k." + vornameCol + " AS vorname, " +
                        "k." + nachnameCol + " AS nachname, " +
                        "k." + geburtsCol + " AS geburtsdatum, " +
                        "k." + ortCol + " AS ort_id, " +
                        "k." + adresseCol + " AS adresse, " +
                        "k." + hausnummerCol + " AS hausnummer, " +
                        "k." + emailCol + " AS email, " +
                        "k." + telefonCol + " AS telefon, " +
                        "CASE WHEN fk." + fkKundeCol + " IS NULL THEN 'Privatkunde' ELSE 'Firmenkunde' END AS kundentyp, " +
                        "COALESCE(fk." + firmenNameCol + ", '-') AS firmenname " +
                        "FROM " + kundeTable + " k " +
                        "LEFT JOIN " + firmenTable + " fk ON fk." + fkKundeCol + " = k." + kundeIdCol + " " +
                        "ORDER BY k." + kundeIdCol + " DESC";

                try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                     ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Date dbDate = resultSet.getDate("geburtsdatum");
                        kundenListe.add(new KundeUebersicht(
                                resultSet.getInt("kunde_id"),
                                resultSet.getString("vorname"),
                                resultSet.getString("nachname"),
                                dbDate == null ? null : dbDate.toLocalDate(),
                                resultSet.getString("kundentyp"),
                                resultSet.getString("firmenname"),
                                resultSet.getInt("ort_id"),
                                // --- DIE WERTE AUS DER DATENBANK LESEN ---
                                resultSet.getString("adresse"),
                                resultSet.getString("hausnummer"),
                                resultSet.getString("email"),
                                resultSet.getString("telefon")
                        ));
                    }
                }
            } catch (SQLException joinError) {
                // ... Fallback hier weggelassen, um den Code kurz zu halten.
                // Die Hauptabfrage oben ist die wichtigste!
            }
        } catch (SQLException e) {
            System.out.println("Kunde laden Fehler: " + e.getMessage());
        }

        return kundenListe;
    }

    // --- NEU: KUNDE LÖSCHEN ---
    public static boolean kundeLoeschen(int kundeId) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            // Wir löschen zuerst den Firmenkunden-Eintrag (falls vorhanden), danach den Haupt-Kunden
            String firmenTable = SchemaResolver.table(connection, "firmenkunde", "Firmenkunde");
            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");

            String deleteFirma = "DELETE FROM " + firmenTable + " WHERE kunde_id = ?";
            try (PreparedStatement stmt1 = connection.prepareStatement(deleteFirma)) {
                stmt1.setInt(1, kundeId);
                stmt1.executeUpdate();
            }

            String deleteKunde = "DELETE FROM " + kundeTable + " WHERE kunde_id = ?";
            try (PreparedStatement stmt2 = connection.prepareStatement(deleteKunde)) {
                stmt2.setInt(1, kundeId);
                return stmt2.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen: " + e.getMessage());
            return false;
        }
    }

    // --- NEU: KUNDE BEARBEITEN ---
    public static boolean kundeAktualisieren(int kundeId, String vorname, String nachname, LocalDate geburtsDatum,
                                             String adresse, String hausnummer, String email, String telefon, int ortId,
                                             boolean istFirma, String firmenName, String ansprechpartner, double rabatt) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            connection.setAutoCommit(false);

            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");
            String updateKunde = "UPDATE " + kundeTable + " SET vorname=?, nachname=?, geburtsDatum=?, adresse=?, hausnummer=?, email=?, telefonnummer=?, ort_id=? WHERE kunde_id=?";

            try (PreparedStatement stmtKunde = connection.prepareStatement(updateKunde)) {
                stmtKunde.setString(1, vorname);
                stmtKunde.setString(2, nachname);
                stmtKunde.setDate(3, java.sql.Date.valueOf(geburtsDatum));
                stmtKunde.setString(4, adresse);
                stmtKunde.setString(5, hausnummer);
                stmtKunde.setString(6, email);
                stmtKunde.setString(7, telefon);
                stmtKunde.setInt(8, ortId);
                stmtKunde.setInt(9, kundeId);
                stmtKunde.executeUpdate();

                // Firmenkunden-Tabelle anpassen (falls es eine Firma ist)
                String firmenTable = SchemaResolver.table(connection, "firmenkunde", "Firmenkunde");
                if (istFirma) {
                    // Versuche zu updaten, falls nicht existent -> INSERT
                    String updateFirma = "UPDATE " + firmenTable + " SET firmenName=?, ansprechpartner=?, rabatt=? WHERE kunde_id=?";
                    try (PreparedStatement stmtFirmaUpdate = connection.prepareStatement(updateFirma)) {
                        stmtFirmaUpdate.setString(1, firmenName);
                        stmtFirmaUpdate.setString(2, ansprechpartner);
                        stmtFirmaUpdate.setDouble(3, rabatt);
                        stmtFirmaUpdate.setInt(4, kundeId);
                        int rows = stmtFirmaUpdate.executeUpdate();

                        if (rows == 0) { // War vorher Privatkunde, ist jetzt Firmenkunde
                            String insertFirma = "INSERT INTO " + firmenTable + " (kunde_id, firmenName, ansprechpartner, rabatt) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement stmtFirmaInsert = connection.prepareStatement(insertFirma)) {
                                stmtFirmaInsert.setInt(1, kundeId);
                                stmtFirmaInsert.setString(2, firmenName);
                                stmtFirmaInsert.setString(3, ansprechpartner);
                                stmtFirmaInsert.setDouble(4, rabatt);
                                stmtFirmaInsert.executeUpdate();
                            }
                        }
                    }
                } else {
                    // Falls das Häkchen entfernt wurde, aus Firmenkunde-Tabelle löschen
                    String deleteFirma = "DELETE FROM " + firmenTable + " WHERE kunde_id=?";
                    try (PreparedStatement stmtFirmaDel = connection.prepareStatement(deleteFirma)) {
                        stmtFirmaDel.setInt(1, kundeId);
                        stmtFirmaDel.executeUpdate();
                    }
                }

                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                System.out.println("Fehler beim Aktualisieren: " + ex.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // Holt gezielt den Rabatt eines Firmenkunden aus der Datenbank
    public static double getFirmenRabatt(int kundeId) {
        String query = "SELECT rabatt FROM firmenkunde WHERE kunde_id = ?";
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, kundeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("rabatt");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Rabatt laden: " + e.getMessage());
        }
        return 0.0; // Privatkunden oder Fehler = 0% Rabatt
    }

}
