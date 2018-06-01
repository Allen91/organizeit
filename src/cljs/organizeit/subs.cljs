(ns organizeit.subs
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.string :as g-string]
            [cljs-time.format :as c-time-f]))

(def common-formatter
  (c-time-f/formatter "MMMM"))

(def mailbox-formatter
  (c-time-f/formatter "MMMM dd, yyyy' at 'HH:mm"))

(defn get-month
  [date-obj]
  (if date-obj
    (c-time-f/unparse common-formatter date-obj)
    "N/A"))

(defn get-mailbox-time
  [date-obj]
  (if date-obj
    (c-time-f/unparse mailbox-formatter date-obj)
    "N/A"))

(rf/reg-sub
  :route-page
  (fn [db _]
    (:route-page db)))

(rf/reg-sub
  :store-keys
  (fn [db _]
    (keys (:groceries db))))

(rf/reg-sub
  :store-name
  (fn [db [_ id]]
    (get-in db [:stores id])))

(rf/reg-sub
  :add-store-text
  (fn [db _]
    (:add-store-text db)))

(rf/reg-sub
  :add-item-text
  (fn [db [_ store-id]]
    (get-in db [:add-item-text store-id] "")))

(rf/reg-sub
  :items-count-range
  (fn [db [_ name]]
    (range 1 (inc (count (get-in db [:groceries name]))))))

(rf/reg-sub
  :items-keys
  (fn [db [_ name]]
    (keys (get-in db [:groceries name]))))

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
    (get-mailbox-time (:mailbox-time db))))

(rf/reg-sub
  :rent-last-paid
  (fn [db _]
    (get-month (:rent-last-paid db))))

(rf/reg-sub
  :electricity-last-paid
  (fn [db _]
    (get-month (:electricity-last-paid db))))

(rf/reg-sub
  :internet-last-paid
  (fn [db _]
    (get-month (:internet-last-paid db))))