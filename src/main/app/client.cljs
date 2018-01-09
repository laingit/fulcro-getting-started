(ns app.client
  (:require [fulcro.client :as fc]
            [fulcro.client.data-fetch :as df]
            [app.ui.root :as root]
            [app.ui.legenda :as uileg]
            [app.api.muts-ui-legenda :as apiui]))

(defonce app
  (atom (fc/new-fulcro-client
          :started-callback
          (fn [app]
            (println :LOAD)
            (df/load app :current-user root/Person)
            (df/load app :my-friends root/Person {:target        [:person-list/by-id :friends :person-list/people]
                                                  :post-mutation `api/sort-friends})
            (df/load app :my-enemies root/Person {:target [:person-list/by-id :enemies :person-list/people]})))))

(defonce app-ger
  (atom (fc/new-fulcro-client
          :started-callback
          (fn [app]
            (println :LOAD-GERARCHIA)

            #_(df/load app :my-friends root/Person {:target        [:person-list/by-id :friends :person-list/people]
                                                  :post-mutation `api/sort-friends})
            #_(df/load app :my-enemies root/Person {:target [:person-list/by-id :enemies :person-list/people]})))))