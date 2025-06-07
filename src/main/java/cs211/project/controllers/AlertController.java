package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertController {
    @FXML
    private Button closeButton;

    @FXML
    private ImageView iconImage;

    @FXML
    private Label warningLabel;

    public void setData(Stage alert, String status, String text) {
        if (status.equals("success")) {
            Image image = new Image(getClass().getResource("/cs211/project/views/img/success.gif").toString());
            iconImage.setImage(image);
            warningLabel.setText(text);
            closeButton.setStyle("-fx-background-color: #00d642;");
            closeButton.setText("OK");
        }

        closeButton.setOnAction(e -> {
            alert.close();
        });
    }
}
