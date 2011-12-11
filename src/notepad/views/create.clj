(ns notepad.views.create
  (:require [notepad.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/create" []
         (common/layout
           [:p "Welcome to notepad"]))
