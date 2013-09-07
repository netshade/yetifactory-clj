(ns yetifactory.template
  (:use [clojure.stacktrace])
  (:import [org.stringtemplate.v4 ST STRawGroupDir])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.pprint :as pprint])
  (:gen-class))


(defn- template-files
  [template-root]
  (let [directory         (io/file template-root)]
        (filter (fn [f] (not (.isDirectory f))) (file-seq directory))))

(defn- contentmap
  [template-root]
  (STRawGroupDir. template-root \$ \$))

(def templatemap nil)
(def last-modified 0)
(defn reload-templates
  [template-root]
  (println "Reloading templates...")
  (if (nil? templatemap)
    (def templatemap (contentmap template-root))
    (.unload templatemap)))

(defn reload-if-modified
  [template-root]
  (let [cur-last-modified (apply max (map #(.lastModified %1) (template-files template-root)))]
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

(defn wrap-template
  [handler template-root]
  (fn [req]
    (reload-if-modified template-root)
    (let [response-map                                    (handler req)
          { template-name :template template-vars :vars } response-map]
      (if-let [template (.getInstanceOf templatemap template-name)]
        (do
          (doseq [[k v] (merge {} template-vars)]
            (add-value-to-template k v template))
          (merge response-map { :body (.render template) }))
        response-map))))
