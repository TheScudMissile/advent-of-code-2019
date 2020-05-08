(ns advent-of-code.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]))

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
  a 'L' annotation for asteroids to the top-left,
  left, bottom-left, and bottom.  Otherwise we'll
  append 'R'."
  [dy dx]
  (let [bottom-left? (and (pos? dy) (neg? dx))
        top-left? (and (neg? dy) (neg? dx))
        left? (and (zero? dy) (neg? dx))
        down? (and (pos? dy) (zero? dx))
        add-left-annotation? (some true? [bottom-left? top-left? left? down?])]
    (if add-left-annotation?
      "L"
      "R")))

(defn get-slope
  "Determine slope to neighbor asteroids and add
  annotation if necessary."
  [point1 point2 as-slope-map?]
  (if (= point1 point2)
    nil
    (let [y2 (point2 1)
          y1 (point1 1)
          x2 (point2 0)
          x1 (point1 0)
          dy (- y2 y1)
          dx (- x2 x1)

          ;; handle slopes that are the same, but
          ;; in left-hand ("L") positions
          annotation (get-annotation dy dx)
          slope (try (str (/ dy dx) annotation)
                     (catch Exception e
                       (str "9999999999" annotation)))]
      (if as-slope-map?
        {slope [[x2 y2]]}
        slope))))

(def coords (-> (input->grid)
                get-astroid-coords))

(defn get-slopes-for-point
  "Get set of all slopes to detectable asteroids"
  [point as-slope-map?]
  (->> coords
       (map #(get-slope point % as-slope-map?))
       (remove nil?)))

(defn get-asteroids-with-visible-neighbors
  "For each asteroid, return the set of visible
  neighbors"
  []
  (->> coords
       (map #(->> (get-slopes-for-point % false)
                  set
                  (vector %)))))

(defn get-best-asteroid
  "Looks at each asteroid and returns the asteroid
  with the most visible neighbor asteroiods"
  []
  (let [neighbor-count-fn (comp count second)
        result (reduce (fn [acc curr]
                         (if (> (neighbor-count-fn curr)
                                (neighbor-count-fn acc))
                           curr
                           acc))
                       (get-asteroids-with-visible-neighbors))]
    result))

(defn part1-solution
  []
  (count (second (get-best-asteroid))))

;; Part 2

(defn get-best-asteroid-slope-map
  "Returns a map, where keys are slopes and values
  are the neighbor asteroid positions that produce
  that slope, for the best asteroid"
  []
  (let [slope-maps (-> (get-best-asteroid)
                       first
                       (get-slopes-for-point true))]
    (apply merge-with concat slope-maps)))

(defn slope-comparator
  "Orders slopes in a way that simulates clockwise
  targeting for the laser"
  [s1 s2]
  (let [s1-num (edn/read-string (apply str (drop-last 1 s1)))
        s2-num (edn/read-string (apply str (drop-last 1 s2)))
        s1-annotation (apply str (take-last 1 s1))
        s2-annotation (apply str (take-last 1 s2))]
    (if (= s1-annotation s2-annotation)
      (< s1-num s2-num)
      (= s1-annotation "R"))))

(defn part2-solution
  []
  (let [ordered-asteroid-list (->> (get-best-asteroid-slope-map)
                                   (into (sorted-map-by slope-comparator))
                                   concat)
        ;; idx 198 accounts for 0 based indexing and "up" being 9999999999L (i.e. listed last)
        a-200 (nth ordered-asteroid-list 198)
        a-200-pos (get-in a-200 [1 0])]
    (+ (* (a-200-pos 0)
          100)
       (a-200-pos 1))))

