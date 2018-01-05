(ns app.ui.legenda
  (:require
    translations.es
    [fulcro.client.dom :as dom]
    [app.api.muts-ui-legenda :as apiui]
    [fulcro.client.data-fetch :as df]
    [fulcro.ui.bootstrap3 :as b]
    [fulcro.client.primitives :as prim :refer [defsc]]))


(defsc Gerarchia-Item [_this {:keys [id desc_gerarchia level]}]
  {:query [:id :desc_gerarchia :level]
   :ident [:geoppr/gerarchia-byid :id]}
  (let [stringa (apply str (repeat (- level 1) "-|"))]
    (dom/li #js {:className "list-unstyled"}
            (condp = level
              1 (dom/h6 #js {:style #js {:line-height 14
                                         :font-size   (- 18 (* level 2))
                                         :color       "#f00"
                                         :margin      0}}
                        stringa desc_gerarchia)
              (dom/div #js {:style #js
                                       {:line-height 11
                                        :font-size   (- 18 (* level 2))
                                        :margin      0}}
                       stringa desc_gerarchia)))))

(def ui-gerarchia-item (prim/factory Gerarchia-Item {:keyfn :id}))

(defsc Command-Bar [this {:keys [ui/gerarchia-level]}]
  {:query         [:ui/gerarchia-level]
   :initial-state (fn [p] {:ui/gerarchia-level 0})}
  (let []
    (dom/div nil gerarchia-level
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-up {})])}
               "up")
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-down {})])}
               "down"))))

(def ui-command-bar (prim/factory Command-Bar))


(defsc Gerarchia [_this {:keys [ui/react-key
                                ui/ui-geoppr
                                name
                                geoppr/gerarchia-items]}]
  {:query         [:ui/react-key
                   :ui/ui-geoppr
                   :name
                   {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:ui/ui-geoppr           (prim/get-initial-state Command-Bar {})
                     :name                   "Legenda Geologia ppr - Gerarchia"
                     :geoppr/gerarchia-items []})}
  (dom/div #js {:key react-key}
           (ui-command-bar ui-geoppr)
           (dom/ul nil
                   (map ui-gerarchia-item gerarchia-items))))
