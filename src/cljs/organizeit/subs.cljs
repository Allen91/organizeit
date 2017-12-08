(ns organizeit.subs
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))


(rf/reg-sub
  :route-page
  (fn [db _]
    (:route-page db)))

(rf/reg-sub
  :mailbox-time
  (fn [db _]
    (:mailbox-time db)))
