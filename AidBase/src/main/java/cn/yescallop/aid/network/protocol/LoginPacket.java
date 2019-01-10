package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class LoginPacket extends Packet {

    public String username;
    public String password;

    @Override
    public int id() {
        return ID_LOGIN;
    }

    @Override
    public void readFrom(ByteBuf in) {
        username = PacketUtil.readUTF8(in);
        password = PacketUtil.readUTF8(in);
    }

    @Override
    public void writeTo(ByteBuf out) {
        PacketUtil.writeUTF8(out, username);
        PacketUtil.writeUTF8(out, password);
    }

    @Override
    public String toString() {
        return "LoginPacket{" +
                "username='" + username +
                "', password='" + password +
                "'}";
    }
}
