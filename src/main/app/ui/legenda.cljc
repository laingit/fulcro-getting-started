(ns app.ui.legenda
  (:require
   translations.es
   [fulcro.client.dom :as dom]
   [app.api.mutations :as api]
   [fulcro.client.data-fetch :as df]
   [fulcro.client.primitives :as prim :refer [defsc]]))


(defsc Gerarchia-Item [_this {:keys [id desc_gerarchia level]}]
  {:query [:id :desc_gerarchia :level]}
  (dom/div #js {:font-size "8px"} id " - " desc_gerarchia))

(def ui-gerarchia-item (prim/factory Gerarchia-Item {:keyfn :id}))

(defsc Gerarchia [_this {:keys [name
                                geoppr/gerarchia-items]}]
  {:query [:name  {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:name "Legenda Geologia ppr - Gerarchia"
                     :geoppr/gerarchia-items []})}
  (dom/div nil name
   (dom/ul nil
     (map ui-gerarchia-item gerarchia-items))))


