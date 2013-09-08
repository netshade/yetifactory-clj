(ns yetifactory.posts
  (:import [org.ocpsoft.prettytime PrettyTime])
  (:require [clojure.java.jdbc :as j])
  (:require [clojure.string :as string])
  (:require [clojure.java.jdbc.sql :as sql])
  (:require [yetifactory.db :as db])
  (:require [clojure.pprint :as pprint])
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(defn- body-snippet
  [len body]
  (if (<= (.length body) len)
    body
    (str (subs body 0 len) "...")))


(defn- post-data
  [post]
  (cond
    (nil? post)
      nil
    :else
      (let [p               (PrettyTime.)
            created-at-ago  (.format p (:created_at post))
            updated-at-ago  (.format p (:updated_at post))]
          (merge post { :created_at_ago  created-at-ago
                        :updated_at_ago  updated-at-ago }))))

(defn- github-markdown
  [body]
  (let [request-body { :text body :mode "gfm" }
        request-json (json/write-str request-body)
        response     (client/post "https://api.github.com/markdown" { :body request-json :content-type :json })]
    (if (= (:status response) 200)
      (:body response)
      (throw (Throwable. (format "Failure to create GFM, status: %d" (:status response)))))))

(defn- title-from-text
  [body]
  (string/replace-first (first (string/split body #"[\r\n]+")) #"[^\w]+" ""))

;;; TODO: racy, stateful and just dumb.
;;; Corollary to TODO: works on my machine
(defn- slug-from-text
  [body]
  (let [ts (System/currentTimeMillis)
        title (title-from-text body)
        title-only-word-chars (string/replace title #"[^\w]" "-")]
        (string/join "-" [ts title-only-word-chars])))

(defn list-all []
  (map post-data (j/query (db/db-connection) ["SELECT * FROM posts ORDER BY created_at DESC"])))

(defn list-one
  [post-slug]
  (post-data (first (j/query (db/db-connection) ["SELECT * FROM posts WHERE slug = ?" post-slug]))))

(defn list-one-by-id
  [post-id]
  (post-data (first (j/query (db/db-connection) ["SELECT * FROM posts WHERE id = ?" post-id]))))

(defn create
  [content]
  (let [title (title-from-text content)
        slug  (slug-from-text content)
        body (github-markdown content)
        snippet (github-markdown (body-snippet 4096 content))]
    (j/execute! (db/db-connection) [
      "INSERT INTO posts (title,slug,body,body_md,snippet,created_at,updated_at) VALUES (?,?,?,?,?,NOW(),NOW())"
      title
      slug
      body
      content
      snippet])
    { :title title :slug slug :body body }))

(defn update
  [slug content]
  (let [title (title-from-text content)
        body (github-markdown content)
        snippet (github-markdown (body-snippet 4096 content))]
    (j/execute! (db/db-connection) [
      "UPDATE posts SET title = ?, body = ?, body_md = ?, snippet = ?, updated_at = NOW() WHERE slug = ?"
      title
      body
      content
      snippet
      slug])
    { :title title :slug slug :body body }))

(defn destroy
  [slug]
  (j/execute! (db/db-connection) ["DELETE FROM posts WHERE slug = ?" slug]))

