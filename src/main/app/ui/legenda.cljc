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
  (let [stringa (apply str (repeat (- level 1) "|-- "))]
    (dom/li #js {:className "list-unstyled" :style #js {:lineHeight 0.8}}
            (dom/span #js {:style #js {:fontSize "14px"}} stringa
                      (condp = level
                        1 (dom/strong #js {:style #js {:fontSize     (- 18 (* level 2))
                                                       :color        "#000"
                                                       :marginBottom "10px"}}
                                      desc_gerarchia)
                        (dom/em #js {:style #js {:lineHeight 0.8
                                                 :fontSize (- 16 (* level 2))
                                                 :margin   0}}
                                desc_gerarchia))))))

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


(defsc Gerarchia [_this {:keys [ui/fn-x
                                ui/react-key
                                ui/ui-geoppr
                                geoppr/gerarchia-name
                                geoppr/gerarchia-items]}]
  {:query         [:ui/fn-x
                   :ui/react-key
                   :ui/ui-geoppr
                   :geoppr/gerarchia-name
                   {:geoppr/gerarchia-items (prim/get-query Gerarchia-Item)}]
   :initial-state (fn [p]
                    {:ui/fn-x (fn [x] (str x "2" ))
                     :ui/ui-geoppr           (prim/get-initial-state Command-Bar {})
                     :geoppr/gerarchia-items []})}
  (let [show-if (:ui/gerarchia-level ui-geoppr)]
    (dom/div #js {:key react-key} (dom/div nil (fn-x "x"))
             (ui-command-bar ui-geoppr)
             (dom/ul nil
                     (map ui-gerarchia-item
                          (filter (fn [g] (> (:level g) show-if)) gerarchia-items))))))
