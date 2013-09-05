(ns yetifactory.no-cache)

(defn wrap-no-cache [handler]
  (fn [req]
    (let [response-map (handler req)
          response-headers (or (:headers response-map) {})
          no-cache {"Cache-Control" "no-cache, no-store"}
          new-headers { :headers (merge no-cache response-headers) }
          new-response (merge response-map new-headers)]
      new-response)))
