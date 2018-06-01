(defproject organizeit "0.1.0-SNAPSHOT"
  :description "OrganizeIt"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async  "0.3.443"]
                 [figwheel-sidecar "0.5.9"]
                 [reagent "0.6.2"]
                 [bidi "2.0.12"]
                 [re-frame "0.9.4"]
                 [cljs-ajax "0.7.2"]
                 [com.cognitect/transit-clj "0.8.300"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [clj-time "0.14.3"]
                 [re-frisk "0.5.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [migratus "1.0.0"]
                 [com.layerware/hugsql "0.4.8"]
                 [cljsjs/react-bootstrap "0.31.0-0" :exclusions [cljsjs/react]]
                 [ring "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [day8.re-frame/http-fx "0.1.4"]
                 [org.immutant/web "2.1.9"]]
  :resource-paths ["resources/public/"]
  :source-paths ["src/clj" "src/cljc" "env/dev"]
  :cljsbuild {
    :builds [{
              :id "dev"
              :source-paths ["src/cljs" "src/cljc" "env/dev"]
              :figwheel {:on-jsload "organizeit.core/on-js-reload"
                         ;; :open-urls will pop open your application
                         ;; in the default browser once Figwheel has
                         ;; started and compiled your application.
                         ;; Comment this out once it no longer serves you.
                         :websocket-host :js-client-host}
              :compiler {:main organizeit.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/core.js"
                         :output-dir "resources/public/js/compiled/out"
                         :optimizations :none
                         :pretty-print true
                         :preloads [re-frisk.preload]}}]}
  :figwheel {:http-server-root "public" ;; default and assumes "resources"
             :server-port 3448 ;; default
             :server-ip "0.0.0.0"
             :nrepl-port 7003
             :ring-handler organizeit.core/backend-handler})