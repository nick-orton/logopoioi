(ns logopoioi.views.list
  (:require [logopoioi.views.layouts :as layouts]
            [logopoioi.models.logos :as note])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [logopoioi.views.crud :only [login-required]]
        [clojure.string :only [split]]))

(defpage "/" []
  (redirect "/list"))

(defn note->link [note] 
   [:li.menu_list 
     [:a {:href (str "/view/" (note/identifier note))} 
         (note/title note)]])

(defpage "/list" []
  (login-required       
    (let [notes (note/all-notes)
          links (map note->link notes)]
      (layouts/common [:div#stuff 
                          [:ul#links_list links]
                          [:a {:href "/create"} "create"]
                          [:div#tag_cloud ]]))))

(defn tags->bag [tags]
  (reduce 
    (fn [bag tag]
      (let [cur (get bag tag)
            cur' (if (nil? cur) 0 cur)]
        (assoc bag tag (inc cur')))) {} tags))

(defn- all-tags []
  (->> (note/all-notes)
       (map note/bangtags)
       (filter #(not (nil? %)))
       (flatten)
       (tags->bag)))

(defpage "/all_tags" []
  (layouts/json (all-tags)))

(defpage "/bangtag/:tag" {tag :tag} 
  (login-required
    (let [notes (note/notes-for tag)
          links (map note->link notes)]
      (layouts/common [:div#stuff
                       [:h1 (str "Notes Tagged !" tag)]
                       [:ul#links_list links]
                       [:a {:href "/create"} "create"]]))))
