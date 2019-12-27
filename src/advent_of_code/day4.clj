(ns advent-of-code.day4
  (:import (java.lang Character)))

(defn num-to-seq
  [num]
  (map #(Character/digit % 10) (str num)))

(defn has-dup-adjacent-digits
  [num-seq]
  (not= (count num-seq) (count (dedupe num-seq))))

(defn monotonically-increasing
  [num-seq]
  (let [curr-elems (drop-last num-seq)
        next-elems (drop 1 num-seq)]
    (every? true? (map #(<= % %2) curr-elems next-elems))))

(defn get-num-possible-passwords
  []
  (count (filter
           #(let [num-seq (num-to-seq %)]
              (and
                (has-dup-adjacent-digits num-seq)
                (monotonically-increasing num-seq)))
           (range 382345 843168))))

(println (get-num-possible-passwords))