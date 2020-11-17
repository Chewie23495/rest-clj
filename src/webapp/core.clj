(ns webapp.core
  (:require [ring.util.response :as response]
            [ring.middleware.json :as ring-json]
            [ring.adapter.jetty :as jetty]
            [schema.core :as s :include-macros true]
            [schema.coerce :as coerce]
            [schema.utils :as s-utils]
            [ring.middleware.cors :refer [wrap-cors]])
  (:gen-class))
(def request-schema
   {:address
         {:colorKeys [s/Str]
          :values [s/Num]}
    :meta {:digits s/Num
           :processingPattern s/Str}})


(defn body-json-handler [request]
  (ring-json/json-body-request request {:keywords? true :bigdecimals? false}))

(defn get-values [request]
  (-> request :address :values))

(defn sum-digits [n]
  (loop [count n acc 0]
    (if (zero? count) acc
                      (recur
                        (quot count 10)
                        (+ acc (mod count 10))))))

(defn handler [request]
  (if (= "application/json" (-> request
                                (get :content-type)))
    (response/response {:result (-> request
                                    (body-json-handler)
                                    (as-> body-map
                                          (s/validate request-schema (get body-map :body)))
                                    (get-values)
                                    (as-> values
                                          (reduce + values))
                                    (sum-digits))})
    (response/response {:error (format "Content-Typ %s" (-> request
                                                            (get :content-type)))})))


(def app
  (-> handler
      ring-json/wrap-json-response
      (wrap-cors
        :access-control-allow-origin [#".*"]
        :access-control-allow-methods [:post])))


(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))



"(defn handler [request]
  (ring.util.response/response
    (clojure.data.json/read-str request :key-fn keyword)))

(defn body-json-handler [request]
  (println
    (ring-json/json-body-request request {:keys [true true]})))

(defn get-values [request]
  (:values(:address(:body request))))

(defn sum-values [vector]
  (reduce + vector))

(defn sum-of-digits [n]
  (loop [count n acc 0]
    (if (zero? count) acc
                  (recur
                              (quot count 10)
                              (+ acc (mod count 10))))))

(defn handler [request]
  (response
    (get-values
      (body-json-handler request))))"