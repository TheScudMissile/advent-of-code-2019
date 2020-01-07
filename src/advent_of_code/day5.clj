(ns advent-of-code.day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import (java.lang Character)))

(def instructions-file (io/resource "day5-input.txt"))

;; Part 1
(defn get-instructions-vec
  "Convert raw text input into a list of integer
   values"
  []
  (vec (map edn/read-string (-> instructions-file
                                (slurp)
                                (str/split #",")))))

(defn format-opcode-seq
  "Remove unneeded leading 0 for function val
   and add any missing 0s"
  [opcode-seq]
  (let [op (first opcode-seq)
        params (drop 2 opcode-seq)
        num-zeros-needed (if (or (= 3 op) (= 4 op))
                           0
                           (min 3 (- 5 (count opcode-seq))))]
    (cons op (concat params (repeat num-zeros-needed 0)))))

;; (split-at 2)

(defn get-opcode-seq
  "Convert opcode into a list of values.
   The first indicates the fn and the rest
   indicate the params"
  [opcode]
  (->> opcode
        (str)
        (map #(Character/digit % 10))
        (reverse)
        (format-opcode-seq)))

(println (get-opcode-seq 1002))