(ns yetifactory.app
  (:use [clojure.stacktrace])
  (:require [clojure.pprint :as pprint])
  (:require [yetifactory.posts :as posts])
  (:require [clojure.string :as string]))


(defn- slug-from-request [request]
  (string/replace-first (:uri request) #"/post/", ""))

(defn notfound [request]
  {:status 404
    :body "Not Found" })

(defn teapot [request]
  { :status 418 :body "halo" })

(defn index [request]
  {:status        200
    :vars         { :posts (posts/list-all) }
    :template     (if (= (:accept request) "text/csv") "index_csv" "index")
    :layout       (if (= (:accept request) "text/csv") nil "layout")
  })

(defn show-post [request]
  (if-let [post (posts/list-one (slug-from-request request))]
    (if (= (:accept request) "text/markdown")
      { :status 200 :body (:body_md post) }
      { :status 200 :vars {:post post } })
    (notfound request)))

(defn create-post [request]
  (let [body (slurp (:body request))]
    (posts/create body)
    {:status 201 :body "Created"}))

(defn destroy-post [request]
  (if-let [slug (slug-from-request request)]
    (do
      (posts/destroy slug)
      {:status 200 :body "Destroyed"})
    (notfound request)))

(defn update-post [request]
  (let [body (slurp (:body request))]
    (if-let [slug (slug-from-request request)]
      (do
        (posts/update slug body)
        {:status 200 :body "Updated"})
      (notfound request))))


