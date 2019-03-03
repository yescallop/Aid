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
    private AVCodecContext decoder;
    private AVCodecParameters par;
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

        par = stream.codecpar();
        AVCodec codec = avcodec_find_decoder(par.codec_id());
        if (codec == null)
            throw new FFmpegException("Codec not found");

        decoder = avcodec_alloc_context3(codec);
        if (decoder == null)
            throw new FFmpegException("Could not allocate decoder codec context");

        AVRational frameRate = av_guess_frame_rate(fmtCtx, stream, null);
        if (frameRate.num() == 0)
            throw new FFmpegException("Could not guess the frame rate");
        decoder.framerate(av_guess_frame_rate(fmtCtx, stream, null));

        avcodec_parameters_to_context(decoder, par);
        handler.init(decoder);

        if (avcodec_open2(decoder, codec, (PointerPointer) null) < 0)
            throw new FFmpegException("Could not open decoder codec");

        packet = av_packet_alloc();
        frame = av_frame_alloc();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) grabFrame();
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
                if (avcodec_send_packet(decoder, packet) < 0)
                    throw new FFmpegException("Error sending a packet for decoding");
                if (avcodec_receive_frame(decoder, frame) < 0)
                    throw new FFmpegException("Error during decoding");

                handler.frameGrabbed(frame);
            }
        } else if (ret == AVERROR_EOF || ret == AVERROR_EIO()) {
            free();
            handler.eofReached();
            interrupt();
        } else if (ret != AVERROR_EAGAIN()) { //if EAGAIN retry
            throw new FFmpegException("Error reading a frame: " + ret);
        }
    }

    private void free() {
//        avformat_close_input(fmtCtx);
        avcodec_free_context(decoder);
        avcodec_parameters_free(par);
        av_packet_free(packet);
        av_frame_free(frame);

        fmtCtx = null;
        decoder = null;
        par = null;
        frame = null;
    }

    public AVCodecParameters codecpar() {
        return par;
    }
}
