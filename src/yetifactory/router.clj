(ns yetifactory.router
  (:require [yetifactory.app :as app])
  (:use [clojure.stacktrace]))

(defn recognize
  [request]
  (let [path (:uri request)]
    (cond
      (re-find #"^(/(index)?)?$" path)
        :index
      :else
        :notfound)))

(defn route [request]
  (let [routename  (recognize request)
        appfn      (ns-resolve (symbol "yetifactory.app") (symbol (name routename)))]
    (apply appfn [request])))
