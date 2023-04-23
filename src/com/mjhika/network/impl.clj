(ns com.mjhika.network.impl
  (:require
   [babashka.cli :as cli]
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

(comment
  (def i "10.1.1.1")

  (ip/make-network "10.1.1.1/24")
  (seq (ip/make-network i 24))

  (ip/ip-address (ip/make-ip-address "10.1.1.1"))

  (ip/make-network (ip/make-ip-address i) 24)

  (map #(ip/ip-address %)
       (ip/make-network (ip/make-ip-address i) 24)))

(defn get-all-subent-addresses
  "will return a `coll` of IPAddresses as strings

  This function will not return anything for CIDR notations
  of 30 and 31 as there are no usable IP addresses
  from that range."
  [^String cidr]
  (let [sn (.getInfo (SubnetUtils. cidr))
        sn-bean (bean sn)]
    (cond
      (= "255.255.255.255" (:netmask sn-bean)) (list (:address sn-bean))
      :else (seq (.getAllAddresses sn)))))

(defn get-local-ip
  "Get's the IP of the host and assumes a /24 network."
  []
  (str (:hostAddress (bean
                      (InetAddress/getLocalHost)))
       "/24"))

(comment

  (let [sn (.getInfo (SubnetUtils. "1.1.1.1/32"))
        sn-b (bean sn)]
    (list (:address sn-b)))

  (let [sn (.getInfo (SubnetUtils. "10.1.1.0/24"))]
    (seq (.getAllAddresses sn)))

  (get-all-subent-addresses "1.1.1.1/32")

  (map #(timed-ping % 150) (get-all-subent-addresses "1.1.1.1/32"))

  (:hostAddress
   (bean
    (InetAddress/getLocalHost))))
