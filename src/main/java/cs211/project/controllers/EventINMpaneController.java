package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cs211.project.services.DataReceivable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EventINMpaneController {

    @FXML
    private Label eventName;

    @FXML
    private Label eventName1;  // This seems to be the role label based on the FXML you provided

    public void setData(String eventAndRole) {
        // Splitting eventAndRole, assuming it's a simple comma-separated string for simplicity
        String[] data = eventAndRole.split(",");
        String eventNameData = data[0];
        String roleData = data[1];

        eventName.setText(eventNameData);
        eventName1.setText(roleData);
    }
}
