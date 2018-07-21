(ns threadfriend.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; Views

(def user-text (reagent/atom ""))

(defn home-page []
  [:div
   [:h1 "Welcome to ThreadFriend"]
   [:input {:type "text"
            :style {:width "100%" :height "100px"}
            :placeholder "Insert Slack Conversation Here"
            :value @user-text
            :on-change #(reset! user-text (-> % .-target .-value))}]
   [:input {
            :type "button"
            :value "Click Me"
            :on-click #(.log js/console @user-text)
            }
    ]
   ]
  )



;; -------------------------
;; Routes

(defonce page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
