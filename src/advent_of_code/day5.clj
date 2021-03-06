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
   and add any missing 0s or 1s"
  [opcode-seq]
  (let [op (first opcode-seq)
        params (drop 2 opcode-seq)
        num-zeros-needed (if (or (= 3 op) (= 4 op))
                           0
                           (min 3 (- 5 (count opcode-seq))))]
    (cons op (concat params (repeat num-zeros-needed 0)))))


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

(defn get-opcode-fn
  "Determine the appropriate function based on the
   provided integer"
  [opcode]
  (cond
    (= opcode 1) #(+ %1 %2)
    (= opcode 2) #(* %1 %2)
    (= opcode 7) #(if (< %1 %2)
                    1
                    0)
    (= opcode 8) #(if (= %1 %2)
                    1
                    0)))

(defn get-amount-to-adv
  "Determine the amount to advance the instruction
   pointer"
  [opcode-fn-num]
  (cond
    (or (= opcode-fn-num 3) (= opcode-fn-num 4)) 2
    (or (= opcode-fn-num 5) (= opcode-fn-num 6)) 3
    :else 4))

(defn get-value-based-on-mode
  "If mode is 1, return the value because we
   are in immediate mode.  Otherwise we are
   in positional mode and need to return
   the value that lives at the value's idx"
  [state mode value]
  (if (= mode 1)
    value
    (nth state value)))

(defn get-params
  "Get the params vector for this opcode
   function number"
  [state opcode-fn-num ptr]
  (cond
    (or (= opcode-fn-num 3) (= opcode-fn-num 4)) (subvec state (+ ptr 1) (+ ptr 2))
    (or (= opcode-fn-num 5) (= opcode-fn-num 6)) (subvec state (+ ptr 1) (+ ptr 3))
    :else (subvec state (+ ptr 1) (+ ptr 4))))

(defn get-new-state
  "Based on the function / function number
   from the opcode, return the new state
   returned by applying the necessary fn"
  [state fn fn-num result-idx modes params input-val]
  (cond
    (= fn-num 3) (assoc state result-idx input-val)
    (some #(= fn-num %) [4 5 6]) state
    :else    (assoc
               state
               result-idx
               (apply fn
                      (map #(get-value-based-on-mode state %1 %2)
                           (drop-last modes)
                           (drop-last params))))))

;; Part 2
(defn get-jump-condition
  [opcode-num]
  (if (= opcode-num 5)
    #(not= 0 %)
    #(= 0 %)))

(defn get-new-ptr-idx
  [curr-idx opcode-seq params state]
  (let [[opcode-num & modes] opcode-seq]
    (cond
      (and (or (= opcode-num 5) (= opcode-num 6))
           ((get-jump-condition opcode-num) (get-value-based-on-mode state
                                            (first modes)
                                            (first params)))) (get-value-based-on-mode state
                                                                                   (second modes)
                                                                                   (second params))
      :else (+ curr-idx (get-amount-to-adv opcode-num)))))

(defn process-instructions
  "Given an initial state, process all
   instructions"
  [state pointer-idx input-val]
  (if (not= (get state pointer-idx) 99)
    (let [opcode-seq (get-opcode-seq (get state pointer-idx))
          fn (get-opcode-fn (first opcode-seq))
          modes (rest opcode-seq)
          params (get-params state (first opcode-seq) pointer-idx)
          result-idx (last params)
          new-state (get-new-state state
                                   fn
                                   (first opcode-seq)
                                   result-idx
                                   modes
                                   params
                                   input-val)
          new-pointer-idx (get-new-ptr-idx pointer-idx opcode-seq params state)]
      (do
        (if (= (first opcode-seq) 4)
          (println (get-value-based-on-mode state (first modes) (first params))))
        (recur new-state new-pointer-idx input-val)))))

(defn get-day5-solution
  [input-val]
  (process-instructions (get-instructions-vec) 0 input-val))