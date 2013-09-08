(ns yetifactory.template
  (:use [clojure.stacktrace])
  (:import [org.stringtemplate.v4 ST STRawGroupDir])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.pprint :as pprint])
  (:require [yetifactory.router :as router])
  (:gen-class))

(def templatemap nil)
(def last-modified 0)

(defn- template-files
  [template-root]
  (let [directory         (io/file template-root)]
        (filter (fn [f] (not (.isDirectory f))) (file-seq directory))))

(defn- contentmap
  [template-root]
  (STRawGroupDir. template-root \$ \$))

(defn- reload-templates
  [template-root]
  (println "Reloading templates...")
  (if (nil? templatemap)
    (def templatemap (contentmap template-root))
    (.unload templatemap)))

(defn- reload-if-modified
  [template-root]
  (let [cur-last-modified (apply max (map #(.lastModified %) (template-files template-root)))]
    (if (> cur-last-modified last-modified)
      (do
        (def last-modified cur-last-modified)
        (reload-templates template-root)))))

(defn- add-value-to-template
  [k v template]
  (cond
    (seq? v)
      (doseq [item v] (add-value-to-template k item template))
    (map? v)
      (let [pairs (string/join "," (map name (keys v)))
            aggr-key (format "%s.{%s}" (name k) pairs)]
        (.addAggr template aggr-key (to-array (vals v))))
    :else
      (.add template (name k) v)))

(defn- layout-name-from-response
  [response-map]
  (let [layout (:layout response-map)]
    (cond
      (true? layout)
        "layout"
      (string? layout)
        layout
      :else
        nil)))

(defn wrap-template
  [handler template-root]
  (fn [req]
    (reload-if-modified template-root)
    (let [route-name                       (name (router/recognize req))
          template-options-default         { :template route-name :layout true }
          response-map                     (handler req)
          template-options                 (merge template-options-default response-map)
          template-name                    (:template template-options)
          template-vars                    (merge {} (:vars template-options))
          template-layout                  (.getInstanceOf templatemap (layout-name-from-response template-options))
          template-route                   (.getInstanceOf templatemap template-name)
          all-templates                    (filter #(not (nil? %)) [template-layout template-route])
          body                             (:body response-map)]
      (if (nil? body)
        (do
          (doseq [template all-templates]
            (doseq [[k v] template-vars]
              (add-value-to-template k v template)))
          (if (> (count all-templates) 1)
            (add-value-to-template "__content__" (.render (last all-templates)) (first all-templates)))
            ; TODO:
            ; This __content__ special var is less than ideal, but going w/ it for now due
            ; StringTemplate not supporting passthru (...) on dynamic templates. Probably
            ; a simpler way.
          (merge response-map { :body (.render (first all-templates)) }))
        response-map))))
