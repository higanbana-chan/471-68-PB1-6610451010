package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.io.IOException;

public class ChangePasswordController {
    @FXML private Text currPasswordText;
    @FXML private Text confirmPasswordText;
    @FXML private PasswordField currPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button confirmButton;

    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;
    private String currPasswordInput;
    private String newPasswordInput;
    private String confirmPasswordInput;

    private SettingsController settingsController;
    private AdminPageController adminController;

    @FXML
    public void initialize() {
        // disable buttons until fields are not empty
        confirmButton.disableProperty().bind(
                Bindings.createBooleanBinding( () ->
                        currPasswordField.getText().trim().isEmpty(), currPasswordField.textProperty()
                )
                .or(Bindings.createBooleanBinding( () ->
                        newPasswordField.getText().trim().isEmpty(), newPasswordField.textProperty()
                     )
                )
                .or(Bindings.createBooleanBinding( () ->
                        confirmPasswordField.getText().trim().isEmpty(), confirmPasswordField.textProperty()
                     )
                )
        );

        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }

    public void setAdminController(AdminPageController controller) {
        this.adminController = controller;
    }

    private void checkCredentials() throws IOException {
        if (adminController != null) {
            user = adminController.getAdmin();
        } else {
            user = settingsController.getUser();
        }
        currPasswordInput = currPasswordField.getText().trim();
        newPasswordInput = newPasswordField.getText().trim();
        confirmPasswordInput = confirmPasswordField.getText().trim();

        if (user.validatePassword(currPasswordInput)) {
            if (newPasswordInput.equals(confirmPasswordInput)) {
                user_accounts.changePassword(user.getUsername(), newPasswordInput);
                datasource.writeData(user_accounts);
                if (adminController != null) {
                    adminController.hidePopup();
                } else {
                    settingsController.hidePopup();
                }
                FXRouter.goTo("login-page");
            } else {
                confirmPasswordText.setText("CONFIRM NEW PASSWORD - passwords do not match!");
            }
        } else {
            currPasswordText.setText("CURRENT PASSWORD - wrong password!");
        }
    }

    @FXML
    public void onConfirmButtonClick() throws IOException {
        checkCredentials();
    }

    @FXML
    public void onCancelButtonClick() {
        if (adminController != null) {
            adminController.hidePopup();
        }

        if (settingsController != null) {
            settingsController.hidePopup();
        }
    }
}
