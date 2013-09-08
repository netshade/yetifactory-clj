(ns yetifactory.header-defaults)

(defn wrap-header-defaults [handler header-defaults]
  (fn [req]
    (let [response-map (handler req)
          response-headers (or (:headers response-map) {})
          new-headers { :headers (merge header-defaults response-headers) }
          new-response (merge response-map new-headers)]
      new-response)))
