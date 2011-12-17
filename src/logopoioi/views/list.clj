(ns logopoioi.views.list
  (:require [logopoioi.views.layouts :as layouts]
            [logopoioi.models.logos :as note]
            [redis.core :as redis])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [clojure.string :only [split]]))

(defpage "/" []
  (redirect "/list"))

(defpage "/list" []
  (let [notes (note/all-notes)
        links (map (fn [note] 
                     [:li 
                       [:a {:href (str "/edit/" (note/identifier note))} 
                         (note/title note)]])
                     notes)]
      (layouts/common [:div#stuff 
                        [:ul#links_list links]
                        [:a {:href "/create"} "create"]])))
