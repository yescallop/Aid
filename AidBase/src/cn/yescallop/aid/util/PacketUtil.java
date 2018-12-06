package cn.yescallop.aid.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author Scallop Ye
 */
public class PacketUtil {

    private PacketUtil() {
        //no instance
    }

    public static String readUTF8(ByteBuf in) {
        byte[] buf = new byte[in.readInt()];
        in.readBytes(buf);
        return new String(buf, StandardCharsets.UTF_8);
    }

    public static void writeUTF8(ByteBuf out, String str) {
        byte[] buf = str.getBytes(StandardCharsets.UTF_8);
        out.writeInt(buf.length);
        out.writeBytes(buf);
    }
}
