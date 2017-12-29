(ns app.ui.legenda
  (:require
   translations.es
   [fulcro.client.dom :as dom]
   [app.api.mutations :as api]
   [fulcro.client.data-fetch :as df]
   [fulcro.client.primitives :as prim :refer [defsc]]))

(defsc Gerarchia [_this _props]
  "Gerarchia Legenda"
  (dom/div nil "Gerarchia"))