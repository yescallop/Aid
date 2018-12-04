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

    public static CharSequence readUTF8(ByteBuf in) {
        return in.readCharSequence(in.readInt(), StandardCharsets.UTF_8);
    }

    public static void writeUTF8(ByteBuf out, CharSequence sequence) {
        out.writeInt(sequence.length());
        ByteBufUtil.writeUtf8(out, sequence);
    }
}
