(ns advent-of-code.day4
  (:import (java.lang Character)))

;; Part 1
(defn num-to-seq
  "convert number into a sequence"
  [num]
  (map #(Character/digit % 10) (str num)))

(defn has-dup-adjacent-digits
  "Checks weather a sequence contains adjacent duplicates"
  [num-seq]
  (not= (count num-seq) (count (dedupe num-seq))))

(defn monotonically-increasing
  "Determines whether a sequence monotonically increases"
  [num-seq]
  (let [curr-elems (drop-last num-seq)
        next-elems (drop 1 num-seq)]
    (every? true? (map #(<= % %2) curr-elems next-elems))))

;; Part 2
(defn has-dup-pair
  "Determines whether a sequence contains 1 or more
   pairs of duplicates (e.g. 1 1, 2 2, and NOT 3, 3, 3...)"
  [num-seq]
      (let [partitioned (partition-by identity num-seq)]
        (some #(= (count %) 2) partitioned)))

(defn get-possible-passwords
  "Determine the number of possible passwords given to
  'only-pairs' condition"
  [only-pairs]
  (filter
    #(let [num-seq (num-to-seq %)]
       (and
           (has-dup-adjacent-digits num-seq)
           (if only-pairs
             (has-dup-pair num-seq)
             true)
         (monotonically-increasing num-seq)))
    (range 382345 843168)))