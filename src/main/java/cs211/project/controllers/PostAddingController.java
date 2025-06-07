package cs211.project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import cs211.project.services.FXRouter;


import java.io.*;
import java.io.IOException;

public class PostAddingController {

    @FXML
    private TextField postTextField;

    private static final String post_csv_path = "data/postDetails.csv";

    private String teamId;
    private String username;


    public void initialize() {
        Object data = FXRouter.getData();

        if (data instanceof String) {
            String[] splitData = ((String) data).split(",");
            if (splitData.length >= 2) {
                this.username = splitData[0];
                this.teamId = splitData[1];
            }
        }
    }

    @FXML
    private void postButtonClick() {
        String postText = postTextField.getText();

        if (postText != null && !postText.trim().isEmpty()) {
            int nextId = getNextId();
            appendToCSV(nextId, postText);
            postTextField.clear();
        }
        goBack();
    }
    private void goBack() {
        try {
            FXRouter.goTo("community-post-page");
        } catch (IOException e) {

        }
    }

    private int getNextId() {
        int lastId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    lastId = Integer.parseInt(values[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastId + 1;
    }


    private void appendToCSV(int id, String postText) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(post_csv_path, true))) {
            bw.write("\n" + id + "," + postText + "," + this.teamId + "," + this.username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickGoBack(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("community-post-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
