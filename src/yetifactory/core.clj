(ns yetifactory.core
  (:use [clojure.stacktrace])
  (:use [environ.core])
  (:require [yetifactory.adapter :as adapter])
  (:require [yetifactory.router :as router])
  (:require [ring.middleware.file :as file])
  (:require [clojure.java.io])
  (:require [ring.middleware.file-info :as file-info])
  (:require [yetifactory.template :as template])
  (:require [yetifactory.header-defaults :as header-defaults])
  (:require [clojure.pprint :as pprint]))

(defn -main []
  (let [port (or (env :port) 5000)
        template-defaults { :google_analytics_id (env :google-analytics-id)
                            :disqus_name         (env :disqus-name)
                            :gauges_id           (env :gauges-id) }]
    (adapter/run-server
        (header-defaults/wrap-header-defaults
          (file-info/wrap-file-info
            (file/wrap-file
              (template/wrap-template router/route "./templates" template-defaults)
            "./public"))
          {
            "Content-Type"  "text/html"
            "Cache-Control" "private, max-age=0, must-revalidate"
          })
      { :port (if (string? port) (Integer/parseInt port) port) })))
