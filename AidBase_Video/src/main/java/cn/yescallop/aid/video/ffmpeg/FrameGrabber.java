package cn.yescallop.aid.video.ffmpeg;

import cn.yescallop.aid.video.ffmpeg.util.Util;
import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;


/**
 * @author Scallop Ye
 * Reference: https://github.com/FFmpeg/FFmpeg/blob/master/doc/examples/decode_video.c
 */
public class FrameGrabber extends Thread {

    private AVFormatContext fmtCtx;
    private int videoStreamIndex;
    private AVCodecContext codecCtx;
    private AVCodecParameters codecpar;
    private AVPacket packet;
    private AVFrame frame;

    private FrameHandler handler;

    public FrameGrabber(AVFormatContext fmtCtx, FrameHandler handler) throws FFmpegException {
        this.fmtCtx = fmtCtx;
        this.handler = handler;
        init();
    }

    private void init() throws FFmpegException {
        if (avformat_find_stream_info(fmtCtx, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not find stream information");

        AVStream stream = Util.findVideoStream(fmtCtx);
        if (stream == null)
            throw new FFmpegException("Could not find video stream");
        videoStreamIndex = stream.index();

        codecpar = stream.codecpar();
        AVCodec codec = avcodec_find_decoder(codecpar.codec_id());
        if (codec == null)
            throw new FFmpegException("Codec not found");

        codecCtx = avcodec_alloc_context3(codec);
        if (codecCtx == null)
            throw new FFmpegException("Could not allocate codec context");

        avcodec_parameters_to_context(codecCtx, codecpar);

        if (avcodec_open2(codecCtx, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open codec");

        packet = av_packet_alloc();
        frame = av_frame_alloc();
    }

    @Override
    public void run() {
        try {
            do {
                grabFrame();
            } while (!isInterrupted());
        } catch (Exception e) {
            handler.exceptionCaught(e);
        } finally {
            free();
        }
    }

    private void grabFrame() throws FFmpegException {
        int ret = av_read_frame(fmtCtx, packet);
        if (ret >= 0) {
            if (packet.stream_index() == videoStreamIndex) { //video packet
                if (avcodec_send_packet(codecCtx, packet) < 0)
                    throw new FFmpegException("Error sending a packet for decoding");
                ret = avcodec_receive_frame(codecCtx, frame);
                if (ret < 0) {
                    if (ret == AVERROR_EAGAIN() || ret == AVERROR_EOF) //just retry
                        return;
                    throw new FFmpegException("Error during decoding");
                }

                handler.frameGrabbed(frame);
            }
        } else if (ret == AVERROR_EOF) {
            handler.eofReached();
        } else if (ret != AVERROR_EAGAIN()) { //if EAGAIN retry
            throw new FFmpegException("Error reading a frame");
        }
    }

    private void free() {
        avformat_close_input(fmtCtx);
        avcodec_free_context(codecCtx);
        avcodec_parameters_free(codecpar);
        av_packet_free(packet);
        av_frame_free(frame);

        fmtCtx = null;
        codecCtx = null;
        codecpar = null;
        frame = null;
    }

    public AVCodecParameters codecpar() {
        return codecpar;
    }
}
