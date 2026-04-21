package com.drivecheaper.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MietvertragDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static class MietvertragUebersicht {
        private final int mietvertragId;
        private final int kundeId;
        private final int fahrzeugId;
        private final int mitarbeiterId;
        private final LocalDate startdatum;
        private final LocalDate endedatum;
        private final double gesamtkosten;

        public MietvertragUebersicht(int mietvertragId, int kundeId, int fahrzeugId, int mitarbeiterId,
                                     LocalDate startdatum, LocalDate endedatum, double gesamtkosten) {
            this.mietvertragId = mietvertragId;
            this.kundeId = kundeId;
            this.fahrzeugId = fahrzeugId;
            this.mitarbeiterId = mitarbeiterId;
            this.startdatum = startdatum;
            this.endedatum = endedatum;
            this.gesamtkosten = gesamtkosten;
        }

        public int getMietvertragId() {
            return mietvertragId;
        }

        public int getKundeId() {
            return kundeId;
        }

        public int getFahrzeugId() {
            return fahrzeugId;
        }

        public int getMitarbeiterId() {
            return mitarbeiterId;
        }

        public LocalDate getStartdatum() {
            return startdatum;
        }

        public LocalDate getEndedatum() {
            return endedatum;
        }

        public double getGesamtkosten() {
            return gesamtkosten;
        }
    }

    public static ObservableList<MietvertragUebersicht> ladeAlleMietvertraege() {
        ObservableList<MietvertragUebersicht> liste = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            Names n = names(connection);
            String query = "SELECT " + n.idCol + ", " + n.kundeIdCol + ", " + n.fahrzeugIdCol + ", " +
                    n.mitarbeiterIdCol + ", " + n.startCol + ", " + n.endeCol + ", " + n.kostenCol +
                    " FROM " + n.table + " ORDER BY " + n.idCol + " DESC";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Date start = resultSet.getDate(5);
                    Date ende = resultSet.getDate(6);
                    liste.add(new MietvertragUebersicht(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getInt(4),
                            start == null ? null : start.toLocalDate(),
                            ende == null ? null : ende.toLocalDate(),
                            resultSet.getDouble(7)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Mietvertrag laden Fehler: " + e.getMessage());
        }
        return liste;
    }

    public static boolean mietvertragHinzufuegen(int kundeId, int fahrzeugId, int mitarbeiterId,
                                                 LocalDate startdatum, LocalDate endedatum, double gesamtkosten) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            Names n = names(connection);
            String query = "INSERT INTO " + n.table + " (" + n.kundeIdCol + ", " + n.fahrzeugIdCol + ", " +
                    n.mitarbeiterIdCol + ", " + n.startCol + ", " + n.endeCol + ", " + n.kostenCol +
                    ") VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, kundeId);
                statement.setInt(2, fahrzeugId);
                statement.setInt(3, mitarbeiterId);
                statement.setDate(4, Date.valueOf(startdatum));
                statement.setDate(5, Date.valueOf(endedatum));
                statement.setDouble(6, gesamtkosten);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mietvertrag speichern Fehler: " + e.getMessage());
            return false;
        }
    }

    public static boolean mietvertragBearbeiten(int mietvertragId, int kundeId, int fahrzeugId, int mitarbeiterId,
                                                LocalDate startdatum, LocalDate endedatum, double gesamtkosten) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            Names n = names(connection);
            String query = "UPDATE " + n.table + " SET " +
                    n.kundeIdCol + " = ?, " + n.fahrzeugIdCol + " = ?, " + n.mitarbeiterIdCol + " = ?, " +
                    n.startCol + " = ?, " + n.endeCol + " = ?, " + n.kostenCol + " = ? WHERE " + n.idCol + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, kundeId);
                statement.setInt(2, fahrzeugId);
                statement.setInt(3, mitarbeiterId);
                statement.setDate(4, Date.valueOf(startdatum));
                statement.setDate(5, Date.valueOf(endedatum));
                statement.setDouble(6, gesamtkosten);
                statement.setInt(7, mietvertragId);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mietvertrag bearbeiten Fehler: " + e.getMessage());
            return false;
        }
    }

    public static boolean mietvertragLoeschen(int mietvertragId) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            Names n = names(connection);
            String query = "DELETE FROM " + n.table + " WHERE " + n.idCol + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, mietvertragId);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mietvertrag loeschen Fehler: " + e.getMessage());
            return false;
        }
    }

    private static Names names(Connection connection) throws SQLException {
        String table = SchemaResolver.table(connection, "mietvertrag", "Mietvertrag");
        String idCol = SchemaResolver.column(connection, table, "mietvertrag_id", "mietvertragId", "id");
        String kundeIdCol = SchemaResolver.column(connection, table, "kunde_id", "kunden_id", "kundeId");
        String fahrzeugIdCol = SchemaResolver.column(connection, table, "fahrzeug_id", "fahrzeugId");
        String mitarbeiterIdCol = SchemaResolver.column(connection, table, "mitarbeiter_id", "mitarbeiterId");
        String startCol = SchemaResolver.column(connection, table, "mietBeginn", "startdatum", "startDatum");
        String endeCol = SchemaResolver.column(connection, table, "mietEnde", "endedatum", "endDatum");
        String kostenCol = SchemaResolver.column(connection, table, "gesamtPreis", "gesamtkosten", "summe");

        return new Names(table, idCol, kundeIdCol, fahrzeugIdCol, mitarbeiterIdCol, startCol, endeCol, kostenCol);
    }

    private static class Names {
        private final String table;
        private final String idCol;
        private final String kundeIdCol;
        private final String fahrzeugIdCol;
        private final String mitarbeiterIdCol;
        private final String startCol;
        private final String endeCol;
        private final String kostenCol;

        private Names(String table, String idCol, String kundeIdCol, String fahrzeugIdCol,
                      String mitarbeiterIdCol, String startCol, String endeCol, String kostenCol) {
            this.table = table;
            this.idCol = idCol;
            this.kundeIdCol = kundeIdCol;
            this.fahrzeugIdCol = fahrzeugIdCol;
            this.mitarbeiterIdCol = mitarbeiterIdCol;
            this.startCol = startCol;
            this.endeCol = endeCol;
            this.kostenCol = kostenCol;
        }
    }
}
