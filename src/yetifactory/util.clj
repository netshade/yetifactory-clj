(ns yetifactory.util
  "Compatibility functions for dealing w/ Netty requests and response."
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:require [clojure.pprint :as pprint])
  (:import [java.io File InputStream FileInputStream]
           [io.netty.buffer ByteBufInputStream ByteBufOutputStream]
           [io.netty.handler.codec.http DefaultFullHttpResponse
                                          FullHttpResponse
                                          FullHttpRequest
                                          HttpHeaders
                                          HttpVersion
                                          HttpResponseStatus
                                          HttpHeaders$Names]
           [io.netty.buffer Unpooled]
           [io.netty.util CharsetUtil]))

(defn- get-headers
  "Creates a name/value map of all the request headers."
  [^FullHttpRequest request]
  (let [request-headers (.headers request)]
    (reduce
      (fn [headers, ^String name]
        (assoc headers
          (.toLowerCase name)
          (->> (.getAll request-headers name)
               (string/join ","))))
      {}
      (.names request-headers))))

(defn build-request-map
  "Create the request map from the HttpServletRequest object."
  [^FullHttpRequest request]
  (let [headers (.headers request)]
    {:server-port        (last (string/split (.get headers "Host") #":"))
     :server-name        (first (string/split (.get headers "Host") #":"))
     :remote-addr        "127.0.0.1"
     :uri                (.getUri request)
     :query-string       (last (string/split (.getUri request) #"\?"))
     :scheme             :http
     :request-method     (keyword (.toLowerCase (.toString (.getMethod request))))
     :headers            (get-headers request)
     :content-type       (.get headers HttpHeaders$Names/CONTENT_TYPE)
     :content-length     (HttpHeaders/getContentLength request 0)
     :character-encoding (.get headers HttpHeaders$Names/CONTENT_ENCODING)
     :body               (ByteBufInputStream. (.content request))}))


(defn set-headers
  "Update a HttpResponse with a map of headers."
  [^FullHttpResponse response headers]
  (let [response-headers (.headers response)]
    (doseq [[key val-or-vals] headers]
      (doseq [val (flatten (list val-or-vals))]
          (.set response-headers key val)))))

(defn- get-body-bytebuf
  "Update a HttpResponse body with a String, ISeq, File or InputStream."
  [body]
  (cond
    (string? body)
      (Unpooled/copiedBuffer body CharsetUtil/UTF_8) ; TODO: feels like using writeBytes is wrong here, given above UTF-8 len calc
    (seq? body)
      (Unpooled/copiedBuffer (string/join "" body) CharsetUtil/UTF_8)
    (instance? InputStream body)
      (let [buf (Unpooled/buffer 524288)]
        (with-open [writer (ByteBufOutputStream. buf)
                    ^InputStream b body]
          (io/copy b writer))
        buf)
    (instance? File body)
      (let [buf (Unpooled/buffer (.length body))
            writer (ByteBufOutputStream. buf)]
        (io/copy body writer)
        buf)
    (nil? body)
      Unpooled/EMPTY_BUFFER
    :else
      (throw (Exception. ^String (format "Unrecognized body: %s" body)))))

(defn generate-netty-response
  "Update the FullHttpResponse using a response map."
  [response-map]
  (let [{status :status body :body headers :headers} response-map
        bodybuf                                      (get-body-bytebuf body)
        response                                     (DefaultFullHttpResponse. HttpVersion/HTTP_1_1 (HttpResponseStatus/valueOf (or status 200)) bodybuf)]
    (set-headers response (merge headers { "Content-Length" (.readableBytes bodybuf) }))
    response))
