(ns cljs.user
  (:require
    [fulcro.client :as fc]
    [app.client :as core]
    [app.ui.root :as root]
    [app.ui.root :as uilege]
    [cljs.pprint :refer [pprint]]
    [fulcro.client.logging :as log]))

(enable-console-print!)

(log/set-level :all)

(defn mount []
  (reset! core/app-ger (fc/mount @core/app-ger uilege/Gerarchia "app")))

(mount)

(defn app-state [] @(:reconciler @core/app-ger))

(defn log-app-state [& keywords]
  (pprint (let [app-state (app-state)]
            (if (= 0 (count keywords))
              app-state
              (select-keys app-state keywords)))))
