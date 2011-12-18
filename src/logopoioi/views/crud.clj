(ns logopoioi.views.crud
  (:require [logopoioi.views.layouts :as layouts]
            [logopoioi.models.logos :as note]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [hiccup.page-helpers :only [javascript-tag]]
        [clojure.string :only [split]]
        [hiccup.core :only [html]]))

(defn text-area-setting [value]
  (str "set_text_area(\"" value "\");"))

(def save-btn [:a#save.btn {:href "#"} "save"])
(def create-btn [:a#create.btn {:href "/create"} "create"])
(def list-btn [:a#list.btn {:href "/list"} "list"])
(defn delete-btn [id] [:a#delete.btn {:href (str "/delete/" id)} "delete"])
(defn view-btn [id] [:a#view.btn {:href (str "/view/" id)} "share"])
(defn edit-btn [id] [:a#edit.btn {:href (str "/edit/" id)} "edit"])
(def _| " | ")

(defn create-page []
  [:div
    [:div 
      save-btn _| create-btn _| list-btn ]
    [:form#the_form {:action "/save" :method "post"} 
      [:textarea#the_box {:name "box" :rows 40 } ]] ])

(defn is-logged-in []
  (session/get :logged-in))

(defmacro login-required [& page]
 `(if (not (is-logged-in))
   (redirect "/login")
   (do
     ~@page)
  ))

(defn edit-page [contents id]
  [:div
    [:div 
      save-btn _| create-btn _| (delete-btn id) _| (view-btn id) _| list-btn]
    [:form#the_form {:action "/edit" :method "post"} 
      [:textarea#the_box {:name "box" :rows 40 } contents]
      [:input#hidden_id {:name "id" :type "hidden" :value id}]]])

(defn view-page [note]
  (let [id (note/identifier note)
        text (note/content note)]
    [:div
     (if (is-logged-in) 
       [:div
        save-btn _| create-btn _| (delete-btn id) _| (edit-btn id) _| list-btn])
    [:div#view_area 
      [:pre text]]]))

(defpage "/create" []
  (login-required
    (layouts/common (create-page ))))

(defpage "/delete/:id" {id :id}
  (login-required 
    (note/delete (note/make-note id ""))
    (redirect "/list")))

(defpage "/view/:id" {id :id}
  (let [note (note/fetch id)]
      (layouts/common (view-page note))))

(defpage "/edit/:id" {id :id}
  (login-required
    (let [text (note/content (note/fetch id))]
      (layouts/common (edit-page text id)))))

(defpage [:post "/edit"] {:keys [box id]}
  (login-required
    (let [note (note/make-note (str id) box)]
      (note/update note)
      (redirect (str "/edit/" id)))))

(defpage [:post "/save"] {:keys [box]}
  (login-required
    (let [note  (note/create box)]
      (redirect (str "/edit/" (note/identifier note))))))
