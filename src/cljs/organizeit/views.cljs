(ns organizeit.views
  (:require [goog.string :as g-string]
            [reagent.core :as r]
            [re-frame.core :as rf]
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
  [store-id pos]
  (let [name @(rf/subscribe [:item-name store-id pos])
        bought @(rf/subscribe [:item-checkbox store-id pos])]
    [:tr
     [:td
      [:input.form-control {:type :text
                            :class "item-textbox"
                            :value name
                            :disabled true}]]
     [:td
      [bs/checkbox {:checked bought
                    :disabled (= name "")
                    :on-change #(rf/dispatch [:check-item
                                              (.-target.checked %)
                                              store-id
                                              pos])}]]]))

(defn store-panel
  [store-id]
  (let [items-keys @(rf/subscribe [:items-keys store-id])
        items-count @(rf/subscribe [:count-items store-id])
        store-name @(rf/subscribe [:store-name store-id])
        add-item-text @(rf/subscribe [:add-item-text store-id])]
    [bs/panel {:header store-name :class :text-center}
     [bs/table {:class :text-left}
      [:thead
       [:tr
        [:th {:class "col-md-11"} "Item"]
        [:th "Bought?"]]]
      [:tbody
       (for [pos items-keys]
         ^{:key (g-string/format "store-%s-item-%d" store-id pos)}
         [item-row store-id pos])
       [:tr
        [:td
         [:input.form-control {:type :text
                               :class "item-textbox"
                               :value add-item-text
                               :on-change #(rf/dispatch [:update-item-text store-id (.-target.value %)])}]]
        [:td {:class "store-button-col"}
         [bs/button {:class :btn-primary
                     :disabled (= add-item-text "")
                     :on-click #(rf/dispatch [:add-item store-id add-item-text])} "Add Item"]]]
       [:tr
        [:td
         [bs/button {:class :btn-danger
                     :on-click #(rf/dispatch [:clear-store store-id])} "Clear Store"]
         [bs/button {:class "btn-warning margin-left-20"
                     :on-click #(rf/dispatch [:clear-checked store-id])
                     :disabled (= items-count 0)} "Clear Checked"]]
        [:td
         [bs/button {:class :btn-success
                     :on-click #(rf/dispatch [:check-all store-id])
                     :disabled (= items-count 0)} "Check All"]]]]]]))

(defn add-store-panel
  []
  (let [add-store-text @(rf/subscribe [:add-store-text])]
  [bs/panel {:header "Add Store" :class :text-center}
   [bs/table {:class :text-left}
    [:tbody
     [:tr
      [:td
       [:input.form-control {:type :text
                             :class "store-textbox"
                             :value add-store-text
                             :on-change #(rf/dispatch [:update-store-text (.-target.value %)])}]]
      [:td {:class "store-button-col"}
       [bs/button {:class :btn-primary
                   :disabled (= add-store-text "")
                   :on-click #(rf/dispatch [:add-store add-store-text])} "Add Store"]]]]]]))

(defn mailbox-panel
  []
  [bs/panel {:header "Mailbox" :class :text-center}
   [bs/button {:class :btn-primary :on-click #(rf/dispatch [:update-mailbox])} "Checked Mailbox"]
   [:p {:class "margin-top-20"}"Last checked on " [:strong bold @(rf/subscribe [:mailbox-time])]]])

(defn rent-panel
  []
  [bs/panel {:header "Rent" :class :text-center}
   [bs/button {:class :btn-primary :on-click #(rf/dispatch [:paid-current-rent])} "Paid current month"]
   [bs/button {:class "btn-primary margin-left-20" :on-click #(rf/dispatch [:paid-next-rent])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:rent-last-paid])]]])

(defn electricity-panel
  []
  [bs/panel {:header "Electricity Bill" :class :text-center}
   [bs/button {:class :btn-primary :on-click #(rf/dispatch [:paid-current-electricity])} "Paid current month"]
   [bs/button {:class "btn-primary margin-left-20" :on-click #(rf/dispatch [:paid-next-electricity])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:electricity-last-paid])]]])

(defn internet-panel
  []
  [bs/panel {:header "Internet Bill" :class :text-center}
   [bs/button {:class :btn-primary :on-click #(rf/dispatch [:paid-current-internet])} "Paid current month"]
   [bs/button {:class "btn-primary margin-left-20" :on-click #(rf/dispatch [:paid-next-internet])} "Paid next month"]
   [:p {:class "margin-top-20"}"Paid for " [:strong bold @(rf/subscribe [:internet-last-paid])]]])

(defn main-panel
  []
  (let [stores  @(rf/subscribe [:store-keys])]
    [bs/panel {:header "Groceries"  :class :text-center}
     (for [store-id stores]
       ^{:key (g-string/format "store-%s" store-id)}
       [store-panel store-id])
     [add-store-panel]]))

(defn home-page
  []
  [:div {:class "home"}
   [navigation]
   [bs/jumbotron
    [:h1 "Welcome to "[:span {:class "main-color" }"OrganizeIt!"]]
    [:p {:class "margin-top-20 margin-bottom-30"} "An app for roommates to organize their daily chores."]
    [bs/button {:class :btn-primary :on-click #(rf/dispatch [:load-page main-page])} "Start Organizing here!!"]]])

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

(defn error-page
  []
  [:div
   [navigation]
   "Something went wrong. Please inform Allen about this issue and help him improve the Application." ])