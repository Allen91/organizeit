(ns organizeit.subs
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.string :as g-string]
            [cljs-time.core :refer [hour]]))


(defn month-map
  [month-number]
  ({"01" "January"
    "02" "February"
    "03" "March"
    "04" "April"
    "05" "May"
    "06" "June"
    "07" "July"
    "08" "August"
    "09" "September"
    "10" "October"
    "11" "November"
    "12" "December"} month-number))

(defn get-month
  [date-obj]
  (if date-obj
    (month-map (subs (str date-obj) 4 6))
    "N/A"))

(defn get-mailbox-time
  [date-obj]
  (let [date-str (str date-obj)
        month (month-map (subs date-str 4 6))
        date (subs date-str 6 8)
        hrs (subs date-str 9 11)
        mins (subs date-str 11 13)
        year (subs date-str 0 4)]
    (if date-obj
      (g-string/format "%s %s, %s at %s:%s" date month year hrs mins)
      "N/A")))

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