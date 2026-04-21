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

public class HomeDAO {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String connectionURL = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static class HomeStatistik {
        private final int gesamtFahrzeuge;
        private final int verfuegbareFahrzeuge;
        private final int gesamtKunden;
        private final int gesamtFirmenkunden;
        private final int gesamtMitarbeiter;
        private final int aktiveMietvertraege;
        private final int vermieteteAutosZeitraum;
        private final double umsatzZeitraum;

        public HomeStatistik(int gesamtFahrzeuge, int verfuegbareFahrzeuge, int gesamtKunden,
                             int gesamtFirmenkunden, int gesamtMitarbeiter, int aktiveMietvertraege,
                             int vermieteteAutosZeitraum, double umsatzZeitraum) {
            this.gesamtFahrzeuge = gesamtFahrzeuge;
            this.verfuegbareFahrzeuge = verfuegbareFahrzeuge;
            this.gesamtKunden = gesamtKunden;
            this.gesamtFirmenkunden = gesamtFirmenkunden;
            this.gesamtMitarbeiter = gesamtMitarbeiter;
            this.aktiveMietvertraege = aktiveMietvertraege;
            this.vermieteteAutosZeitraum = vermieteteAutosZeitraum;
            this.umsatzZeitraum = umsatzZeitraum;
        }

        public int getGesamtFahrzeuge() {
            return gesamtFahrzeuge;
        }

        public int getVerfuegbareFahrzeuge() {
            return verfuegbareFahrzeuge;
        }

        public int getGesamtKunden() {
            return gesamtKunden;
        }

        public int getGesamtFirmenkunden() {
            return gesamtFirmenkunden;
        }

        public int getGesamtMitarbeiter() {
            return gesamtMitarbeiter;
        }

        public int getAktiveMietvertraege() {
            return aktiveMietvertraege;
        }

        public int getVermieteteAutosZeitraum() {
            return vermieteteAutosZeitraum;
        }

        public double getUmsatzZeitraum() {
            return umsatzZeitraum;
        }
    }

    public static class BerichtEintrag {
        private final String titel;
        private final String wert;
        private final String details;

        public BerichtEintrag(String titel, String wert, String details) {
            this.titel = titel;
            this.wert = wert;
            this.details = details;
        }

        public String getTitel() {
            return titel;
        }

        public String getWert() {
            return wert;
        }

        public String getDetails() {
            return details;
        }
    }

    public static HomeStatistik ladeHomeStatistik(LocalDate von, LocalDate bis) {
        try (Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            String fahrzeugTable = SchemaResolver.table(connection, "fahrzeug", "Fahrzeug");
            String kundeTable = SchemaResolver.table(connection, "kunde", "Kunde");
            String firmenkundeTable = SchemaResolver.table(connection, "firmenkunde", "Firmenkunde");
            String mitarbeiterTable = SchemaResolver.table(connection, "mitarbeiter", "Mitarbeiter");
            String mietvertragTable = SchemaResolver.table(connection, "mietvertrag", "Mietvertrag");
            String statusCol = SchemaResolver.column(connection, fahrzeugTable, "status");
            String kostenCol = SchemaResolver.column(connection, mietvertragTable, "gesamtPreis", "gesamtkosten", "summe");
            String datumCol = SchemaResolver.column(connection, mietvertragTable, "mietBeginn", "startdatum", "startDatum");
            String fahrzeugIdCol = SchemaResolver.column(connection, mietvertragTable, "fahrzeug_id", "fahrzeugId", "fahrzeugid");

            int gesamtFahrzeuge = count(connection, "SELECT COUNT(*) FROM " + fahrzeugTable);
            int verfuegbareFahrzeuge = count(connection, "SELECT COUNT(*) FROM " + fahrzeugTable + " WHERE " + statusCol + " = 1");
            int gesamtKunden = count(connection, "SELECT COUNT(*) FROM " + kundeTable);
            int gesamtFirmenkunden = count(connection, "SELECT COUNT(*) FROM " + firmenkundeTable);
            int gesamtMitarbeiter = count(connection, "SELECT COUNT(*) FROM " + mitarbeiterTable);

            QueryPart zeitraum = buildZeitraumFilter(datumCol, von, bis);
            int aktiveMietvertraege = count(connection,
                    "SELECT COUNT(*) FROM " + mietvertragTable + zeitraum.sql, zeitraum.von, zeitraum.bis);
            int vermieteteAutos = count(connection,
                    "SELECT COUNT(DISTINCT " + fahrzeugIdCol + ") FROM " + mietvertragTable + zeitraum.sql, zeitraum.von, zeitraum.bis);
            double umsatz = sum(connection,
                    "SELECT COALESCE(SUM(" + kostenCol + "), 0) FROM " + mietvertragTable + zeitraum.sql, zeitraum.von, zeitraum.bis);

            return new HomeStatistik(
                    gesamtFahrzeuge, verfuegbareFahrzeuge, gesamtKunden, gesamtFirmenkunden,
                    gesamtMitarbeiter, aktiveMietvertraege, vermieteteAutos, umsatz
            );
        } catch (SQLException e) {
            System.out.println("HomeDAO Fehler: " + e.getMessage());
            return new HomeStatistik(0, 0, 0, 0, 0, 0, 0, 0.0);
        }
    }

