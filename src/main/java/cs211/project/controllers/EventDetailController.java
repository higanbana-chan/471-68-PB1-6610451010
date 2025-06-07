package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.List;

public class EventDetailController {
    @FXML private TextField eventNameTextField;
    @FXML private TextField categoryTextField;
    @FXML private TextField locationTextField;
    @FXML private Label participantLabel;
    @FXML private TextArea eventDetailTextArea;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private ImageView eventPosterImageView;

    @FXML private AnchorPane focusOverlay;
    @FXML private AnchorPane userSettingsAnchorPane;
    @FXML private Label nameLabel;
    @FXML private Circle imgCircle;

    private List<Object> obj = (List<Object>) FXRouter.getData();
    private UserAccount user = (UserAccount) obj.get(0);
    private Event receivedEvent = (Event) obj.get(1);

    private EventCollection events;
    private Event event;

    public void initialize() {
        Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        RoleCollection roleCollection = roleCollectionDatasource.readData();

        events.addUserToEvent(accountCollection, roleCollection);

        String eventId = receivedEvent.getId();
        event = events.findEventById(eventId);

        focusOverlay.setVisible(false);
        userSettingsAnchorPane.setVisible(false);

        eventNameTextField.setText(event.getName());
        categoryTextField.setText(event.getType());
        locationTextField.setText(event.getLocation());
        participantLabel.setText(event.getCurrParticipants() + "/" + event.getMaxParticipants());
        eventDetailTextArea.setText(event.getDetail());
        startDateField.setText(event.getStart());
        endDateField.setText(event.getEnd());
        Image poster = new Image("file:" + event.getPoster());
        eventPosterImageView.setImage(poster);

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        String imagePath = user.getImgPath();
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

        // focus overlay when user icon is clicked
        focusOverlay.setOnMouseClicked((MouseEvent overlayFocused) -> {
            userSettingsAnchorPane.setVisible(false);
            focusOverlay.setVisible(false);
        });
    }

    @FXML
    public void onClickCloseButton() throws IOException {
        FXRouter.goTo("main-page");
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
}
