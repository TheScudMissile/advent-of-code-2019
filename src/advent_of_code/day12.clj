(ns advent-of-code.day12)

(def init-state {:positions  [[9 13 -8] [-3, 16, -17] [-4, 11, -10] [0 -2 -2]]
                 :velocities [[0 0 0] [0 0 0] [0 0 0] [0 0 0]]})

;; Part 1
(defn calc-gravity-scalar-delta
  "Given 2 moon positions, calculate the
   gravity change that m2 applies to target"
  [target-pos m2-pos]
  (cond
    (> target-pos m2-pos) -1
    (< target-pos m2-pos) 1
    :else 0))

(defn calc-gravity-vector
  "Given 2 moon position vectors, calculate the
   gravity change vector that m2 applies to target"
  [target m2]
  (map calc-gravity-scalar-delta target m2))

(defn calc-velocity-delta
  "Given all moon position vectors, calculate
   the change in velocity vector to be added to
   the current velocity"
  [target other-moons]
  (apply map + (map #(calc-gravity-vector target %) other-moons)))

(defn get-next-step-map
  "Get the next step position and velocity for target moon"
  [target current-velocity other-moons]
  (let [new-velocity (map + current-velocity (calc-velocity-delta target other-moons)) ]
    {:new-position (vec (map + target new-velocity))
     :new-velocity (vec new-velocity)}))

(defn get-all-next-step-data
  "Get list of maps that contain all moon new
   velocities and positions"
  [curr-state]
  (let [moon-positions (:positions curr-state)]
    (for [idx (range (count moon-positions))]
      (let [target (get moon-positions idx)
            target-velocity (get (:velocities curr-state) idx)
            other-moons (concat
                          (subvec moon-positions 0 idx)
                          (subvec moon-positions (+ idx 1)))]
        (get-next-step-map target target-velocity other-moons)))))

(defn update-state
  "Return a new map representing the change to the
   moon positions and velocities"
  [curr-state]
  (let [next-step (get-all-next-step-data curr-state)]
    (hash-map :positions (vec (map :new-position next-step))
              :velocities (vec (map :new-velocity next-step)))))

(defn take-n-steps
  "Return list of n position/velocity states"
  [n]
  (take (+ n 1) (iterate update-state init-state)))

(defn abs-value
  "Calculates absolute value"
  [n]
  (max n (- n)))

(defn get-moon-energy
  "Gets total energy for a single moon"
  [position velocity]
  (* (apply + (map abs-value position))
     (apply + (map abs-value velocity))))

(defn get-total-energy
  "Gets the total system energy"
  [num-steps]
  (let [{:keys [positions velocities]} (last (take-n-steps num-steps))]
    (apply + (map get-moon-energy positions velocities))))

;; Part 2
;; *********** From https://rosettacode.org/wiki/Least_common_multiple#Clojure ********
(defn gcd
  [a b]
  (if (zero? b)
    a
    (recur b, (mod a b))))

(defn lcm
  [a b]
  (/ (* a b) (gcd a b)))

(defn lcmv [& v] (reduce lcm v))
;; ************************************************************************************

(defn get-single-dim-map
  "Calculate the map of positions and velocities that
   represent a single dimension (X, Y, or Z)"
  [state dim]
  {:positions (map #(nth % dim) (:positions state))
   :velocities (map #(nth % dim) (:velocities state))})

(defn get-single-dimension-cycle-period
  "Determine the number of steps it takes to repeat
   the state of a single dimension"
  [d]
  (let [init-state-dim-map (get-single-dim-map init-state d)]
    (+ 1 (count (doall (take-while
                         #(not= init-state-dim-map (get-single-dim-map % d))
                         (drop 1 (iterate update-state init-state))))))))

(defn get-full-cycle-period
  "Calculate the number of steps it takes for the entire
   state to repeat by calculating each individul dimension's
   cycle period and taking the least common multiple of
   these values"
  []
  (reduce lcmv (map get-single-dimension-cycle-period [0 1 2])))
