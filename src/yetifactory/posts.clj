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

;; This method presumes the query has:
;; one %s token in it to take the identity column name
;; the "?" token that the value will be replacing is the last one in the query params
;; TODO: this method is a clear litmus test for putting a better db lib in here. junk this eventually.
(defn- query-posts-from-map
  [query id-or-slug-map & query-values]
  (if-let [slug (:slug id-or-slug-map)]
    (filter #(not(nil? %)) (conj (vec (concat [(format query "slug")] (or query-values []))) slug))
    (if-let [id (:id id-or-slug-map)]
      (filter #(not(nil? %)) (conj (vec (concat [(format query "id")] (or query-values []))) id))
      (throw (Throwable. "Can't generate query from parameters")))))

(defn list-all []
  (map post-data (j/query (db/db-connection) ["SELECT * FROM posts ORDER BY created_at DESC"])))

(defn list-one
  [post-params]
  (post-data (first (j/query (db/db-connection) (query-posts-from-map "SELECT * FROM posts WHERE %s = ?" post-params)))))

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
    { :title title :body body }))

(defn update
  [post-params content]
  (let [title (title-from-text content)
        body (github-markdown content)
        snippet (github-markdown (body-snippet 4096 content))]
    (j/execute! (db/db-connection) (query-posts-from-map
      "UPDATE posts SET title = ?, body = ?, body_md = ?, snippet = ?, updated_at = NOW() WHERE %s = ?"
      post-params
      title
      body
      content
      snippet))
    { :title title :body body }))

(defn destroy
  [post-params]
  (j/execute! (db/db-connection) (query-posts-from-map "DELETE FROM posts WHERE %s = ?" post-params)))

