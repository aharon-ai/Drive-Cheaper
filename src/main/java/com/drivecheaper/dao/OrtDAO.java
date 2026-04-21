package com.drivecheaper.dao;

import com.drivecheaper.model.Ort;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrtDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static ObservableList<Ort> ladeAlleOrte() {
        ObservableList<Ort> liste = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String ortTable = SchemaResolver.table(connection, "ort", "Ort");
            String ortIdCol = SchemaResolver.column(connection, ortTable, "ort_id", "ortId", "id");
            String plzCol = SchemaResolver.column(connection, ortTable, "plz", "PLZ", "postleitzahl");
            String ortNameCol = SchemaResolver.column(connection, ortTable, "ort", "ortsname", "stadt", "name");

            String query = "SELECT " + ortIdCol + ", " + plzCol + ", " + ortNameCol + " FROM " + ortTable + " ORDER BY " + plzCol;
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    liste.add(new Ort(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("OrtDAO Fehler: " + e.getMessage());
        }

        return liste;
    }
}
