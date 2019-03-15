package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class VideoInfoPacket extends Packet {

    public int codecId;
    public int pixFmt;
    public int width;
    public int height;
    public int sarDen;
    public int sarNum;

    @Override
    public int id() {
        return ID_VIDEO_INFO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        codecId = in.readInt();
        pixFmt = in.readInt();
        width = in.readInt();
        height = in.readInt();
        sarDen = in.readInt();
        sarNum = in.readInt();
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeInt(codecId);
        out.writeInt(pixFmt);
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(sarDen);
        out.writeInt(sarNum);
    }
}
