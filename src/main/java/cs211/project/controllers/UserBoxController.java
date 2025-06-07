package cs211.project.controllers;

import cs211.project.models.UserAccount;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class UserBoxController {
    @FXML private Label userName;

    @FXML private Circle profilePicture;

    private UserAccount user;

    public void setData(UserAccount account) {
        user = account;
        userName.setText(account.getUsername());

        String imagePath = account.getImgPath();
        try {
            FileInputStream imgStream = new FileInputStream(imagePath);
            Image img = new Image(imgStream);
            imgStream.close();
            profilePicture.setFill(new ImagePattern(img));
        } catch (FileNotFoundException | InvalidPathException | NullPointerException e) {
            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
            profilePicture.setFill(new ImagePattern(img));
        } catch (IllegalArgumentException e) {
            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
            profilePicture.setFill(new ImagePattern(img));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onClickAdminButtonDetail() throws IOException {
        FXRouter.goTo("admin-user-detail", user);
    }


}
