package com.drivecheaper.dao;

import com.drivecheaper.model.Mitarbeiter;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MitarbeiterDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static ObservableList<Mitarbeiter> ladeAlleMitarbeiter() {
        ObservableList<Mitarbeiter> liste = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String table = SchemaResolver.table(connection, "mitarbeiter", "Mitarbeiter");
            String idCol = SchemaResolver.column(connection, table, "mitarbeiter_id", "mitarbeiterId", "id");
            String vornameCol = SchemaResolver.column(connection, table, "vorname");
            String nachnameCol = SchemaResolver.column(connection, table, "nachname");
            String emailCol = SchemaResolver.column(connection, table, "email");

            // HIER GEÄNDERT: "telefon" steht jetzt als Erstes!
            String telefonCol = SchemaResolver.column(connection, table, "telefon", "telefonnummer");

            String query = "SELECT " + idCol + ", " + vornameCol + ", " + nachnameCol + ", " + emailCol + ", " + telefonCol +
                    " FROM " + table + " ORDER BY " + idCol + " DESC";

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    liste.add(new Mitarbeiter(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Mitarbeiter laden Fehler: " + e.getMessage());
        }
        return liste;
    }

    public static boolean mitarbeiterHinzufuegen(String vorname, String nachname, String email, String telefonnummer) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String table = SchemaResolver.table(connection, "mitarbeiter", "Mitarbeiter");
            String vornameCol = SchemaResolver.column(connection, table, "vorname");
            String nachnameCol = SchemaResolver.column(connection, table, "nachname");
            String emailCol = SchemaResolver.column(connection, table, "email");

            // HIER GEÄNDERT
            String telefonCol = SchemaResolver.column(connection, table, "telefon", "telefonnummer");

            String query = "INSERT INTO " + table + " (" + vornameCol + ", " + nachnameCol + ", " + emailCol + ", " + telefonCol + ") VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, vorname);
                statement.setString(2, nachname);
                statement.setString(3, email);
                statement.setString(4, telefonnummer);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mitarbeiter speichern Fehler: " + e.getMessage());
            return false;
        }
    }

    public static boolean mitarbeiterBearbeiten(Mitarbeiter mitarbeiter) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String table = SchemaResolver.table(connection, "mitarbeiter", "Mitarbeiter");
            String idCol = SchemaResolver.column(connection, table, "mitarbeiter_id", "mitarbeiterId", "id");
            String vornameCol = SchemaResolver.column(connection, table, "vorname");
            String nachnameCol = SchemaResolver.column(connection, table, "nachname");
            String emailCol = SchemaResolver.column(connection, table, "email");

            // HIER GEÄNDERT
            String telefonCol = SchemaResolver.column(connection, table, "telefon", "telefonnummer");

            String query = "UPDATE " + table + " SET " + vornameCol + " = ?, " + nachnameCol + " = ?, " + emailCol + " = ?, " + telefonCol + " = ? WHERE " + idCol + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, mitarbeiter.getVorname());
                statement.setString(2, mitarbeiter.getNachname());
                statement.setString(3, mitarbeiter.getEmail());
                statement.setString(4, mitarbeiter.getTelefonnummer());
                statement.setInt(5, mitarbeiter.getMitarbeiter_id());
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mitarbeiter bearbeiten Fehler: " + e.getMessage());
            return false;
        }
    }

    public static boolean mitarbeiterLoeschen(int mitarbeiterId) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String table = SchemaResolver.table(connection, "mitarbeiter", "Mitarbeiter");
            String idCol = SchemaResolver.column(connection, table, "mitarbeiter_id", "mitarbeiterId", "id");

            String query = "DELETE FROM " + table + " WHERE " + idCol + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, mitarbeiterId);
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Mitarbeiter loeschen Fehler: " + e.getMessage());
            return false;
        }
    }
}