module cs211.project.cs211661project {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires org.apache.commons.lang3;
    requires java.datatransfer;
    requires java.desktop;

    opens cs211.project.cs211661project to javafx.fxml;
    exports cs211.project.cs211661project;
    exports cs211.project.controllers;
    opens cs211.project.controllers to javafx.fxml;

    exports cs211.project.models;
    opens cs211.project.models to java.base;
    exports cs211.project.models.collections;
    opens cs211.project.models.collections to java.base;
}