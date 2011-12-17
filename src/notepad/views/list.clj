(ns notepad.views.list
  (:require [notepad.views.layouts :as layouts]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [clojure.string :only [split]]))

(def SERVER {:host "127.0.0.1" 
             :port 6379 
             :db 0 
             :timeout 5000})

(defn get-first-line-from [key]
  ((split (redis/get key) #"\n") 0))

(defpage "/" []
  (redirect "/list"))

(defpage "/list" []
  (redis/with-server SERVER
    (let [items (split (redis/keys "notepad:note*") #" ")
          id+titles (map (fn [key] 
                           [(.substring key 13)
                            (get-first-line-from key)]) 
                     items) 
          links (map (fn [id+title] 
                       [:li 
                        [:a {:href (str "/edit/" (first id+title))} 
                            (last id+title)]])
                     id+titles)]
      (layouts/common [:div#stuff 
                        [:ul#links_list links]
                        [:a {:href "/create"} "create"]]))))


