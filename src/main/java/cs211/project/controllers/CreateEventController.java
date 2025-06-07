package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.BreakIterator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CreateEventController
{
    @FXML private AnchorPane userSettingsAnchorPane;
    @FXML private AnchorPane focusOverlay;
    @FXML private TextField eventNameTextField;
    @FXML private TextArea eventDetailTextArea;
    @FXML private ComboBox<String> eventCategory;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField locationTextField;
    @FXML private TextField participantTextField;
    @FXML private ImageView eventPoster;

    @FXML private Label nameLabel;
    @FXML private Circle imgCircle;

    private EventCollection events;
    private Datasource<EventCollection> eventListFileDatasourceDatasource;

    private String projectImagePath;
    private Path destinationPath;
    private File selectedFile;

    private UserAccount user;
    private LocalDate currentDate;
    private String formattedDate;
    private String formattedEndDate;
    private String currEventId = Event.getNextEventId();

    String eventId = Event.getNextEventId();

    public void initialize() {
        eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();

        user = (UserAccount) FXRouter.getData();
        focusOverlay.setVisible(false);
        userSettingsAnchorPane.setVisible(false);

        ObservableList<String> eventCategories = FXCollections.observableArrayList(
                "Professional Events",
                "Social Events",
                "Cultural Events",
                "Entertainment Events",
                "Educational Events",
                "Fundraising Events",
                "Sports and Recreational Events",
                "Special Events",
                "Community Events",
                "Expo and Convention Events",
                "Arts and Creativity Events",
                "Food and Beverage Events",
                "Technology Events",
                "Wellness Events",
                "Civic and Government Events",
                "Performance Arts Events",
                "Religious and Spiritual Events",
                "Awards and Recognition Events",
                "Tourism and Promotion Events",
                "Others"
        );

        eventCategory.setItems(eventCategories);

        // sets user image icon & name
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
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());

        // sets start date to the current system time
        Date date = new Date();
        Instant instant = date.toInstant();
        currentDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        startDatePicker.setValue(currentDate);
        formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        startDatePicker.setDisable(true);

        // disables DatePicker past dates
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(currentDate) <= 0);
            }
        });

        // focus overlay when user icon is clicked
        focusOverlay.setOnMouseClicked((MouseEvent overlayFocused) -> {
            userSettingsAnchorPane.setVisible(false);
            focusOverlay.setVisible(false);
        });
    }

    private void alertPopup(String status, String text) {
        Stage alert = new Stage();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Custom Alert");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/alert.fxml"));
            VBox alertComponent = fxmlLoader.load();
            AlertController alertController = fxmlLoader.getController();
            alertController.setData(alert, status, text);
            Scene alertScene = new Scene(alertComponent, 500, 300);
            alert.setScene(alertScene);
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteImg() throws IOException {
        Files.deleteIfExists(destinationPath);
    }

    private void uploadImg() {
        File imgDir = new File("data" + File.separator +  "poster_img");
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        if (selectedFile != null) {
            try {
                projectImagePath = "data/poster_img/";

                Path sourcePath = selectedFile.toPath();
                destinationPath = Path.of(projectImagePath + currEventId + ".png");
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to get extension of a file
    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) //if the name is not empty
            return fileName.substring(i + 1).toLowerCase();

        return extension;
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        List<String> validExtensions = Arrays.asList("png", "jpg", "jpeg");

        if (event.getDragboard().hasFiles()) {
            List<String> fileExtensions = event.getDragboard().getFiles().stream()
                    .map(file -> getExtension(file.getName()))
                    .collect(Collectors.toList());

            if (fileExtensions.stream().anyMatch(extension -> !validExtensions.contains(extension))) {
                event.consume();
            } else {
                event.acceptTransferModes(TransferMode.ANY);
            }
        }
        event.consume();
    }


    @FXML
    private void handleDragDrop(DragEvent event) throws IOException {
        List<File> files = event.getDragboard().getFiles();
        FileInputStream imgStream = new FileInputStream(files.get(0));
        Image img = new Image(imgStream);
        eventPoster.setImage(img);

        selectedFile = files.get(0);
        uploadImg();

        event.consume();
        imgStream.close();
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
    private void onClickUserProfile() {
        userSettingsAnchorPane.setVisible(true);
        focusOverlay.setVisible(true);
    }

    @FXML
    private void onSettingsButtonClick() throws IOException {
        FXRouter.goTo("settings-page", user);
    }

    @FXML
    private void onClickLogoutButton() throws IOException {
        FXRouter.goTo("login-page");
    }

    @FXML
    private void onSetEndDate() {
        LocalDate endDate = endDatePicker.getValue();
        formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    @FXML
    private void onClickChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage fileChooserStage = new Stage();
        selectedFile = fileChooser.showOpenDialog(fileChooserStage);
        uploadImg();

        Image image = new Image(selectedFile.toURI().toString());
        eventPoster.setImage(image);

        if (selectedFile != null) {
            try {
                eventPoster.setImage(image);

                projectImagePath = "data/poster_img/";
                BreakIterator fileNameLabel = null;
                fileNameLabel.setText(selectedFile.getName());

                Path sourcePath = selectedFile.toPath();
                Path destinationPath = Path.of(projectImagePath + eventId + ".png");
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @FXML
    private void onClickCreateButton() {
        if (selectedFile != null && !eventNameTextField.getText().isEmpty() && !eventDetailTextArea.getText().isEmpty() && endDatePicker.getValue() != null && eventCategory.getValue() != null && !locationTextField.getText().isEmpty() && !participantTextField.getText().isEmpty()) {
            try {
                int maxParticipant = Integer.parseInt(participantTextField.getText().trim());
                events.addEvent(currEventId, eventNameTextField.getText(), String.valueOf(destinationPath), eventDetailTextArea.getText(), formattedDate, formattedEndDate, eventCategory.getValue(), locationTextField.getText(), user.getUsername(), 0, maxParticipant);
                eventListFileDatasourceDatasource.writeData(events);
                alertPopup("success", "Create event successful");

                eventNameTextField.setText("");
                eventDetailTextArea.setText("");
                locationTextField.setText("");
                participantTextField.setText("");

                try {
                    FXRouter.goTo("main-page", user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (NumberFormatException e) {
                alertPopup("fail", "Invalid input");
            }
        } else {
            alertPopup("fail", "Invalid input");
        }
    }
    @FXML
    private void onClickCancelButton() throws IOException {
        if (selectedFile != null) {
            deleteImg();
            FXRouter.goTo("main-page");
        } else {
            FXRouter.goTo("main-page");
        }
    }
}
