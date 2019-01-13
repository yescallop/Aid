package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class DeviceListPacket extends Packet {
    @Override
    public int id() {
        return ID_DEVICE_LIST;
    }

    @Override
    public void readFrom(ByteBuf in) {

    }

    @Override
    public void writeTo(ByteBuf out) {

    }
}
