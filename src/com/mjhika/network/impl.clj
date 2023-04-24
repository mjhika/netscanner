(ns com.mjhika.network.impl
  (:require
   [babashka.cli :as cli]
   [clojure.core.reducers :as r]
   [clojure.network.ip :as ip])
  (:import
   java.net.InetAddress))

(set! *warn-on-reflection* true)

(defn cli-opts-parser [cli-args]
  (cli/parse-opts cli-args {:alias {:c :cidr
                                    :t :timeout}
                            :coerce {:cidr :string
                                     :timeout :int}}))

(defn timed-ping
  "thank you Clojure Cookbook for the timed-ping"
  [^String ip ^Integer timeout]
  (let [addr (InetAddress/getByName ip)
        start (. System (nanoTime))
        result (.isReachable addr timeout)
        total (/ (double (- (. System (nanoTime)) start)) 1000000.0)]
    {:address ip
     :time total
     :result result}))

(defn get-all-subent-addresses
  "will return a `coll` of IPAddresses as strings"
  [cidr]
  (mapv #(ip/ip-address %)
        (seq (ip/make-network cidr))))

(defn sweep-cidr [cidr timeout]
  (r/map #(timed-ping % timeout)
         (get-all-subent-addresses cidr)))

(comment
  (ip/ip-address (ip/make-network "10.1.1.1/32"))

  (get-all-subent-addresses "10.1.1.1/24")
  (get-all-subent-addresses "10.1.1.1/32")

  (map #(timed-ping % 50) (get-all-subent-addresses "10.1.1.1/32"))

  (into []
        (r/map #(timed-ping % 20)
               (get-all-subent-addresses "10.1.1.1/24")))

  (filterv #(:result %)
           (sweep-cidr "10.1.1.1/24" 10))

  (timed-ping "10.1.1.0" 10)
  ;; => {:address "10.1.1.0", :time 503.5155, :result false} result on windows
  ;; failed to use proper timeout?
  )
