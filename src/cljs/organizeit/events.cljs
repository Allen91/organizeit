(ns organizeit.events
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [organizeit.views :as views]
            [organizeit.components.bootstrap :as bs]))

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
    (-> (js/Date.)
        str
        (.split #" GMT")
        first
        (->> (assoc db :mailbox-time)))))

(rf/reg-event-db
  :paid-rent
  (fn [db [_ _]]
    (-> (js/Date.)
        str
        (.split #" \d\d:")
        first
        (->> (assoc db :rent-last-paid)))))

(rf/reg-event-db
  :paid-electricity
  (fn [db [_ _]]
    (-> (js/Date.)
        str
        (.split #" \d\d:")
        first
        (->> (assoc db :electricity-last-paid)))))

(rf/reg-event-db
  :paid-internet
  (fn [db [_ _]]
    (-> (js/Date.)
        str
        (.split #" \d\d:")
        first
        (->> (assoc db :internet-last-paid)))))