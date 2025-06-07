package cs211.project.controllers;


import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.Team;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamList;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AllTeamTableControllers {

    @FXML private Button addButton;
    @FXML private TableView allTeamTableView;

    @FXML private AnchorPane AddTeamAnchorPane;
    @FXML private TextField addTeamNameTextField;
    @FXML private TextField addTeamCapacityTextField;
    @FXML private Label errorLabel;
    @FXML private DatePicker start;
    @FXML private DatePicker end;


    @FXML private Pane blurPane;

    @FXML private AnchorPane whenSelectRecordAnchorPane;
    @FXML private AnchorPane editTeamAnchorPane;
    @FXML private Label errorEditLabel;
    @FXML private TextField editTeamNameTextField;
    @FXML private TextField editTeamCapacityTextField;

    private Team team;
    private TeamList teamList;
    private Datasource <TeamList> datasource;

    String[] data = ((String) FXRouter.getData()).split(",");
    String eventId = data[0];
    String username = data[1];
    String role = data[2];

    private UserAccount user;
    private Event event;
    private EventCollection events;
    private Datasource<EventCollection> eventListFileDatasourceDatasource;

    @FXML
    public void initialize() {

        if (role.equals("Leader")){
            addButton.setVisible(false);
        }else {
            addButton.setVisible(true);
        }
        AddTeamAnchorPane.setVisible(false);
        blurPane.setVisible(false);
        whenSelectRecordAnchorPane.setVisible(false);


        addTeamNameTextField.setText("");
        addTeamCapacityTextField.setText("");
        errorLabel.setText("");
        errorLabel.setVisible(false);

        editTeamAnchorPane.setVisible(false);
        errorEditLabel.setVisible(false);
        errorEditLabel.setText("");

        eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();
        event = events.findEventById(eventId);

        datasource = new AllTeamListFileDatasource("data", "all-team.csv");
        teamList = datasource.readData();

        Datasource<AccountCollection> accountDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountDatasource.readData();

        user = accountCollection.findAccount(username);

        for (Team team1 : teamList.getTeams()){
            if ( eventId.equals(team1.getEventId()) ){
                event.getTeamList().getTeams().add(team1);
            }
        }

        showTable(event.getTeamList());

        allTeamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {

            @Override
            public void changed(ObservableValue observable, Team oldValue, Team newValue) {
                if (newValue != null) {

                    team = event.getTeamList().findTeamByTeamId(newValue.getTeamId());

                    whenSelectRecordAnchorPane.setVisible(true);
                    blurPane.setVisible(true);
                    editTeamNameTextField.setText(newValue.getTeamName());
                    editTeamCapacityTextField.setText(newValue.getCapacity()+"");
                }
            }
        });
    }

    private void showTable(TeamList teamList) {


        allTeamTableView.setRowFactory(tv -> new CustomTableRow<>());
        allTeamTableView.getStyleClass().add("custom-table");

        // กำหนด column ให้มี title ว่า Team Name และใช้ค่าจาก attribute team ของ object EventDetail
        TableColumn<Team, String> teamNameColumn = new TableColumn<>("");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        teamNameColumn.setStyle("-fx-background-color: #AFD5F0;");
        teamNameColumn.setPrefWidth(850);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel = new Label("Team Name");
        headerLabel.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        //headerLabel.setTextAlignment(TextAlignment.CENTER); // จัดตำแหน่งข้อความกลาง
        teamNameColumn.setGraphic(headerLabel);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ Time
        teamNameColumn.setCellFactory(column -> {
            return new TableCell<Team, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        //setText(item);
                        setStyle("-fx-text-fill: blue;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        //setAlignment(Pos.CENTER); // จัดให้ตัวอักษรอยู่กึ่งกลาง
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

        TableColumn<Team, Integer> teamCapacityColumn = new TableColumn<>("");
        teamCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("teamCapacity"));

        teamCapacityColumn.setStyle("-fx-background-color: #AFD5F0;");
        teamCapacityColumn.setPrefWidth(140);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel2 = new Label("Capacity");
        headerLabel2.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        teamCapacityColumn.setGraphic(headerLabel2);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ Capacity
        teamCapacityColumn.setCellFactory(column -> {
            return new TableCell<Team, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: blue;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        setAlignment(Pos.CENTER); // จัดให้ตัวอักษรอยู่กึ่งกลาง
                        String itemAsString = String.valueOf(item);
                        Text text = new Text(itemAsString);
                        text.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        text.setFill(Paint.valueOf("blue")); // กำหนดสีข้อความใน Text
                        setGraphic(text);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        allTeamTableView.getColumns().clear();

        allTeamTableView.getColumns().add(teamCapacityColumn);
        allTeamTableView.getColumns().add(teamNameColumn);
        allTeamTableView.getItems().clear();

        // ใส่ข้อมูล Team ทั้งหมดจาก teamList ไปแสดงใน TableView
        for (Team team2 : teamList.getTeams()) {
            allTeamTableView.getItems().add(team2);
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAddNewTeamButtonClick() {

        LocalDate date = start.getValue(); // Replace this with your DateTime object
        LocalDate date1 = end.getValue();
        // Define a pattern for the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Customize the format pattern as needed
        // Format the LocalDateTime object to a String
        String formattedDateS = date.format(formatter);
        String formattedDateE = date1.format(formatter);



        if (addTeamNameTextField.getText().equals("") || addTeamCapacityTextField.getText().equals("")){
            errorLabel.setVisible(true);
            errorLabel.setText("*Please fill in all fields with complete information.");
        }
        boolean isAllDigits = addTeamCapacityTextField.getText().chars().allMatch(Character::isDigit);

        if (!addTeamNameTextField.getText().equals("") && !addTeamCapacityTextField.getText().equals("")){

            if (isAllDigits == true) {
                String addTeamNameSting = addTeamNameTextField.getText().trim();
                String addTeamCapacitySting = addTeamCapacityTextField.getText().trim();
                int capacityInt = Integer.parseInt(addTeamCapacitySting);
                teamList = datasource.readData();
                Team team1 = teamList.findTeamByTeamName(addTeamNameSting);
                if (team1 == null) {
                    teamList.addNewTeam(String.valueOf(Team.nextTeamId), addTeamNameSting, capacityInt, eventId , formattedDateS, formattedDateE);
                    datasource.writeData(teamList);
                    errorLabel.setVisible(false);

                    try {
                        FXRouter.goTo("all-team-table",eventId + "," + username + "," + role);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    errorLabel.setVisible(true);
                    errorLabel.setText("*Team names must not be duplicated.");
                }
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("*Capacity should only contain numerical values.");
            }
        }
    }

    @FXML
    public void onCancelButtonClick() {
        try {
            FXRouter.goTo("all-team-table",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onAddTeamButtonClick() {
        blurPane.setVisible(true);
        AddTeamAnchorPane.setVisible(true);
    }

    @FXML
    public void onSeeDetailButtonClick(){
        whenSelectRecordAnchorPane.setVisible(false);
        blurPane.setVisible(true);
        try {
            FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onEditButtonClick(){
        whenSelectRecordAnchorPane.setVisible(false);
        editTeamAnchorPane.setVisible(true);
        blurPane.setVisible(true);
    }
    @FXML
    public void OnXButtonClick(){
        try {
            FXRouter.goTo("all-team-table",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onConfirmEditButtonClick(){

        if (editTeamNameTextField.getText().equals("") || editTeamCapacityTextField.getText().equals("")){
            errorEditLabel.setText("*Please fill in all fields with complete information.");
            errorEditLabel.setVisible(true);
        }
        boolean isAllDigits = editTeamCapacityTextField.getText().chars().allMatch(Character::isDigit);
        if (!editTeamNameTextField.getText().equals("") && !editTeamCapacityTextField.getText().equals("")){

            if (isAllDigits == true) {
                String editTeamNameSting = editTeamNameTextField.getText().trim();
                String editTeamCapacitySting = editTeamCapacityTextField.getText().trim();
                //check ห้ามซ้ำ
                TeamList teamList5 =  datasource.readData();
                Team team3 = teamList5.findTeamByTeamName(editTeamNameSting);
                if (team3 == null) {
                    int capacityInt = Integer.parseInt(editTeamCapacitySting);
                    teamList.setTeamNameById(team.getTeamId(), editTeamNameSting);
                    teamList.setCapacityById(team.getTeamId(), capacityInt);

                    datasource.writeData(teamList);
                    errorLabel.setVisible(false);
                    try {
                        FXRouter.goTo("all-team-table", eventId + "," + username + "," + role);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    errorEditLabel.setVisible(true);
                    errorEditLabel.setText("*Team names must not be duplicated.");
                }
            } else {
                errorEditLabel.setVisible(true);
                errorEditLabel.setText("*Capacity should only contain numerical values.");
            }
        }
    }

    @FXML
    public void onTeamScheduleButtonClick(){
        try {
            FXRouter.goTo("team-schedule",team.getTeamId() + "," + username + "," + role);
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