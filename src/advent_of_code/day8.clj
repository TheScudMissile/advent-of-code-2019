(ns advent-of-code.day8
  (:require [clojure.java.io :as io]))

;; Part 1

;; Partition text input into a list of lists where
;; The first dimension is the image layers and the
;; second dimension is the layer's pixels
(def image-representation (->> "day8-input.txt"
                               (io/resource)
                               (slurp)
                               (partition (* 25 6))))

(defn part1-solution
  "Finds the layer with the fewest 0s and multiplies
  that layer's number of 1s with its number of 2s"
  []
  (let [freq-maps (map frequencies image-representation)
        min-0s-layer (apply min-key #(get % \0) freq-maps)]
    (* (get min-0s-layer \1)
       (get min-0s-layer \2))))

;; Part 2
(defn- get-image-column-color
  "Creates a list that represents an image 'column'
  (i.e. position n of every layer) and returns the first
  non-2 value."
  [n]
  (let [column (map #(nth % n) image-representation)
        colored-first-value (drop-while #(= % \2) column)]
    (first colored-first-value)))