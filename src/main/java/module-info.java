module ridesfx {
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

    opens eus.ehu.ridesfx.domain to org.hibernate.orm.core;
    opens eus.ehu.ridesfx.uicontrollers to javafx.fxml, com.google.gson;
    opens eus.ehu.ridesfx.ui to javafx.fxml;

    exports eus.ehu.ridesfx.ui;
    exports eus.ehu.ridesfx.businessLogic;
    exports eus.ehu.ridesfx.dataAccess;
    exports eus.ehu.ridesfx.domain;
    exports eus.ehu.ridesfx.exceptions;
    exports eus.ehu.ridesfx.msgClient;
    exports eus.ehu.ridesfx.uicontrollers;
    exports eus.ehu.ridesfx.utils;
    exports eus.ehu.ridesfx.configuration;

}