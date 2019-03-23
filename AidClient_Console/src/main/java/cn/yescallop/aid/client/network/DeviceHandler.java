package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.ClientConsoleMain;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.FramePacket;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import cn.yescallop.aid.video.ffmpeg.util.FXImageHelper;
import io.netty.channel.ChannelHandlerContext;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bytedeco.javacpp.PointerPointer;

import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avutil.*;

public class DeviceHandler extends ClientPacketHandler {

    private int frameCount = 0;
    private long lastTime = -1;

    private AVCodecContext decoder;
    private AVPacket packet;
    private AVFrame frame;

    private FXImageHelper fxImageHelper;

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
            case Packet.ID_DEVICE_HELLO:
                DeviceHelloPacket dhp = (DeviceHelloPacket) packet;
                try {
                    initDecoder(dhp.codecId);
                } catch (FFmpegException e) {
                    Logger.logException(e);
                }
                break;
            case Packet.ID_VIDEO:
                try {
                    processVideoPacket((FramePacket) packet);
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

    private void initDecoder(int codecId) throws FFmpegException {
        AVCodec codec = avcodec_find_decoder(codecId);
        if (codec == null)
            throw new FFmpegException("Codec not found");

        decoder = avcodec_alloc_context3(codec);
        if (decoder == null)
            throw new FFmpegException("Could not allocate decoder codec context");

        if (avcodec_open2(decoder, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open decoder codec");

        packet = av_packet_alloc();
        frame = av_frame_alloc();
        Logger.info("Decoder codec initialized");
    }

    private void processVideoPacket(FramePacket p) throws FFmpegException {
        ByteBuffer data = p.buf.nioBuffer();

        av_packet_from_data(packet, data, p.size);

        int ret;
        if ((ret = avcodec_send_packet(decoder, packet)) < 0) {
            if (ret == -1094995529) {
                Logger.info("Waiting for stream info");
            } else throw new FFmpegException("Error sending a packet for decoding: " + ret);
        }
        p.buf.release();

        while (true) {
            ret = avcodec_receive_frame(decoder, frame);
            if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF)
                break;
            else if (ret < 0) {
                throw new FFmpegException("Error during decoding: " + ret);
            }

            countFrame();
            processFrame();
            av_frame_unref(frame);
        }
    }

    private void countFrame() {
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
    }

    private void processFrame() {
        if (fxImageHelper == null) {
            fxImageHelper = new FXImageHelper(frame);
            Stage stage = new Stage();
            ImageView imageView = new ImageView();
            Image image = fxImageHelper.convertFromAVFrame(frame);
            imageView.setImage(image);
            Scene scene = new Scene(new StackPane(imageView));
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        }


    }
}
