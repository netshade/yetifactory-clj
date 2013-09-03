(ns yetifactory.app
  (:use [clojure.stacktrace])
  (:require [me.raynes.laser :as l]))

(defn index [request]
  {:status 200
    :headers {"Content-Type" "text/html"}
    :template "index.html"
    :selectors {
      (l/class= "content") (l/content "I am dynamic content")
    }
  })

(defn notfound [request]
  {:status 404
    :body "Not Found" })
