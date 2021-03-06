;; Several glitch examples

(ns examples.ex19-glitch
  (:require [clojure2d.math :as m]
            [clojure2d.pixels :as p]
            [clojure2d.core :refer :all]
            [clojure2d.color :as c]
            [clojure2d.extra.variations :as v]
            [clojure2d.extra.glitch :as g])
  (:import [clojure2d.pixels Pixels]))


(def ^Pixels img (p/load-pixels "results/test.jpg"))

;; slitscan

(p/save-pixels (p/filter-channels (g/make-slitscan-filter) nil img) "results/ex19/slit.jpg")
(p/save-pixels (p/filter-channels (g/make-slitscan-filter) (g/make-slitscan-filter) (g/make-slitscan-filter) nil img) "results/ex19/slit_s.jpg")

;; channel shift

(p/save-pixels (p/filter-channels (g/make-shift-channels-filter 0.1 true true)
                                  nil
                                  (g/make-shift-channels-filter -0.1 true true)
                                  nil img) "results/ex19/chshift.jpg")

(p/save-pixels (->> img
                    (p/filter-colors c/to-HWB)
                    (p/filter-channels (g/make-shift-channels-filter 0.1 true false)
                                       nil
                                       (g/make-shift-channels-filter -0.1 true false)
                                       nil)
                    (p/filter-colors c/from-HWB)) "results/ex19/chshifthwb.jpg")

;; mirror image

(defn make-random-mirror
  ""
  []
  (partial p/filter-channels 
           (g/make-mirror-filter (rand-nth (keys g/mirror-types)))
           (g/make-mirror-filter (rand-nth (keys g/mirror-types)))
           (g/make-mirror-filter (rand-nth (keys g/mirror-types)))
           nil))

(p/save-pixels (->> img
                    ((make-random-mirror))
                    ((make-random-mirror))) "results/ex19/mirror.jpg")


;; slitscan 2

(let [v1 (v/make-variation (rand-nth v/variation-list-not-random) 1.0 {})
      v2 (v/make-variation (rand-nth v/variation-list-not-random) 1.0 {})
      f (comp v1 v2)]

  (binding [p/*pixels-edge* :wrap]
    (p/save-pixels (p/filter-channels (g/make-slitscan2-filter f 2.0)
                                      (g/make-slitscan2-filter f 1.9)
                                      (g/make-slitscan2-filter f 2.1) nil img) "results/ex19/slit2.jpg")))
