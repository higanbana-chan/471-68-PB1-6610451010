package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class JoinTeamsController {

    @FXML
    private VBox teamsContainer;

    private String[] data = ((String) FXRouter.getData()).split(",");

    private String username = data[0];
    private String eventId = data[1];

    public void initialize() {
        Datasource<EventCollection> eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        EventCollection events = eventListFileDatasourceDatasource.readData();

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();

        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");
        RoleCollection roleCollection = roleCollectionDatasource.readData();

        Datasource<TeamList> teamListDatasource = new AllTeamListFileDatasource("data", "all-team.csv");
        TeamList teamList = teamListDatasource.readData();

        UserAccount userAccount = accountCollection.findAccount(username);

        events.addUserToEvent(accountCollection, roleCollection);
        teamList.addUserToTeam(accountCollection, roleCollection);

        for (Team team : teamList.getTeams()) {
            if (team.getEventId().equals(eventId) && team.recruit()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/team-component.fxml"));
                    HBox teamComponent = fxmlLoader.load();
                    TeamComponentController teamComponentController = fxmlLoader.getController();
                    teamComponentController.setData(team, userAccount);
                    teamsContainer.getChildren().add(teamComponent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    @FXML
    private void onClickBackButton() {
        try {
            FXRouter.goTo("main-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
