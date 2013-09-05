(ns yetifactory.template
  (:use [clojure.stacktrace])
  (:require [me.raynes.laser :as l])
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
  (let [directory         (io/file template-root)
        directory-path    (str (.getAbsolutePath directory) "/")
        files             (template-files template-root)
        filepaths         (map (fn [f] (keyword (string/replace (.getAbsolutePath f) directory-path ""))) files)
        filemap           (zipmap filepaths files)]
    (zipmap filepaths (map (fn [f] (slurp (.getAbsolutePath f))) files))))

(def templatemap nil)
(def last-modified 0)
(defn reload-templates
  [template-root]
  (println "Reloading templates...")
  (def templatemap (contentmap template-root)))

(defn reload-if-modified
  [template-root]
  (let [cur-last-modified (apply max (map #(.lastModified %1) (template-files template-root)))]
    (if (> cur-last-modified last-modified)
      (do
        (def last-modified cur-last-modified)
        (reload-templates template-root)))))


(defn wrap-template
  [handler template-root]
  (fn [req]
    (reload-if-modified template-root)
    (let [response-map                                (handler req)
          { template :template selectors :selectors } response-map]
      (if-let [template-key (keyword template)]
        (if-let [content (template-key templatemap)]
          (let [template-args (flatten (map identity (merge {} selectors)))
                result (apply l/document (cons (l/parse content) template-args))]
            (merge response-map { :body result }))
          response-map)
        response-map))))
