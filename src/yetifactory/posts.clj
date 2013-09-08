(ns yetifactory.posts
  (:import [org.ocpsoft.prettytime PrettyTime])
  (:require [clojure.java.jdbc :as j])
  (:require [clojure.java.jdbc.sql :as sql])
  (:require [yetifactory.db :as db]))

(defn- body-snippet
  [len body]
  (if (<= (.length body) len)
    body
    (str (subs body 0 len) "...")))


(defn- post-data
  [post]
  (let [p               (PrettyTime.)
        created-at-ago  (.format p (:created_at post))
        updated-at-ago  (.format p (:updated_at post))]
    (merge post { :created_at_ago  created-at-ago
                  :updated_at_ago  updated-at-ago })))

(defn list-all []
  (map post-data (j/query (db/db-connection) ["SELECT * FROM posts ORDER BY created_at DESC"])))

(defn list-one
  [post-slug]
  (post-data (j/query (db/db-connection) ["SELECT * FROM posts WHERE slug = ?" post-slug])))

(defn list-one-by-id
  [post-id]
  (post-data (j/query (db/db-connection) ["SELECT * FROM posts WHERE id = ?" post-id])))

