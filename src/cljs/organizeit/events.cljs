(ns organizeit.events
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [organizeit.views :as views]
            [cljs-time.local :refer [local-now]]
            [cljs-time.format :refer [formatter unparse]]
            [organizeit.components.bootstrap :as bs]))

(def common-formatter
  "Custom datetime formatter"
  (formatter "MMMM dd yyyy"))

(def mailbox-formatter
  "Custom datetime formatter"
  (formatter "MMMM dd yyyy' at 'HH:mm"))


(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:route-page views/home-page
     :mailbox-time "N/A"
     :rent-last-paid "N/A"
     :electricity-last-paid "N/A"
     :internet-last-paid "N/A"}))


(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route-page new-route)))

(rf/reg-event-db
  :update-mailbox
  (fn [db [_ _]]
    (assoc db :mailbox-time (unparse mailbox-formatter (local-now)))))

(rf/reg-event-db
  :paid-rent
  (fn [db [_ _]]
    (assoc db :rent-last-paid (unparse common-formatter (local-now)))))

(rf/reg-event-db
  :paid-electricity
  (fn [db [_ _]]
    (assoc db :electricity-last-paid (unparse common-formatter (local-now)))))

(rf/reg-event-db
  :paid-internet
  (fn [db [_ _]]
    (assoc db :internet-last-paid (unparse common-formatter (local-now)))))
