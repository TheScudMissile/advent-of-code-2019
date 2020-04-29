(ns advent-of-code.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; Part 1

(defn input->grid
  "convert text input into a 2D vector 'grid'
  of strings."
  []
  (->> "day10-input.txt"
       io/resource
       slurp
       str/split-lines
       (map #(str/split % #""))
       vec))

(defn get-astroid-coords
  "Produce a collection of [x y] coordinate
  tuples so that we can calculate slopes."
  [grid]
  (let [grid-length (count grid)
        astroid-coords (for [y (range grid-length)
                             x (range grid-length)]
                         (let [
                               value (get-in grid [y x])]
                           (when (= value "#")
                             [x y])))]
    (remove nil? astroid-coords)))

(defn get-annotation
  "Because asteroids in a line have the same
  slope, we need a way to distinguish one side
  of the grid from the other.  We will append
  a 'C' annotation for asteroids to the top-left,
  left, bottom-left, and bottom."
  [dy dx]
  (let [bottom-left? (and (pos? dy) (neg? dx))
        top-left? (and (neg? dy) (neg? dx))
        left? (and (zero? dy) (neg? dx))
        down? (and (pos? dy) (zero? dx))
        add-annotation? (some true? [bottom-left? top-left? left? down?])]
    (when add-annotation?
      "C")))

(defn get-slope
  "Determine slope to neighbor asteroids and add
  annotation if necessary."
  [point1 point2]
  (if (= point1 point2)
    nil
    (let [y2 (point2 1)
          y1 (point1 1)
          x2 (point2 0)
          x1 (point1 0)
          dy (- y2 y1)
          dx (- x2 x1)

          ;; handle slopes that are the same, but
          ;; in "complementary" ("C") positions
          annotation (get-annotation dy dx)]
      (try (str (/ dy dx) annotation)
           (catch Exception e
             (str "infinity" annotation))))))

(def coords (-> (input->grid)
                get-astroid-coords))

(defn get-slopes-for-point
  "Get set of all slopes to detectable asteroids"
  [point]
  (->> coords
       (map #(get-slope point %))
       (remove nil?)
       set
       count))

(defn part1-solution
  []
  (->> coords
       (map get-slopes-for-point)
       (apply max)))

