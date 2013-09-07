(ns yetifactory.app
  (:use [clojure.stacktrace])
  (:require [clojure.pprint :as pprint])
  (:require [yetifactory.db :as db])
  (:require [clojure.string :as string]))



(defn index [request]
  {:status 200
    :headers {"Content-Type" "text/html"}
    :template "index.html"
    :vars {
      :posts (db/list-posts)
    }
  })

(defn notfound [request]
  {:status 404
    :body "Not Found" })
