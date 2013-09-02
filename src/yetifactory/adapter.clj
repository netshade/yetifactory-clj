(ns yetifactory.adapter
  "Adapter for netty."
  (:import
     [io.netty.bootstrap ServerBootstrap]
     [io.netty.channel Channel
                        ChannelInboundHandlerAdapter
                        ChannelInitializer
                        ChannelFutureListener
                        ChannelHandlerContext]
     [io.netty.handler.codec.http HttpServerCodec HttpObjectAggregator]
     [io.netty.channel.nio NioEventLoopGroup]
     [io.netty.channel.socket.nio NioServerSocketChannel]
     [io.netty.handler.timeout WriteTimeoutHandler]
     [io.netty.handler.codec.http DefaultFullHttpResponse
                                    FullHttpResponse
                                    HttpRequest
                                    HttpHeaders
                                    HttpResponseStatus
                                    HttpVersion
                                    HttpHeaders$Names
                                    HttpHeaders$Values])
  (:use [clojure.stacktrace])
  (:require [yetifactory.util :as util]))

(defn make-proxy-handler
  "Returns a Netty handler."
  [handler]
  (proxy [ChannelInboundHandlerAdapter] []
    (channelReadComplete [ctx]
      (-> ctx .channel .flush))

    (channelRead [ctx msg]
      (when (instance? HttpRequest msg)
        (def req ^FullHttpRequest msg)
        (if (HttpHeaders/is100ContinueExpected req)
          (netutil.Netty/write ctx (DefaultFullHttpResponse. HttpVersion/HTTP_1_1 HttpResponseStatus/CONTINUE)))
        (let [request-map (util/build-request-map req)
              response-map (handler request-map)
              response (util/generate-netty-response response-map)]
          (if (HttpHeaders/isKeepAlive msg)
            (do
              (.set (.headers response) HttpHeaders$Names/CONNECTION HttpHeaders$Values/KEEP_ALIVE)
              (netutil.Netty/write (.channel ctx) response))
            (.addListener (netutil.Netty/write (.channel ctx) response) ChannelFutureListener/CLOSE))))
      (netutil.Netty/flush (.channel ctx)))

    (exceptionCaught [ctx throwable]
      (println "@exceptionCaught" throwable)
      (.printStackTrace throwable)
      (-> ctx .channel .close))))

(defn make-initializer
  "Returns a channel initializer"
  [handler]
  (proxy [ChannelInitializer] []
    (initChannel [socket-channel]
      (let [pipeline (.pipeline socket-channel)]
        (.addLast pipeline "codec" (HttpServerCodec.))
        (.addLast pipeline "aggregator" (HttpObjectAggregator. 1048576))
        (.addLast pipeline "writeTimeoutHandler" (WriteTimeoutHandler. 5))
        (.addLast pipeline "handler" (make-proxy-handler handler))))))

(defn create-server
  "Start a Netty server. Returns the pipeline."
  [port handler]
  (let [bootstrap (ServerBootstrap.)]
      (netutil.Netty/group bootstrap (NioEventLoopGroup.) (NioEventLoopGroup.))
      (netutil.Netty/channel bootstrap NioServerSocketChannel)
      (netutil.Netty/childHandler bootstrap (make-initializer handler))
      bootstrap))


(defn ^ServerBootstrap run-server
  "Start a Netty webserver to serve the given handler according to the
  supplied options:

  :configurator - a function called with the Netty Server instance
  :port         - the port to listen on (defaults to 5000)"
  [handler options]
  (let [
    port (or (:port options) 5000)
    ^ServerBootstrap s (create-server port handler)]
    (when-let [configurator (:configurator options)]
      (configurator s))
    (let [future (-> s
                      (.bind port)
                      (.sync))]
      (-> future
        (.channel)
        (.closeFuture)
        (.sync)))
    s))
