(ns app.ui.legenda
  (:require
    [fulcrologic.semantic-ui.factories :as f]
    [fulcrologic.semantic-ui.icons :as i]
    translations.es
    [fulcro.client.dom :as dom]
    [app.api.muts-ui-legenda :as apiui]
    [fulcro.client.data-fetch :as df]
    #_[fulcro.ui.bootstrap3 :as b]
    [fulcro.client.primitives :as prim :refer [defsc]]))


(defsc Legenda-Item [_this {:keys [desc_gerar sigla gerar tipo geoppr id
                                   unita_eta unita_desc unita_nome eta
                                   cartografato rgb]}]
  {:query [:desc_gerar
           :sigla
           :gerar
           :tipo
           :geoppr
           :id
           :unita_eta
           :unita_desc
           :unita_nome
           :eta
           :cartografato
           :rgb]
   :ident [:geoppr/legenda-byid :id]}
  (let []
    (f/ui-list-item #js {:className "list-unstyled" :style #js {:lineHeight 1}}
                    (dom/div #js {:style #js {:marginBottom "3px"
                                              :marginLeft   "60px"
                                              :maxWidth     "900px"
                                              :fontSize     "14px"}}
                             (if cartografato (f/ui-button #js {:width "60px" :height 20
                                                          :style #js {:marginRight     "5px"
                                                                      :borderWidth     "1px"
                                                                      :border          "solid"
                                                                      :backgroundColor rgb}} geoppr)
                                              )
                             (dom/em nil sigla) " | "
                             (dom/strong nil unita_nome) ": "
                             unita_desc))))

(def ui-legenda-item (prim/factory Legenda-Item {:keyfn :id}))


(defsc Gerarchia-Item [this {:keys [id desc_gerarchia level cod_gerarchia items-legenda]}]
  {:query [:id :desc_gerarchia :level :cod_gerarchia {:items-legenda (prim/get-query Legenda-Item)}]
   :ident [:geoppr/gerarchia-byid :id]}
  (let [stringa (apply str (repeat (- level 1) "|-- "))]

    (f/ui-list-item #js {:className "ui container"}
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
            (f/ui-button #js {:content "Me"
                              :onClick #(prim/transact! this `[(apiui/associa-legenda-a-me {:id ~id :cod_gerarchia ~cod_gerarchia})])
                              :icon    i/check-circle-icon})
            (f/ui-list nil
                       (map ui-legenda-item items-legenda)))))

(def ui-gerarchia-item (prim/factory Gerarchia-Item {:keyfn :id}))


(defsc Command-Bar [this {:keys [ui/gerarchia-level]}]
  {:query         [:ui/gerarchia-level]
   :initial-state (fn [p] {:ui/gerarchia-level 0})}
  (let []
    (dom/div nil gerarchia-level
             (dom/div #js{:className "ui small buttons"} "x"
                      (f/ui-button
                        #js {:onClick #(prim/transact! this `[(apiui/associa-legenda-a-gerarchia {}) [:geoppr/gerarchia-items]])}
                        "ASSOCIA ger legenda")
                      (f/ui-button
                        #js {:onClick #(df/load this :geoppr/gerarchia-items Gerarchia-Item)}
                        "load Gerarchia")
                      (f/ui-button
                        #js {:onClick #(df/load this :geoppr/legenda-items Legenda-Item)}
                        "load ")
                      (f/ui-button
                        #js {:onClick #(prim/transact! this `[(apiui/show-legenda {})])}
                        "SHOW legenda/GERARCHIA")
                      (f/ui-button
                        #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-up {})])}
                        "up")
                      (f/ui-button
                        #js {:onClick #(prim/transact! this `[(apiui/gerarchia-level-down {})])}
                        "down"))
             )))

(def ui-command-bar (prim/factory Command-Bar))

(defsc Prove [_ {:keys [ui/react-key reload]}]
  {:query [:ui/react-key :reload]}
  (dom/div #js{:key react-key} reload))

(def ui-prova (prim/factory Prove))

(defsc Gerarchia [this {:keys [ui/show-lege
                               ui/react-key
                               ui/ui-geoppr
                               geoppr/gerarchia-items
                               geoppr/legenda-items]}]
  {:query         [:ui/show-lege
                   :ui/react-key
                   :ui/ui-geoppr
                   {:geoppr/legenda-items (prim/get-query Legenda-Item)}
                   {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:ui/show-lege           false
                     :ui/ui-geoppr           (prim/get-initial-state Command-Bar {})
                     :geoppr/legenda-items   []
                     :geoppr/gerarchia-items []})}
  (let [show-if (:ui/gerarchia-level ui-geoppr)]
    (dom/div #js {:key react-key} (dom/div nil (ui-geoppr :ui/gerarchia-level))
             (ui-command-bar ui-geoppr)
             (ui-prova {:reload "prova"})
             (dom/ul nil
                     (if show-lege
                       (map ui-legenda-item legenda-items)
                       (map ui-gerarchia-item
                            (filter (fn [g] (> (:level g) show-if)) gerarchia-items)))))))
