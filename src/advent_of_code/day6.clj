(ns advent-of-code.day6
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

;; Part 1
(def orbits (->> (io/resource "day6-input.txt")
       (slurp)
       (str/split-lines)))

(defn create-planet-map
  "Structure the list of orbits into a a map
   of child-parent key-value pairs that will be
   traversed.  Since each planet (child) directly
   orbits 1 other (parent), we can look at each
   planet and count the orbits along the parent
   path until we reach a planet that has no parent.
   Add all these path sums and we have the solution"
  []
  (apply hash-map
         (mapcat #(reverse (str/split % #"\)")) orbits)))

(defn count-orbits
  "Recursively count the number of orbits
   in a path"
  [planet-map curr-planet orbit-total]
  (let [parent (get planet-map curr-planet)]
    (if (= nil parent)
      orbit-total
      (recur planet-map parent (inc orbit-total)))))

(defn get-orbit-sum
  "Add all path counts together"
  []
  (let [planet-map (create-planet-map)]
    (->> planet-map
         (keys)
         (map #(count-orbits planet-map % 0))
         (apply +))))

;; Part 2
(defn get-full-path
  [planet-map curr-planet planet-acc]
  (let [parent (get planet-map curr-planet)]
    (if (= nil parent)
      planet-acc
      (recur planet-map parent (conj planet-acc parent)))))

(defn count-num-transfers
  []
  (let [planet-map (create-planet-map)
        you-path (get-full-path planet-map "YOU" #{})
        san-path (get-full-path planet-map "SAN" #{})]
    (count (set/difference
             (set/union you-path san-path)
             (set/intersection you-path san-path)))))