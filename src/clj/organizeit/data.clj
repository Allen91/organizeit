(ns core
  (require [clojure.java.io :as io]
           [clojure.data.json :as json]))

(def write-to-file
  [content]
  (fn []
    (with-open [wrtr (writer "resources\\public\\data.json")]
      (.write wrtr (json/write-str content)))))