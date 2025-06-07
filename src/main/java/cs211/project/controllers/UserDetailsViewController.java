package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.services.DataReceivable;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDetailsViewController implements DataReceivable {

    @FXML
    private Label userUsername;
    @FXML
    private Label lastLoggedInLabel;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField accountCreationDate;

    @FXML
    private Circle profilePicture;

    @FXML
    private VBox eventInManagement;

    private static final String user_csv_path = "data/user-accounts.csv";
    private static final String role_csv_path = "data/role.csv";
    private static final String all_event_csv_path = "data/all-event.csv";

    private UserAccount user;

    public void initialize() {
        user = (UserAccount) FXRouter.getData();
    }

    public void setData(Object data) {
        if (user != null) {
            lastLoggedInLabel.setText("Last logged in: " + user.getLastLoggedIn());
            userUsername.setText(user.getUsername());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());
            accountCreationDate.setText(user.getRegisteredDate());

            String imagePath = user.getImgPath();
            try {
                FileInputStream imgStream = new FileInputStream(imagePath);
                Image img = new Image(imgStream);
                imgStream.close();
                profilePicture.setFill(new ImagePattern(img));
            } catch (FileNotFoundException | InvalidPathException | NullPointerException e) {
                Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                profilePicture.setFill(new ImagePattern(img));
            } catch (IllegalArgumentException e) {
                Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                profilePicture.setFill(new ImagePattern(img));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Populate the VBox with events and roles:
            List<String> userEventsRoles = getUserEventsAndRoles(user.getUsername());
            for (String eventAndRole : userEventsRoles) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/eventINMpane.fxml"));
                    Node node = loader.load();
                    EventINMpaneController controller = loader.getController();
                    controller.setData(eventAndRole); // Assumes a method to set data in EventINMpaneController
                    eventInManagement.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> getUserEventsAndRoles(String username) {
        List<String> userEventsRoles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(role_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username)) {
                    String eventId = values[1];
                    String role = values[2];

                    // Check if the user is the organizer of the event
                    String organizer = getOrganizerOfEvent(eventId);
                    if (username.equals(organizer)) {
                        role = "Organizer";
                    }

                    String eventDetail = getEventDetail(eventId);
                    if (eventDetail != null) {
                        userEventsRoles.add(eventDetail + ", Role: " + role);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userEventsRoles;
    }

    private String getOrganizerOfEvent(String eventId) {
        try (BufferedReader br = new BufferedReader(new FileReader(all_event_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(eventId)) {
                    return values[7];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getEventDetail(String eventId) {
        try (BufferedReader br = new BufferedReader(new FileReader(all_event_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(eventId)) {
                    return values[1]; // Return the event name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private List<String> getUserDetailsFromCSV(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(user_csv_path))) {
            String line;
            System.out.println("Fetching details for username: " + username);

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[2].equals(username)) {
                    return Arrays.asList(values);  // Return the matched user details as a list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("No match found for username: " + username);
        return null;
    }
    @FXML
    private void onClickSignOutButton() throws IOException {
        FXRouter.goTo("login-page");
    }
    @FXML
    public void onClickGoHomeButton() throws IOException {
        FXRouter.goTo("admin-page");
    }
}