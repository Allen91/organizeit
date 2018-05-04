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