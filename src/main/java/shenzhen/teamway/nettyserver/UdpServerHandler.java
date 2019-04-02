package shenzhen.teamway.nettyserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shenzhen.teamway.config.RestErrorHandler;
import shenzhen.teamway.util.OtherUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: onvifservice
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-03-07 09:31
 **/
@Component
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    //@Autowired
    //private CommondHandlerRefact commondHandlerRefact;
    @Autowired
    private CommondHanlder commondHanlder;
    Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req = msg.content().toString(CharsetUtil.UTF_8);
        logger.debug("收到的请求:" + req);
        commondHanlder.chooseCommond(req, ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.error(cause.getMessage(), cause);
    }


}