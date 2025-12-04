module com.etc.pdsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires itextpdf;
    requires java.naming;
    requires java.desktop;
    requires controlsfx;
    requires mail;
    requires activation;
    requires jbcrypt;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires org.apache.poi.ooxml;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.javafx;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.poi.poi;


    opens com.etc.pdsapp.controller to javafx.fxml;
    opens screens to javafx.fxml;
    exports com.etc.pdsapp.controller;
    exports com.etc.pdsapp.model;




}
