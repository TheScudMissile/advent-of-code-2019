(ns advent-of-code.day2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def instructions-file (io/resource "day2-input.txt"))

;; Part 1
(defn get-initial-instructions
  "Convert raw text input into a list of integer
   values (Replacing index 1 with the noun
   and index 2 with the verb"
  [noun verb]
  (vec (map edn/read-string (-> instructions-file
                                (slurp)
                                (str/split #",")
                                (assoc 1 noun)
                                (assoc 2 verb)))))

(defn partition-instructions
  "Split list of instructions into chunks of
   4 so they can be processed"
  [noun verb]
  (partition 4 (get-initial-instructions noun verb)))

(defn get-opcode-fn
  "Determine the appropriate function based on the
   provided integer.  If not 1 or 2, return nil"
  [opcode]
  (cond
    (= opcode 1) #(+ % %2)
    (= opcode 2) #(* %1 %2)))

(defn get-instruction-chunk-result
  "Returns the result of applying the appropriate function
   to the values at index 1 and 2"
  [all-instructions instruction-chunk]
  (if-let [opcode-fn (get-opcode-fn (first instruction-chunk))]
    (opcode-fn
      (nth all-instructions (nth instruction-chunk 1))
      (nth all-instructions (nth instruction-chunk 2)))))


(defn get-final-instruction-state
  "Traverses initial instruction list and applies updates as
   it goes in order to attain the final instruction list"
  [noun verb]
  (reduce
    (fn [state chunk]
      (if (= (first chunk) 99)
        state
        (assoc state (last chunk) (get-instruction-chunk-result state chunk))))
    (get-initial-instructions noun verb)
    (partition-instructions noun verb)))

;; Part 2
(defn get-noun-verb-string-range
  "Returns a list of strings from 0 to 99"
  []
  (map str (range 0 100)))

(defn get-noun-verb-pair
  "For each noun value (0-99), looks at each verb value
  (0-99) and checks if the pair matches the specified
  goal value.  If it does, returns a vector containing
  the pair"
  [goal-value]
  (mapcat
    (fn [noun]
      (reduce
        (fn [acc verb]
          (if (= goal-value (first (get-final-instruction-state noun verb)))
            (map edn/read-string [noun verb] )
            acc))
        []
        (get-noun-verb-string-range)))
    (get-noun-verb-string-range)))

(defn get-final-expression-result
  []
  (let [[noun verb] (get-noun-verb-pair 19690720)]
    (+ verb (* 100 noun))))

