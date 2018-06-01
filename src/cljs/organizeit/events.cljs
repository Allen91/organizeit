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
     :add-item-text {}
     :api-result nil
     :stores {}
     :groceries {}}))

(rf/reg-event-fx
  :get-groceries
  (fn [world _]
    (send-request :post :fetch-groceries {} :groceries-success)))

(rf/reg-event-fx
  :get-stores
  (fn [world _]
    (send-request :post :fetch-stores {} :stores-success)))

(rf/reg-event-fx
  :check-item
  (fn [world [_ bought store-id item-id]]
    {:http-xhrio {:method :post
                  :uri (bidi/path-for routes/routes :check-item)
                  :request-timeout 5000
                  :params          {:store-id store-id :item-id item-id :bought bought}
                  :format          (ajax/transit-request-format)
                  :response-format (ajax/transit-response-format)
                  :on-success      [:get-store-groceries store-id]
                  :on-failure      [:bad-post-result]}}))

(rf/reg-event-db
  :load-page
  (fn [db [_ new-route]]
    (assoc db :route-page new-route)))

(rf/reg-event-db
  :update-store-text
  (fn [db [_ text]]
    (assoc db :add-store-text text)))

(rf/reg-event-db
  :update-item-text
  (fn [db [_ store-id text]]
    (assoc-in db [:add-item-text store-id] text)))

(rf/reg-event-fx
  :clear-store
  (fn [world [_ id]]
    (send-request :post :clear-store {:store-id id} :get-groceries)))

(rf/reg-event-fx
  :add-store
  (fn [world [_ store]]
    {:http-xhrio [{:method :post
                   :uri (bidi/path-for routes/routes :add-store)
                   :request-timeout 5000
                   :params          {:store store}
                   :format          (ajax/transit-request-format)
                   :response-format (ajax/transit-response-format)
                   :on-success      [:get-stores]
                   :on-failure      [:bad-post-result]}
                  {:method :post
                   :uri (bidi/path-for routes/routes :fetch-groceries)
                   :request-timeout 5000
                   :params          {}
                   :format          (ajax/transit-request-format)
                   :response-format (ajax/transit-response-format)
                   :on-success      [:groceries-success]
                   :on-failure      [:bad-post-result]}]}))

(rf/reg-event-fx
  :check-all
  (fn [world [_ store-id]]
    {:http-xhrio {:method :post
                  :uri (bidi/path-for routes/routes :check-all)
                  :request-timeout 5000
                  :params          {:store-id store-id}
                  :format          (ajax/transit-request-format)
                  :response-format (ajax/transit-response-format)
                  :on-success      [:get-store-groceries store-id]
                  :on-failure      [:bad-post-result]}}))

(rf/reg-event-fx
  :clear-checked
  (fn [world [_ store-id]]
    (send-request :post :clear-checked {:store-id store-id} :get-groceries)))

(rf/reg-event-fx
  :add-item
  (fn [world [_ store_id item-name]]
    (if (not= item-name "")
      {:http-xhrio {:method :post
                     :uri (bidi/path-for routes/routes :add-item)
                     :request-timeout 5000
                     :params          {:store-id store_id :item-name item-name}
                     :format          (ajax/transit-request-format)
                     :response-format (ajax/transit-response-format)
                     :on-success      [:get-store-groceries store_id]
                     :on-failure      [:bad-post-result]}})))

(rf/reg-event-fx
  :get-store-groceries
  (fn [world [_ store_id]]
    {:http-xhrio {:method :post
                  :uri (bidi/path-for routes/routes :fetch-store-groceries)
                  :request-timeout 5000
                  :params          {:store-id store_id}
                  :format          (ajax/transit-request-format)
                  :response-format (ajax/transit-response-format)
                  :on-success      [:store-success]
                  :on-failure      [:bad-post-result]}}))

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
    (assoc db :mailbox-time (to-default-time-zone (UtcDateTime.fromIsoString (:mailbox result))))))

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

(rf/reg-event-db
  :groceries-success
  (fn [db [_ result]]
    (assoc db :groceries (:groceries result))))

(rf/reg-event-db
  :store-success
  (fn [db [_ result]]
    (let [store (first result)
          key (key store)
          value (val store)]
      (-> db
          (assoc-in [:groceries key] value)
          (assoc-in [:add-item-text key] "")))))

(rf/reg-event-db
  :stores-success
  (fn [db [_ result]]
    (-> db
        (assoc :stores result)
        (assoc :add-store-text ""))))