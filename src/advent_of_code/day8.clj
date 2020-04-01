(ns advent-of-code.day8
  (:require [clojure.java.io :as io]))

;; Part 1
(defn- input->image-representation
  "Convert input string into a 2D
  character vector representation.

  First dimension: image layers
  Second dimension: layer pixels"
  []
  (->> "day8-input.txt"
       (io/resource)
       (slurp)
       (partition (* 25 6))))

(defn part1-solution
  []
  (let [freq-maps (map frequencies (input->image-representation))
        min-0s-layer (apply min-key #(get % \0) freq-maps)]
    (* (get min-0s-layer \1)
       (get min-0s-layer \2))))


