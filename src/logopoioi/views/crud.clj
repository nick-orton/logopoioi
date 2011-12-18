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

(defn create-page []
  [:div
    [:div 
      [:a#save.btn {:href "#"} "save"] " | " 
      [:a#create.btn {:href "/create"} "create"] " | "
      [:a#list.btn {:href "/list"} "list"]]
    [:form#the_form {:action "/save" :method "post"} 
      [:textarea#the_box {:name "box" :rows 40 } ]] ])

(defmacro login-required [& page]
 `(if (not (session/get :logged-in))
   (redirect "/login")
   (do
     ~@page)
  ))

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
  (login-required
    (layouts/common (create-page ))))

(defpage "/delete/:id" {id :id}
  (login-required 
    (note/delete (note/make-note id ""))
    (redirect "/list")))

(defpage "/view/:id" {id :id}
  (let [text (note/content (note/fetch id))]
      (layouts/common (view-page text))))

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
