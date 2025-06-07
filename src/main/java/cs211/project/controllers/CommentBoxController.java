package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.UserAccountDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CommentBoxController {

    @FXML
    private Label commentDetails;
    @FXML
    private Label userName;
    @FXML
    private ImageView profilePicture;
    private AccountCollection accountCollection;
    private Datasource<AccountCollection> datasourceAccount;
    private UserAccount userAccount;

    public void setCommentDetail(String comment, String firstName) {
        datasourceAccount = new UserAccountDatasource("data","user-accounts.csv");
        accountCollection = datasourceAccount.readData();
        datasourceAccount = new UserAccountDatasource("data","user-accounts.csv");
        accountCollection = datasourceAccount.readData();
        userAccount = accountCollection.findAccount(firstName);
        commentDetails.setText(comment);
        userName.setText(userAccount.getFirstName());
    }

    public void setProfilePicture(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath); // Assuming imagePath is a file path
            profilePicture.setImage(image);
        }
    }

}
