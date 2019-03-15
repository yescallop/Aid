package cn.yescallop.aid.device.video;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.network.ClientManager;
import cn.yescallop.aid.network.protocol.VideoPacket;
import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import cn.yescallop.aid.video.ffmpeg.FrameHandler;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.swscale.*;

/**
 * @author Scallop Ye
 */
public class DeviceFrameHandler implements FrameHandler {

    public static final int CODEC_ID = AV_CODEC_ID_MPEG2VIDEO;
    public static final int PIX_FMT = AV_PIX_FMT_YUV420P;
    public static int width;
    public static int height;
    public static AVRational sampleAspectRatio;

    private long pts;
    private int frameCount = 0;
    private long lastTime = -1;
    private AVCodec codec;
    private AVCodecContext encoder;
    private SwsContext swsContext;

    private AVFrame swsFrame;
    private AVPacket packet;

    public DeviceFrameHandler() throws FFmpegException {
        codec = avcodec_find_encoder(CODEC_ID);
        if (codec == null)
            throw new FFmpegException("Codec not found");

        encoder = avcodec_alloc_context3(codec);
        if (encoder == null)
            throw new FFmpegException("Could not allocate encoder codec context");
    }

    @Override
    public void frameGrabbed(AVFrame frame) throws FFmpegException {
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

        sws_scale(swsContext, frame.data(), frame.linesize(), 0, frame.height(), swsFrame.data(), swsFrame.linesize());

        swsFrame.pts(pts++);

        if (avcodec_send_frame(encoder, swsFrame) < 0)
            throw new FFmpegException("Error sending a frame for encoding");
        while (true) {
            int ret = avcodec_receive_packet(encoder, packet);
            if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF)
                return;
            else if (ret < 0) {
                throw new FFmpegException("Error during encoding: " + ret);
            }

            VideoPacket p = new VideoPacket();
            p.time = curTime;
            p.data = packet.data().asByteBuffer();
            ClientManager.broadcastPacket(p);
        }
    }

    @Override
    public void eofReached() {
        System.exit(0);
    }

    @Override
    public void exceptionCaught(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void init(AVCodecContext decoder) throws FFmpegException {
        width = decoder.width();
        height = decoder.height();
        sampleAspectRatio = decoder.sample_aspect_ratio();

        encoder.bit_rate(1000 * 1024); //1000k
        encoder.pix_fmt(PIX_FMT);
        encoder.width(width);
        encoder.height(height);
        encoder.sample_aspect_ratio(sampleAspectRatio);
        AVRational fps = decoder.framerate();
        int intFps = Math.round(fps.num() / (float) fps.den());
        AVRational timeBase = av_make_q(1, intFps);
        encoder.time_base(timeBase);

        if (avcodec_open2(encoder, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open encoder codec");

        BytePointer outBuffer = new BytePointer(av_malloc(av_image_get_buffer_size(PIX_FMT, width, height, 1)));
        swsFrame = av_frame_alloc();
        swsFrame.format(PIX_FMT);
        swsFrame.width(width);
        swsFrame.height(height);
        av_image_fill_arrays(swsFrame.data(), swsFrame.linesize(), outBuffer, PIX_FMT, width, height, 1);

        swsContext = sws_getContext(width, height, decoder.pix_fmt(), width, height, PIX_FMT, SWS_BILINEAR, null, null, (DoublePointer) null);

        packet = av_packet_alloc();
    }
}
