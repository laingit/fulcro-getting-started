(ns app.ui.legenda
  (:require
   translations.es
   [fulcro.client.dom :as dom]
   [app.api.mutations :as api]
   [fulcro.client.data-fetch :as df]
   [fulcro.ui.bootstrap3 :as b]
   [fulcro.client.primitives :as prim :refer [defsc]]))


(defsc Gerarchia-Item [_this {:keys [id desc_gerarchia level]}]
  {:query [:id :desc_gerarchia :level]}
  (let [stringa (apply str (repeat (- level 1) "-|"))]
    (dom/li #js {:className "list-unstyled"}
            (condp = level
                   1 (dom/h6 #js {:style #js {:line-height 14
                                              :font-size (- 18 (* level 2))
                                              :color "#f00"
                                              :margin 0}}
                             stringa desc_gerarchia)
                   (dom/div #js {:style #js
                                            {:line-height 11
                                             :font-size (- 18 (* level 2))
                                             :margin 0}}
                             stringa desc_gerarchia)))))

(def ui-gerarchia-item (prim/factory Gerarchia-Item {:keyfn :id}))


(defsc Gerarchia [_this {:keys [ui/react-key
                                name
                                geoppr/gerarchia-items]}]
  {:query [:ui/react-key :name  {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:name "Legenda Geologia ppr - Gerarchia"
                     :geoppr/gerarchia-items []})}
  (dom/div #js {:key react-key}
   (dom/ul nil
     (map ui-gerarchia-item gerarchia-items))))


