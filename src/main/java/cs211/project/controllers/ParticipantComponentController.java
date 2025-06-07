package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.RoleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.nio.file.InvalidPathException;

public class ParticipantComponentController {


        @FXML private Label userName;

        @FXML private Button onDetailButtonClick;


        @FXML
        public void initialize() {
        }

        public void setUsername(EventCollection eventCollection, String role, String eventId, RoleCollection roleCollection, UserAccount userAccount, AnchorPane detail , Label name, Label userNameLabel , Label email, ImageView imgUser , Button onBanButtonClick) {
            userName.setText(userAccount.getUsername());
            onDetailButtonClick.setOnAction(e->{
                    detail.setVisible(true);
                    name.setText(userAccount.getFirstName() + " " + userAccount.getLastName());
                    userNameLabel.setText(userAccount.getUsername());
                    email.setText(userAccount.getEmail());

                    String imagePath = userAccount.getImgPath();
                    try {
                            Image img = new Image("file:" + imagePath);
                            imgUser.setImage(img);
                    } catch (InvalidPathException | NullPointerException ex) {
                            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                            imgUser.setImage(new Image(String.valueOf(img)));
                    } catch (IllegalArgumentException ex) {
                            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                            imgUser.setImage(new Image(String.valueOf(img)));
                    }
                    imgUser.setVisible(true);

                    onBanButtonClick.setOnAction(ee->{
                            Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
                            Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
                            Event event = eventCollection.findEventById(eventId);
                            roleCollection.setRoleByEventIdAndUsername(userAccount.getUsername(),eventId,"BAN");
                            event.setCurrParticipants(false);
                            eventListFileDatasourceDatasource.writeData(eventCollection);
                            roleCollectionDatasource.writeData(roleCollection);

                            try {
                                    FXRouter.goTo("show-participant",eventId + "," + userAccount.getUsername() + "," + role);
                            } catch (IOException er) {
                                    throw new RuntimeException(er);
                            }
                    });
            });
        }

}

