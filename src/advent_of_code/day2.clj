(ns advent-of-code.day2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def instructions-file (io/resource "day2-input.txt"))

(defn get-initial-instructions
  "Convert raw text input into a list of integer
   values (accounts for replacing index 1 with
   the value 12 and index 2 with the value 2 as
   stated by the problem spec)"
  []
  (vec (map edn/read-string (-> instructions-file
                                (slurp)
                                (str/split #",")
                                (assoc 1 "12")
                                (assoc 2 "2")))))

(defn partition-instructions
  "Split list of instructions into chunks of
   4 so they can be processed"
  []
  (partition 4 (get-initial-instructions)))

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
  []
  (reduce
    (fn [state chunk]
      (if (= (first chunk) 99)
        state
        (assoc state (last chunk) (get-instruction-chunk-result state chunk))))
    (get-initial-instructions)
    (partition-instructions)))

(println (get-final-instruction-state))
