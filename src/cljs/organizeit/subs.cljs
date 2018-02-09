(ns organizeit.subs
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))


(rf/reg-sub
  :route-page
  (fn [db _]
    (:route-page db)))

(rf/reg-sub
  :store-keys
  (fn [db _]
    (keys (:groceries db))))

(rf/reg-sub
  :store
  (fn [db [_ name]]
    (get-in db [:groceries name])))

(rf/reg-sub
  :add-store-text
  (fn [db _]
    (:add-store-text db)))

(rf/reg-sub
  :items-count-range
  (fn [db [_ name]]
    (range (inc (count (get-in db [:groceries name]))))))

(rf/reg-sub
  :count-items
  (fn [db [_ name]]
    (count (get-in db [:groceries name]))))

(rf/reg-sub
  :item-name
  (fn [db [_ store pos]]
    (get-in db [:groceries store pos :name] "")))

(rf/reg-sub
  :item-checkbox
  (fn [db [_ store pos]]
    (get-in db [:groceries store pos :value] false)))

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