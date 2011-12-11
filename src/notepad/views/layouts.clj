(ns notepad.views.layouts
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial common [& content]
            (html5
              [:head
               [:title "notepad"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))
