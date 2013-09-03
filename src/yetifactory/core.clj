(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:require [ring.middleware.file :as file])
  (:require [ring.middleware.file-info :as file-info])
  (:require [yetifactory.template :as template])
  (:gen-class))

(defn -main []
  (adapter/run-server
      (file-info/wrap-file-info
        (file/wrap-file
          (template/wrap-template router "./templates")
        "./public"))
    { :port 5000 }))
