(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:require [yetifactory.adapter :as adapter])
  (:require [yetifactory.router :as router])
  (:require [ring.middleware.file :as file])
  (:require [ring.middleware.file-info :as file-info])
  (:require [yetifactory.template :as template])
  (:require [yetifactory.header-defaults :as header-defaults])
  (:require [clojure.pprint :as pprint])
  (:gen-class))


(defn -main []
  (adapter/run-server
      (header-defaults/wrap-header-defaults
        (file-info/wrap-file-info
          (file/wrap-file
            (template/wrap-template router/route "./templates")
          "./public"))
        {
          "Content-Type"  "text/html"
          "Cache-Control" "no-cache, no-store"
        })
    { :port 5000 }))
