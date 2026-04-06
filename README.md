# 🚗 DriveCheaper - Fahrzeugvermietungssystem

**DriveCheaper** ist eine moderne, datenbankgestützte Desktop-Applikation zur effizienten Verwaltung des Fuhrparks einer Autovermietung. Die Software wurde entwickelt, um interne Geschäftsprozesse wie die Fahrzeugverwaltung, Kundenbetreuung und die Abwicklung von Mietverträgen zu digitalisieren und zentral abzubilden.

## ✨ Features
* **Fahrzeugverwaltung (CRUD):** Anlage, Bearbeitung und Löschung unterschiedlicher Fahrzeugtypen (PKW, LKW, Motorrad).
* **Dynamische Statusanzeige:** Sofortige visuelle Rückmeldung, ob ein Fahrzeug aktuell verfügbar oder vermietet ist.
* **Kundenmanagement:** Getrennte Erfassung von Privat- und Firmenkunden (inklusive automatischer Rabattberechnung).
* **Mietvertragsabwicklung:** Zentrale Verknüpfung von Mietern, Fahrzeugen und verantwortlichen Mitarbeitern zur Berechnung von Mietdauer und Gesamtkosten.
* **Strikte Eingabevalidierung:** Integrierte Fehlermeldungen (Alerts) zur Vermeidung von Fehleingaben oder Systemabstürzen.

## 🛠️ Tech Stack
* **Frontend:** JavaFX (FXML) für eine strikte Trennung von Layout und Logik.
* **Backend:** Java (Objektorientiert mit Fokus auf Vererbung und Typsicherheit).
* **Datenbank:** MySQL (Anbindung über die JDBC-Schnittstelle).
* **Architektur:** MVC-Muster (Model-View-Controller) & DAO-Pattern (Data Access Object).

## 🚀 Projektstruktur
Das Projekt wurde nach Clean-Code-Prinzipien strukturiert und nutzt das Data Access Object (DAO) Pattern, um die direkte SQL-Kommunikation vollständig von der grafischen Benutzeroberfläche (Controller) zu trennen. Tabellenstrukturen und Testdaten lassen sich über integrierte DDL-Skripte initialisieren.
