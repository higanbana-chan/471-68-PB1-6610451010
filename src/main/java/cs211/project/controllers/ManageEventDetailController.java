package cs211.project.controllers;

import cs211.project.models.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ManageEventDetailController {
    @FXML
    private Label endLabel;

    @FXML
    private Label eventDetailLabel;

    @FXML
    private Label eventLocationLabel;

    @FXML
    private Label eventNameLabel;

    @FXML
    private ImageView eventPosterImageView;

    @FXML
    private Label eventTypeLabel;

    @FXML
    private Label participantLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Button backButton;

    public void setData(Event event, Stage alert) {
        eventNameLabel.setText(event.getName());
        eventTypeLabel.setText(event.getType());
        eventLocationLabel.setText(event.getLocation());
        eventDetailLabel.setText(event.getDetail());
        startLabel.setText(event.getStart());
        endLabel.setText(event.getEnd());
        participantLabel.setText(event.getCurrParticipants() + "/" + event.getMaxParticipants());
        Image poster = new Image("file:" + event.getPoster());
        eventPosterImageView.setImage(poster);

        backButton.setOnAction(e -> {
            alert.close();
        });
    }
}
