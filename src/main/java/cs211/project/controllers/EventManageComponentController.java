package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EventManageComponentController {
    @FXML
    private Label dateLabel;

    @FXML
    private Label eventNameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label teamNameLabel;

    @FXML
    private Button detailButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Button editButton;

    @FXML
    private HBox eventManageBox;

    @FXML
    private HBox buttonBox;

    public void setData(Event event, Stage alert, String role, String username) {
        eventNameLabel.setText(event.getName());
        dateLabel.setText(event.getStart());

        if (role.equals("Organizer")) {
            teamNameLabel.setVisible(false);
            roleLabel.setText("Organizer");

            editButton.setOnAction((e) -> {
                try {
                    FXRouter.goTo("edit-event-manager", event.getId() + "," + username);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } else if (role.equals("Participant")) {
            teamNameLabel.setVisible(false);
            roleLabel.setText("Participant");

            buttonBox.getChildren().remove(editButton);
        }

        detailButton.setOnAction((e) -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-manage-detail.fxml"));
                VBox eventDetail = fxmlLoader.load();
                ManageEventDetailController manageEventDetailController = fxmlLoader.getController();
                manageEventDetailController.setData(event, alert);
                Scene alertScene = new Scene(eventDetail, 1040, 600);
                alert.setScene(alertScene);
                alert.showAndWait();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        scheduleButton.setOnAction((e) -> {
            try {
                FXRouter.goTo("event-schedule-manager", event.getId() + "," + username + "," + role);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setDataTeam(Event event, Team team, Stage alert, String role, String username) {
        eventNameLabel.setText(event.getName());
        dateLabel.setText(event.getStart());
        teamNameLabel.setText(team.getTeamName());
        buttonBox.getChildren().remove(editButton);
        roleLabel.setText(role);

        if (role.equals("Member")) {
            teamNameLabel.setVisible(false);

            scheduleButton.setOnAction((e) -> {
                try {
                    FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } else {
            scheduleButton.setOnAction((e) -> {
                try {
                    FXRouter.goTo("event-schedule-manager", event.getId() + "," + username + "," + role);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        detailButton.setOnAction((e) -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-manage-detail.fxml"));
                VBox eventDetail = fxmlLoader.load();
                ManageEventDetailController manageEventDetailController = fxmlLoader.getController();
                manageEventDetailController.setData(event, alert);
                Scene alertScene = new Scene(eventDetail, 1040, 600);
                alert.setScene(alertScene);
                alert.showAndWait();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
