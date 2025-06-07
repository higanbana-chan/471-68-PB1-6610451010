package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.time.format.DateTimeFormatter;

public class EditEventManagerController {
    @FXML
    private TextField OtherTextField;

    @FXML
    private TextField endTextField;

    @FXML
    private Label errorField;

    @FXML
    private TextArea eventDetailTextArea;

    @FXML
    private TextField eventNameTextField;

    @FXML
    private Circle imgCircle;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField participantTextField;

    @FXML
    private ImageView posterImage;

    @FXML
    private TextField startTextField;

    @FXML
    private AnchorPane userSettingAnchorPane;

    @FXML
    private Label nameLabel;


    private String[] data = ((String) FXRouter.getData()).split(",");

    private String eventId = data[0];
    private String username = data[1];

    boolean status = false;

    private Event event;
    private EventCollection events;
    private Datasource<EventCollection> eventListFileDatasourceDatasource;


    public void initialize() {
        eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();

        event = events.findEventById(eventId);

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        UserAccount user = accountCollection.findAccount(username);
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

        eventNameTextField.setText(event.getName());
        eventDetailTextArea.setText(event.getDetail());
        startTextField.setText(event.getStart());
        endTextField.setText(event.getEnd());
        participantTextField.setText("" + event.getMaxParticipants());
        locationTextField.setText(event.getLocation());
        Image posterImg = new Image("file:" + event.getPoster());
        posterImage.setImage(posterImg);

        userSettingAnchorPane.setVisible(status);
        errorField.setText("");
    }

    private void alertPopup() {
        Stage alert = new Stage();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Custom Alert");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/alert.fxml"));
            VBox alertComponent = fxmlLoader.load();
            AlertController alertController = fxmlLoader.getController();
            alertController.setData(alert, "warning", "Invalid input");
            Scene alertScene = new Scene(alertComponent, 500, 300);
            alert.setScene(alertScene);
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClickUpdateButton() {
        if (!eventNameTextField.getText().isEmpty() && !eventDetailTextArea.getText().isEmpty() && !startTextField.getText().isEmpty() && !endTextField.getText().isEmpty() && !locationTextField.getText().isEmpty() && !participantTextField.getText().isEmpty()) {
            errorField.setText("");
            try {
                int maxParticipants = Integer.parseInt(participantTextField.getText());
                event.setMaxParticipants(maxParticipants);
                event.setName(eventNameTextField.getText());
                event.setDetail(eventDetailTextArea.getText());
//                event.setStart(startTextField.getText());
                event.setEnd(endTextField.getText());
                event.setLocation(locationTextField.getText());
                eventListFileDatasourceDatasource.writeData(events);
                try {
                    FXRouter.goTo("manage-event");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (NumberFormatException e) {
                alertPopup();
            }
        } else {
            alertPopup();
        }
    }

    @FXML
    void onClickLogoutButton() {
        try {
            FXRouter.goTo("login-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClickCancelButton() {
        try {
            FXRouter.goTo("manage-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUserProfile() {
        status = !status;
        userSettingAnchorPane.setVisible(status);
    }
}
