(ns com.mjhika.network.impl
  (:require
   [babashka.cli :as cli]
   [clojure.core.async :as a])
  (:import
   java.net.InetAddress
   (org.apache.commons.net.util SubnetUtils
                                SubnetUtils$SubnetInfo)))

(defn cli-opts-parser [cli-args]
  (cli/parse-opts cli-args {:coerce {:cidr :string}}))

(defn ping-domain [domain timeout]
  (-> (InetAddress/getByName domain)
      (.isReachable timeout)))

(defn get-all-subent-addresses [cidr]
  (-> (.getInfo (SubnetUtils. cidr))
      .getAllAddresses
      seq))

(defn load-addresses [c sq]
  (a/go
    (a/>! c sq)))

(comment
  (ping-domain "google.com" 250)

  (:hostAddress (bean (InetAddress/getLocalHost)))

  (let [sn (.getInfo (SubnetUtils. "192.168.1.0/24"))
        all-addrs (seq (.getAllAddresses sn))]
    all-addrs)

  (cli-opts-parser *command-line-args*)
  (cli/parse-opts *command-line-args* {:coerce {:middleware :string}})

  (a/<! (a/timeout 1000)))
