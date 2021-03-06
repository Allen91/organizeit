(ns backend
  (:require [organizeit.core :refer [backend-handler]]
            [immutant.web :as web]
            [ring.middleware.defaults :refer :all])
  (:gen-class))

(defn main
  [& args]
  (web/run-dmc backend-handler {:host "0.0.0.0" :port 3000 :path "/"}))