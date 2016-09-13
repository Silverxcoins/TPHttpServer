package handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import utils.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private final String directory;
    private final String directoryIndex;

    public ServerInboundHandler(String directory, String directoryIndex) {
        this.directory = directory;
        this.directoryIndex = directoryIndex;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print(msg);
        final HttpRequest request = new HttpRequest((String)msg);

        if (request.isValid()) {
            String pathString = directory + request.getPath();
            final File file = new File(pathString);

            if (!file.exists() || file.isHidden()) {
                //TODO
            }

            if (file.isDirectory()) {
                pathString += '/' + directoryIndex;
            }

            final Path path = Paths.get(pathString);
            try {
                final byte[] data = Files.readAllBytes(path);
                final ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.wrappedBuffer(data));
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // TODO
            System.out.println("Invalid request");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
