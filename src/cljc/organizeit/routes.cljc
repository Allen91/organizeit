(ns organizeit.routes)

(def routes ["/" {"index.html" :index
                  [[#"(css|js|static)" :resource-type]] {[[#".*" :resource-name]] :static-internal}
                  "api/" {"update/" {"mailbox" :update-mailbox
                                     "rent" :update-rent
                                     "electricity" :update-electricity
                                     "internet" :update-internet}
                          "fetch/" {"mailbox" :fetch-mailbox
                                    "rent" :fetch-rent
                                    "electricity" :fetch-electricity
                                    "internet" :fetch-internet}}}])