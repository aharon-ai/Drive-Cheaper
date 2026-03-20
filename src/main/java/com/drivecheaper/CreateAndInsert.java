package com.drivecheaper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAndInsert {

    public static void createAndInsert() {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        try {
            // Verbindung herstellen
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    "root",
                    "user1234"
            );

            statement = connection.createStatement();

            // Datenbank und Tabelle erstellen
            statement.executeUpdate("DROP DATABASE IF EXISTS firma2");
            statement.executeUpdate("CREATE DATABASE firma2");
            statement.executeUpdate("USE firma2");

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS personen (
                    name VARCHAR(30),
                    vorname VARCHAR(25),
                    personalnummer INT,
                    gehalt DOUBLE,
                    geburtstag DATE,
                    PRIMARY KEY (personalnummer)
                ) ENGINE=InnoDB DEFAULT CHARSET=UTF8
            """);

            String insertQuery = """
                INSERT INTO personen (name, vorname, personalnummer, gehalt, geburtstag)
                VALUES (?, ?, ?, ?, ?)
            """;

            // Transaktion starten
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertQuery);

            try {
                // Datensätze einfügen
                preparedStatement.setString(1, "Maier");
                preparedStatement.setString(2, "Alex");
                preparedStatement.setInt(3, 6714);
                preparedStatement.setDouble(4, 3500);
                preparedStatement.setDate(5, java.sql.Date.valueOf("1962-03-15"));
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Schmitz");
                preparedStatement.setString(2, "Peter");
                preparedStatement.setInt(3, 7777);
                preparedStatement.setDouble(4, 3750);
                preparedStatement.setDate(5, java.sql.Date.valueOf("1958-04-12"));
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Acilles");
                preparedStatement.setString(2, "Albrecht");
                preparedStatement.setInt(3, 8888);
                preparedStatement.setDouble(4, 8888.8);
                preparedStatement.setDate(5, java.sql.Date.valueOf("1988-08-08"));
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Meterns");
                preparedStatement.setString(2, "Julia");
                preparedStatement.setInt(3, 9787);
                preparedStatement.setDouble(4, 3621.5);
                preparedStatement.setDate(5, java.sql.Date.valueOf("1959-12-30"));
                preparedStatement.executeUpdate();

                // Commit
                connection.commit();
                System.out.println("Transaktion erfolgreich durchgeführt.");

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Fehler beim Einfügen, Rollback: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Fehler bei Verbindung oder Erstellung: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        createAndInsert();
    }
}