(ns cleito.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [cleito.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[cleito started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[cleito has shut down successfully]=-"))
   :middleware wrap-dev})
