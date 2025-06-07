package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.DataReceivable;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import cs211.project.models.Comment;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommentViewsController implements DataReceivable {

    @FXML
    private VBox postsContainer;
    @FXML
    private Label postDetails;
    @FXML
    private TextField commentField;
    @FXML
    private Label userNamePoster;
    @FXML
    private Label role;

    String[] data = ((String) FXRouter.getData()).split(",");
    String teamId = data[1];
    String username = data[2];
    String roleTeam = data[3];

    String postId = data[0];
    private AccountCollection accountCollection;
    private Datasource<AccountCollection> datasourceAccount;
    private UserAccount userAccount;

    private String firstName;

    private static final String commentBox_fxml_path = "/cs211/project/views/commentBox.fxml";

    private static final String post_csv_path = "data/postDetails.csv";
    @Override
    public void setData(Object Data){
        datasourceAccount = new UserAccountDatasource("data","user-accounts.csv");
        accountCollection = datasourceAccount.readData();
        datasourceAccount = new UserAccountDatasource("data","user-accounts.csv");
        accountCollection = datasourceAccount.readData();
        userAccount = accountCollection.findAccount(username);
        role.setText(roleTeam);
        userNamePoster.setText(userAccount.getFirstName());
        postDetails.setText(getPostDetailForId(postId));
        loadComments();
    }

    private List<String> getUserEventsAndRoles(String username) {
        List<String> userEventsRoles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/role.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username)) {
                    String eventId = values[1];
                    String role = values[2];

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

    private String getEventDetail(String eventId) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/all-event.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(eventId)) {
                    return values[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getOrganizerOfEvent(String eventId) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/all-event.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(eventId)) {
                    return values[8];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUserNameByPostId(int postId) {
        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            // Skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (Integer.parseInt(values[0]) == postId) {
                    return values[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pair<String, String> getDataFromCSV(String username) {
        String user_csv_path = "data/user-accounts.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(user_csv_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[2].equals(username)) {
                    String firstName = values[0];
                    String imagePath = "file:" + values[8];
                    return new Pair<>(firstName, imagePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPostDetailForId(String postId) {
        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line: " + line);
                String[] values = line.split(",");
                int currentId = Integer.parseInt(values[0]);
                if (currentId == Integer.parseInt(postId)) { // Convert to int
                    return values[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleCommentSend() {
        String commentText = commentField.getText();
        if (commentText == null || commentText.trim().isEmpty()) {
            System.out.println("Comment is empty. Not saving.");
            return;
        }

        int nextCommentId = getNextCommentId();
        String username = getUserNameByPostId(Integer.parseInt(postId)); // Convert to int
        appendCommentToCSV(nextCommentId, Integer.parseInt(postId), commentText, username); // Convert to int

        // Load the new comment into the UI
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(commentBox_fxml_path));
            AnchorPane commentBox = loader.load();
            CommentBoxController controller = loader.getController();
            controller.setCommentDetail(commentText, username);

            Pair<String, String> userData = getDataFromCSV(username);
            if (userData != null) {
                String imagePath = userData.getValue();
                controller.setProfilePicture(imagePath);
            }

            postsContainer.getChildren().add(commentBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        commentField.clear();
    }

    private int getNextCommentId() {
        int maxId = 0;
        List<Comment> comments = getAllCommentsFromCSV();
        for (Comment comment : comments) {
            if (comment.getId() > maxId) {
                maxId = comment.getId();
            }
        }
        return maxId + 1;
    }

    private List<Comment> getAllCommentsFromCSV() {
        List<Comment> comments = new ArrayList<>();
        String comment_csv_path = "data/commentDetails.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(comment_csv_path))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int commentId = Integer.parseInt(values[0]);
                int commentPostId = Integer.parseInt(values[1]);
                String commentDetail = values[2];
                String usernameComment = values[3];
                comments.add(new Comment(commentId, commentPostId, commentDetail,usernameComment));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private void appendCommentToCSV(int commentId, int postId, String commentDetail, String Username) {
        String comment_csv_path = "data/commentDetails.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(comment_csv_path, true))) {
            pw.println(commentId + "," + postId + "," + commentDetail + "," + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComments() {
        List<Comment> comments = getCommentsFromCSV(Integer.parseInt(postId)); // Convert to int

        for (Comment comment : comments) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(commentBox_fxml_path));
                AnchorPane commentBox = loader.load();
                CommentBoxController controller = loader.getController();

                String commentText = comment.getCommentDetail();
                String usernameComment = comment.getUsernameComment();
                String username = getUserNameByPostId(Integer.parseInt(postId)); // Convert to int

                Pair<String, String> userData = getDataFromCSV(usernameComment);
                if (userData != null) {
                    String imagePath = userData.getValue();
                    controller.setProfilePicture(imagePath); // Set the profile picture
                }

                controller.setCommentDetail(commentText, usernameComment);
                postsContainer.getChildren().add(commentBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Comment> getCommentsFromCSV(int postId) {
        List<Comment> comments = new ArrayList<>();
        String comment_csv_path = "data/commentDetails.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(comment_csv_path))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int commentId = Integer.parseInt(values[0]);
                int commentPostId = Integer.parseInt(values[1]);
                String usernameComment = values[3];
                String commentDetail = values[2];

                if (commentPostId == postId) {
                    comments.add(new Comment(commentId, commentPostId, commentDetail, usernameComment));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public void goBackButton() throws IOException {

        try {

            FXRouter.goTo("community-post-page", teamId + "," + username + "," + roleTeam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
