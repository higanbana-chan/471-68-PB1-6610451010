package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.EventScheduleList;
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



public class EventsScheduleTableController {
    @FXML private TableView eventDetailTableView;
    @FXML private AnchorPane whenSelectRecordAnchorPane;

    @FXML private TextField editTimeTextField;
    @FXML private TextField editNameTextField;
    @FXML private TextField editDetailTextField;
    @FXML private AnchorPane editAnchorPane;
    @FXML private Label errorEditLabel;

    @FXML private TextField addTimeTextField;
    @FXML private TextField addNameTextField;
    @FXML private TextField addDetailTextField;
    @FXML private AnchorPane addAnchorPane;
    @FXML private Label errorLabel;

    @FXML private Button addButton;
    @FXML private Button teamsButton;
    @FXML private Button participantsListButton;
    @FXML private Button teamsLeaderButton;

    @FXML private Pane blurPane;
    private EventScheduleList eventScheduleList;
    private Datasource <EventScheduleList> datasource;
    private EventSchedule eventSchedule;

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
        Datasource<AccountCollection> accountDatasource = new UserAccountDatasource("data", "user-accounts.csv");
        AccountCollection accountCollection = accountDatasource.readData();

        user = accountCollection.findAccount(username);
        teamsLeaderButton.setVisible(false);
        whenSelectRecordAnchorPane.setVisible(false);

        editAnchorPane.setVisible(false);
        addAnchorPane.setVisible(false);
        blurPane.setVisible(false);
        errorEditLabel.setVisible(false);

        addTimeTextField.setText("");
        addDetailTextField.setText("");
        addNameTextField.setText("");
        errorLabel.setText("");
        errorLabel.setVisible(false);

        eventListFileDatasourceDatasource = new EventListFileDatasource("data", "all-event.csv");
        events = eventListFileDatasourceDatasource.readData();
        event = events.findEventById(eventId);

        datasource = new EventsDetailListFileDatasource("data", "event-schedule-manager.csv");
        eventScheduleList = datasource.readData();

        for (EventSchedule eventSchedule6 : eventScheduleList.getEventDetails()){
            if ( eventId.equals(eventSchedule6.getEventId()) ){
                event.getScheduleList().getEventDetails().add(eventSchedule6);
            }
        }

        showTable(event.getScheduleList());

