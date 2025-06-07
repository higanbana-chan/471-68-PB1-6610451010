package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;

public class TeamScheduleController {
    @FXML
    private TableView teamScheduleTableView;

    @FXML private AnchorPane whenSelectRecordAnchorPane;

    @FXML private TextField editNameActTextField;
    @FXML private TextField editDetailTextField;
    @FXML private AnchorPane editAnchorPane;
    @FXML private Label errorEditLabel;
    @FXML private Label LabelEventName;
    @FXML private Label LabelTeamName;
    @FXML private Button addButton;

    @FXML private TextField addNameActTextField;
    @FXML private TextField addDetailTextField;
    @FXML private AnchorPane addAnchorPane;
    @FXML private Label errorLabel;

    @FXML private Pane blurPane;

    private TeamScheduleCollection teamScheduleCollection;
    private Datasource <TeamScheduleCollection> datasource;
    private TeamSchedule teamSchedule;

    private EventCollection eventCollection;
    private Datasource <EventCollection> datasourceEvent;
    private Event event;

    String[] data = ((String) FXRouter.getData()).split(",");
    String teamId = data[0];
    String username = data[1];
    String role = data[2];

    private Team team;
    private TeamList teamList;
    private Datasource<TeamList> teamListDatasource;

    UserAccount userAccount;

