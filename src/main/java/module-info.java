module com.drivecheaper.drivecheaper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.drivecheaper to javafx.fxml;
    exports com.drivecheaper;
    exports com.drivecheaper.Fahrzeuge;
    opens com.drivecheaper.Fahrzeuge to javafx.fxml;
}