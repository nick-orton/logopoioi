(ns notepad.views.create
  (:require [notepad.views.layouts :as layouts])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/create" []
         (layouts/common
           [:p "Enter Text Below"]
           [:textarea#the_box {:rows 10 :cols 80}]))
