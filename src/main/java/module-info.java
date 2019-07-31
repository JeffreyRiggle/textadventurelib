module ilusr.textadventurelib {
    requires java.base;
    requires java.xml;
    requires java.scripting;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.fxml;
    requires javafx.web;
    requires ilusr.core;
    requires ilusr.logrunner;
    requires ilusr.persistencelib;
    requires ilusr.gamestatemanager;
    requires ilusr.playerlib;

    exports textadventurelib.actions;
    exports textadventurelib.core;
    exports textadventurelib.gamestates;
    exports textadventurelib.layout;
    exports textadventurelib.macro;
    exports textadventurelib.options;
    exports textadventurelib.persistence;
    exports textadventurelib.timers;
    exports textadventurelib.triggers;
}