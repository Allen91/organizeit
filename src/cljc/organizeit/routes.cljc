(ns organizeit.routes)

(def routes ["/" {"" :index
                  "index.html" :index
                  [[#"(css|js|static)" :resource-type]] {[[#".*" :resource-name]] :static-internal}
                  "api/" {"update/" {"mailbox" :update-mailbox
                                     "rent" :update-rent
                                     "electricity" :update-electricity
                                     "internet" :update-internet}
                          "fetch/" {"mailbox" :fetch-mailbox
                                    "rent" :fetch-rent
                                    "electricity" :fetch-electricity
                                    "internet" :fetch-internet
                                    "groceries" :fetch-groceries
                                    "store-groceries" :fetch-store-groceries
                                    "stores" :fetch-stores}
                          "add/" {"store" :add-store
                                  "item" :add-item}
                          "clear/" {"store" :clear-store
                                    "checked" :clear-checked}
                          "check/" {"item" :check-item
                                    "all" :check-all}}}])