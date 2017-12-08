(ns organizeit.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [organizeit.components.bootstrap :as bs]))

(defn router
  []
  [@(rf/subscribe [:route-page])])


(defn navigation
  []
  [bs/navbar {:fluid true}
   [bs/navbar-header
    [bs/navbar-brand
     [bs/button {:on-click #(rf/dispatch [:load-page home-page])} "OrganizeIt!"]]]])


(defn mailbox-panel
  []
  [bs/panel {:header "Mailbox" :class :text-center}
   [bs/button {:on-click #(rf/dispatch [:update-mailbox])} "Checked Mailbox"]
   [:p {:class "margin-top-20"}"Last checked: " [:strong bold @(rf/subscribe [:mailbox-time])]]])

(defn main-panel
  []
  [bs/panel {:header "Groceries"}
   [bs/table
    [:tbody
     [:tr
      [:td "Milk"]]]]])


(defn home-page
  []
  [:div {:class "home"}
   [navigation]
   [bs/jumbotron
    [:h1 "Welcome to "[:span {:class "main-color" }"OrganizeIt!"]]
    [:p {:class "margin-top-20 margin-bottom-30"} "An app for roommates to organize their daily chores."]
    [bs/button {:on-click #(rf/dispatch [:load-page main-page])} "Start Organizing here!!"]]])

(defn main-page
  []
  [:div
   [navigation]
   [bs/grid {:fluid false}
    [bs/row
     [bs/col {:md 8}
      [main-panel]]
     [bs/col {:md 4}
      [mailbox-panel]]]]])