    @FXML
    public void initialize() {

        whenSelectRecordAnchorPane.setVisible(false);

        editAnchorPane.setVisible(false);
        addAnchorPane.setVisible(false);
        blurPane.setVisible(false);
        errorEditLabel.setVisible(false);

        addNameActTextField.setText("");
        addDetailTextField.setText("");
        errorLabel.setText("");
        errorLabel.setVisible(false);

        teamListDatasource = new AllTeamListFileDatasource("data", "all-team.csv");
        teamList = teamListDatasource.readData();
        team = teamList.findTeamByTeamId(teamId);


        datasource = new TeamScheduleListFileDatasource("data", "team-schedule.csv");
        teamScheduleCollection = datasource.readData();

        datasourceEvent = new EventListFileDatasource("data","all-event.csv");
        eventCollection = datasourceEvent.readData();
        event = eventCollection.findEventById(team.getEventId());

        Datasource<AccountCollection> accountCollectionDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountCollectionDatasource.readData();
        userAccount = accountCollection.findAccount(username);

        for (TeamSchedule teamSchedule1 : teamScheduleCollection.getTeamSchedules()){
            if ( teamId.equals(teamSchedule1.getTeamId()) ){
                team.getTeamScheduleCollection().getTeamSchedules().add(teamSchedule1);
            }
        }



        LabelTeamName.setText(team.getTeamName());
        LabelEventName.setText(event.getName());

        showTable(team.getTeamScheduleCollection());

        if (role.equals("Member")) {
            addButton.setVisible(false);
            teamScheduleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TeamSchedule>() {
                @Override
                public void changed(ObservableValue observable, TeamSchedule oldValue, TeamSchedule newValue) {
                    try {
                        FXRouter.goTo("community-post-page",team.getTeamId() + "," + username + "," + role);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            teamScheduleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TeamSchedule>() {
                @Override
                public void changed(ObservableValue observable, TeamSchedule oldValue, TeamSchedule newValue) {
                    if (newValue != null && newValue.getStatus().equals("In progress")) {

                        teamSchedule = team.getTeamScheduleCollection().findTeamScheduleById(newValue.getIdRecord());

                        whenSelectRecordAnchorPane.setVisible(true);
                        blurPane.setVisible(true);

                        editNameActTextField.setText(newValue.getNameAct_teamSchedule());
                        editDetailTextField.setText(newValue.getDetail());

                    }
                }
            });
        }


    }

    private void showTable(TeamScheduleCollection teamScheduleCollection2) {
        teamScheduleTableView.setRowFactory(tv -> new CustomTableRow<>());
        teamScheduleTableView.getStyleClass().add("custom-table");

        TableColumn<EventSchedule, String> nameActColumn = new TableColumn<>("");
        nameActColumn.setCellValueFactory(new PropertyValueFactory<>("nameAct_teamSchedule"));

        nameActColumn.setStyle("-fx-background-color: #AFD5F0;");
        nameActColumn.setPrefWidth(300);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel = new Label("Activity Name");
        headerLabel.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        nameActColumn.setGraphic(headerLabel);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ Time
        nameActColumn.setCellFactory(column -> {
            return new TableCell<EventSchedule, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: black;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        Text text = new Text(item);
                        text.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        text.setFill(Paint.valueOf("black")); // กำหนดสีข้อความใน Text
                        setGraphic(text);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });

        // กำหนด column ให้มี title ว่า Detail และใช้ค่าจาก attribute detail ของ object EventDetail
        TableColumn<EventSchedule, String> detailColumn = new TableColumn<>("");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.setStyle("-fx-background-color: AFD5F0;");
        detailColumn.setPrefWidth(555);

        Label headerLabel2 = new Label("Detail");
        headerLabel2.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        detailColumn.setGraphic(headerLabel2);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ detail
        detailColumn.setCellFactory(column -> {
            return new TableCell<EventSchedule, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: black;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        Text text = new Text(item);
                        text.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        text.setFill(Paint.valueOf("black")); // กำหนดสีข้อความใน Text
                        setGraphic(text);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });

        TableColumn<EventSchedule, String> statusColumn = new TableColumn<>("");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusColumn.setStyle("-fx-background-color: #AFD5F0;");
        statusColumn.setPrefWidth(135);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel3 = new Label("Status");
        headerLabel3.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        statusColumn.setGraphic(headerLabel3);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์
        statusColumn.setCellFactory(column -> {
            return new TableCell<EventSchedule, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: black;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        setAlignment(Pos.CENTER); // จัดให้ตัวอักษรอยู่กึ่งกลาง
                        Text text = new Text(item);
                        text.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        if (item.equals("In progress")) text.setFill(Paint.valueOf("blue")); // กำหนดสีข้อความใน Text
                        else text.setFill(Paint.valueOf("green")); // กำหนดสีข้อความใน Text
                        setGraphic(text);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });


        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        teamScheduleTableView.getColumns().clear();
        teamScheduleTableView.getColumns().add(nameActColumn);
        teamScheduleTableView.getColumns().add(detailColumn);
        teamScheduleTableView.getColumns().add(statusColumn);

        teamScheduleTableView.getItems().clear();

        // ใส่ข้อมูล EventDetail ทั้งหมดจาก EventsDetailList ไปแสดงใน TableView
        for (TeamSchedule teamSchedule3 : teamScheduleCollection2.getTeamSchedules()) {
            teamScheduleTableView.getItems().add(teamSchedule3);

        }
    }


    @FXML
    public void onDeleteButtonClick() {
        for (TeamSchedule teamScheduleD : teamScheduleCollection.getTeamSchedules()){
            if ( teamSchedule.getIdRecord() == teamScheduleD.getIdRecord() ){
                teamScheduleCollection.getTeamSchedules().remove(teamScheduleD);
                break;
            }
        }
        datasource.writeData(teamScheduleCollection);
        try {
            FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onEditButtonClick() {
        whenSelectRecordAnchorPane.setVisible(false);
        blurPane.setVisible(false);
        editAnchorPane.setVisible(true);
        blurPane.setVisible(true);
    }

    @FXML
    public void OnXButtonClick(){
        try {
            FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCommunityButtonClick() {
        try {
            FXRouter.goTo("community-post-page",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBackButtonClick() {
        if (role.equals("Member")){
            try {
                FXRouter.goTo("main-page");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                FXRouter.goTo("all-team-table",team.getEventId() + "," + username + "," + role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    public void onCancelButtonClick() {
        try {
            FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onConfirmEditButtonClick() {

        if (!editNameActTextField.getText().equals("") && !editDetailTextField.getText().equals("")){
            String nameActString = editNameActTextField.getText().trim();
            String detailString = editDetailTextField.getText().trim();

            teamScheduleCollection.setNameActTeamScheduleById(teamSchedule.getIdRecord(), nameActString);
            teamScheduleCollection.setDetailById(teamSchedule.getIdRecord(), detailString);

            editDetailTextField.clear();
            editNameActTextField.clear();

            datasource.writeData(teamScheduleCollection);

            try {
                FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorEditLabel.setVisible(true);
            errorEditLabel.setText("*Please fill in all fields with complete information.");
        }

    }

    public void onAddButtonClick() {
        blurPane.setVisible(true);
        addAnchorPane.setVisible(true);
    }

    public void onAddNewButtonClick() {
        if (addNameActTextField.getText().equals("") || addDetailTextField.getText().equals("")){
            errorLabel.setVisible(true);
            errorLabel.setText("*Please fill in all fields with complete information.");
        }
        if (!addNameActTextField.getText().equals("") && !addDetailTextField.getText().equals("")){
            String addNameActSting = addNameActTextField.getText().trim();
            String addDetailSting = addDetailTextField.getText().trim();
            String addStatusSting = "In progress";
            //teamScheduleCollection = datasource.readData();
            teamScheduleCollection.addNewTeamSchedule(String.valueOf(TeamSchedule.nextTeamScheduleId),addNameActSting,addDetailSting,addStatusSting,team.getEventId(),teamId);
            datasource.writeData(teamScheduleCollection);
            errorLabel.setVisible(false);
            try {
                FXRouter.goTo("team-schedule", team.getTeamId() + "," + username + "," + role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void onEndActButtonClick(){
        teamScheduleCollection.setStatusById(teamSchedule.getIdRecord(),"Completed");
        datasource.writeData(teamScheduleCollection);

        try {
            FXRouter.goTo("team-schedule", team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onHomePageClick() {
        try {
            FXRouter.goTo("main-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
