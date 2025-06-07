package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController {
    @FXML private AnchorPane popupPane;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox userBoxContainer;
    @FXML private Button buttonOne;
    @FXML private Button buttonTwo;
    @FXML private Button buttonThree;
    @FXML private Button buttonFour;
    @FXML private Button buttonFive;
    @FXML private Button buttonSix;
    @FXML private Button changePassword;
    @FXML private TextField searchField;

    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private ArrayList<UserAccount> allUsers;
    private ObservableList<String> allUsernames = FXCollections.observableArrayList();
    private FilteredList<String> filteredUsernames;

    private static final int USERS_PER_PAGE = 10;  // number of users displayed per page
    private static final String userBox_fxml_path = "/cs211/project/views/userBox.fxml";

    @FXML
    public void initialize() {
        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();
        allUsers = user_accounts.getUserAccounts();

        allUsernames.setAll(getUsernames(allUsers));
        filteredUsernames = new FilteredList<>(allUsernames, s -> true);

        popupPane.setVisible(false);
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText();
            if(filter == null || filter.isEmpty()) {
                filteredUsernames.setPredicate(s -> true); // show all data
            } else {
                filteredUsernames.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase())); // Filter based on input
            }
            loadUsers(1); // reload users whenever search input changes
        });
        buttonOne.setOnAction(event -> loadUsers(1));
        buttonTwo.setOnAction(event -> loadUsers(2));
        buttonThree.setOnAction(event -> loadUsers(3));
        buttonFour.setOnAction(event -> loadUsers(4));
        buttonFive.setOnAction(event -> loadUsers(5));
        buttonSix.setOnAction(event -> loadUsers(6));

        loadUsers(1); //first page by default

        // adjusted scroll pane speed
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            scrollPane.setVvalue(scrollPane.getVvalue() - scrollEvent.getDeltaY() * 0.0015);
        });
    }

    public UserAccount getAdmin() {
        return (UserAccount) FXRouter.getData();
    }

    private List<String> getUsernames(ArrayList<UserAccount> accounts) {
        List<String> usernames = new ArrayList<>();

        for (UserAccount get : accounts) {
            usernames.add(get.getUsername());
        }

        return usernames;
    }

    @FXML
    private void changePasswordPopUp() {
        String resourcesPath = "/cs211/project/views/settings/overlays/";
        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource(resourcesPath + "password-overlay" + ".fxml"));
        try {
            AnchorPane newPopup = popupLoader.load();
            ChangePasswordController changePasswordController = popupLoader.getController();
            changePasswordController.setAdminController(this);
            popupPane.getChildren().setAll(newPopup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        popupPane.setVisible(true);
    }

    @FXML
    public void hidePopup() {
        popupPane.setVisible(false);
    }
    @FXML
    private void onClickSignOutButton() throws IOException {
        FXRouter.goTo("login-page");
    }
    //loadUsers โหลด userBox เข้า Vbox
    private void loadUsers(int page) {
        int start = (page - 1) * USERS_PER_PAGE;
        int end = Math.min(start + USERS_PER_PAGE, filteredUsernames.size()); // Ensure 'end' is within bounds

        userBoxContainer.getChildren().clear(); // clear existing user box

        for (int i = start; i < end; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(userBox_fxml_path));
                Pane userBox = loader.load();

                UserBoxController controller = loader.getController();
                String username = filteredUsernames.get(i);
                controller.setData(user_accounts.findAccount(username));

                userBoxContainer.getChildren().add(userBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

