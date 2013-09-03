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
           [io.netty.buffer Unpooled]))

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
  (def bodylen (cond
    (string? body)
      (alength (.getBytes body "UTF-8"))
    (seq? body)
      (reduce + (fn [str] (alength (.getBytes str "UTF-8"))))
    (instance? InputStream body)
      nil
    (instance? File body)
      (.length body)
    (nil? body)
      0
    :else
      (throw (Exception. "Unrecognized body for len"))))
  (let [initialCapacity (or bodylen 524288) ; 512kb
        maxCapacity (or bodylen 1048576)    ; 1mb
        buf (Unpooled/directBuffer initialCapacity maxCapacity) ; TODO: is using direct buffer here correct?
        writer (ByteBufOutputStream. buf)]
    (cond
      (string? body)
        (do
          (.writeBytes writer body); TODO: feels like using writeBytes is wrong here, given above UTF-8 len calc
          (.flush writer))
      (seq? body)
        (doseq [chunk body]
          (.writeBytes writer body)
          (.flush writer))
      (instance? InputStream body)
        (with-open [^InputStream b body]
          (io/copy b writer))
      (instance? File body)
        (let [^File f body]
          (with-open [stream (FileInputStream. f)]
            (get-body-bytebuf stream)))
      (nil? body)
        nil
      :else
        (throw (Exception. ^String (format "Unrecognized body: %s" body))))
    buf))

(defn generate-netty-response
  "Update the FullHttpResponse using a response map."
  [response-map]
  (let [{status :status body :body headers :headers} response-map
        bodybuf                                      (get-body-bytebuf body)
        response                                     (DefaultFullHttpResponse. HttpVersion/HTTP_1_1 (HttpResponseStatus/valueOf (or status 200)) bodybuf)]
    (pprint/pprint response-map)
    (doto response
        (set-headers (merge { "Content-Length" (.capacity bodybuf) } headers)))
    response))
