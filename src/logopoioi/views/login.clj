(ns logopoioi.views.login
  (:require [logopoioi.views.layouts :as layouts]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]))

(def questions ["I don't know you, B"
                "What is the airspeed velocity of an unladen sparrow?"
                "What's the frequency Kenneth?"
                "Who's your Daddy?"
                "Which way is up?"
                "Where are my pants?"
                "Who wears short shorts?" ])

(def error_msgs ["That wasn't right" 
                 "Try again, son"
                 "Have you ever seen a baby pigeon? Well, neither have I. I got a hunch they exist"
                 "It happens sometimes. People just explode. Natural causes."]) 

(def r (java.util.Random.))

(defn random-from [messages]
  (messages (.nextInt r (count messages))))

(defn login-page [failed?]
  [:div
    (if failed? [:p.error_msg  (random-from error_msgs)] "")
    [:p#question (random-from questions)]
    [:form#login_form {:action "/login" :method "post"}
      [:div
        [:input#pwd_input {:type "password" :name "login"}]]]])

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
