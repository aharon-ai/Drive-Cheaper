# 🚗 DriveCheaper - Autovermietung Management System

Ein modernes, benutzerfreundliches und ausfallsicheres Desktop-Programm zur vollständigen Verwaltung einer Autovermietung. Entwickelt mit **Java**, **JavaFX** und **MySQL**.

<img width="280" height="180" alt="logo" src="https://github.com/user-attachments/assets/d88fbc32-79d2-4f3d-abe8-49389876c5a8" />


## ✨ Kernfunktionen

Das System bietet eine vollständige MVC-Architektur und smarte Automatisierungen im Hintergrund, die Fehler vermeiden und den Mitarbeitern Arbeit abnehmen:

* 📋 **Intelligentes Vertrags-Management:**
    * Automatische Berechnung von Miettagen, Tageskosten und Endpreisen.
    * Automatische Integration von Firmenkunden-Rabatten in die Preisberechnung.
    * Klarnamen-Anzeige in den Tabellen statt kryptischer IDs (mit verknüpften Dropdown-Menüs).
* 🚗 **Automatischer Fahrzeug-Status:**
    * Wird ein Vertrag geschlossen, springt das Fahrzeug automatisch auf "❌ Vermietet" und verschwindet aus der Auswahl für neue Verträge.
    * Wird das Auto zurückgegeben (oder der Vertrag gelöscht), ist es sofort wieder "✅ Verfügbar".
* 🔍 **Echtzeit Live-Suche (FilteredList):**
    * Pfeilschnelle, verzögerungsfreie Filterung aller Tabellen (Kunden, Fahrzeuge, Mitarbeiter, Verträge) direkt beim Tippen.
* 👥 **Kundenverwaltung:** Unterscheidung zwischen Privatkunden und Firmenkunden (inkl. Rabattsystem).
* 🛡️ **Fail-Safe Design:** * Fehlervermeidung durch Dropdown-Auswahl statt Texteingabe.
    * Sichere Datenbank-Transaktionen (`commit` & `rollback`), damit bei Abstürzen keine halben Daten gespeichert werden.

## 🛠️ Verwendete Technologien

* **Programmiersprache:** Java (JDK 21)
* **Benutzeroberfläche:** JavaFX (FXML & Scene Builder)
* **Datenbank:** MySQL 
* **Datenbank-Verbindung:** JDBC
* **Umgebungsvariablen:** `dotenv-java` (für sichere Datenbank-Zugangsdaten)
* **Architektur:** Model-View-Controller (MVC) & Data Access Objects (DAO)

## 🗂️ Projektstruktur

Das Projekt folgt strikt dem **MVC-Pattern**:
* `com.drivecheaper.model`: Java-Klassen (Fahrzeug, Kunde, Mitarbeiter etc.)
* `com.drivecheaper.dao`: Datenbankzugriffe (SQL-Abfragen, Schema-Auflösung)
* `com.drivecheaper.controllers`: JavaFX Controller-Klassen mit der Anwendungslogik
* `resources/com/drivecheaper/`: FXML-Dateien für das grafische Interface

## 🚀 Installation & Setup

1. **Repository klonen:**
   ```bash
   git clone https://github.com/aharon-ai/Drive-Cheaper

### 2. Datenbank einrichten
Erstelle eine MySQL-Datenbank und führe die benötigten `CREATE TABLE` Skripte für Fahrzeuge, Kunden, Firmenkunden, Mitarbeiter und Mietverträge aus.

### 3. Umgebungsvariablen (.env) konfigurieren
Erstelle im Hauptverzeichnis des Projekts eine Datei namens `.env` (ohne Dateiendung) und trage deine Datenbank-Zugangsdaten ein:

```env
DB_URL=jdbc:mysql://localhost:3306/DEINE_DATENBANK
DB_USER=dein_benutzername
DB_PASSWORD=dein_passwort
```

### 4. Starten
Starte die Anwendung über deine IDE (z.B. IntelliJ IDEA oder Eclipse) oder via Maven/Gradle.
