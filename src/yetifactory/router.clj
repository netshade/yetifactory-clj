(ns yetifactory.router
  (:require [yetifactory.app :as app])
  (:use [yetifactory.regex-match])
  (:use [clojure.stacktrace])
  (:use [clojure.core.match :only [match]]))

(defn recognize
  [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (match [uri method request]
      [(:or "/" "/index")           :get      _]
        :index
      [#"/post/(\d+)-.*"            :get      _]
        :show-post
      ["/teapot"                    :get      _]
        :teapot
      ["/post"                      :post     _]
        :create-post
      [#"/post/(\d+)-.*"            :delete   _]
        :destroy-post
      [#"/post/(\d+)-.*"            :put      _]
        :update-post
      :else
        :notfound)))

(defn route [request]
  (let [routename  (recognize request)
        appfn      (ns-resolve (symbol "yetifactory.app") (symbol (name routename)))]
    (apply appfn [request])))
