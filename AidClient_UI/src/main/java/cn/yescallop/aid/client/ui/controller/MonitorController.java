package cn.yescallop.aid.client.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


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
                    break;
                case A:
                    System.out.println("A");
                    break;
                case S:
                    System.out.println("S");
                    break;
                case D:
                    System.out.println("D");
                    break;
                case UP:
                    System.out.println("UP");
                    break;
                case DOWN:
                    System.out.println("DOWN");
                    break;
                case RIGHT:
                    System.out.println("RIGHT");
                    break;
                case LEFT:
                    System.out.println("LEFT");
            }
        });
    }
    public ImageView getScreen() {
        return screen;
    }
}
