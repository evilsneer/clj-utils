(ns utils.core
  (:require [buddy.core :as hash]
            [buddy.core.codecs :as cod])
  (:import (java.util UUID)))


(defn transpose [m]
  (apply mapv vector m))

(defn map-vals [f m]
  (zipmap (keys m) (map f (vals m))))

(defn map-keys [f m]
  (zipmap (map f (keys m)) (vals m)))

;; @todo to prewalk
(defn hydrate [xs [from-key to-key] getter]
  (let [xs-keys-uniq (->> xs
                       (map from-key)
                       (into #{}))
        mapper       (zipmap xs-keys-uniq (pmap getter xs-keys-uniq))]
    (->> xs
      (map #(assoc %
              to-key
              (mapper (from-key %)))))))

(defn map-k-v-on [coll to-level [kf vf]
                  {:keys [into-coll _start-level]
                   :or   {into-coll   []
                          _start-level 1}
                   :as   opts}]
  (if (= _start-level to-level)
    (->> coll
      (map (fn [[k v]]
             [(kf k) (vf v)]))
      (into into-coll))
    (->> coll
      (map (fn [[k v]]
             [k (map-k-v-on v to-level [kf vf]
                  (assoc opts
                    :_start-level (inc _start-level)))])))))

(defn walk-collect2 [start-item &
                     {:keys [need-to-unfold? unfold-as exclude]
                      :or   {need-to-unfold? coll?
                             unfold-as       identity
                             exclude         (constantly false)}}]
  (->> start-item
    (tree-seq need-to-unfold? unfold-as)
    (filter (comp not exclude))))

;; @todo finish

(defn sort-cols-fn [rows]
  (let [first-cols (reverse [:id :name :description])]
    (->>
      rows
      (into (sorted-map-by (fn [a b]
                             (>=
                               (.indexOf first-cols a)
                               (.indexOf first-cols b))))))))


;; @todo add salt

(defn hash-it [x salt]
  (-> (str x salt)
    hash/sha256
    cod/bytes->hex))

(defn uuid []
  (.toString (UUID/randomUUID)))

;;

(defn ->pprint-> [x & {:keys [text convert-fn] :or {convert-fn identity}}]
  (clojure.pprint/pprint text (convert-fn x))
  x)

(defn ->atom-seq-> [value a]
  (swap! a conj value)
  value)

;;

(defn increasing-repeat [xs]
  "[1 2 3] -> [(1) (1 2) (1 2 3)]"
  (->>
    xs
    (repeat (count xs))
    (map-indexed vector)
    (map (fn [[i v]]
           (take (inc i) v)))))