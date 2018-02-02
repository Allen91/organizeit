(ns organizeit.events
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [organizeit.views :as views]
            [cljs-time.core :refer [plus months]]
            [cljs-time.local :refer [local-now]]
            [cljs-time.format :refer [formatter unparse]]
            [organizeit.components.bootstrap :as bs]))

(def common-formatter
  (formatter "MMMM yyyy"))

(def mailbox-formatter
  (formatter "MMMM dd yyyy' at 'HH:mm"))


(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:route-page views/home-page
     :mailbox-time "N/A"
     :rent-last-paid "N/A"
     :electricity-last-paid "N/A"
     :internet-last-paid "N/A"
     :add-store-text ""
     :groceries {"HEB" {0 {:name "milk" :value false} 1 {:name "sugar" :value true}} "Indian Store" {0 {:name "paneer" :value false}}}}))

(rf/reg-event-db
  :update-item-name
  (fn [db [_ new-value store pos key]]
    (assoc-in db [:groceries store pos :name] new-value)))

(rf/reg-event-db
  :update-item-value
  (fn [db [_ new-value store pos key]]

(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route-page new-route)))

(rf/reg-event-db
  :update-store-text
  (fn [db [_ text]]
    (assoc db :add-store-text text)))

(rf/reg-event-db
  :add-store
  (fn [db [_ new-store]]
    (-> db
        (assoc :groceries (conj (:groceries db) {new-store {}}))
        (assoc :add-store-text ""))))

(rf/reg-event-db
  :update-mailbox
  (fn [db [_ _]]
    (assoc db :mailbox-time (unparse mailbox-formatter (local-now)))))

(rf/reg-event-db
  :paid-current-rent
  (fn [db [_ _]]
    (assoc db :rent-last-paid (unparse common-formatter (local-now)))))

(rf/reg-event-db
  :paid-next-rent
  (fn [db [_ _]]
    (assoc db :rent-last-paid (unparse common-formatter (plus (local-now) (months 1))))))

(rf/reg-event-db
  :paid-current-electricity
  (fn [db [_ _]]
    (assoc db :electricity-last-paid (unparse common-formatter (local-now)))))

(rf/reg-event-db
  :paid-next-electricity
  (fn [db [_ _]]
    (assoc db :electricity-last-paid (unparse common-formatter (plus (local-now) (months 1))))))

(rf/reg-event-db
  :paid-current-internet
  (fn [db [_ _]]
    (assoc db :internet-last-paid (unparse common-formatter (local-now)))))

(rf/reg-event-db
  :paid-next-internet
  (fn [db [_ _]]
    (assoc db :internet-last-paid (unparse common-formatter (plus (local-now) (months 1))))))