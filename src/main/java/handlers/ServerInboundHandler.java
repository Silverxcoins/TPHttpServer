package handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print(msg);

        Path path = Paths.get("/Users/Sasha/Desktop/projects/TPHttpServer/some_directory/index.html");
        try {
            byte[] data = Files.readAllBytes(path);
            final ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.wrappedBuffer(data));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }
}
