(ns organizeit.events
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :as ajax]
            [bidi.bidi :as bidi]
            [organizeit.routes :as routes]
            [organizeit.views :as views]
            [cljs-time.core :refer [plus months to-default-time-zone]]
            [cljs-time.local :refer [local-now]]
            [day8.re-frame.http-fx])
  (:import goog.date.UtcDateTime))


(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:route-page views/home-page
     :mailbox-time nil
     :rent-last-paid nil
     :electricity-last-paid nil
     :internet-last-paid nil
     :add-store-text ""
     :api-result nil
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

(defn send-request
  [method-type uri-path params success]
  {:http-xhrio {:method method-type
                :uri (bidi/path-for routes/routes uri-path)
                :request-timeout 5000
                :params          params
                :format          (ajax/transit-request-format)
                :response-format (ajax/transit-response-format)
                :on-success      [success]
                :on-failure      [:bad-post-result]}})

(rf/reg-event-db
  :mailbox-success
  (fn [db [_ result]]
    (assoc db :mailbox-time (UtcDateTime.fromIsoString (:mailbox result)))))

(rf/reg-event-db
  :bad-post-result
  (fn [db [_ _]]
    (assoc db :route-page views/error-page)))

(rf/reg-event-fx
  :fetch-mailbox
  (fn [world _]
    (send-request :get :fetch-mailbox {} :mailbox-success)))

(rf/reg-event-fx
  :update-mailbox
  (fn [world _]
    (send-request :post :update-mailbox {} :fetch-mailbox)))

(rf/reg-event-db
  :rent-success
  (fn [db [_ result]]
    (assoc db :rent-last-paid (to-default-time-zone (UtcDateTime.fromIsoString (:rent result))))))

(rf/reg-event-fx
  :fetch-rent
  (fn [world _]
    (send-request :get :fetch-rent {} :rent-success)))

(rf/reg-event-fx
  :paid-current-rent
  (fn [world _]
    (send-request :post :update-rent {:months 0} :fetch-rent)))

(rf/reg-event-fx
  :paid-next-rent
  (fn [world _]
    (send-request :post :update-rent {:months 1} :fetch-rent)))

(rf/reg-event-db
  :internet-success
  (fn [db [_ result]]
    (assoc db :internet-last-paid (to-default-time-zone (UtcDateTime.fromIsoString (:internet result))))))

(rf/reg-event-fx
  :fetch-internet
  (fn [world _]
    (send-request :get :fetch-internet {} :internet-success)))

(rf/reg-event-fx
  :paid-current-internet
  (fn [world _]
    (send-request :post :update-internet {:months 0} :fetch-internet)))

(rf/reg-event-fx
  :paid-next-internet
  (fn [world _]
    (send-request :post :update-internet {:months 1} :fetch-internet)))

(rf/reg-event-db
  :electricity-success
  (fn [db [_ result]]
    (assoc db :electricity-last-paid (to-default-time-zone (UtcDateTime.fromIsoString (:electricity result))))))

(rf/reg-event-fx
  :fetch-electricity
  (fn [world _]
    (send-request :get :fetch-electricity {} :electricity-success)))

(rf/reg-event-fx
  :paid-current-electricity
  (fn [world _]
    (send-request :post :update-electricity {:months 0} :fetch-electricity)))

(rf/reg-event-fx
  :paid-next-electricity
  (fn [world _]
    (send-request :post :update-electricity {:months 1} :fetch-electricity)))
