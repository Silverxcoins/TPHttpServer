package server;

import handlers.ServerInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

public class Server {
    private final String serverName;
    private final int port;
    private final String directory;
    private final String directoryIndex;
    private final int cpuNumber;

    @SuppressWarnings("SameParameterValue")
    public Server(String serverName, int port, String directory, String directoryIndex, int cpuNumber) {
        this.serverName = serverName;
        this.port = port;
        this.directory = directory;
        this.directoryIndex = directoryIndex;
        this.cpuNumber = cpuNumber;
    }

    public void run() throws InterruptedException {
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup(cpuNumber);
        try {
            final ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF8")));
                            ch.pipeline().addLast(new ServerInboundHandler(Server.this));
                        }
                    });

            final ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getDirectory() {
        return directory;
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }
}
