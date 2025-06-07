package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterDoneController {
    @FXML
    protected void onDoneButtonClick() throws IOException {
        FXRouter.goTo("login-page");
    }
}
