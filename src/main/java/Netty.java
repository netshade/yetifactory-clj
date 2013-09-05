package netutil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.Bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ServerChannel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelHandlerContext;

public class Netty
{
  public static ChannelFuture close (Channel channel)
  {
    return channel.close ();
  }

  public static ChannelHandlerContext flush (ChannelHandlerContext context)
  {
    return context.flush ();
  }

  public static ChannelFuture write (ChannelHandlerContext context, Object message)
  {
    return context.write (message);
  }

  public static ChannelFuture writeAndFlush (ChannelHandlerContext context, Object message)
  {
    return context.writeAndFlush(message);
  }

  public static ChannelFuture disconnect (ChannelHandlerContext context)
  {
    return context.disconnect();
  }

  public static ChannelPipeline pipeline (Channel channel)
  {
    return channel.pipeline ();
  }

  public static ServerBootstrap channel (ServerBootstrap bootstrap,
                                         Class<? extends ServerChannel> channelClass)
  {
    return bootstrap.channel (channelClass);
  }

  public static Bootstrap clientChannel (Bootstrap bootstrap,
                                         Class<? extends Channel> channelClass)
  {
    return bootstrap.channel (channelClass);
  }

  public static ServerBootstrap group (ServerBootstrap bootstrap, EventLoopGroup bossGroup, EventLoopGroup workerGroup)
  {
    return bootstrap.group (bossGroup, workerGroup);
  }

  public static Bootstrap handler (Bootstrap bootstrap,
                                   ChannelHandler handler)
  {
    return bootstrap.handler (handler);
  }

public static ServerBootstrap childHandler (ServerBootstrap bootstrap,
                                   ChannelHandler handler)
  {
    return bootstrap.childHandler (handler);
  }

  public static ServerBootstrap option (ServerBootstrap bootstrap,
                                  ChannelOption option,
                                  Object value)
  {
    return bootstrap.option (option, value);
  }

  public static ChannelPipeline pipeline (ChannelHandlerContext ctx)
  {
    return ctx.pipeline ();
  }
}
