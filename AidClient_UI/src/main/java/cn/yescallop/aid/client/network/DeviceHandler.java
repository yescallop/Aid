package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.ui.controller.MonitorController;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.*;
import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import cn.yescallop.aid.video.ffmpeg.util.FXImageHelper;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bytedeco.javacpp.PointerPointer;

import java.io.IOException;
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

    private MonitorController controller;
    private ImageView screen;

    private int deviceId;
    private Stage stage;

    public DeviceHandler(int deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!Factory.Network.isStopping()) {
            if (lastState == ChannelState.ACTIVE) {
                Factory.UI.println("Connection to device closed");
            } else {
                Factory.UI.println("Device unexpectedly closed the connection");
            }
        }
        Platform.runLater(stage::close);
        Factory.Network.removeDeviceChannelById(deviceId);
        free();
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.id()) {
            case Packet.ID_DEVICE_HELLO:
                DeviceHelloPacket dhp = (DeviceHelloPacket) packet;
                try {
                    initDecoder(dhp.codecId);
                } catch (FFmpegException e) {
                    Factory.UI.showDialog("Exception", e.getMessage());
                }
                break;
            case Packet.ID_FRAME:
                try {
                    processVideoPacket((FramePacket) packet);
                } catch (FFmpegException e) {
                    Factory.UI.showDialog("Exception", e.getMessage());
                }
                break;
            default:
                Factory.UI.println("From " + ctx.channel().remoteAddress() + ": " + packet);
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Factory.UI.showDialog("Exception", re.getMessage());
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
        Factory.UI.println("Decoder codec initialized");
    }

    private void processVideoPacket(FramePacket p) throws FFmpegException {
        ByteBuffer data = p.buf.nioBuffer();

        av_packet_from_data(packet, data, p.size);

        int ret;
        if ((ret = avcodec_send_packet(decoder, packet)) < 0) {
            if (ret == -1094995529) {
                Factory.UI.println("Waiting for stream info");
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
//                Factory.UI.println("FPS: " + String.format("%.1f", fps));
                frameCount = 0;
                lastTime = curTime;
            }
        } else lastTime = curTime;
    }

    private void processFrame() {
        if (fxImageHelper == null) {
            fxImageHelper = new FXImageHelper(frame);
            int height = frame.height();
            int width = frame.width();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Monitor.fxml"));
                        Parent root = fxmlLoader.load();
                        controller = fxmlLoader.getController();
                        screen = controller.getScreen();
                        controller.deviceId = deviceId;
                        Scene scene = new Scene(root);
                        scene.getRoot().requestFocus();
                        DeviceListPacket.DeviceInfo info = Factory.Network.deviceInfoById(deviceId);
                        stage.setTitle(String.format("[%d] %s", deviceId, info.name));
                        stage.setScene(scene);
                        stage.setOnCloseRequest(event -> Factory.Network.getDeviceChannelById(deviceId)
                                .close().syncUninterruptibly()
                        );
                        stage.setHeight(height);
                        stage.setWidth(width);
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Image image = fxImageHelper.convertFromAVFrame(frame);
        Platform.runLater(() -> screen.setImage(image));
    }

    private void free() {
        avcodec_free_context(decoder);
        fxImageHelper.free();
    }
}
