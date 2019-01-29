package cn.yescallop.aid.client.network;

import cn.yescallop.aid.network.protocol.DeviceListPacket;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.net.Inet4Address;
import java.util.Map;

/**
 * @author Magical Sheep
 */
public class DeviceInfo extends RecursiveTreeObject<DeviceInfo> {

    private SimpleIntegerProperty id;
    private StringProperty name;
    private SimpleMapProperty localAddresses;
    private SimpleLongProperty registerTime;

    private DeviceInfo(int id, String name, Map<Inet4Address, byte[]> localAddresses, long registerTime) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.localAddresses = new SimpleMapProperty(FXCollections.observableMap(localAddresses));
        this.registerTime = new SimpleLongProperty(registerTime);
    }

    public DeviceInfo(DeviceListPacket.DeviceInfo deviceInfo) {
        this(deviceInfo.id, deviceInfo.name, deviceInfo.localAddresses, deviceInfo.registerTime);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public SimpleMapProperty localAddressesProperty() {
        return localAddresses;
    }

    public SimpleLongProperty registerTimeProperty() {
        return registerTime;
    }
}