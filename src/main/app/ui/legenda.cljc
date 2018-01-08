(ns app.ui.legenda
  (:require
    translations.es
    [fulcro.client.dom :as dom]
    [app.api.muts-ui-legenda :as apiui]
    [fulcro.client.data-fetch :as df]
    [fulcro.ui.bootstrap3 :as b]
    [fulcro.client.primitives :as prim :refer [defsc]]))

(defsc Legenda-Item [_this {:keys [desc_gerar sigla gerar tipo geoppr id
                                   unita_eta unita_desc unita_nome eta]}]
  {:query [:desc_gerar
           :sigla
           :gerar
           :tipo
           :geoppr
           :id
           :unita_eta
           :unita_desc
           :unita_nome
           :eta]
   :ident [:geoppr/legenda-byid :id]}
  (let []
    (dom/li #js {:className "list-unstyled" :style #js {:lineHeight 1}}
            (dom/span #js {:style #js {:fontSize "14px"}}
                      geoppr ": " sigla " - " unita_nome " - " unita_desc))))

(def ui-legenda-item (prim/factory Legenda-Item {:keyfn :id}))


(defsc Gerarchia-Item [this {:keys [id desc_gerarchia level cod_gerarchia items-legenda]}]
  {:query [:id :desc_gerarchia :level :cod_gerarchia {:items-legenda (prim/get-query Legenda-Item)}]
   :ident [:geoppr/gerarchia-byid :id]}
  (let [stringa (apply str (repeat (- level 1) "|-- "))]

    (dom/li #js {:className "list-unstyled" :style #js {:lineHeight 0.8}}
            (dom/span #js {:style #js {:fontSize "14px"}} cod_gerarchia ": " stringa
                      (condp = level
                        1 (dom/strong #js {:style #js {:fontSize     (- 18 (* level 2))
                                                       :color        "#000"
                                                       :marginBottom "10px"}}
                                      desc_gerarchia)
                        (dom/em #js {:style #js {:lineHeight 0.8
                                                 :fontSize   (- 16 (* level 2))
                                                 :margin     0}}
                                desc_gerarchia)))
            (dom/button #js{:onClick #(prim/transact! this `[(apiui/associa-legenda-a-me {:id ~id :cod_gerarchia ~cod_gerarchia})])} "me")
            (dom/ul nil
                    (map ui-legenda-item items-legenda)))))

(def ui-gerarchia-item (prim/factory Gerarchia-Item {:keyfn :id}))



(defsc Command-Bar [this {:keys [ui/gerarchia-level]}]
  {:query         [:ui/gerarchia-level]
   :initial-state (fn [p] {:ui/gerarchia-level 0})}
  (let []
    (dom/div nil gerarchia-level
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/show-legenda {})])}
               "SHOW legenda/GERARCHIA")
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-up {})])}
               "up")
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-down {})])}
               "down")
             (dom/button
               #js {:onClick #(prim/transact! this `[(apiui/associa-legenda-a-gerarchia {}) [:geoppr/gerarchia-items]])}
               "ASSOCIA"))))

(def ui-command-bar (prim/factory Command-Bar))


(defsc Gerarchia [this {:keys [ui/show-lege
                               ui/react-key
                               ui/ui-geoppr
                               geoppr/gerarchia-name
                               geoppr/gerarchia-items
                               geoppr/legenda-items]}]
  {:query         [:ui/show-lege
                   :ui/react-key
                   :ui/ui-geoppr
                   :geoppr/gerarchia-name
                   {:geoppr/legenda-items (prim/get-query Legenda-Item)}
                   {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:ui/show-lege           false
                     :ui/ui-geoppr           (prim/get-initial-state Command-Bar {})
                     :geoppr/gerarchia-items []})}
  (let [show-if (:ui/gerarchia-level ui-geoppr)]
    (dom/div #js {:key react-key} (dom/div nil (ui-geoppr :ui/gerarchia-level))
             (ui-command-bar ui-geoppr)
             (dom/button
               #js {:onClick #(df/load this :geoppr/gerarchia-items Gerarchia-Item)}
               "load Gerarchia")
             (dom/ul nil
                     (if show-lege
                       (map ui-legenda-item legenda-items)
                       (map ui-gerarchia-item
                            (filter (fn [g] (> (:level g) show-if)) gerarchia-items)))))))
