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
   :update-internet update-internet})

(defn backend-handler
  [request]
  (if-let [{:keys [handler route-params]}
           (bidi/match-route routes/routes (:uri request))]
    (if-let [handler-f (handler backend-map)]
      (handler-f (assoc request :route-params route-params))
      (index-handler request))
    (not-found-handler request)))