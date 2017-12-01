(ns organizeit.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [organizeit.components.bootstrap :as bs]))

(defn router
  []
  [@(rf/subscribe [:route-page])]
  )

(defn navigation
  []
  [bs/navbar {:fluid true}
   [bs/navbar-header
    [bs/navbar-brand
     [bs/button {:on-click #(rf/dispatch [:load-page home-page])} "Organize it!"]]]]
  )

(defn home-page
  []
  [:div {:class "home"}
   [navigation]
   [bs/jumbotron {:style {:padding "48px"}}
    [:h1 {:style {:margin-bottom "10px"}} "Welcome to "[:span {:style {:color "#2f4e91"}}"OrganizeIt!"]]
    [:p {:style {:margin-bottom "30px"}} "An app for roommates to organize their daily chores."]
    [bs/button {:on-click #(rf/dispatch [:load-page main-page])} "Start Organizing here!!"]]])

(defn main-page
  []
  [:div
   [navigation]
   [bs/grid {:fluid false}
    [bs/row
     [bs/col {:md 8}
      [bs/panel {:header "Groceries"}
       [bs/table
        [:tbody
         [:tr
          [:td "Milk"]]]]]]
     [bs/col {:md 4}
      [bs/panel {:header "Mailbox" :class :text-center}
       [bs/button {:on-click #(rf/dispatch [:update-mailbox])} "Checked Mailbox"]
       [:p {:style {:margin-top "20px"}}"Last checked: " [:strong bold @(rf/subscribe [:mailbox-time])]]]]]]])