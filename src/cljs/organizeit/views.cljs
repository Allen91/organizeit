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

(defn item-row
  []
  [:tr
   [:td
    [:input.form-control {:type :text
             :class "item-textbox"
             :value "Milk"}]]
   [:td
    [:input {:type :checkbox}]]])

(defn mailbox-panel
  []
  [bs/panel {:header "Mailbox" :class :text-center}
   [bs/button {:on-click #(rf/dispatch [:update-mailbox])} "Checked Mailbox"]
   [:p {:class "margin-top-20"}"Last checked on " [:strong bold @(rf/subscribe [:mailbox-time])]]])

(defn rent-panel
  []
  [bs/panel {:header "Rent" :class :text-center}
   [bs/button {:on-click #(rf/dispatch [:paid-current-rent])} "Paid current month"]
   [bs/button {:class "margin-left-20" :on-click #(rf/dispatch [:paid-next-rent])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:rent-last-paid])]]])

(defn electricity-panel
  []
  [bs/panel {:header "Electricity Bill" :class :text-center}
   [bs/button {:on-click #(rf/dispatch [:paid-current-electricity])} "Paid current month"]
   [bs/button {:class "margin-left-20" :on-click #(rf/dispatch [:paid-next-electricity])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:electricity-last-paid])]]])

(defn internet-panel
  []
  [bs/panel {:header "Internet Bill" :class :text-center}
   [bs/button {:on-click #(rf/dispatch [:paid-current-internet])} "Paid current month"]
   [bs/button {:class "margin-left-20" :on-click #(rf/dispatch [:paid-next-internet])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:internet-last-paid])]]])

(defn main-panel
  []
  [bs/panel {:header "Groceries"  :class :text-center}
   [bs/table {:class :text-left}
    [:thead
     [:tr
      [:th "Item"]
      [:th "Bought?"]]]
    [:tbody
     [item-row]]]])

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
      [mailbox-panel]
      [rent-panel]
      [electricity-panel]
      [internet-panel]]]]])