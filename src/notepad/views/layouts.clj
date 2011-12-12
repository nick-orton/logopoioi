(ns notepad.views.layouts
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-js include-css html5]]))

(defpartial common [& content]
            (html5
              [:head
               [:title "notepad"]
               (include-css "/css/reset.css")
               (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
                           "/js/notepad.js")]
              [:body
               [:div#wrapper
                content]]))
