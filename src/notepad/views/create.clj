(ns notepad.views.create
  (:require [notepad.views.layouts :as layouts]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [hiccup.page-helpers :only [javascript-tag]]
        [hiccup.core :only [html]]))

(defn text-area-setting [value]
  (str "set_text_area(\"" value "\");"))

(defn create-page []
  [:div
    [:form#the_form {:action "/save" :method "post"} 
      [:textarea#the_box {:name "box" :rows 10 :cols 80} ]]
    [:div 
      [:a#save.btn {:href "#"} "save"] " | " 
      [:a#create.btn {:href "/create"} "create"]]])


(defn edit-page [contents id]
  [:div
    [:form#the_form {:action "/edit" :method "post"} 
      [:textarea#the_box {:name "box" :rows 10 :cols 80} contents]
      [:input#hidden_id {:name "id" :type "hidden" :value id}]]
    [:div 
      [:a#save.btn {:href "#"} "save"] " | " 
      [:a#create.btn {:href "/create"} "create"] " | "
      [:a#delete.btn {:href (str "/delete/" id)} "delete"]]])

(defn view-page [contents]
  [:div#view_area 
    [:pre contents]])

(defpage "/create" []
  (layouts/common (create-page )))

(def SERVER {:host "127.0.0.1" :port 6379 :db 0})

(defpage "/delete/:id" {id :id}
  (redis/with-server SERVER
    (do
      (redis/del (str "notepad:note:" id))
      (redirect "/create"))))

(defpage "/view/:id" {id :id}
  (redis/with-server SERVER
    (let [resp (redis/get (str "notepad:note:" id))]
      (layouts/common (view-page resp)))))


(defpage "/edit/:id" {id :id}
  (redis/with-server SERVER
    (let [resp (redis/get (str "notepad:note:" id))]
      (layouts/common (edit-page resp id)))))

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
