(ns yetifactory.regex-match
  (:import [java.util.regex Pattern])
  (:gen-class)
  (:use [clojure.core.match :only [emit-pattern to-source groupable?]]))


(defrecord RegexPattern [regex])

(defmethod emit-pattern Pattern
  [pat]
  (RegexPattern. pat))

(defmethod to-source RegexPattern
  [pat ocr]
  `(re-find ~(:regex pat) ~ocr))

(defmethod groupable? [RegexPattern RegexPattern]
  [a b]
  (let [^Pattern ra (:regex a)
        ^Pattern rb (:regex b)]
    (and (= (.pattern ra) (.pattern rb))
         (= (.flags ra) (.flags rb)))))
