(ns notepad.views.create
  (:require [notepad.views.layouts :as layouts])
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

(defpage "/edit/:id" {id :id}
  (layouts/common page ))


(defpage [:post "/save"] {:keys [box]}
  (layouts/common page ))
