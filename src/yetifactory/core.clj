(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:gen-class))

(defn app [request]
  {:status 200
    :headers {"Content-Type" "text/html"}
    :body "Hello World"})

(defn -main []
  (adapter/run-server app { :port 5000 }))