        if (role.equals("Organizer")) {
            eventDetailTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EventSchedule>() {
                @Override
                public void changed(ObservableValue observable, EventSchedule oldValue, EventSchedule newValue) {
                    if (newValue != null) {
                    eventSchedule = event.getScheduleList().findEventDetailById(newValue.getIdRecord());
                    whenSelectRecordAnchorPane.setVisible(true);
                    blurPane.setVisible(true);

                    editTimeTextField.setText(newValue.getTime());
                    editNameTextField.setText(newValue.getNameAct());
                    editDetailTextField.setText(newValue.getDetail());

                    }
                }
            });
        } else if (role.equals("Leader")){
            addButton.setVisible(false);
            teamsLeaderButton.setVisible(true);
            teamsButton.setVisible(false);
            participantsListButton.setVisible(false);
        } else {
            addButton.setVisible(false);
            teamsButton.setVisible(false);
            participantsListButton.setVisible(false);
        }
    }

    private void showTable(EventScheduleList eventScheduleList2) {
        eventDetailTableView.setRowFactory(tv -> new CustomTableRow<>());
        eventDetailTableView.getStyleClass().add("custom-table");

        // กำหนด column ให้มี title ว่า Time และใช้ค่าจาก attribute time ของ object EventDetail
        TableColumn<EventSchedule, String> timeColumn = new TableColumn<>("");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        timeColumn.setStyle("-fx-background-color: #AFD5F0;");
        timeColumn.setPrefWidth(135);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel = new Label("Time");
        headerLabel.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        timeColumn.setGraphic(headerLabel);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ Time
        timeColumn.setCellFactory(column -> {
            return new TableCell<EventSchedule, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: blue;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        setAlignment(Pos.CENTER); // จัดให้ตัวอักษรอยู่กึ่งกลาง
                        Text text = new Text(item);
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


        TableColumn<EventSchedule, String> nameColumn = new TableColumn<>("");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameAct"));

        nameColumn.setStyle("-fx-background-color: #AFD5F0;");
        nameColumn.setPrefWidth(300);

        // สร้าง Label สำหรับแสดงข้อความ
        Label headerLabel3 = new Label("Activity name");
        headerLabel3.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        nameColumn.setGraphic(headerLabel3);

        // กำหนด cell factory เพื่อกำหนดสีของข้อความในคอลัมน์ Time
        nameColumn.setCellFactory(column -> {
            return new TableCell<EventSchedule, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setStyle("-fx-text-fill: black;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        Text text3 = new Text(item);
                        text3.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        text3.setFill(Paint.valueOf("black")); // กำหนดสีข้อความใน Text
                        setGraphic(text3);
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
                        Text text2 = new Text(item);
                        text2.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        text2.setFill(Paint.valueOf("black")); // กำหนดสีข้อความใน Text
                        setGraphic(text2);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        eventDetailTableView.getColumns().clear();
        eventDetailTableView.getColumns().add(timeColumn);
        eventDetailTableView.getColumns().add(nameColumn);
        eventDetailTableView.getColumns().add(detailColumn);

        eventDetailTableView.getItems().clear();

        // ใส่ข้อมูล EventDetail ทั้งหมดจาก EventsDetailList ไปแสดงใน TableView
        for (EventSchedule eventSchedule2 : eventScheduleList2.getEventDetails()) {
            eventDetailTableView.getItems().add(eventSchedule2);

        }
    }


    @FXML
    public void onDeleteButtonClick() {
        for (EventSchedule eventScheduleD : eventScheduleList.getEventDetails()){
            if ( eventSchedule.getIdRecord() == eventScheduleD.getIdRecord() ){
                eventScheduleList.getEventDetails().remove(eventScheduleD);
                break;
            }
        }
        datasource.writeData(eventScheduleList);
        try {
            FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
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
            FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onBackButtonClick() {
        if (role.equals("Organizer")) {
            try {
                FXRouter.goTo("manage-event-page", user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                FXRouter.goTo("join-events-page", user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    public void onParticipantsButtonClick() {
        try {
            FXRouter.goTo("show-participant",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onTeamButtonClick() {
        try {
            FXRouter.goTo("all-team-table",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onTeamLeaderButtonClick() {
        try {
            FXRouter.goTo("all-team-table",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancelButtonClick() {
        try {
            FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onConfirmEditButtonClick() {

        if (!editTimeTextField.getText().equals("") && !editDetailTextField.getText().equals("") && !editNameTextField.getText().equals("")){
            String timeString = editTimeTextField.getText().trim();
            String detailString = editDetailTextField.getText().trim();
            String nameString = editNameTextField.getText().trim();

            eventScheduleList.setTimeById(eventSchedule.getIdRecord(), timeString);
            eventScheduleList.setDetailById(eventSchedule.getIdRecord(), detailString);
            eventScheduleList.setNameById(eventSchedule.getIdRecord(), nameString);

            editDetailTextField.clear();
            editTimeTextField.clear();
            editNameTextField.clear();

            datasource.writeData(eventScheduleList);

            try {
                FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
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
        if (addTimeTextField.getText().equals("") || addDetailTextField.getText().equals("") || addNameTextField.getText().equals("")){
            errorLabel.setVisible(true);
            errorLabel.setText("*Please fill in all fields with complete information.");
        }
        if (!addTimeTextField.getText().equals("") && !addDetailTextField.getText().equals("") && !addNameTextField.getText().equals("")) {
            String addTimeSting = addTimeTextField.getText().trim();
            String addDetailSting = addDetailTextField.getText().trim();
            String addNameSting = addNameTextField.getText().trim();
            eventScheduleList = datasource.readData();
            eventScheduleList.addNewEventsDetail (addTimeSting,addNameSting,addDetailSting,event.getId());
            datasource.writeData(eventScheduleList);
            errorLabel.setVisible(false);
            try {
                FXRouter.goTo("event-schedule-manager",eventId + "," + username + "," + role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void onHomePageClick() {
        try {
            FXRouter.goTo("main-page", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
