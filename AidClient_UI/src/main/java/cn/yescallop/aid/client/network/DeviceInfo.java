package cn.yescallop.aid.client.network;

import cn.yescallop.aid.network.protocol.DeviceListPacket;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Magical Sheep
 */
public class DeviceInfo extends RecursiveTreeObject<DeviceInfo> {

    private SimpleIntegerProperty id;
    private StringProperty name;
    private SimpleLongProperty registerTime;

    private DeviceInfo(int id, String name, long registerTime) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.registerTime = new SimpleLongProperty(registerTime);
    }

    public DeviceInfo(DeviceListPacket.DeviceInfo deviceInfo) {
        this(deviceInfo.id, deviceInfo.name, deviceInfo.registerTime);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public SimpleLongProperty registerTimeProperty() {
        return registerTime;
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public long getRegisterTime() {
        return registerTime.get();
    }
}