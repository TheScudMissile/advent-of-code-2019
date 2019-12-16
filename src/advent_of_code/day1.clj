(ns advent-of-code.day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]))

(def masses-file (io/resource "day1-input.txt" ))

(def modules (->> masses-file
                  (slurp)
                  (str/split-lines)
                  (map edn/read-string)))

;; Part 1
(defn calc-fuel-requirement
  "Get the fuel required for a mass"
  [mass]
  (- (Math/floorDiv mass 3) 2))

(defn get-fuel-reqs-seq
  "Get the seq of all module's fuel requirement"
  [modules]
  (map calc-fuel-requirement modules))

(defn get-total-fuel-ignore-fuel-mass
  "Get the total fuel requirement without
   considering the mass of the added fuel"
  []
  (reduce + (get-fuel-reqs-seq modules)))

;; Part 2
(defn get-module-total-fuel
  "Get the total fuel requirement for a single
   module while also accounting for the mass
   of the added fuel"
  [mass total]
  (let [fuel-req (calc-fuel-requirement mass)]
    (if (<= fuel-req 0)
      total
      (recur fuel-req (+ total fuel-req)))))

(defn get-total-fuel-req
  "Calculate the total fuel requirement"
  []
  (reduce + (map #(get-module-total-fuel % 0) modules)))
