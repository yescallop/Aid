package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import cn.yescallop.aid.client.ui.frame.Frame;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
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
public class VideoPageController implements UIHandler {
    @FXML
    private StackPane root;
    @FXML
    private Canvas screen;
    @FXML
    private JFXButton play;
    @FXML
    private JFXSlider slider;

    private PixelWriter pixelWriter;

    private WritablePixelFormat<ByteBuffer> pixelFormat;

    private DirectMediaPlayerComponent mediaPlayerComponent;
    private boolean isPlaying = false;

    private final double LRSpacing = 30;
    private final double TBSpacing = 20;

    private Stage stage;

    private AnimationTimer timer;
    private File video;

    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);
        stage = Factory.UIData.getStage();

        new Thread(() -> {
            new NativeDiscovery().discover();
            mediaPlayerComponent = new DefaultMediaPlayerComponent(new DefaultBufferFormatCallback());
            Platform.runLater(() -> play.setDisable(false));
        }).start();

        AnchorPane.setLeftAnchor(screen, LRSpacing);
        AnchorPane.setRightAnchor(screen, LRSpacing);
        AnchorPane.setTopAnchor(screen, TBSpacing);
        AnchorPane.setBottomAnchor(screen, TBSpacing);

        AnchorPane.setLeftAnchor(slider, LRSpacing);
        AnchorPane.setRightAnchor(slider, LRSpacing);
        AnchorPane.setBottomAnchor(slider, TBSpacing-5);

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
            if (video != null) play();
        });
        screen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (isPlaying) {
                mediaPlayerComponent.getMediaPlayer().pause();
            } else if (mediaPlayerComponent.getMediaPlayer().getTime() != 0 && !isPlaying) {
                mediaPlayerComponent.getMediaPlayer().play();
            }
        });
    }

    private void stop(){
        timer.stop();
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.getMediaPlayer().release();
        isPlaying = false;
        slider.setVisible(false);
        screen.setVisible(false);
        play.setVisible(true);
    }

    private void play() {
        mediaPlayerComponent.getMediaPlayer().playMedia(video.getPath());
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                stop();
            }
        });
        play.setVisible(false);
        timer.start();
        isPlaying = true;
    }

    private File chooseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择录像");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Videos", "*.*"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"),
                new FileChooser.ExtensionFilter("FLV", "*.flv")
        );
        return fileChooser.showOpenDialog(stage);
    }

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
                }
            }
        }
        mediaPlayerComponent.getMediaPlayer().unlock();
    }

    @Override
    public void showDialog(String heading, String body) {
        JFXButton ok = new JFXButton("确定");
        ok.setPrefSize(70, 35);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        content.setActions(ok);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        ok.setOnAction(event -> dialog.close());
    }

    private class DefaultMediaPlayerComponent extends DirectMediaPlayerComponent{

        DefaultMediaPlayerComponent(BufferFormatCallback bufferFormatCallback) {
            super(bufferFormatCallback);
        }
    }

    private class DefaultBufferFormatCallback implements BufferFormatCallback{

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {

            double screenWidth = getScreenWidth();
            double screenHeight = getScreenHeight();

            Platform.runLater(() -> {
                screen.setWidth(screenWidth);
                screen.setHeight(screenHeight);
                slider.setVisible(true);
                slider.setMax(mediaPlayerComponent.getMediaPlayer().getLength());
            });
            return new RV32BufferFormat((int) screenWidth, (int) screenHeight);
        }

        private double getScreenWidth() {
            return stage.getWidth() - (2 * LRSpacing);
        }

        private double getScreenHeight() {
            if (stage.isFullScreen()) {
                return stage.getHeight() - Frame.getToolbarHeight() - (2 * TBSpacing);
            } else {
                return stage.getHeight() - Frame.getToolbarHeight() - (2 * TBSpacing) - 32;
            }
        }
    }
}
