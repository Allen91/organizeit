(ns organizeit.views
  (:require [goog.string :as g-string]
            [reagent.core :as r]
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

(defn item-checkbox
  [store item-name bought]
  [:td [bs/checkbox {:checked (true? bought)
                     :on-change #(rf/dispatch [:update-checkbox
                                               (.-target.checked %)
                                               store
                                               item-name])}]])

(defn item-text
  [item-name]
  [:td
   [:input.form-control {:type :text
                         :class "item-textbox"
                         :value item-name
                         :disabled true}]])

(defn item-row
  [store item-name bought]
  [:tr
   [item-text item-name]
   [item-checkbox store item-name bought]])

(defn store-panel
  [store]
  (let [items  @(rf/subscribe [:store store])]
    [bs/panel {:header store :class :text-center}
     [bs/table {:class :text-left}
      [:thead
       [:tr
        [:th "Item"]
        [:th {:class "col-2"} "Bought?"]]]
      [:tbody
       (for [item items]
         ^{:key (g-string/format "store-%s-item-%s" store item)}
         [item-row store (key item) (val item)])
       [:tr
        [:td
         [:input.form-control {:type :text
                               :class "item-textbox"
                               :on-blur #(rf/dispatch [:add-item store (.-target.value %)])}]]
        [:td {:class "item-checkbox-col"}
         [bs/checkbox {:disabled true
                       :checked false}]]]]]]))

(defn add-store-panel
  []
  (let [store-text @(rf/subscribe [:store-text])]
  [bs/panel {:header "Add Store" :class :text-center}
   [bs/table {:class :text-left}
    [:tbody
     [:tr
      [:td
       [:input.form-control {:type :text
                             :class "store-textbox"
                             :value store-text
                             :on-change #(rf/dispatch [:update-store-text (.-target.value %)])}]]
      [:td {:class "store-button-col"}
       [bs/button {:class "store-button"
                   :disabled (= store-text "")
                   :on-click #(rf/dispatch [:add-store store-text])} "Add Store"]]]]]]))

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
  (let [groceries  @(rf/subscribe [:groceries])]
    [bs/panel {:header "Groceries"  :class :text-center}
     (for [store groceries]
       ^{:key (g-string/format "store-%s" store)}
       [store-panel (key store)])
     [add-store-panel]]))


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