package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserAccountDatasource;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RegisterProfileController {
    @FXML private Circle imgCircle;
    private Datasource<AccountCollection> datasource;
    private AccountCollection user_accounts;
    private UserAccount user;
    private Path target;
    private File destDir;
    private File file;
    private String filename;

    @FXML
    public void initialize() {
        datasource = new UserAccountDatasource("data", "user-accounts.csv");
        setDefaultImage();

        user_accounts = datasource.readData();
        user = (UserAccount) FXRouter.getData();
    }

    private void setDefaultImage() {
        Image defaultImg = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
        imgCircle.setFill(new ImagePattern(defaultImg));
    }

    private void deleteImg() {
        if (target != null) {
            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleUploadClick(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        // SET FILECHOOSER INITIAL DIRECTORY
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // DEFINE ACCEPTABLE FILE EXTENSION
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        // GET FILE FROM FILECHOOSER WITH JAVAFX COMPONENT WINDOW
        Node source = (Node) event.getSource();
        file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            // CREATE FOLDER IF NOT EXIST
            destDir = new File("data/user_img");
            if (!destDir.exists()) destDir.mkdirs();
            // RENAME FILE
            String[] fileSplit = file.getName().split("\\.");
            filename = user.getUsername() + "."
                    + fileSplit[fileSplit.length - 1];
            target = FileSystems.getDefault().getPath(
                    destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
            );

            // SET NEW FILE PATH TO IMAGE
            Image img = new Image(file.toURI().toString());
            imgCircle.setFill(new ImagePattern(img));
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        deleteImg();
        FXRouter.goTo("register-page", user);
    }

    @FXML
    protected void onRemoveButtonClick() {
        deleteImg();
        setDefaultImage();
    }

    @FXML
    protected void onSkipButtonClick() throws IOException {
        deleteImg();
        user.setImgPath(null);
        user_accounts.addAccount(user);
        datasource.writeData(user_accounts);
        FXRouter.goTo("register-page-3");
    }

    @FXML
    protected void onContinueButtonClick() throws IOException {
        if (target != null) {
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            user.setImgPath(filename);
            user_accounts.addAccount(user);
            datasource.writeData(user_accounts);
        }
        FXRouter.goTo("register-page-3");
    }
}
