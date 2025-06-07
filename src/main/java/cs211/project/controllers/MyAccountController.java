package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.UserAccountDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public class MyAccountController {
    @FXML private Circle imgCircle;
    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Label emailLabel;
    @FXML private Label usernameLabel;

    private SettingsController settingsController;
    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;

    @FXML
    public void initialize() {
        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }

    public void setData(String username) {
        user = user_accounts.findAccount(username);
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
        dateLabel.setText("Date Created: " + user.getRegisteredDate());
        emailLabel.setText(user.getEmail());
        usernameLabel.setText(user.getUsername());
    }
    @FXML
    private void onEditProfileButtonClick() {
        if (settingsController != null) {
            settingsController.loadPage("profile");
        }
    }

    @FXML
    private void onEmailEditButtonClick() {
        if (settingsController != null) {
            settingsController.showEmailPopup();
        }
    }

    @FXML
    private void onChangePwdButtonClick() {
        if (settingsController != null) {
            settingsController.showPasswordPopup();
        }
    }
}
