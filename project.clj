(defproject organizeit "0.1.0-SNAPSHOT"
  :description "OrganizeIt"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [org.clojure/core.async  "0.3.443"]
                 [reagent "0.6.1"]
                 [re-frame "0.10.1"]
                 [cljsjs/react-bootstrap "0.31.0-0" :exclusions [cljsjs/react]]]

  :plugins [[lein-figwheel "0.5.13"]
            [lein-cljsbuild "1.1.1"]]
  :resource-paths ["resources" "src/cljs"]
  :cljsbuild {
    :builds [{
              :id "dev"
              :source-paths ["src/cljs"]
              :figwheel {:on-jsload "organizeit.core/on-js-reload"
                         ;; :open-urls will pop open your application
                         ;; in the default browser once Figwheel has
                         ;; started and compiled your application.
                         ;; Comment this out once it no longer serves you.
                         :open-urls ["http://localhost:3448/index.html"]}
              :compiler {:main organizeit.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/core.js"
                         :output-dir "resources/public/js/compiled/out"
                         :optimizations :none
                         :pretty-print true}}]}
  :figwheel {:http-server-root "public" ;; default and assumes "resources"
             :server-port 3448 ;; default
             :server-ip "0.0.0.0"

             ;;:css-dirs ["resources/public/css"] ;; watch and update CSS
             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server :) (see lein-ring)

             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

             ;; to pipe all the output to the repl
             ;; :server-logfile false
             })