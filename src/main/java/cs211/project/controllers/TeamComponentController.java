package cs211.project.controllers;

import cs211.project.models.Team;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.RoleListFileDatasource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class TeamComponentController {
    @FXML
    private Label membersLabel;

    @FXML
    private Label teamNameLabel;

    @FXML
    private Button joinButton;

    public void setData(Team team, UserAccount userAccount) {
        Datasource<RoleCollection> roleCollectionDatasource = new RoleListFileDatasource("data", "role.csv");

        teamNameLabel.setText(team.getTeamName());
        membersLabel.setText(team.getMembers().getUserAccounts().size() + " / " + team.getTeamCapacity());

        joinButton.setOnAction((e) -> {
            RoleCollection roleCollection = roleCollectionDatasource.readData();
            roleCollection.addRole(userAccount.getUsername(), team.getTeamId(), "Member");
            roleCollectionDatasource.writeData(roleCollection);
            try {
                FXRouter.goTo("join-events-page", userAccount);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
