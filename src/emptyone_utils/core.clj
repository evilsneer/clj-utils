(ns emptyone-utils.core
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(defn vec* [& xs]
  (vec (apply list* xs)))

;; keywords
(defn keyword?->string [x]
  (if (keyword? x)
    (name x)
    x))

;; strings
(defn string?->keyword [x]
  (if (string? x)
    (keyword x)
    x))

(defn string?->wrapped-string [x]
  (if (string? x)
    (str "'" x "'")
    x))

(defn reduce-multi-slashes [s]
  (->
    s
    (str/replace #"/+" "/")))

;;; numbers
(defn parse-num
  "returns num or nil" [x]
  (when-let [[x- _] (re-matches #"^\d+(\.\d+)?|$" (str/replace (str x) #"," "."))]
    (edn/read-string x-)))