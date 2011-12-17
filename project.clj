(defproject notepad "0.1.0-SNAPSHOT"
            :description "A pastebin app"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [org.clojars.tavisrudd/redis-clojure "1.3.0"]
                           [noir "1.2.1"]]
            :main notepad.server)

