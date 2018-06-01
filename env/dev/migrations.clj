(ns migrations
  (:require [migratus.core :as migratus]))

(def config {:store :database
             :migration-dir "migrations"
             :db {:classname "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :subname "src/clj/organizeit/db/organizeit.db"}})

;(migratus/up config 20180316214131)

;(migratus/down config 20180316214131)