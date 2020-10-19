(ns webapp.core
  (:require [ring.util.response :as response]
            [ring.middleware.json :as ring-json]
            [ring.adapter.jetty :as jetty])
  (:gen-class))


(def result-map (atom {:result "value"}))

(defn body-json-handler [request]
  (ring-json/json-body-request request {:keywords? true :bigdecimals? false}))

(defn get-values [request]
  (:values(:address(:body request))))

(defn sum-values [vector]
  (reduce + vector))

(defn sum-digits [n]
  (loop [count n acc 0]
    (if (zero? count) acc
                      (recur
                        (quot count 10)
                        (+ acc (mod count 10))))))

(defn handler [request]
  (response/response (swap! result-map assoc :result(sum-digits
                                                      (sum-values
                                                        (get-values
                                                          (body-json-handler request)))))))

(def app
  (ring-json/wrap-json-response handler))


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