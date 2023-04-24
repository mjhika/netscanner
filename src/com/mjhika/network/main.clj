(ns com.mjhika.network.main
  (:gen-class)
  (:require
   [clojure.pprint :as pp]
   [com.mjhika.network.impl :as mjn]))

(set! *warn-on-reflection* true)

(defn -main [& _]
  (let [{:keys [cidr timeout]
         :or {timeout 100}} (mjn/cli-opts-parser *command-line-args*)]
    (pp/pprint
     (filterv #(:result %)
              (mjn/sweep-cidr cidr timeout)))))
