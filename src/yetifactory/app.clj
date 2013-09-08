(ns yetifactory.app
  (:use [clojure.stacktrace])
  (:require [clojure.pprint :as pprint])
  (:require [yetifactory.posts :as posts])
  (:require [clojure.string :as string]))



(defn index [request]
  {:status 200
    :vars {
      :posts (posts/list-all)
    }
  })



(defn notfound [request]
  {:status 404
    :body "Not Found" })
