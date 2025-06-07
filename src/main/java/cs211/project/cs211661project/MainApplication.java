package cs211.project.cs211661project;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        FXRouter.bind(this, stage, "CS211 661 Project", 1200, 750);
        configRoute();
        FXRouter.goTo("login-page");
    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("admin-page", resourcesPath + "admin-page.fxml");
        FXRouter.when("admin-user-detail", resourcesPath + "admin-user-detail.fxml");
        FXRouter.when("login-page", resourcesPath + "login-page.fxml");
        FXRouter.when("register-page", resourcesPath + "register-page.fxml");
        FXRouter.when("register-page-2", resourcesPath + "register-page-2.fxml");
        FXRouter.when("register-page-3", resourcesPath + "register-page-3.fxml");
        FXRouter.when("main-page", resourcesPath + "main-page.fxml");
        FXRouter.when("settings-page", resourcesPath + "settings/settings-page.fxml");
        FXRouter.when("manage-event-page", resourcesPath + "manage-event.fxml");
        FXRouter.when("join-events-page", resourcesPath + "join-events.fxml");
        FXRouter.when("join-teams-page", resourcesPath + "join-teams.fxml");
        FXRouter.when("community-post-page", resourcesPath + "community-main.fxml");
        FXRouter.when("post-adding-page", resourcesPath + "post-adding.fxml");
        FXRouter.when("manage-event", resourcesPath + "manage-event.fxml");
        FXRouter.when("event-detail", resourcesPath + "event-detail.fxml");
        FXRouter.when("create-event-page", resourcesPath + "create-event-page.fxml");
        FXRouter.when("create-event-confirm", resourcesPath + "create-event-confirm.fxml");
        FXRouter.when("create-event-missing-field", resourcesPath + "create-event-missing-field.fxml");
        FXRouter.when("event-schedule-manager", resourcesPath + "event-schedule-manager.fxml");
        FXRouter.when("edit-event-manager", resourcesPath + "edit-event-manager.fxml");
        FXRouter.when("all-team-table", resourcesPath + "all-team-table.fxml");
        FXRouter.when("team-schedule", resourcesPath + "team-schedule.fxml");
        FXRouter.when("team-members-schedule", resourcesPath + "team-members-schedule.fxml");
        FXRouter.when("comment-views", resourcesPath + "comment-views.fxml");
        FXRouter.when("event-schedule-user", resourcesPath + "event-schedule-user.fxml");
        FXRouter.when("table-role", resourcesPath + "table-role.fxml");
        FXRouter.when("show-participant", resourcesPath + "show-participant.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}