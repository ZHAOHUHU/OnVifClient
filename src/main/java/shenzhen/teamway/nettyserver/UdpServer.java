package shenzhen.teamway.nettyserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shenzhen.teamway.config.RestErrorHandler;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-07 09:27
 **/
@Component
public class UdpServer {
    Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);
    @Autowired
    UdpServerHandler udpServerHandler;

    public void run(int port) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(udpServerHandler);
            bootstrap.bind(port).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}