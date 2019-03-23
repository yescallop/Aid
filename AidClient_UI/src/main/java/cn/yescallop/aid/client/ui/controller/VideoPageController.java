package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.sun.jna.Memory;
import io.datafx.controller.ViewController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/VideoPage.fxml", title = "Video")
public class VideoPageController extends UIHandler {

    @FXML
    private Canvas screen;
    @FXML
    private JFXButton play;
    @FXML
    private JFXSlider slider;

    private PixelWriter pixelWriter;
    private WritablePixelFormat<ByteBuffer> pixelFormat;
    private DirectMediaPlayerComponent mediaPlayerComponent; // 媒体播放器
    private AnimationTimer timer; // 帧绘图计时器
    private File video; // 选中播放的视频文件
    private float position = 0; // 播放位置

    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);

        new Thread(() -> {
            new NativeDiscovery().discover();
            mediaPlayerComponent = new DefaultMediaPlayerComponent(new DefaultBufferFormatCallback());
            Platform.runLater(() -> play.setDisable(false));
        }).start();

        AnchorPane.setLeftAnchor(screen, Factory.UIData.LRSpacing);
        AnchorPane.setRightAnchor(screen, Factory.UIData.LRSpacing);
        AnchorPane.setTopAnchor(screen, Factory.UIData.TBSpacing);
        AnchorPane.setBottomAnchor(screen, Factory.UIData.TBSpacing);

        AnchorPane.setLeftAnchor(slider, Factory.UIData.LRSpacing);
        AnchorPane.setRightAnchor(slider, Factory.UIData.LRSpacing);
        AnchorPane.setBottomAnchor(slider, Factory.UIData.TBSpacing - 5);

        pixelWriter = screen.getGraphicsContext2D().getPixelWriter();
        pixelFormat = PixelFormat.getByteBgraInstance();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            video = chooseVideo();
            if (video != null) {
                slider.setVisible(true);
                play.setVisible(false);
                screen.setVisible(true);
                play();
            }

        });
        screen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mediaPlayerComponent.getMediaPlayer().pause());
    }

    /**
     * 视频播放结束时调用
     */
    private void stop() {
        timer.stop();
        mediaPlayerComponent.getMediaPlayer().stop();
        slider.setVisible(false);
        screen.setVisible(false);
        play.setVisible(true);
        video = null;
        position = 0;
    }

    /**
     * 开始播放视频时调用
     */
    private void play() {
        mediaPlayerComponent.getMediaPlayer().playMedia(video.getPath());
        mediaPlayerComponent.getMediaPlayer().setPosition(position);
        timer.start();
    }

    /**
     * 选择视频文件
     *
     * @return 视频文件
     */
    private File chooseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择录像");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Videos", "*.*"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"),
                new FileChooser.ExtensionFilter("FLV", "*.flv")
        );
        return fileChooser.showOpenDialog(Factory.UIData.getStage());
    }

    /**
     * 视频绘图方法
     */
    private void draw() {
        Memory[] nativeBuffers = mediaPlayerComponent.getMediaPlayer().lock();
        if (nativeBuffers != null) {
            Memory nativeBuffer = nativeBuffers[0];
            if (nativeBuffer != null) {
                ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                BufferFormat bufferFormat = ((DefaultDirectMediaPlayer) mediaPlayerComponent.getMediaPlayer()).getBufferFormat();
                if (bufferFormat.getWidth() > 0 && bufferFormat.getHeight() > 0) {
                    pixelWriter.setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                    Platform.runLater(() -> slider.setValue(mediaPlayerComponent.getMediaPlayer().getTime()));
                    position = mediaPlayerComponent.getMediaPlayer().getPosition();
                }
            }
        }
        mediaPlayerComponent.getMediaPlayer().unlock();
    }

    /**
     * 窗口大小改变时调用该方法，重新加载视频以调整视频大小
     */
    @Override
    public void resize() {
        if (position != 0) {
            timer.stop();
            mediaPlayerComponent.getMediaPlayer().stop();
            play();
        }
    }

    @Override
    public void release() {
        if (position != 0) stop();
        mediaPlayerComponent.getMediaPlayer().release();
    }

    /**
     * VLC媒体播放器
     */
    private class DefaultMediaPlayerComponent extends DirectMediaPlayerComponent {

        DefaultMediaPlayerComponent(BufferFormatCallback bufferFormatCallback) {
            super(bufferFormatCallback);
        }

        public void finished(MediaPlayer mediaPlayer) {
            stop();
        }
    }

    /**
     * 加载视频时对每帧画面作格式处理
     */
    private class DefaultBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {

            double screenWidth = Factory.UIData.getScreenWidth();
            double screenHeight = Factory.UIData.getScreenHeight();

            Platform.runLater(() -> {
                screen.setWidth(screenWidth);
                screen.setHeight(screenHeight);
                slider.setMax(mediaPlayerComponent.getMediaPlayer().getLength());
            });
            return new RV32BufferFormat((int) screenWidth, (int) screenHeight);
        }
    }
}
