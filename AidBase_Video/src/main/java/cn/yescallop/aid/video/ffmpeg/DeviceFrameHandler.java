package cn.yescallop.aid.video.ffmpeg;

import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 */
public class DeviceFrameHandler implements FrameHandler {

    private int frameCount = 0;
    private long lastTime = -1;
    private AVCodec codec;
    private AVCodecContext encoder;

    private AVPacket packet;

    public DeviceFrameHandler() throws FFmpegException {
        codec = avcodec_find_encoder(AV_CODEC_ID_MPEG2VIDEO);
        if (codec == null)
            throw new FFmpegException("Codec not found");
        codec.type(AVMEDIA_TYPE_VIDEO);

        encoder = avcodec_alloc_context3(codec);
        if (encoder == null)
            throw new FFmpegException("Could not allocate encoder codec context");

        encoder.codec_type(AVMEDIA_TYPE_VIDEO);
    }

    @Override
    public void frameGrabbed(AVFrame frame) throws FFmpegException {
        frameCount++;
        long curTime = System.currentTimeMillis();
        if (lastTime != -1) {
            long dur = curTime - lastTime;
            if (dur >= 1000) {
                float fps = frameCount / (dur / 1000f);
                System.out.println("FPS: " + String.format("%.1f", fps));
                frameCount = 0;
                lastTime = curTime;
            }
        } else lastTime = curTime;

        //TODO: Find out why this function occurs error EXCEPTION_ACCESS_VIOLATION (0xc0000005)
        if (avcodec_send_frame(encoder, frame) < 0)
            throw new FFmpegException("Error sending a frame for encoding");
        if (avcodec_receive_packet(encoder, packet) < 0)
            throw new FFmpegException("Error during decoding");

        System.out.println(packet.size());
    }

    @Override
    public void eofReached() {

    }

    @Override
    public void exceptionCaught(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void init(AVCodecContext decoder) throws FFmpegException {
        encoder.bit_rate(1000 * 1024); //1000k
        encoder.pix_fmt(AV_PIX_FMT_YUV422P);
        encoder.width(decoder.width());
        encoder.height(decoder.height());
        encoder.sample_aspect_ratio(decoder.sample_aspect_ratio());
        AVRational fps = decoder.framerate();
        int intFps = Math.round(fps.num() / (float) fps.den());
        AVRational timeBase = av_make_q(1, intFps);
        encoder.time_base(timeBase);

        if (avcodec_open2(encoder, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open encoder codec");

        packet = av_packet_alloc();
    }
}
