package cn.yescallop.aid.client;

import cn.yescallop.aid.client.frame.Frame;
import cn.yescallop.aid.network.Network;
import com.jfoenix.controls.JFXDecorator;
import io.datafx.controller.context.FXMLViewContext;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Scallop Ye
 */
public class ClientMain extends Application {

    @FXMLViewContext
    private ViewFlowContext flowContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        flowContext = new ViewFlowContext();
        flowContext.register("Stage",primaryStage);
        Flow flow = new Flow(Frame.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(primaryStage,container.getView(),true,true,true);
        Scene scene = new Scene(decorator,1100,680);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style/material-designs-style.css").toExternalForm());
        primaryStage.setTitle("AidClient");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void start() {
        try {
            Channel channel = Network.startClient("127.0.0.1", 9000, new ClientHandler());
            System.out.println("Connected to " + channel.remoteAddress());
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
