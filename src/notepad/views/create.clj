(ns notepad.views.create
  (:require [notepad.views.layouts :as layouts]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [hiccup.page-helpers :only [javascript-tag]]
        [hiccup.core :only [html]]))

(defn text-area-setting [value]
  (str "set_text_area(\"" value "\");"))

(defn page [contents form-action id]
  [:div
    [:p "Enter Text Below"]
      [:form#the_form {:action form-action :method "post"} 
        [:textarea#the_box {:name "box" :rows 10 :cols 80} contents]
        [:input#hidden_id {:name "id" :type "hidden" :value id}]]
    [:div 
     [:a#save.btn {:href "#"} "save"] " | " 
     [:a#create.btn {:href "/create"} "create"]]])

(defpage "/create" []
  (layouts/common (page "" "/save" 0)))

(def SERVER {:host "127.0.0.1" :port 6379 :db 0})

(defpage "/edit/:id" {id :id}
  (redis/with-server SERVER
    (let [resp (redis/get (str "notepad:note:" id))]
      (layouts/common (page resp "/edit" id)))))

(defpage [:post "/edit"] {:keys [box id]}
  (redis/with-server SERVER
    (let [str-id (str "notepad:note:" id)]
      (redis/set str-id box)
      (redirect (str "/edit/" id)))))

(defpage [:post "/save"] {:keys [box]}
  (redis/with-server SERVER
    (let [num-id (redis/incr "notepad:note:id:count")
          str-id (str "notepad:note:" num-id)]
      (redis/set str-id box)
      (redirect (str "/edit/" num-id)))))
