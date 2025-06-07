package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingsController {
    @FXML private AnchorPane popupPane;
    @FXML private AnchorPane mainPane;
    @FXML private ToggleButton myAccountButton;
    @FXML private ToggleButton myProfileButton;
    @FXML private ToggleGroup navButton;

    private UserAccount user;

    @FXML
    public void initialize() {
        user = (UserAccount) FXRouter.getData();

        popupPane.setVisible(false);

        // set the first fxml page to "My Account"
        // set labels to match sent user object
        loadPage("my-account");

        // prevents the button from getting deselected
        navButton.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    toggle.setSelected(true);
                }
            }
        });
    }


    public void loadPage(String name) {
        String resourcesPath = "/cs211/project/views/settings/";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcesPath + name + ".fxml"));
        try {
            AnchorPane newPane = loader.load();
            if (name.equals("my-account")) {
                myAccountButton.setSelected(true);
                MyAccountController accountController = loader.getController();
                accountController.setData(user.getUsername());
                accountController.setSettingsController(this);
            } else if (name.equals("profile")) {
                myProfileButton.setSelected(true);
                ProfileSettingsController profController = loader.getController();
                profController.setData(user.getUsername());
                profController.setSettingsController(this);
            }
            mainPane.getChildren().setAll(newPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPopup(String popup) {
        String resourcesPath = "/cs211/project/views/settings/overlays/";
        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource(resourcesPath + popup + ".fxml"));
        try {
            AnchorPane newPopup = popupLoader.load();
            if (popup.equals("password-overlay")) {
                ChangePasswordController changePasswordController = popupLoader.getController();
                changePasswordController.setSettingsController(this);
            } else if (popup.equals("email-overlay")) {
                ChangeEmailController changeEmailController = popupLoader.getController();
                changeEmailController.setSettingsController(this);
            }
            popupPane.getChildren().setAll(newPopup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showPasswordPopup() {
        loadPopup("password-overlay");
        popupPane.setVisible(true);
    }

    public void showEmailPopup() {
        loadPopup("email-overlay");
        popupPane.setVisible(true);
    }

    public void hidePopup() {
        popupPane.setVisible(false);
    }

    @FXML
    private void onBackButtonClick() throws IOException {
        FXRouter.goTo("main-page");
    }

    @FXML
    private void onEscKeyPressed(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ESCAPE) {
            FXRouter.goTo("main-page");
        }
    }

    @FXML
    private void onMyAccountButtonClick() {
        loadPage("my-account");
    }

    @FXML
    private void onMyProfileButtonClick() {
        loadPage("profile");
    }

    public UserAccount getUser() {
        return user;
    }
}
