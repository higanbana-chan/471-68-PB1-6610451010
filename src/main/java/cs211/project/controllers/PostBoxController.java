package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.util.Pair;

public class PostBoxController {

    @FXML
    private Label postDetails;
    @FXML
    private Label userName;

    @FXML
    private ImageView profilePicture;

    private int postId;
    String[] data = ((String) FXRouter.getData()).split(",");
    String teamId = data[0];
    String username = data[1];
    String role = data[2];

    public void setPostDetail(String detail, String username, int postId) {
        this.postId = postId;
        postDetails.setText(detail);

        Pair<String, String> userData = getDataFromCSV(username);
        if (userData != null) {
            userName.setText(userData.getKey());

            String imagePath = userData.getValue();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(imagePath);
                    profilePicture.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading profile picture: " + e.getMessage());
                }
            } else {
                profilePicture.setImage(null);
            }
        }
    }

    private Pair<String, String> getDataFromCSV(String username) {
        String user_csv_path = "data/user-accounts.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(user_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[2].equals(username)) {
                    String firstName = values[0];
                    String imagePath = "file:"+values[8];
                    return new Pair<>(firstName, imagePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void goToComment() throws IOException {
        FXRouter.goTo("comment-views", postId + "," + teamId + "," + username + "," + role);
    }
}
