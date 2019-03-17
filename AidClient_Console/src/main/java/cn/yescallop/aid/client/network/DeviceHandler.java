package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.ClientConsoleMain;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.VideoInfoPacket;
import cn.yescallop.aid.network.protocol.VideoPacket;
import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import io.netty.channel.ChannelHandlerContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avutil.*;

public class DeviceHandler extends ClientPacketHandler {

    private long pts;
    private int frameCount = 0;
    private long lastTime = -1;

    private AVCodecContext decoder;
    private AVPacket packet;
    private AVFrame frame;

    public DeviceHandler() {

    }

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!ClientConsoleMain.isStopping()) {
            if (lastState == ChannelState.ACTIVE) {
                Logger.info("Connection to device closed");
            } else {
                Logger.warning("Device unexpectedly closed the connection");
            }
        }
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.id()) {
            case Packet.ID_VIDEO:
                try {
                    processVideoPacket((VideoPacket) packet);
                } catch (FFmpegException e) {
                    Logger.logException(e);
                }
                break;
            case Packet.ID_VIDEO_INFO:
                try {
                    initDecoder((VideoInfoPacket) packet);
                } catch (FFmpegException e) {
                    Logger.logException(e);
                }
                break;
            default:
                Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Logger.logException(re);
    }

    private void initDecoder(VideoInfoPacket p) throws FFmpegException {
        AVCodec codec = avcodec_find_decoder(p.codecId);
        if (codec == null)
            throw new FFmpegException("Codec not found");

        decoder = avcodec_alloc_context3(codec);
        if (decoder == null)
            throw new FFmpegException("Could not allocate decoder codec context");

        decoder.pix_fmt(p.pixFmt);
        decoder.width(p.width);
        decoder.height(p.height);
        decoder.sample_aspect_ratio(av_make_q(p.sarNum, p.sarDen));

        if (avcodec_open2(decoder, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open decoder codec");

        packet = av_packet_alloc();
        frame = av_frame_alloc();
        Logger.info("Decoder codec initialized");
    }

    private void processVideoPacket(VideoPacket p) throws FFmpegException {
        frameCount++;
        long curTime = System.currentTimeMillis();
        if (lastTime != -1) {
            long dur = curTime - lastTime;
            if (dur >= 1000) {
                float fps = frameCount / (dur / 1000f);
                Logger.info("FPS: " + String.format("%.1f", fps));
                frameCount = 0;
                lastTime = curTime;
            }
        } else lastTime = curTime;

        av_packet_from_data(packet, p.data, p.data.limit());

//        packet.pts(pts++);
//        System.out.println(packet.pts());

        int ret;
        if ((ret = avcodec_send_packet(decoder, packet)) < 0)
            throw new FFmpegException("Error sending a packet for decoding: " + ret);
        av_packet_unref(packet);

        while (true) {
            ret = avcodec_receive_frame(decoder, frame);
            if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF)
                break;
            else if (ret < 0) {
                throw new FFmpegException("Error during decoding: " + ret);
            }

            Logger.info("Frame decoded");
            av_frame_unref(frame);
        }
    }
}
