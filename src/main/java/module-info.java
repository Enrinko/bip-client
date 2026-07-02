module com.example.cleintdontdeletefuck {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.gson;

    requires okhttp3;
    requires org.apache.poi.ooxml;

    opens client.bip.cleintdontdeletefuck;
    opens client.bip.cleintdontdeletefuck.controller;
    opens client.bip.cleintdontdeletefuck.entity;
    opens client.bip.cleintdontdeletefuck.utils;
    exports client.bip.cleintdontdeletefuck;
    exports client.bip.cleintdontdeletefuck.controller;
    exports client.bip.cleintdontdeletefuck.entity;
    exports client.bip.cleintdontdeletefuck.utils;
}