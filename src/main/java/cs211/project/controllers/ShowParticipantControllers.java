package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.Team;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.EventScheduleList;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ShowParticipantControllers {
    @FXML
    private Label accountNameLabel;

    @FXML
    private Pane blurPane;

    @FXML
    private Label emailLabel;

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private VBox showParticipantVbox;

    @FXML
    private ImageView userImageView;

    @FXML
    private Label usernameLabel;

    @FXML private Button onBanButtonClick;


    String[] data = ((String) FXRouter.getData()).split(",");
    String eventId = data[0];
    String username = data[1];
    String role = data[2];

    private UserAccount user;
    private Event event;
    private EventCollection events;


    public void initialize(){

        detailAnchorPane.setVisible(false);
        blurPane.setVisible(false);


        Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();
        user = accountCollection.findAccount(username);

        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        RoleCollection roleCollection = roleCollectionDatasource.readData();

        events.addUserToEvent(accountCollection,roleCollection);
        event = events.findEventById(eventId);

        for (UserAccount user : event.getParticipantList().getUserAccounts()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/participant-component.fxml"));
                    Pane participantComponent = fxmlLoader.load();
                    ParticipantComponentController participantComponentController = fxmlLoader.getController();
                    participantComponentController.setUsername(events,role,eventId,roleCollection,user,detailAnchorPane,accountNameLabel,usernameLabel,emailLabel,userImageView,onBanButtonClick);
                    showParticipantVbox.getChildren().add(participantComponent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
    }

    @FXML
    public void OnXButtonClick(){
        try {
            FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("event-schedule-manager", eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onHomePageClick() {
        try {
            FXRouter.goTo("main-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
