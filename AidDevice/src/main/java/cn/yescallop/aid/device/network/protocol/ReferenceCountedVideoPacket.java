package cn.yescallop.aid.device.network.protocol;

import cn.yescallop.aid.network.protocol.VideoPacket;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 * A non-thread-safe reference-counted implementation of VideoPacket
 */
public class ReferenceCountedVideoPacket extends VideoPacket implements ReferenceCounted {

    private int refCnt = 1;
    public AVBufferRef bufRef;

    @Override
    public void writeTo(ByteBuf out) {
        out.writeLong(time);
        out.writeInt(size);
        ByteBuffer data = bufRef.data()
                .position(0)
                .limit(bufRef.size())
                .asByteBuffer();
        out.writeBytes(data);
    }

    @Override
    public int refCnt() {
        return refCnt;
    }

    @Override
    public ReferenceCounted retain() {
        refCnt++;
        return this;
    }

    @Override
    public ReferenceCounted retain(int increment) {
        refCnt += increment;
        return this;
    }

    @Override
    public ReferenceCounted touch() {
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        return this;
    }

    @Override
    public boolean release() {
        return release(1);
    }

    @Override
    public boolean release(int decrement) {
        refCnt -= decrement;
        if (refCnt <= 0) {
            av_buffer_unref(bufRef);
            bufRef = null;
            return true;
        }
        return false;
    }
}
