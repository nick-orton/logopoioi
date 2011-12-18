(ns logopoioi.views.login
  (:require [logopoioi.views.layouts :as layouts]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]))

(def questions ["I don't know you, B"
                "What is the airspeed velocity of an unladen sparrow?"])

(defn login-page [failed?]
  [:div
    (if failed? [:p  "That wasn't right"] "")
    [:p (questions 0)]
    [:form {:action "/login" :method "post"}
      [:input {:type "password" :name "login"}]]])

(defpage "/login" []
  (layouts/common (login-page false) ))

(defpage [:post "/login"] {:keys [login]}
  (let [pwd (System/getenv "LOGOPOIOI_PWD")]
    (if (= pwd login)
      (do
        (session/put! :logged-in true)
        (redirect "/list"))
      (layouts/common (login-page true)))))

(defpage "/logout" []
  (session/remove! :logged-in)
  (layouts/common (login-page false)))
