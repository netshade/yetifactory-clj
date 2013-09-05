(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:require [yetifactory.router :as router])
  (:require [ring.middleware.file :as file])
  (:require [ring.middleware.file-info :as file-info])
  (:require [yetifactory.template :as template])
  (:require [clojure.pprint :as pprint])
  (:gen-class))

(defn wrap-no-cache [handler]
  (fn [req]
    (let [response-map (handler req)
          response-headers (or (:headers response-map) {})
          no-cache {"Cache-Control" "no-cache, no-store"}
          new-headers { :headers (merge no-cache response-headers) }
          new-response (merge response-map new-headers)]
      new-response)))

(defn -main []
  (adapter/run-server
      (wrap-no-cache
        (file-info/wrap-file-info
          (file/wrap-file
            (template/wrap-template router/route "./templates")
          "./public")))
    { :port 5000 }))
