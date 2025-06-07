package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public class TableRoleController {
    @FXML private Label teamNameLabel;
    @FXML private Label peopleLabel;
    @FXML private TableView roleTableView;
    @FXML private AnchorPane nominateAnchorPane;
    @FXML private Pane blurPane;
    @FXML private ImageView leaderImageView;
    @FXML private Button releaseButton;
    @FXML private ImageView userImageView;
    @FXML private Label usernameLabel;
    @FXML private Label accountNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label errorLabel;
    @FXML private Button releaseBanButton;
    @FXML private ImageView bannedImageView;
    @FXML private Button leaderButton;


    String[] data = ((String) FXRouter.getData()).split(",");
    String teamId = data[0];
    String username = data[1];
    String role = data[2];

    private RoleCollection roleCollection;
    private Datasource <RoleCollection> datasource;
    private Role roleObj;

    private Team team;
    private TeamList teamList;
    private Datasource<TeamList> teamListDatasource;

    private EventCollection eventCollection;
    private Datasource <EventCollection> datasourceEvent;
    private Event event;

    private AccountCollection accountCollection;
    private Datasource <AccountCollection> datasourceAccount;
    private UserAccount userAccount;



    private int totalPeople;

    @FXML
    public void initialize() {
        nominateAnchorPane.setVisible(false);
        blurPane.setVisible(false);
        errorLabel.setText("");
        bannedImageView.setVisible(false);
        leaderButton.setVisible(true);

        releaseBanButton.setVisible(false);
        leaderImageView.setVisible(false);
        releaseButton.setVisible(false);

        teamListDatasource = new AllTeamListFileDatasource("data", "all-team.csv");
        teamList = teamListDatasource.readData();
        team = teamList.findTeamByTeamId(teamId);

        datasourceEvent = new EventListFileDatasource("data","all-event.csv");
        eventCollection = datasourceEvent.readData();
        event = eventCollection.findEventById(team.getEventId());

        datasource = new RoleListFileDatasource("data","role.csv");
        roleCollection = datasource.readData();

        datasourceAccount = new UserAccountDatasource("data","user-accounts.csv");
        accountCollection = datasourceAccount.readData();

        RoleCollection roleCollection2 = new RoleCollection();
        for (Role roleObj1 : roleCollection.getRoles()){
            if (  teamId.equals(roleObj1.getEventId() )){ // eventId เก็บช่องเดียวกับ teamId
                roleCollection2.addRole(roleObj1.getUsername(),roleObj1.getEventId(),roleObj1.getRole());
            }
        }


        AccountCollection accountCollection2 = new AccountCollection();
        for (UserAccount userObj : accountCollection.getUserAccounts()) {
            for (Role roleObj1 : roleCollection2.getRoles()){
                if (userObj.getUsername().equals(roleObj1.getUsername())){
                    accountCollection2.addAccount(userObj);
                }

            }
        }

        ShowNameAndRoleCollection showNameAndRoleCollection = new ShowNameAndRoleCollection();
        for (UserAccount userObj6 : accountCollection2.getUserAccounts()) {
            for (Role roleObj6 : roleCollection2.getRoles()) {
                if (userObj6.getUsername().equals(roleObj6.getUsername()) && !(roleObj6.getRole().equals("Participant"))){
                    ShowNameAndRole showNameAndRole = new ShowNameAndRole(
                            userObj6.getUsername(),
                            userObj6.getFirstName(),
                            userObj6.getLastName(),
                            roleObj6.getRole()
                    );
                    totalPeople += 1;
                    showNameAndRoleCollection.addShowNameAndRole(showNameAndRole);
                    break;
                }
            }
        }

        teamNameLabel.setText(team.getTeamName());
        peopleLabel.setText(totalPeople + "/" + team.getCapacity());

        showTable(showNameAndRoleCollection);

        roleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ShowNameAndRole>() {
            @Override
            public void changed(ObservableValue observable, ShowNameAndRole oldValue, ShowNameAndRole newValue) {
                if (newValue != null) {
                    // ตรวจสอบว่า userAccount ไม่เป็น null ก่อนที่จะใช้งาน
                    roleObj = roleCollection2.findUserByUsername(newValue.getUsername());
                    userAccount = accountCollection2.findAccount(newValue.getUsername());

                    if (userAccount != null && roleObj != null) {
                        nominateAnchorPane.setVisible(true);
                        blurPane.setVisible(true);
                        accountNameLabel.setText(userAccount.getFirstName() + "  " + userAccount.getLastName());
                        usernameLabel.setText(userAccount.getUsername());
                        emailLabel.setText(userAccount.getEmail());
                        String imagePath = userAccount.getImgPath();
                        try {
                            Image img = new Image("file:" + imagePath);
                            userImageView.setImage(img);
                        } catch (InvalidPathException | NullPointerException e) {
                            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                            userImageView.setImage(new Image(String.valueOf(img)));
                        } catch (IllegalArgumentException e) {
                            Image img = new Image(getClass().getResourceAsStream("/cs211/project/views/img/default_user_profile.png"));
                            userImageView.setImage(new Image(String.valueOf(img)));
                        }
                        userImageView.setVisible(true);

                        if (roleObj.getRole().equals("Leader")){
                            leaderImageView.setVisible(true);
                            releaseButton.setVisible(true);
                        }
                        if(roleObj.getRole().equals("BAN")){
                            releaseBanButton.setVisible(true);
                            bannedImageView.setVisible(true);
                            leaderButton.setVisible(false);
                        }
                    }
                }
            }
        });


    }

    private void showTable(ShowNameAndRoleCollection showNameAndRoleCollection) {
        roleTableView.setRowFactory(tv -> new CustomTableRow<>());
        roleTableView.getStyleClass().add("custom-table");


        TableColumn<ShowNameAndRole, String> roleColumn = new TableColumn<>("");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        roleColumn.setStyle("-fx-background-color: #AFD5F0;");
        roleColumn.setPrefWidth(100);

        Label headerLabel3 = new Label("Role");
        headerLabel3.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        roleColumn.setGraphic(headerLabel3);

        roleColumn.setCellFactory(column -> {
            return new TableCell<ShowNameAndRole, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        if (item.equals("Member"))  setStyle("-fx-text-fill: blue;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        else if (item.equals("Leader")) setStyle("-fx-text-fill: orange;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        else setStyle("-fx-text-fill: red;"); // กำหนดสีให้กับข้อความในคอลัมน์
                        setAlignment(Pos.CENTER); // จัดให้ตัวอักษรอยู่กึ่งกลาง
                        Text text = new Text(item);
                        text.setFont(Font.font("Angsana new", 25)); // ปรับขนาดตัวอักษรตามที่ต้องการ
                        if (item.equals("Member")) text.setFill(Paint.valueOf("blue")); // กำหนดสีข้อความใน Text
                        else if (item.equals("Leader")) text.setFill(Paint.valueOf("orange")); // กำหนดสีข้อความใน Text
                        else text.setFill(Paint.valueOf("red")); // กำหนดสีข้อความใน Text
                        setGraphic(text);
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
        });


        TableColumn<ShowNameAndRole, String> usernameColumn = new TableColumn<>("");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        usernameColumn.setStyle("-fx-background-color: #AFD5F0;");
        usernameColumn.setPrefWidth(300);

        Label headerLabel = new Label("Username");
        headerLabel.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        usernameColumn.setGraphic(headerLabel);

        usernameColumn.setCellFactory(column -> {
            return new TableCell<ShowNameAndRole, String>() {
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

        TableColumn<ShowNameAndRole, String> firstNameColumn = new TableColumn<>("");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        firstNameColumn.setStyle("-fx-background-color: #AFD5F0;");
        firstNameColumn.setPrefWidth(300);

        Label headerLabel1 = new Label("First Name");
        headerLabel1.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        firstNameColumn.setGraphic(headerLabel1);

        firstNameColumn.setCellFactory(column -> {
            return new TableCell<ShowNameAndRole, String>() {
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

        TableColumn<ShowNameAndRole, String> lastNameColumn = new TableColumn<>("");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        lastNameColumn.setStyle("-fx-background-color: #AFD5F0;");
        lastNameColumn.setPrefWidth(300);

        Label headerLabel2 = new Label("Surname");
        headerLabel2.setFont(Font.font("Angsana new", FontWeight.BOLD, 40)); // ปรับแต่งขนาดและตัวหนา
        lastNameColumn.setGraphic(headerLabel2);

        lastNameColumn.setCellFactory(column -> {
            return new TableCell<ShowNameAndRole, String>() {
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


        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่

        roleTableView.getColumns().clear();
        roleTableView.getColumns().add(usernameColumn);
        roleTableView.getColumns().add(firstNameColumn);
        roleTableView.getColumns().add(lastNameColumn);
        roleTableView.getColumns().add(roleColumn);

        roleTableView.getItems().clear();


        for (ShowNameAndRole show : showNameAndRoleCollection.getItems()) {
            roleTableView.getItems().add(show);
        }

    }




    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("all-team-table",team.getEventId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnXButtonClick(){
        try {
            FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
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

    @FXML
    public void onTeamCommunityButtonClick(){
        try {
            FXRouter.goTo("community-post-page",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void onBanButtonClick(){
        roleCollection.setRoleByEventIdAndUsername(userAccount.getUsername(),roleObj.getEventId(),"BAN");
        datasource.writeData(roleCollection);

        try {
            FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onReleaseButtonClick(){
        roleCollection.setRoleByEventIdAndUsername(userAccount.getUsername(),roleObj.getEventId(),"Member");
        datasource.writeData(roleCollection);

        try {
            FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onLeaderButtonClick(){
        roleCollection = datasource.readData();
        if (roleCollection.findLeaderInTeam(teamId)){
            errorLabel.setVisible(true);
            errorLabel.setText("Only one leader!!");
        } else {
            roleCollection.setRoleByEventIdAndUsername(userAccount.getUsername(),roleObj.getEventId(),"Leader");
            datasource.writeData(roleCollection);

            try {
                FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    public void onMemberButtonClick(){
        roleCollection.setRoleByEventIdAndUsername(userAccount.getUsername(),roleObj.getEventId(),"Member");
        datasource.writeData(roleCollection);

        try {
            FXRouter.goTo("table-role",team.getTeamId() + "," + username + "," + role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}