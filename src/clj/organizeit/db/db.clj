(ns organizeit.db.db
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "organizeit/db/sql/organizeit.sql")

(def conn {:classname "org.sqlite.JDBC"
           :subprotocol "sqlite"
           :subname "src/clj/organizeit/db/organizeit.db"})

(defn update-mailbox-now
  [time]
  (update-mailbox conn {:group_id 1 :mailbox time}))

(defn get-mailbox-time
  []
  (get-mailbox conn {:group_id 1}))

(defn update-rent-now
  [time]
  (update-rent conn {:group_id 1 :rent time}))

(defn get-rent-now
  []
  (get-rent conn {:group_id 1}))

(defn get-internet-now
  []
  (get-internet conn {:group_id 1}))

(defn update-internet-now
  [time]
  (update-internet conn {:group_id 1 :internet time}))

(defn update-electricity-now
  [time]
  (update-electricity conn {:group_id 1 :electricity time}))

(defn get-electricity-now
  []
  (get-electricity conn {:group_id 1}))

(defn get-groceries-now
  []
  (get-groceries conn {:group_id 1}))

(defn get-store-groceries-now
  [store_id]
  (get-store-groceries conn {:group_id 1 :store_id store_id}))

(defn add-store
  [store-name]
  (insert-store conn {:store_name store-name :group_id 1}))

(defn fetch-stores
  []
  (get-stores conn {:group_id 1}))

(defn add-transaction
  [store-id item-id]
  (insert-transaction conn {:store_id store-id :item_id item-id}))

(defn add-item
  [item-name]
  (insert-item conn {:item_name item-name}))

(defn clear-store
  [store-id]
  (delete-store conn {:store_id store-id}))

(defn check-item
  [store-id item-id bought]
  (update-transaction conn {:store_id store-id :item_id item-id :bought (if bought
                                                                          "true"
                                                                          "false")}))

(defn check-all
  [store-id]
  (update-transactions-store conn {:store_id store-id}))

(defn clear-checked
  [store-id]
  (delete-transactions-store conn {:store_id store-id}))