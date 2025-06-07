package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class RegisterController {
    @FXML TextField firstnameField;
    @FXML TextField lastnameField;
    @FXML TextField emailField;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML PasswordField passwordConfirmField;
    @FXML Text errorLabel;

    private AccountCollection user_accounts;
    private Datasource<AccountCollection> datasource;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();

        UserAccount passed = (UserAccount) FXRouter.getData();
        if (passed != null) {
            firstnameField.setText(passed.getFirstName());
            lastnameField.setText(passed.getLastName());
            emailField.setText(passed.getEmail());
            usernameField.setText(passed.getUsername());
        }
    }

    private void checkDetails() throws IOException {
        String firstName = firstnameField.getText().trim();
        String lastName = lastnameField.getText().trim();
        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        String confirmPassword = passwordConfirmField.getText().trim();
        String email = emailField.getText().trim().toLowerCase();

        if (areFieldsEmpty(firstName, lastName, username, password)) {
            showError("Fields cannot be empty");
        } else if (isEmailValid(email) && isNameValid(firstName) && isNameValid(lastName) && password.equals(confirmPassword)) {
            handleValidInput(username, password, email, firstName, lastName);
        } else {
            handleInvalidInput(email, firstName, lastName, password, confirmPassword);
        }
    }

    private boolean areFieldsEmpty(String firstName, String lastName, String username, String password) {
        return firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty();
    }

    private boolean isEmailValid(String email) {
        return email.matches("[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?");
    }

    private boolean isNameValid(String name) {
        return name.matches("[-A-Za-z]+");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void handleValidInput(String username, String password, String email, String firstName, String lastName) throws IOException {
        UserAccount foundAccount = user_accounts.findAccount(username);
        UserAccount foundEmail = user_accounts.findEmail(email);

        if (foundAccount == null && foundEmail == null) {
            UserAccount user = new UserAccount(username, password, email, firstName, lastName);
            FXRouter.goTo("register-page-2", user);
        } else if (foundAccount != null && foundEmail != null) {
            showError("Duplicated Username & Email");
        } else if (foundAccount == null && foundEmail != null) {
            showError("Duplicated Email");
        } else if (foundAccount != null && foundEmail == null) {
            showError("Duplicated Username");
        }
    }

    private void handleInvalidInput(String email, String firstName, String lastName, String password, String confirmPassword) {
        if (!isEmailValid(email) && password.equals(confirmPassword)) {
            showError("Incorrect Email Format");
        } else if (isNameValid(firstName) && !isNameValid(lastName)) {
            showError("Invalid Last Name");
        } else if (!isNameValid(firstName) && isNameValid(lastName)) {
            showError("Invalid First Name");
        } else {
            showError("Passwords do not match");
        }
    }

    @FXML
    protected void onContinueButtonClick() throws IOException {
        checkDetails();
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXRouter.goTo("login-page");
    }
}
