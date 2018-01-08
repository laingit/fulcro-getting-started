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

(defmutation show-legenda [_noprams]
  (action [{:keys [state]}]
          (swap! state update :ui/show-lege not)))


(defn- add-legenda* [gerarchia gruppi]
  (let [nuovo (reduce (fn [acc [k {:keys [cod_gerarchia] :as m}]]
                        (let [esiste (get gruppi cod_gerarchia)]
                          (if esiste
                            (let [idents-legenda (mapv (fn [{:keys [id]}] [:geoppr/legenda-byid id]) esiste)]
                              (assoc acc k (assoc m :items-legenda idents-legenda)))
                            (assoc acc k m))))
                        {}
                        gerarchia)]
    nuovo))

(defmutation associa-legenda-a-gerarchia [_noprams]
  (action [{:keys [state]}]
          (let [s @state
                valori (s :geoppr/legenda-byid)
                gerarchia (s :geoppr/gerarchia-byid)
                chiavi (map (fn [{:keys [id gerar]}] {:id id :gerar gerar}) (vals valori))
                gruppi (group-by :gerar chiavi)]
            (swap! state assoc :geoppr/gerarchia-byid (add-legenda* gerarchia gruppi)))))


(defmutation associa-legenda-a-me [{:keys [id cod_gerarchia]}]
  (action [{:keys [state]}]
          (let [s @state
                valori (s :geoppr/legenda-byid)
                gerarchia (s :geoppr/gerarchia-byid)
                chiavi (map (fn [{:keys [id gerar]}] {:id id :gerar gerar}) (vals valori))
                gruppi (group-by :gerar chiavi)
                esiste (get gruppi cod_gerarchia)
                idents-legenda (mapv (fn [{:keys [id]}] [:geoppr/legenda-byid id]) esiste)]
              (swap! state assoc-in [:geoppr/gerarchia-byid id :items-legenda] idents-legenda))))
