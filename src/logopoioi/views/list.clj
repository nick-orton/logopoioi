(ns logopoioi.views.list
  (:require [logopoioi.views.layouts :as layouts]
            [logopoioi.models.logos :as note])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [logopoioi.views.crud :only [login-required]]
        [clojure.string :only [split]]))

(defpage "/" []
  (redirect "/list"))

(defpage "/list" []
  (login-required       
    (let [notes (note/all-notes)
          links (map (fn [note] 
                       [:li 
                         [:a {:href (str "/view/" (note/identifier note))} 
                           (note/title note)]])
                       notes)]
        (layouts/common [:div#stuff 
                          [:ul#links_list links]
                          [:a {:href "/create"} "create"]]))))
