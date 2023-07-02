(ns vegamite.verification
  (:require [clojure.pprint :as pprint]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]
            [scicloj.kindly-default.v1.api :as kindly-default]
            [clojure.data.json :as json]
            [malli.core :as ma]
            [malli.json-schema :as majs]
            [malli.json-schema.parse :as majsp]
            [malli.util :as mu]))

(def vega-lite-schema
  (-> (slurp "vega-lite.v5.json")
      (json/read-str :key-fn keyword)
      (majsp/json-schema-document->malli)))

(kindly-default/setup!)

(defn vega-lite-point-plot [data]
  ^#:kindly{:kind :kind/vega-lite}
  {:data {:values data},
   :mark "point"
   :encoding
   {:size {:field "w" :type "quantitative"}
    :x    {:field "x", :type "quantitative"},
    :y    {:field "y", :type "quantitative"},
    :fill {:field "z", :type "nominal"}}})

(defn random-data [n]
  (->> (repeatedly n #(- (rand) 0.5))
       (reductions +)
       (map-indexed (fn [x y]
                      {:w (rand-int 9)
                       :z (rand-int 9)
                       :x x
                       :y y}))))

(defn random-vega-lite-plot [n]
  (-> (random-data n)
      (vega-lite-point-plot)))

(def my-plot (random-vega-lite-plot 9))

(binding [*print-meta* true]
  (pprint/pprint my-plot))
