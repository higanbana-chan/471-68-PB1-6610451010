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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class ChangeEmailController {
    @FXML private Text currPasswordText;
    @FXML private Text confirmEmailText;
    @FXML private Text newEmailText;
    @FXML private PasswordField currPasswordField;
    @FXML private TextField newEmailField;
    @FXML private TextField confirmEmailField;
    @FXML private Button confirmButton;

    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;
    private String currPasswordInput;
    private String newEmailInput;
    private String confirmEmailInput;

    private SettingsController settingsController;

    @FXML
    public void initialize() {
        // disable buttons until fields are not empty
        confirmButton.disableProperty().bind(
                Bindings.createBooleanBinding( () ->
                        currPasswordField.getText().trim().isEmpty(), currPasswordField.textProperty()
                )
                .or(Bindings.createBooleanBinding( () ->
                        newEmailField.getText().trim().isEmpty(), newEmailField.textProperty()
                     )
                )
                .or(Bindings.createBooleanBinding( () ->
                        confirmEmailField.getText().trim().isEmpty(), confirmEmailField.textProperty()
                     )
                )
        );

        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }

    private void checkCredentials() throws IOException {
        user = settingsController.getUser();
        currPasswordInput = currPasswordField.getText().trim();
        newEmailInput = newEmailField.getText().trim();
        confirmEmailInput = confirmEmailField.getText().trim();

        if (user.validatePassword(currPasswordInput)) {
            currPasswordText.setText("CURRENT PASSWORD");
            handleEmailChange(user, newEmailInput, confirmEmailInput);
        } else {
            currPasswordText.setText("CURRENT PASSWORD - wrong password!");
        }
    }

    private void handleEmailChange(UserAccount user, String newEmailInput, String confirmEmailInput) throws IOException {
        if (isValidEmail(newEmailInput) && isUniqueEmail(newEmailInput)) {
            if (newEmailInput.equals(confirmEmailInput)) {
                user_accounts.changeEmail(user.getUsername(), newEmailInput);
                datasource.writeData(user_accounts);
                settingsController.hidePopup();
                FXRouter.goTo("login-page");
            } else {
                newEmailText.setText("NEW EMAIL");
                confirmEmailText.setText("CONFIRM NEW EMAIL - emails do not match!");
            }
        } else {
            if (!isUniqueEmail(newEmailInput)) {
                confirmEmailText.setText("CONFIRM NEW EMAIL");
                newEmailText.setText("NEW EMAIL - email already exists!");
            } else {
                confirmEmailText.setText("CONFIRM NEW EMAIL");
                newEmailText.setText("NEW EMAIL - invalid email format!");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?");
    }

    private boolean isUniqueEmail(String email) {
        return user_accounts.findEmail(email) == null;
    }

    @FXML
    public void onConfirmButtonClick() throws IOException {
        checkCredentials();
    }

    @FXML
    public void onCancelButtonClick() {
        if (settingsController != null) {
            settingsController.hidePopup();
        }
    }
}
