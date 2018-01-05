(ns app.server
  (:require
    [fulcro.easy-server :refer [make-fulcro-server]]
    ; MUST require these, or you won't get them installed.
    [app.api.read]
    [app.api.read-legenda]
    [app.api.mutations]
    [app.api.muts-ui-legenda]))

(defn build-server
  [{:keys [config] :or {config "config/dev.edn"}}]
  (make-fulcro-server
    :parser-injections #{:config}
    :config-path config))



