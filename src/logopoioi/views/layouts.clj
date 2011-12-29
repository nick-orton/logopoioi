(ns logopoioi.views.layouts
  (:use [noir.core :only [defpartial]]
        [clojure.data.json :only (json-str write-json read-json)]
        [hiccup.page-helpers :only [include-js include-css html5]]))

(defpartial common [& content]
            (html5
              [:head
               [:title "logopoioi"]
               [:link {:href "/img/favicon.ico" :rel "icon" 
                       :type "image/x-icon"}]
               (include-css "/css/reset.css")
               (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
                           "/js/logopoioi.js")]
              [:body
               [:div#wrapper
                content]]))

(defpartial json [& content]
  (json-str content))
