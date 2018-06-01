(ns organizeit.core
  (:require [bidi.bidi :as bidi]
            [organizeit.routes :as routes]
            [ring.util.mime-type :as mime]
            [cognitect.transit :as t]
            [ring.util.response :as ring-r]
            [organizeit.db.db :as db]
            [clj-time.local :as l]
            [clj-time.core :refer [plus months]])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn transit-decode
  [input-stream]
  (when (> (.available input-stream) 0)
    (-> input-stream
        (t/reader :json)
        t/read)))

(defn dump-n-pass [input]
  (println input)
  input)

(defn transit-encode
  [payload]
  (let [output-stream (ByteArrayOutputStream.)
        writer (t/writer output-stream :json)]
    (t/write writer payload)
    (ByteArrayInputStream. (.toByteArray output-stream))))

(defn index-handler [_]
  "Handler for serving the base HTML"
  (-> (ring-r/resource-response "index.html")
      (assoc-in [:headers "Content-Type"] "text/html; charset-utf-8")))

(defn int-handler [request]
  "Handler for the internal static resources"
  (let [resource-type (-> request :route-params :resource-type)
        resource-name (-> request :route-params :resource-name)
        mime-type (mime/ext-mime-type resource-name)]
    (if-let [response (-> (format "%s/%s"
                                  resource-type
                                  resource-name)
                          (ring-r/resource-response)
                          (assoc-in [:headers "Content-Type"]
                                    (format "%s; charset=utf-8" mime-type)))]
      response
      (ring-r/not-found "Resource not found"))))

(defn update-mailbox
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (-> (l/local-now)
             (db/update-mailbox-now)
             transit-encode)})

(defn update-rent
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
             :body
             transit-decode
             :months
             (months)
             (plus (l/local-now) )
             db/update-rent-now
             transit-encode)})

(defn update-internet
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :months
              (months)
              (plus (l/local-now) )
              db/update-internet-now
              transit-encode)})

(defn update-electricity
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :months
              (months)
              (plus (l/local-now) )
              db/update-electricity-now
              transit-encode)})

(defn clear-store
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :store-id
              db/clear-store
              transit-encode)})

(defn parse-stores
  [stores]
  (->>
    (for [store stores]
      {(:store_id store) (:store_name store)})
    (apply merge-with conj)))

(defn add-store
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :store
              db/add-store
              transit-encode)})

(defn fetch-stores
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> (db/fetch-stores)
              parse-stores
              transit-encode)})

(defn add-item
  [request]
  (let [body (->> request
                  :body
                  transit-decode)]
    {:status 200
     :headers {"Content-Type" "application/transit+json"}
     :body (->> body
                :item-name
                db/add-item
                ((keyword "last_insert_rowid()"))
                (db/add-transaction (:store-id body) )
                transit-encode)}))

(defn check-item
  [request]
  (let [body (->> request
                  :body
                  transit-decode)]
    {:status 200
     :headers {"Content-Type" "application/transit+json"}
     :body (->> (db/check-item (:store-id body) (:item-id body) (:bought body))
                transit-encode)}))

(defn check-all
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :store-id
              db/check-all
              transit-encode)})

(defn clear-checked
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :store-id
              dump-n-pass
              db/clear-checked
              transit-encode)})

(defn fetch-mailbox
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (-> (db/get-mailbox-time)
             transit-encode)})

(defn fetch-rent
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (-> (db/get-rent-now)
             transit-encode)})

(defn fetch-internet
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (-> (db/get-internet-now)
             transit-encode)})

(defn fetch-electricity
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (-> (db/get-electricity-now)
             transit-encode)})

(defn parse-groceries
  [groceries]
  (->>
    (for [grocery groceries]
      (if (:item_id grocery)
        {(:store_id grocery) {(:item_id grocery) {:name (:item_name grocery)
                                                  :value (true? (= "true" (:bought grocery)))}}}
        {(:store_id grocery) {}}))
    (apply merge-with conj)))

(defn fetch-groceries
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> (db/get-groceries-now)
              parse-groceries
              (hash-map :groceries)
              transit-encode)})

(defn fetch-store-groceries
  [request]
  {:status 200
   :headers {"Content-Type" "application/transit+json"}
   :body (->> request
              :body
              transit-decode
              :store-id
              (db/get-store-groceries-now)
              parse-groceries
              transit-encode)})

(defn not-found-handler [_]
  "Handler to return 404 responses"
  (ring-r/not-found "Error 404: Could not find resource"))

(def backend-map
  {:index index-handler
   :static-internal int-handler
   :update-mailbox update-mailbox
   :fetch-mailbox fetch-mailbox
   :fetch-rent fetch-rent
   :update-rent update-rent
   :fetch-electricity fetch-electricity
   :update-electricity update-electricity
   :fetch-internet fetch-internet
   :update-internet update-internet
   :fetch-groceries fetch-groceries
   :fetch-store-groceries fetch-store-groceries
   :clear-store clear-store
   :add-store add-store
   :fetch-stores fetch-stores
   :add-item add-item
   :check-item check-item
   :check-all check-all
   :clear-checked clear-checked})

(defn backend-handler
  [request]
  (if-let [{:keys [handler route-params]}
           (bidi/match-route routes/routes (:uri request))]
    (if-let [handler-f (handler backend-map)]
      (handler-f (assoc request :route-params route-params))
      (index-handler request))
    (not-found-handler request)))