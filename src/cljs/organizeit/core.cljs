(ns organizeit.core
      (:require [reagent.core :as r]
                [re-frame.core :as rf]
                [organizeit.events]
                [organizeit.subs]
                [organizeit.views :as views]
                ))

(defn main []
  (rf/dispatch-sync [:initialize])
  (r/render-component [views/router]
                      (.getElementById js/document "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(enable-console-print!)