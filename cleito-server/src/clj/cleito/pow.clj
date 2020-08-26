(ns cleito.pow
  (:import [java.security MessageDigest]))

(defn sha256 [string]
  (let [digest (.digest (MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(def difficulty 2)

(def db (atom #{}))

(defn new-work [] (let [work (.toString (java.util.UUID/randomUUID))] (swap! db conj work) work))

(defn valid-proof? [proof work nonce]
  (and
   (contains? @db work)
   (clojure.string/starts-with? proof (clojure.string/join (repeat difficulty "0")))
   (= (sha256 (str work (.toString nonce))) proof)))