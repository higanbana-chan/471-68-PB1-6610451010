package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ManageEventController {

    @FXML
    private GridPane eventBox;

    private EventCollection events;

    private Stage alert;

    private UserAccount user;

    public void initialize() {
        Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        RoleCollection roleCollection = roleCollectionDatasource.readData();

        events.addUserToEvent(accountCollection, roleCollection);

        alert = new Stage();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Custom Alert");

        user = (UserAccount) FXRouter.getData();

        try {
            int row = 1;
            for (Event event : events.getEvents()) {
                if (event.isOrganizer(user.getUsername())) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-manage-component.fxml"));
                    HBox eventComponent = fxmlLoader.load();
                    EventManageComponentController eventManageComponentController = fxmlLoader.getController();
                    eventManageComponentController.setData(event, alert, "Organizer", user.getUsername());
                    eventBox.add(eventComponent, 0, row++);
                    GridPane.setMargin(eventComponent, new Insets(10));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXRouter.goTo("main-page");
    }
}
