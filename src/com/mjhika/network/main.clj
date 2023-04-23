(ns com.mjhika.network.main
  (:gen-class)
  (:require
   [clojure.core.reducers :as r]
   [clojure.pprint :as pp]
   [com.mjhika.network.impl :as mjn]))

(set! *warn-on-reflection* true)

(defn -main [& _]
  (let [{:keys [cidr timeout]
         :or {cidr (mjn/get-local)
              timeout 100}} (mjn/cli-opts-parser *command-line-args*)]
    (pp/pprint
     (filterv #(:result %)
              (r/map #(mjn/timed-ping % timeout)
                     (mjn/get-all-subent-addresses cidr))))))
