(ns organizeit.core
      (:require [reagent.core :as r]
                [re-frame.core :as rf]
                [clojure.string :as str]
                ))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:text-value "Guest!"
     :seconds-elapsed 0
     :route page
     }))

(rf/reg-event-db
  :text-change
  (fn [db [_ new-text-value]]
    (assoc db :text-value new-text-value)))

(rf/reg-event-db
  :secs-change
  (fn [db [_ new-secs-value]]
    (assoc db :seconds-elapsed new-secs-value)))

(rf/reg-event-db
  :secs-reset
  (fn [db [_ _]]
    (assoc db :seconds-elapsed 0)))

(rf/reg-event-db
  :text-event
  (fn [db [_ value]]
    #(rf/dispatch [:text-change value]))
  )

(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route new-route)))

(rf/reg-sub
  :text-val
  (fn [db _]
    (:text-value db)))

(rf/reg-sub
  :secs-val
  (fn [db _]
    (:seconds-elapsed db)))

(rf/reg-sub
  :route
  (fn [db _]
    (:route db)))

(defn input-fn [value]
  [:input {:type "text"
           :value value
           :on-change #(rf/dispatch [:text-change (-> % .-target .-value)])
           }])

(defn timer-component []
    (fn []
      (js/setTimeout #(rf/dispatch [:secs-change (inc @(rf/subscribe [:secs-val]))]) 1000)
      [:div "Seconds Elapsed: " @(rf/subscribe [:secs-val])]))

(defn reset-name []
  [:input {:type "button" :value "Reset!"
           :on-click #(rf/dispatch [:text-change "Guest!"])
           }])

(defn page-button []
  [:div
   [:input {:type "button" :value "Page 2!"
            :on-click #(rf/dispatch [:load-page page2])}]]
 )

(defn page2-button []
  [:div
   [:input {:type "button" :value "Page 1!"
            :on-click #(rf/dispatch [:load-page page])}]]
  )

(defn text-component []
  [:div
   [:h1 "Hello " @(rf/subscribe [:text-val])]
   [:p "Change it here: " (input-fn @(rf/subscribe [:text-val]))]
   ])

(defn page []
  [:div
   [timer-component]
   [text-component]
   [reset-name]
   [page-button]])

(defn page2 []
  [:div
   [page2-button]])

(defn router
  []
    [@(rf/subscribe [:route])]
  )

(defn main []
  (rf/dispatch-sync [:initialize])
  (r/render-component [router]
                      (.getElementById js/document "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(enable-console-print!)