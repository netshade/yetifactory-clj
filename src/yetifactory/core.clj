(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:require [ring.middleware.file :as file])
  (:require [ring.middleware.file-info :as file-info])
  (:require [me.raynes.laser :as l])
  (:require [yetifactory.template :as template])
  (:gen-class))

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

(defn router [request]
  (def path (:uri request))
  (cond
    (re-find #"^(/(index)?)?$" path)
      (index request)
    :else
      (notfound request)))

(defn -main []
  (adapter/run-server
      (file-info/wrap-file-info
        (file/wrap-file
          (template/wrap-template router "./templates")
        "./public"))
    { :port 5000 }))
