package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.*;

public class LoginController {
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Text errorLabel;
    @FXML AnchorPane onCreditsClick;

    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;
    private String usernameInput;
    private String passwordInput;

    @FXML
    public void initialize() {
        onCreditsClick.setVisible(false);
        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();

        errorLabel.setVisible(false);
    }
    @FXML
    private void onLoginButtonClick() throws IOException {
        checkCredentials();
    }

    private void checkCredentials() throws IOException {
        usernameInput = usernameField.getText().trim().toLowerCase();
        passwordInput = passwordField.getText().trim();


        user = user_accounts.login(usernameInput, passwordInput);
        if (user != null) {
            datasource.writeData(user_accounts);
            if (user.getAccountType().equals("admin")) {
                FXRouter.goTo("admin-page", user);
            } else {
                FXRouter.goTo("main-page", user);
            }
        } else {
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void onRegisterButtonClick() throws IOException {
        FXRouter.goTo("register-page");
    }

    @FXML
    private void onOpenDocAction(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.google.com"));
    }

    @FXML
    private void onOpenCredits(){
        onCreditsClick.setVisible(true);
    }

    @FXML
    private void onBackButtonClick() {
        onCreditsClick.setVisible(false);
    }
}