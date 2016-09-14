package handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.Server;
import utils.HttpRequest;
import utils.HttpResponse;
import utils.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private final Server server;
    private final String directory;
    private final String directoryIndex;

    public ServerInboundHandler(Server server) {
        this.server = server;
        this.directory = server.getDirectory();
        this.directoryIndex = server.getDirectoryIndex();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        final HttpRequest request = new HttpRequest((String)msg);

        final HttpResponse response;
        if (request.isValid()) {
            String pathString = directory + request.getPath();
            final File file = new File(pathString);

            if (file.exists() && !file.isHidden()) {
                if (file.isDirectory()) {
                    pathString += '/' + directoryIndex;
                }

                final Path path = Paths.get(pathString);
                response = new HttpResponse(server, new Status(Status.OK), request.getMethod(), path);
            } else {
                response = new HttpResponse(server, new Status(Status.NOT_FOUND));
            }
        } else {
            response = new HttpResponse(server, new Status(Status.BAD_REQUEST));
        }

        final ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.wrappedBuffer(response.toByteArray()));
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
