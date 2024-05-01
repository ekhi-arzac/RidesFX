module eus.ehu.ridesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires com.google.gson;
    requires java.net.http;
    requires jdk.compiler;

    opens eus.ehu.ridesfx.domain to org.hibernate.orm.core, javafx.base;
    opens eus.ehu.ridesfx.uicontrollers to javafx.fxml, com.google.gson;
    opens eus.ehu.ridesfx.ui to javafx.fxml;

    exports eus.ehu.ridesfx.ui;

}