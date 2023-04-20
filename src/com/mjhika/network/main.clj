(ns com.mjhika.network.main
  (:gen-class)
  (:require
   [clojure.pprint :as pp]
   [com.mjhika.network.impl :as mjn]
   [clojure.core.reducers :as r]))

(set! *warn-on-reflection* true)

(defn -main [& args]
  (let [{:keys [cidr timeout]
         :or {timeout 100}} (mjn/cli-opts-parser *command-line-args*)
        ip-addresses (mjn/get-all-subent-addresses cidr)]
    (pp/pprint
     (filterv #(:result %)
              (r/map #(mjn/timed-ping % timeout) ip-addresses)))))
