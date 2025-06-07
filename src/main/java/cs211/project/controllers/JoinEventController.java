package cs211.project.controllers;

import cs211.project.models.Role;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.RoleCollection;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.RoleListFileDatasource;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;

public class JoinEventController {
    private Datasource<EventCollection> e_datasource;
    private EventCollection events;
    private Datasource<RoleCollection> datasource;
    private RoleCollection events_roles;
    private ArrayList<String> data;

    @FXML
    private void initialize() {
        datasource = new RoleListFileDatasource("data", "role.csv");
        events_roles = datasource.readData();
        e_datasource = new EventListFileDatasource("data", "all-event.csv");
        events = e_datasource.readData();

    }

    @FXML
    private void onJoinParticipantClick() throws IOException {
        Role found = events_roles.findUserByEventIdAndUsername(data.get(0), data.get(1));
        if (found == null) {
            events_roles.addRole(data.get(0), data.get(1), "Participant");
            events.setCurrentParticipant(data.get(1));
            datasource.writeData(events_roles);
            e_datasource.writeData(events);
            FXRouter.goTo("main-page");
        }
    }

    @FXML
    private void onJoinMemberClick() throws IOException {
        FXRouter.goTo("join-teams-page");
    }
}