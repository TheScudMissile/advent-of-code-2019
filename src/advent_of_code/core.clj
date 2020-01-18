(ns advent-of-code.core
  (:require [advent-of-code.day1 :as day1]
            [advent-of-code.day2 :as day2]
            [advent-of-code.day3 :as day3]
            [advent-of-code.day4 :as day4]
            [advent-of-code.day5 :as day5]
            [advent-of-code.day6 :as day6]
            [advent-of-code.day12 :as day12]))

(defn day1-solutions
  []
  (do
    (println (day1/get-total-fuel-ignore-fuel-mass))
    (println (day1/get-total-fuel-req))))

(defn day2-solutions
  []
  (do
    (println (first (day2/get-final-instruction-state "12" "2")))
    (println (day2/get-final-expression-result))))

(defn day3-solutions
  []
  (do
    (println (day3/get-manhattan))
    (println (day3/min-steps-to-intersection))))

(defn day4-solutions
  []
  (do
    (println (count (day4/get-possible-passwords false)))
    (println (count (day4/get-possible-passwords true)))))

(defn day5-solutions
  []
  (do
    (println (day5/get-day5-solution 1))
    (println (day5/get-day5-solution 5))))

(defn day6-solutions
  []
  (do
    (println (day6/get-orbit-sum))))

(defn day12-solutions
  []
  (do
    (println (day12/get-total-energy 1000))
    (println (day12/get-full-cycle-period))))