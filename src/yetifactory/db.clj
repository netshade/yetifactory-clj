(ns yetifactory.db
  (:use [clojure.stacktrace])
  (:use [environ.core])
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource])
  (:require [clojure.pprint :as pprint])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as sql]))

(def db-config {  :classname    (or (env :db-driver) "com.mysql.jdbc.Driver")
                  :subprotocol  (or (env :db-protocol) "mysql")
                  :subname      (str "//"
                                    (or (env :db-host) "localhost")
                                    "/"
                                    (or (env :db-name) "blog"))
                  :user         (or (env :db-user) "root")
                  :password     (or (env :db-password) "")})

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
