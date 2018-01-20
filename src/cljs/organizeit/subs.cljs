(ns organizeit.subs
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))


(rf/reg-sub
  :route-page
  (fn [db _]
    (:route-page db)))

(rf/reg-sub
  :groceries
  (fn [db _]
    (:groceries db)))

(rf/reg-sub
  :store-text
  (fn [db _]
    (:store-text db)))

(rf/reg-sub
  :store
  (fn [db [_ name]]
    (get-in db [:groceries name])))

(rf/reg-sub
  :mailbox-time
  (fn [db _]
    (:mailbox-time db)))

(rf/reg-sub
  :rent-last-paid
  (fn [db _]
    (:rent-last-paid db)))

(rf/reg-sub
  :electricity-last-paid
  (fn [db _]
    (:electricity-last-paid db)))

(rf/reg-sub
  :internet-last-paid
  (fn [db _]
    (:internet-last-paid db)))