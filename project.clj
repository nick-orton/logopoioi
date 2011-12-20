(defproject logopoioi "0.1.0-SNAPSHOT"
            :description "A pastebin app"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [org.apache.hbase/hbase "0.90.4"]
                           [org.mozilla/rhino "1.7R3"]
                           [noir "1.2.1"]]
            :main logopoioi.server)

