(ns advent-of-code.day3
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.set :as set]))

(def wires-file (io/resource "day3-input.txt"))

;; Part 1
(defn get-wire-instruction-vectors
  "Transforms direction text input into a vector
   of 2 vectors.  Each inner vector holds the
   directions of a single wire"
  []
  (->> wires-file
       (slurp)
       (str/split-lines)
       (map #(str/split % #","))
       (vec)))

(defn get-path-steps-from-instruction
  "Given a direction (e.g. U21), extract direction val
   and number of steps to take in order to build a list
   that holds each integer step for a single single instruction"
  [curr-coords direction direction-num]
  (let [x (first curr-coords)
        y (last curr-coords)
        coord-deltas (range 1 (+ 1 direction-num))]
    (cond
      (= direction "U") (map #(vector x (+ y %)) coord-deltas)
      (= direction "R") (map #(vector (+ x %) y) coord-deltas)
      (= direction "D") (map #(vector x (- y %)) coord-deltas)
      (= direction "L") (map #(vector (- x %) y) coord-deltas))))

(defn get-all-steps-vector
  "Given a wire instruction vector, build a vector that
   holds every step for all instructions"
  [wire]
  (reduce
    (fn [coords instruction]
      (concat
        coords
        (get-path-steps-from-instruction
          (last coords)
          (subs instruction 0 1)
          (edn/read-string (subs instruction 1)))))
    [[0 0]]
    wire))

(defn abs
  "Calculates absolute value"
  [n]
  (max n (- n)))

(defn get-smallest-manhattan
  "For each point where the wires intersect, find
   the smallest manhattan distance from the origin"
  [intersections]
  (reduce
    (fn [dist curr]
            (let [x-dist (abs (first curr))
                  y-dist (abs (last curr))]
              (if (< (+ x-dist y-dist) dist)
                (+ x-dist y-dist)
                dist)))
    999999999999
    intersections))

(defn get-step-sets
  "Convert wire step list into a set so we can do set operations on them"
  []
  (map
    #(disj (set (get-all-steps-vector %)) [0 0])
    (get-wire-instruction-vectors)))

(defn get-manhattan
  "Returns the manhattan distance to the closest intersection"
  []
  (get-smallest-manhattan (apply set/intersection (get-step-sets))))

;; Part 2

(def intersections (apply set/intersection (get-step-sets)))

(defn get-num-steps-coll
  "For a given wire, calculate the number of steps it takes
   to reach each intersection"
  [wire]
  (map #(+ 1 (.indexOf wire %)) intersections))

(defn get-all-intersection-steps
  "Get the num-steps-coll for each wire"
  []
  (let [wires (map #(drop 1 (get-all-steps-vector %)) (get-wire-instruction-vectors))]
    (map get-num-steps-coll wires)))

(defn min-steps-to-intersection
  "Find the smallest number of steps it takes to
   reach an intersection"
  []
  (let [[wire-a-steps wire-b-steps] (get-all-intersection-steps)]
    (apply min (map + wire-a-steps wire-b-steps))))