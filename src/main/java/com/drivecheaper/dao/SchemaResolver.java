package com.drivecheaper.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

final class SchemaResolver {
    private SchemaResolver() {
    }

    static String table(Connection connection, String... candidates) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        for (String candidate : candidates) {
            if (existsTable(meta, candidate)) {
                return candidate;
            }
        }
        return candidates[0];
    }

    static String column(Connection connection, String table, String... candidates) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        for (String candidate : candidates) {
            if (existsColumn(meta, table, candidate)) {
                return candidate;
            }
        }
        return candidates[0];
    }

    private static boolean existsTable(DatabaseMetaData meta, String table) throws SQLException {
        try (ResultSet rs = meta.getTables(null, null, table, null)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = meta.getTables(null, null, table.toLowerCase(), null)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = meta.getTables(null, null, table.toUpperCase(), null)) {
            return rs.next();
        }
    }

    private static boolean existsColumn(DatabaseMetaData meta, String table, String column) throws SQLException {
        try (ResultSet rs = meta.getColumns(null, null, table, column)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = meta.getColumns(null, null, table.toLowerCase(), column.toLowerCase())) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = meta.getColumns(null, null, table.toUpperCase(), column.toUpperCase())) {
            return rs.next();
        }
    }
}
