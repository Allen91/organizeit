(ns organizeit.events
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [organizeit.views :as views]
            [cljs-time.core :refer [plus months]]
            [cljs-time.local :refer [local-now]]
            [organizeit.components.bootstrap :as bs]))


(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:route-page views/home-page
     :mailbox-time nil
     :rent-last-paid nil
     :electricity-last-paid nil
     :internet-last-paid nil
     :add-store-text ""
     :groceries {"HEB" {0 {:name "milk" :value false} 1 {:name "sugar" :value true}} "Indian Store" {0 {:name "paneer" :value false}} "Walmart" {}}}))

(rf/reg-event-db
  :update-item-name
  (fn [db [_ new-value store pos]]
    (assoc-in db [:groceries store pos :name] new-value)))

(rf/reg-event-db
  :update-item-value
  (fn [db [_ new-value store pos]]
    (assoc-in db [:groceries store pos :value] new-value)))

(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route-page new-route)))

(rf/reg-event-db
  :update-store-text
  (fn [db [_ text]]
    (assoc db :add-store-text text)))

(rf/reg-event-db
  :clear-store
  (fn [db [_ store]]
    (update db :groceries dissoc store)))

(defn update-item-keys
  [old-map]
  (zipmap (range (count old-map)) (vals old-map)))

(rf/reg-event-db
  :check-all
  (fn [db [_ name]]
    (let [store-items (get-in db [:groceries name])]
      (->> store-items
           (map #(assoc (second %) :value true))
           (zipmap (range (count store-items)))
           (assoc-in db [:groceries name])))))

(rf/reg-event-db
  :clear-checked
  (fn [db [_ name]]
    (let [store-items (get-in db [:groceries name])]
      (->> store-items
           (remove #(true? (:value (second %))))
           (into {})
           (update-item-keys)
           (assoc-in db [:groceries name])))))

(rf/reg-event-db
  :trim-item
  (fn [db [_ name]]
    (let [store-items (get-in db [:groceries name])]
      (->> store-items
           (remove #(= "" (:name (second %))))
           (into {})
           (update-item-keys)
           (assoc-in db [:groceries name])))))

(rf/reg-event-db
  :add-store
  (fn [db [_ new-store]]
    (-> db
        (assoc :groceries (conj (:groceries db) {new-store {}}))
        (assoc :add-store-text ""))))

(rf/reg-event-db
  :add-item
  (fn [db [_ store new-item]]
    (let [pos (count (get-in db [:groceries store]))]
    (assoc-in db [:groceries store] (conj (get-in db [:groceries store]) {(+ pos 1) {:name new-item :value false}})))))

(rf/reg-event-db
  :update-mailbox
  (fn [db [_ _]]
    (assoc db :mailbox-time (date(local-now))))

(rf/reg-event-db
  :paid-current-rent
  (fn [db [_ _]]
    (assoc db :rent-last-paid (local-now))))

(rf/reg-event-db
  :paid-next-rent
  (fn [db [_ _]]
    (assoc db :rent-last-paid (plus (local-now) (months 1)))))

(rf/reg-event-db
  :paid-current-electricity
  (fn [db [_ _]]
    (assoc db :electricity-last-paid (local-now))))

(rf/reg-event-db
  :paid-next-electricity
  (fn [db [_ _]]
    (assoc db :electricity-last-paid (plus (local-now) (months 1)))))

(rf/reg-event-db
  :paid-current-internet
  (fn [db [_ _]]
    (assoc db :internet-last-paid (local-now))))

(rf/reg-event-db
  :paid-next-internet
  (fn [db [_ _]]
    (assoc db :internet-last-paid (plus (local-now) (months 1)))))