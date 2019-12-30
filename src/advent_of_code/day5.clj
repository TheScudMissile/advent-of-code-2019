(ns advent-of-code.day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import (java.lang Character)))

(def instructions-file (io/resource "day5-input.txt"))

;; Part 1
(defn get-instructions-vec
  "Convert raw text input into a list of integer
   values and partition by instruction chunks (5 elems)"
  []
  (vec (map edn/read-string (-> instructions-file
                                (slurp)
                                (str/split #",")))))

(defn get-opcode-as-seq
  "Convert opcode into into a list of values and
   partition so we have the opcode fn value and
   the modes separate"
  [opcode]
  (->> opcode
       (str)
       (map #(Character/digit % 10))
       (reverse)
       (split-at 2)))

(defn calc-amount-to-adv
  "Determine how much to increment instr pointer"
  [opcode-seq]
  (let [seq-len (count opcode-seq)]
    (if (< seq-len 2)
      2
      seq-len)))

(defn get-opcode-fn
  "Determine the appropriate function based on the
   provided integer.  If not 1 or 2, 3, or 4, return nil"
  [opcode]
  (cond
    (= opcode 1) #(+ % %2)
    (= opcode 2) #(* %1 %2)
    (= opcode 3) (fn [coll idx input] (assoc coll idx input))
    (= opcode 4) #(identity %)))

(defn process-instructions
  [state instrs-left]
  (let [opcode-seq (get-opcode-as-seq (first instrs-left))
        amount-to-adv (calc-amount-to-adv opcode-seq)]
    opcode-seq))

(println (process-instructions (get-instructions-vec) (get-instructions-vec)))