(ns app.api.read-legenda
  (:require
   [taoensso.timbre :as timbre]
   [app.api.utils-legenda :as utils-leg]
   [fulcro.server :refer [defquery-root defquery-entity defmutation]]))


(defquery-root :geoppr/gerarchia-name
  "Queries for the current user and returns it to the client"
  (value [env params]
    " Gerarchia name from server"))

(defquery-root :geoppr/gerarchia-items
  "Queries for the current user and returns it to the client"
  (value [env params]
    (utils-leg/get-gerarchia)))

(defquery-root :geoppr/legenda-items
               "Queries for the current user and returns it to the client"
               (value [env params]
                      (utils-leg/get-legenda)))