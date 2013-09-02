(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:require [ring.middleware.file :as file])
  (:require [ring.middleware.file-info :as file-info])
  (:gen-class))

(defn app [request]
  {:status 200
    :headers {"Content-Type" "text/html"}
    :body "Hello World"})

(defn -main []
  (adapter/run-server
      (file-info/wrap-file-info
        (file/wrap-file app "./public"))
      { :port 5000 }))
