package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.UserAccountDatasource;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class ProfileSettingsController {
    @FXML private Circle imgCircle;
    @FXML private Label nameLabel;
    @FXML private Text nameText;
    @FXML private TextField nameField;
    @FXML private Button saveButton;

    private SettingsController settingsController;
    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;
    private Path target;
    private File destDir;
    private File file;

    @FXML
    public void initialize() {
        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        user_accounts = datasource.readData();

        saveButton.disableProperty().bind(
                Bindings.createBooleanBinding( () ->
                        nameField.getText().trim().isEmpty(), nameField.textProperty()
                )
        );
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
        nameField.setText(user.getFirstName() + " " + user.getLastName());
    }

    @FXML
    public void handleUploadClick(MouseEvent event) {
        user = settingsController.getUser();
        FileChooser chooser = new FileChooser();
        // SET FILECHOOSER INITIAL DIRECTORY
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // DEFINE ACCEPTABLE FILE EXTENSION
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        // GET FILE FROM FILECHOOSER WITH JAVAFX COMPONENT WINDOW
        Node source = (Node) event.getSource();
        file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                destDir = new File("data/user_img");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = user.getUsername() + "."
                        + fileSplit[fileSplit.length - 1];
                target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );

                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                // SET NEW FILE PATH TO IMAGE
                Image img = new Image(target.toUri().toString());
                imgCircle.setFill(new ImagePattern(img));

                user_accounts.changeProfileImage(user.getUsername(), filename);
                datasource.writeData(user_accounts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onSaveButtonClick() {
        boolean check = nameField.getText().trim().matches("[A-Za-z]+\\s[A-Za-z]+");
        if (check) {
            nameText.setText("DISPLAY NAME");
            String[] newName = nameField.getText().split(" ");
            String firstName = newName[0];
            String lastName = newName[1];
            user_accounts.changeAccountName(user.getUsername(), firstName, lastName);
            datasource.writeData(user_accounts);
            nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        } else {
            nameText.setText("DISPLAY NAME - must be first name and last name");
        }
    }
}