(ns yetifactory.router
  (:require [yetifactory.app :as app])
  (:use [clojure.stacktrace]))

(defn route [request]
  (let [path (:uri request)]
    (cond
      (re-find #"^(/(index)?)?$" path)
        (app/index request)
      :else
        (app/notfound request))))
