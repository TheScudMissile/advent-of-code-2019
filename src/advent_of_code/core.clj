(ns advent-of-code.core
  (:require [advent-of-code.day1 :as day1]
            [advent-of-code.day12 :as day12]))

(defn day1-solutions
  []
  (do
    (println (day1/get-total-fuel-ignore-fuel-mass))
    (println (day1/get-total-fuel-req))))

(defn day12-solutions
  []
  (do
    (println (day12/get-total-energy 1000))
    (println (day12/get-full-cycle-period))))
