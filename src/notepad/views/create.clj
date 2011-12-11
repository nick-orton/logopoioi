(ns notepad.views.create
  (:require [notepad.views.layouts :as layouts]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(def page 
  [:div
    [:p "Enter Text Below"]
      [:form#the_form {:action "save" :method "post"} 
        [:textarea#the_box {:name "box" :rows 10 :cols 80}]]
    [:div 
     [:a#save.btn {:href "#"} "save"] " | " [:a#create.btn {:href "/create"} "create"]]])

(defpage "/create" []
  (layouts/common page))

(def SERVER {:host "127.0.0.1" :port 6379 :db 0})

(defpage "/edit/:id" {id :id}
  (let [resp (redis/with-server SERVER
               (do
                 (redis/get (str "notepad:note:" id))))]
  (layouts/common page [:div resp])))


(defpage [:post "/save"] {:keys [box]}
  (let [resp (redis/with-server SERVER
               (do
                 (let [id (redis/incr "notepad:note:id:count")]
                   (redis/set (str "notepad:note:" id) box) )))]
  (layouts/common page [:div resp])))
