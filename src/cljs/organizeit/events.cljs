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
     :mailbox-time "N/A"}))


(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route-page new-route)))

(rf/reg-event-db
  :update-mailbox
  (fn [db [_ _]]
    (-> (js/Date.)
        str
        (.split " GMT")
        first
        (->> (assoc db :mailbox-time)))))