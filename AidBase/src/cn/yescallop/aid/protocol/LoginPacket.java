package cn.yescallop.aid.protocol;

import cn.yescallop.aid.util.PacketUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class LoginPacket extends Packet {
    public static final int ID = 0x00;

    public CharSequence username;
    public CharSequence password;

    public LoginPacket() {
        super(ID);
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
                "', password=" + password +
                "'}";
    }
}
