package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import cs211.project.services.FXRouter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public class MainPageController {
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane focusOverlay;
    @FXML private AnchorPane userSettingsAnchorPane;
    @FXML private GridPane eventBox;
    @FXML private Label nameLabel;
    @FXML private Circle imgCircle;
    @FXML private TextField searchTextField;
    @FXML private AnchorPane confirmPane;
    @FXML private ImageView eventImage;
    @FXML private Label eventNameLabel;
    @FXML private Button joinMemberButton;
    @FXML private Button joinParticipantButton;

    private RoleCollection roleCollection;
    private EventCollection loadedEvents;
    private UserAccount user;
    private TeamList teamList;

    int row;

    public void initialize() {
        user = (UserAccount) FXRouter.getData();
        focusOverlay.setVisible(false);
        userSettingsAnchorPane.setVisible(false);

        // load user data in a background thread
        Thread loadDataThread = new Thread(() -> {
            loadUserData();
            loadEventList();
        });

        loadDataThread.setDaemon(true);  // daemon threads automatically exit when the application exits.
        loadDataThread.start();

        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                eventBox.getChildren().clear();
                EventCollection searchEvent = loadedEvents.findEventByName(newValue);
                showEvents(searchEvent);
            }
        });

        // adjusted scroll pane speed
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            scrollPane.setVvalue(scrollPane.getVvalue() - scrollEvent.getDeltaY() * 0.0015);
        });

        // focus overlay when user icon is clicked
        focusOverlay.setOnMouseClicked((MouseEvent overlayFocused) -> {
            userSettingsAnchorPane.setVisible(false);
            focusOverlay.setVisible(false);
        });
    }

    private void loadUserData() {
        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        // load user data here and update the UI using Platform.runLater()
        UserAccount found = accountCollection.findAccount(user.getUsername());
        Platform.runLater(() -> {
            String imagePath = found.getImgPath();
            try {
                FileInputStream imgStream = new FileInputStream(imagePath);
                Image img = new Image(imgStream);
                imgStream.close();
                imgCircle.setFill(new ImagePattern(img));
            } catch (FileNotFoundException | InvalidPathException | NullPointerException e) {
                Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                imgCircle.setFill(new ImagePattern(img));
            } catch (IllegalArgumentException e) {
                Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                imgCircle.setFill(new ImagePattern(img));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            nameLabel.setText(found.getFirstName() + " " + found.getLastName());
        });
    }

    private void loadEventList() {
        // load event data in a background thread
        EventCollection loadedEvents = loadEventData();

        // update the UI with the loaded events using Platform.runLater()
        Platform.runLater(() -> {
            showEvents(loadedEvents);
        });
    }

    private EventCollection loadEventData() {
        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        roleCollection = roleCollectionDatasource.readData();

        Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        loadedEvents = eventListFileDatasourceDatasource.readData();

        Datasource<TeamList> teamListDatasource = new AllTeamListFileDatasource("data", "all-team.csv");
        teamList = teamListDatasource.readData();

        loadedEvents.sortEventByEndDate(loadedEvents.getEvents(), new EventDateComparator());
        loadedEvents.addUserToEvent(accountCollection, roleCollection);
        return loadedEvents;
    }

    private void showEvents(EventCollection events) {
        row = 1;
        for(Event event : events.getEvents()) {
            showEvent(event);
        }
    }

    private void showEvent(Event event) {
        try {

            if (!event.isOrganizer(user.getUsername())) {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-component.fxml"));
                    HBox eventComponent = fxmlLoader.load();
                    EventComponentController eventComponentController = fxmlLoader.getController();
                    eventComponentController.loadEventComponents(loadedEvents,teamList, roleCollection, event, user, confirmPane, eventNameLabel, eventImage, joinParticipantButton, joinMemberButton);
                    eventBox.add(eventComponent, 0, row++);
                    GridPane.setMargin(eventComponent, new Insets(10));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClickCreateEventButton() throws IOException {
        FXRouter.goTo("create-event-page", user);
    }

    @FXML
    private void onClickUserProfile() {
        userSettingsAnchorPane.setVisible(true);
        focusOverlay.setVisible(true);
    }

    @FXML
    private void onClickLogoutButton() throws IOException {
        FXRouter.goTo("login-page");
    }

    @FXML
    private void onHomeButtonClick() throws IOException {
        FXRouter.goTo("main-page", user);
    }

    @FXML
    private void onClickJoinedButton() throws IOException {
        FXRouter.goTo("join-events-page", user);
    }
    @FXML
    private void onManageEventClick() throws IOException {
        FXRouter.goTo("manage-event", user);
    }

    @FXML
    private void onSettingsButtonClick() throws IOException {
        FXRouter.goTo("settings-page", user);
    }

    @FXML
    private void onCloseButtonClick() {
        confirmPane.setVisible(false);
    }
}