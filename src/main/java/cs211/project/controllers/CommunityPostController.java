package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommunityPostController {

    @FXML
    private Label teamName;

    @FXML
    private VBox postsContainer;

    @FXML
    private Button signOutButton;

    @FXML
    private ImageView profilePic;
    private UserAccount user;
    String[] data = ((String) FXRouter.getData()).split(",");
    String teamId = data[0];
    String username = data[1];
    String role = data[2];


    private static final String post_csv_path = "data/postDetails.csv";
    private static final String postBox_fxml_path = "/cs211/project/views/postBox.fxml";

    public void initialize() {
        Object data = FXRouter.getData();

        if (data instanceof String) {
            setData((String) data);
        } else {
            setData("nat,3,Member");
        }

        Datasource<AccountCollection> accountDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountDatasource.readData();

        user = accountCollection.findAccount(username);

        displayProfilePicture(this.username);
    }



    public void setData(String data) {
        String[] splitData = data.split(",");
        if (splitData.length == 3) {
            this.teamId = splitData[0];
            this.username = splitData[1];
            this.role = splitData[2];
            System.out.println(teamId);
            System.out.println(role);
            System.out.println(username);
            String teamNameName = getTeamNameFromCSV();
            if (teamNameName != null) {
                teamName.setText(teamNameName);
            }
        }
        loadPosts();
    }
    private List<String[]> readPostsFromCSV() {
        List<String[]> postDetailsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            br.readLine(); // Read the header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[2].equals(teamId)) {
                    // Including postId, postDetail, and username respectively.
                    postDetailsList.add(new String[] {values[0], values[1], values[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return postDetailsList;
    }


    private void displayProfilePicture(String username) {
        String picturePath = getProfilePicturePath(username);
        if (picturePath != null) {
            Image image = new Image(new File(picturePath).toURI().toString());
            profilePic.setImage(image);
        }
    }

    private String getProfilePicturePath(String username) {
        String path = null;
        try (BufferedReader br = new BufferedReader(new FileReader("data/user-accounts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[2].equals(username)) {
                    path = "file:"+values[8]; // assuming path is the second column in the CSV
                    if (path.startsWith("file:")) {
                        path = path.substring(5); // Remove the "file:" prefix
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }



    private void loadPosts() {
        List<String[]> postDetailsList = readPostsFromCSV();

        for (String[] details : postDetailsList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(postBox_fxml_path));
                AnchorPane postBox = loader.load();
                PostBoxController controller = loader.getController();

                // Get the postId from the details array and convert it to an integer
                int postId = Integer.parseInt(details[0]);

                // Updated call to setPostDetail with postId
                controller.setPostDetail(details[1], details[2], postId); // details[1] is postDetail, details[2] is username

                postsContainer.getChildren().add(postBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void addPostButtonClick() throws IOException {
        String dataToSend = username + "," + teamId;
        FXRouter.goTo("post-adding-page", dataToSend);
    }
    @FXML
    private void onClickUserProfile() {
        signOutButton.setVisible(!signOutButton.isVisible());
    }

    private static final String team_csv_path = "data/all-team.csv";

    private String getTeamNameFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(team_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[5].equals(teamId)) {
                    return values[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onManageEventClick() throws IOException {
        FXRouter.goTo("manage-event", user);
    }

    @FXML
    public void onClickSignOut() throws IOException {
        FXRouter.goTo("login-page");
    }

    public void onClickHomeBUtton() throws IOException {
        FXRouter.goTo("main-page", user);
    }
}
