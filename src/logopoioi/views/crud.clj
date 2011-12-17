(ns logopoioi.views.crud
  (:require [logopoioi.views.layouts :as layouts]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [hiccup.page-helpers :only [javascript-tag]]
        [clojure.string :only [split]]
        [hiccup.core :only [html]]))

(defn text-area-setting [value]
  (str "set_text_area(\"" value "\");"))

(defn create-page []
  [:div
    [:div 
      [:a#save.btn {:href "#"} "save"] " | " 
      [:a#create.btn {:href "/create"} "create"] " | "
      [:a#list.btn {:href "/list"} "list"]]
    [:form#the_form {:action "/save" :method "post"} 
      [:textarea#the_box {:name "box" :rows 40 } ]] ])


(defn edit-page [contents id]
  [:div
    [:div 
      [:a#save.btn {:href "#"} "save"] " | " 
      [:a#create.btn {:href "/create"} "create"] " | "
      [:a#delete.btn {:href (str "/delete/" id)} "delete"] " | "
      [:a#view.btn {:href (str "/view/" id)} "view"] " | "
      [:a#list.btn {:href "/list"} "list"]]
    [:form#the_form {:action "/edit" :method "post"} 
      [:textarea#the_box {:name "box" :rows 40 } contents]
      [:input#hidden_id {:name "id" :type "hidden" :value id}]]])

(defn view-page [contents]
  [:div#view_area 
    [:pre contents]])

(defpage "/create" []
  (layouts/common (create-page )))

(def SERVER {:host "127.0.0.1" 
             :port 6379 
             :db 0 
             :timeout 5000})

(defpage "/delete/:id" {id :id}
  (redis/with-server SERVER
    (do
      (redis/del (str "notepad:note:" id))
      (redirect "/list"))))

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
    (let [num-id (redis/incr "notepad:idctr")
          str-id (str "notepad:note:" num-id)]
      (redis/set str-id box)
      (redirect (str "/edit/" num-id)))))
