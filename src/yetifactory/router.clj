(ns yetifactory.router
  (:require [yetifactory.app :as app])
  (:require [clojure.string :as string])
  (:require [clojure.data.codec.base64 :as b64])
  (:use [environ.core])
  (:use [clojure.stacktrace])
  (:use [clojure.core.match])
  (:use [yetifactory.regex-match]))

(defn recognize
  [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (match [uri method request]
      [#"^/(index)?$"                 :get      _]
        :index
      ["/feed.rss"                    :get      _]
        :index-rss
      [#"^/post/(\d+)-?.*$"           :get      _]
        :show-post
      ["/teapot"                      :get      _]
        :teapot
      ["/post"                        :post     _]
        :create-post
      [#"^/post/(\d+)-?.*$"           :delete   _]
        :destroy-post
      [#"^/post/(\d+)-?.*$"           :put      _]
        :update-post
      :else
        :notfound)))

(defn- should-authenticate
  [route-name]
  (contains? #{:create-post :destroy-post :update-post} (keyword route-name)))

(defn- authenticate [req user password]
  (let [authorization (last (string/split (or (:authorization req) "") #"\s+"))
        decoded-auth  (String. (b64/decode (.getBytes authorization)) "UTF-8")
        pair          (string/split decoded-auth #":")
        req-user      (first pair)
        req-password  (if (= (count pair) 2) (last pair) "")]
      (if (and (= req-user user)
               (= req-password password))
        true
        false)))

(defn- authUsername []
  (or (env :user) "test"))
(defn- authPassword []
  (or (env :password) "test"))

(defn route [request]
  (let [routename  (recognize request)
        appfn      (ns-resolve (symbol "yetifactory.app") (symbol (name routename)))]
    (if (should-authenticate routename)
      (if (authenticate request (authUsername) (authPassword))
        (apply appfn [request])
        { :status 401 :body "Not Authorized" })
    (apply appfn [request]))))
