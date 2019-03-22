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
    private SimpleLongProperty registerTime;

    private Map<Inet4Address, byte[]> localAddresses;
    private int port;

    private DeviceInfo(int id, String name, long registerTime, Map<Inet4Address, byte[]> localAddresses, int port) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.registerTime = new SimpleLongProperty(registerTime);
        this.localAddresses = localAddresses;
        this.port = port;
    }

    public DeviceInfo(DeviceListPacket.DeviceInfo deviceInfo) {
        this(deviceInfo.id, deviceInfo.name, deviceInfo.registerTime, deviceInfo.localAddresses, deviceInfo.port);
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

    public Map<Inet4Address, byte[]> getLocalAddresses() {
        return localAddresses;
    }

    public int getPort() {
        return port;
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