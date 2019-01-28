package cn.yescallop.aid.client;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Magical Sheep
 */
public class Device extends RecursiveTreeObject<Device> {
    private StringProperty deviceNum;
    private StringProperty regTime;
    private StringProperty loginTime;

    public Device(String deviceNum, String regTime, String loginTime) {
        this.deviceNum = new SimpleStringProperty(deviceNum);
        this.regTime = new SimpleStringProperty(regTime);
        this.loginTime = new SimpleStringProperty(loginTime);
    }

    public StringProperty deviceNumProperty() {
        return deviceNum;
    }

    public StringProperty regTimeProperty() {
        return regTime;
    }

    public StringProperty loginTimeProperty() {
        return loginTime;
    }
}