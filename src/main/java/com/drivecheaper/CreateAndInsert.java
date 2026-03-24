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
            statement.executeUpdate("DROP DATABASE IF EXISTS drivecheaper");
            statement.executeUpdate("CREATE DATABASE drivecheaper");
            statement.executeUpdate("USE drivecheaper");

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS fahrzeug (
                    fahrzeug_id INT NOT NULL AUTO_INCREMENT,
                    status BOOLEAN,
                    hersteller VARCHAR(50),                    
                    modell VARCHAR(50),
                   kennzeichen VARCHAR(30),
                    PRIMARY KEY (fahrzeug_id)
                ) ENGINE=InnoDB DEFAULT CHARSET=UTF8
            """);

            String insertQuery = """
                INSERT INTO fahrzeug (fahrzeug_id, status,  hersteller, modell, kennzeichen)
                VALUES (?, ?, ?, ?, ?)
            """;

            // Transaktion starten
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(insertQuery);

            try {
                // Datensätze einfügen
                preparedStatement.setInt(1, 1);
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "VW");
                preparedStatement.setString(4, "Golf");
                preparedStatement.setString(5, "K-AB 123");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 2);
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "Mercedes");
                preparedStatement.setString(4, "E-Klass");
                preparedStatement.setString(5, "K-BC 456");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 3);
                preparedStatement.setBoolean(2, false);
                preparedStatement.setString(3, "BMW");
                preparedStatement.setString(4, "X5");
                preparedStatement.setString(5, "K-CD 404");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 4);
                preparedStatement.setBoolean(2, true);
                preparedStatement.setString(3, "Toyota");
                preparedStatement.setString(4, "Verso");
                preparedStatement.setString(5, "K-CD 444");
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