(ns cleito.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[cleito started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[cleito has shut down successfully]=-"))
   :middleware identity})
