(ns logopoioi.server
  (:require [noir.server :as server]))

(server/load-views "src/logopoioi/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8091"))]
    (server/start port {:mode mode
                        :ns 'logopoioi})))
