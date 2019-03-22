package cn.yescallop.aid.client.ui.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javax.swing.*;

public class MonitorController {

    @FXML
    private StackPane root;
    @FXML
    private ImageView screen;

    @FXML
    public void initialize(){
        root.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case W:
                    System.out.println("W");
                case A:
                    System.out.println("A");
                case S:
                    System.out.println("S");
                case D:
                    System.out.println("D");
                case UP:
                    System.out.println("UP");
                case DOWN:
                    System.out.println("DOWN");
                case RIGHT:
                    System.out.println("RIGHT");
                case LEFT:
                    System.out.println("LEFT");
            }
        });
    }
}
