(ns cljs.user
  (:require
    [fulcro.client :as fc]
    [app.client :as core]
    [app.ui.root :as root]
    [app.ui.legenda :as uilege]
    [cljs.pprint :refer [pprint]]
    [fulcro.client.logging :as log]))

(enable-console-print!)

(log/set-level :all)

(defn mount-ger []
  (reset! core/app-ger (fc/mount @core/app-ger uilege/Gerarchia "app")))

(defn mount-app []
  (reset! core/app (fc/mount @core/app root/Root "app")))

(mount-ger)

(defn app-state [] @(:reconciler @core/app))
(defn app-state-ger [] @(:reconciler @core/app-ger))

(defn log-app-state [& keywords]
  (pprint (let [app-state (app-state-ger)]
            (if (= 0 (count keywords))
              app-state
              (select-keys app-state keywords)))))
