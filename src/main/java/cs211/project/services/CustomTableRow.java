package cs211.project.services;

import javafx.scene.control.TableRow;


public class CustomTableRow<T> extends TableRow<T> {
    private static final double CUSTOM_ROW_HEIGHT = 50.0; // ความสูงแถว

    public CustomTableRow() {
        setMinHeight(CUSTOM_ROW_HEIGHT);
        setMaxHeight(CUSTOM_ROW_HEIGHT);
        setPrefHeight(CUSTOM_ROW_HEIGHT);
    }
}
