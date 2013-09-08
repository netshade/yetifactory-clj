(ns yetifactory.db
  (:use [clojure.stacktrace])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource])
  (:import [org.ocpsoft.prettytime PrettyTime])
  (:require [clojure.pprint :as pprint])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as sql]))

(def db-config {  :classname    "com.mysql.jdbc.Driver"
                  :subprotocol  "mysql"
                  :subname      "//127.0.0.1:3306/yetifactory"
                  :user         "root"})

(defn pool
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec))
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               ;; expire excess connections after 30 minutes of inactivity:
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity:
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool db-config)))
(defn db-connection [] @pooled-db)

(defn body-snippet
  [len body]
  (if (<= (.length body) len)
    body
    (str (subs body 0 len) "...")))

(defn post-data
  [post]
  (let [p               (PrettyTime.)
        created-at-ago  (.format p (:created_at post))
        updated-at-ago  (.format p (:updated_at post))]
    (merge post { :created_at_ago  created-at-ago
                  :updated_at_ago  updated-at-ago })))

(defn list-posts []
  (map post-data (j/query (db-connection) ["SELECT * FROM posts ORDER BY created_at DESC"])))

(defn post
  [post-id]
  (post-data (j/query (db-connection) ["SELECT * FROM posts WHERE id = ?" post-id])))
