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

@SuppressWarnings("SameParameterValue")
public class Server {
    private final int port;
    private final String directory;
    private final String directoryIndex;
    private final int cpuNumber;

    public Server(int port, String directory, String directoryIndex, int cpuNumber) {
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
                            ch.pipeline().addLast(new ServerInboundHandler(directory, directoryIndex));
                        }
                    });

            final ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
