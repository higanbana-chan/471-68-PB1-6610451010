package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventComponentController {
    @FXML
    private Label eventDetailLabel;

    @FXML
    private Label eventNameLabel;

    @FXML
    private Label eventStartEndLabel;

    @FXML
    private Label participantLabel;

    @FXML
    private Button viewDetailButton;

    @FXML
    private Button joinButton;
    private LocalDate currentDate;

    public void loadEventComponents(EventCollection eventCollection, TeamList teamList, RoleCollection roleCollection, Event event, UserAccount userAccount, AnchorPane confirm, Label eventNameConfirm, ImageView eventImage, Button joinEventButton, Button joinTeamButton) {
        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        // get current date
        Date date = new Date();
        Instant instant = date.toInstant();
        currentDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // parse end date from String inside csv to LocalDate
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedEndDate = event.getEnd();
        LocalDate endDate = LocalDate.parse(formattedEndDate, dtf);

        eventNameLabel.setText(event.getName());
        eventDetailLabel.setText(event.getDetail());
        eventStartEndLabel.setText("From: " + event.getStart() + " - " + event.getEnd());
        participantLabel.setText(event.getCurrParticipants() + "/" + event.getMaxParticipants());

        if (currentDate.compareTo(endDate) > 0) {
            joinButton.setDisable(true);
            joinButton.setStyle("-fx-opacity: 1;" + "-fx-background-color: #E8E8E8;");
            joinButton.setText("ENDED");
        } else {
            if (roleCollection.isUserJoinTeam(userAccount.getUsername(), event.getId(), teamList)) {
                if (roleCollection.isUserTeamBan(userAccount.getUsername(),event.getId() ,teamList)){
                    joinButton.setText("BANNED");
                }else {
                    joinButton.setText("JOINED");
                }
                joinButton.setDisable(true);
                joinButton.setStyle("-fx-background-color: #F28622;");
            }
            if (event.isUserJoin(userAccount.getUsername()) || roleCollection.findUserBan(userAccount.getUsername(),event.getId())) {
                if (roleCollection.findUserBan(userAccount.getUsername(),event.getId())){
                    joinButton.setText("BANNED");

                }else {
                    joinButton.setText("JOINED");
                }
                joinButton.setDisable(true);

            } else {
                if (event.getCurrParticipants() >= event.getMaxParticipants()) {
                    joinButton.setDisable(true);
                    joinButton.setStyle("-fx-opacity: 1;" + "-fx-background-color: #EC4B4B;");
                    joinButton.setText("FULL");
                }
            }
        }

        List<Object> obj = new ArrayList<>();
        obj.add(userAccount);
        obj.add(event);
        viewDetailButton.setOnAction((e) -> {
            try {
                FXRouter.goTo("event-detail", obj);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        joinButton.setOnAction((e) -> {
            confirm.setVisible(true);
            eventNameConfirm.setText(event.getName());
            Image poster = new Image("file:" + event.getPoster());
            eventImage.setImage(poster);

            joinEventButton.setOnAction((ex) -> {

                Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
                Event event1 = eventCollection.findEventById(event.getId());
                event1.setCurrParticipants(true);
                eventListFileDatasourceDatasource.writeData(eventCollection);
                roleCollection.addRole(userAccount.getUsername(), event.getId(), "Participant");
                roleCollectionDatasource.writeData(roleCollection);
                try {
                    FXRouter.goTo("join-events-page", userAccount);
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            });

            joinTeamButton.setOnAction((ex) -> {
                try {
                    FXRouter.goTo("join-teams-page", userAccount.getUsername() + "," + event.getId());
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            });
        });
    }
}