    public static ObservableList<BerichtEintrag> ladeBerichte(LocalDate von, LocalDate bis) {
        HomeStatistik statistik = ladeHomeStatistik(von, bis);
        ObservableList<BerichtEintrag> liste = FXCollections.observableArrayList();

        liste.add(new BerichtEintrag(
                "Autos vermietet (Zeitraum)",
                String.valueOf(statistik.getVermieteteAutosZeitraum()),
                "Anzahl unterschiedlicher Fahrzeuge mit Mietvertrag im gewaehlten Zeitraum."
        ));
        liste.add(new BerichtEintrag(
                "Umsatz (Zeitraum)",
                String.format("%.2f EUR", statistik.getUmsatzZeitraum()),
                "Gesamterloes aus Mietvertraegen im gewaehlten Zeitraum."
        ));
        liste.add(new BerichtEintrag(
                "Mietvertraege (Zeitraum)",
                String.valueOf(statistik.getAktiveMietvertraege()),
                "Anzahl Mietvertraege im gewaehlten Zeitraum."
        ));
        liste.add(new BerichtEintrag(
                "Auslastung Flotte",
                statistik.getGesamtFahrzeuge() == 0 ? "0%" :
                        (100 - (statistik.getVerfuegbareFahrzeuge() * 100 / statistik.getGesamtFahrzeuge())) + "%",
                "Anteil nicht verfuegbarer Fahrzeuge am Gesamtbestand."
        ));

        return liste;
    }

    private static QueryPart buildZeitraumFilter(String dateCol, LocalDate von, LocalDate bis) {
        if (von == null || bis == null) {
            return new QueryPart("", null, null);
        }
        return new QueryPart(" WHERE " + dateCol + " BETWEEN ? AND ?", von, bis);
    }

    private static int count(Connection connection, String query) throws SQLException {
        return count(connection, query, null, null);
    }

    private static int count(Connection connection, String query, LocalDate von, LocalDate bis) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            bindRange(statement, von, bis);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    private static double sum(Connection connection, String query, LocalDate von, LocalDate bis) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            bindRange(statement, von, bis);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getDouble(1) : 0.0;
            }
        }
    }

    private static void bindRange(PreparedStatement statement, LocalDate von, LocalDate bis) throws SQLException {
        if (von != null && bis != null) {
            statement.setDate(1, Date.valueOf(von));
            statement.setDate(2, Date.valueOf(bis));
        }
    }

    private static class QueryPart {
        private final String sql;
        private final LocalDate von;
        private final LocalDate bis;

        private QueryPart(String sql, LocalDate von, LocalDate bis) {
            this.sql = sql;
            this.von = von;
            this.bis = bis;
        }
    }
}
