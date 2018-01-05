(ns app.api.muts-ui-legenda
  (:require
    [fulcro.client.mutations :refer [defmutation]]
    [fulcro.client.logging :as log]))

(defmutation gerarchia-level-up [_noprams]
  (action [{:keys [state]}]
          (swap! state update-in [:ui/ui-geoppr :ui/gerarchia-level] inc)))

(defmutation gerarchia-level-down [_noprams]
  (action [{:keys [state]}]
          (swap! state update-in [:ui/ui-geoppr :ui/gerarchia-level] dec)))

