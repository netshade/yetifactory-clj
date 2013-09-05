(ns yetifactory.template
  (:use [clojure.stacktrace])
  (:require [me.raynes.laser :as l])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.pprint :as pprint])
  (:gen-class))


(defn wrap-template
  [handler template-root]
  (let [directory         (io/file template-root)
        directory-path    (str (.getAbsolutePath directory) "/")
        files             (filter (fn [f] (not (.isDirectory f))) (file-seq directory))
        filepaths         (map (fn [f] (keyword (string/replace (.getAbsolutePath f) directory-path ""))) files)
        filemap           (zipmap filepaths files)
        contentmap        (zipmap filepaths (map (fn [f] (slurp (.getAbsolutePath f))) files))]
    (fn [req]
      (let [response-map                                (handler req)
            { template :template selectors :selectors } response-map]
        (if-let [template-key (keyword template)]
          (if-let [content (template-key contentmap)]
            (let [template-args (flatten (map identity (merge {} selectors)))
                  result (apply l/document (cons (l/parse content) template-args))]
              (merge response-map { :body result }))
            response-map)
          response-map)))))
