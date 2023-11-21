(ns main
  (:require [clojure.string :as string])
)

(defn change-player [player]
  (cond
    (= player "X") "O"
    :else "X"
  )
)

(defn generate-random-move [unused-fields] (rand-nth (seq unused-fields)))

(defn get-player-input [dim len]
  (println (format "Please enter your %s (0-%d)!" dim len))
  (try
    (let [input (-> (read-line) string/trim string/lower-case)]
      (cond
        (= input "exit") (System/exit 0)
        (and (integer? (Integer/parseInt input)) (<= 0 (Integer/parseInt input) len)) (Integer/parseInt input)
        :else (do
                (println "Your number was not between 0 and 2!")
                (get-player-input dim len)
              )
      )
    )
    (catch Exception e (do
                         (println "Your input was wrong!")
                         (get-player-input dim len)
                        )
    )
  )
)

(defn execute-move [player curr-player board field]
  (let [row (quot field (count board)) col (mod field (count board))]
    (println (format "%s placing %s in col %d and row %d" (if (= player curr-player) "You are" "Enemy is") curr-player col row))
    (update-in board [row col] (constantly curr-player))
  )
)

(defn print-board [board]
  (doseq [row (interpose (apply str (repeat (count board) "---")) (map #(string/join " | " %) board))]
    (println row)
  )
)

(defn calculate-move [board col row]
  (+ (* row (count board)) col)
)

(defn ask-for-field [board player curr-player unused-fields get-input-fn]
  (if (= player curr-player) 
    (do
      (println "Your turn!")
      (loop [board_len (- (count board) 1) col (get-input-fn "col" board_len) row (get-input-fn "row" board_len)]
        (if (contains? unused-fields (calculate-move board col row))
              (calculate-move board col row) 
            (do
              (println (format "An %s has already been placed at this point!" (get-in board [row col])))
              (recur board_len (get-input-fn "col" board_len) (get-input-fn "row" board_len))
            )
        )
      )
    )
    (do
      (println "Enemys turn!")
      (generate-random-move unused-fields)
    )
  )
)

(defn check-distinct-values [values]
  (and 
    (every? #{"X" "O"} values)
    (apply = values)
  )
)

(defn game-over? [board]
  (every? (fn [row] (every? #{"X" "O"} row)) board)
)

(defn first-diagonal-equal? [board]
  (let [first-diagonal (map (fn [idx] (get-in board [idx idx])) (range (count board)))]
    (check-distinct-values first-diagonal)
  )
)

(defn second-diagonal-equal? [board]
  (let [second-diagonal (map (fn [idx] (get-in board [idx (- (count board) idx 1)])) (range (count board)))]
    (check-distinct-values second-diagonal)
  )
)

(defn game-won? [board]
  (or
     (boolean (some (fn [column] (check-distinct-values column)) (apply map vector board)))
     (boolean (some (fn [row] (check-distinct-values row)) board))
     (first-diagonal-equal? board)
     (second-diagonal-equal? board)
   )
)

(defn create-tic-tac-toe [field_size]
  (fn []
    (let [player (if (= (Math/round (rand)) 0) "X" "O")]
      (println "Let's play a game of tic tac toe!")
      (println "You can exit this game if you type exit!")
      (println (format "You are player %s!" player))

      (loop [board (vec (repeat field_size (vec (repeat field_size " ")))) curr-player "X" unused-fields (set (range (* field_size field_size)))]
        (let [field (ask-for-field board player curr-player unused-fields get-player-input) new-board (execute-move player curr-player board field)]
          (print-board new-board)
          (cond
            (game-won? new-board) (println (format "The game is over! Player %s (%s) has won." curr-player (if (= player curr-player) "you" "enemy")))
            (game-over? new-board) (println "The game is over! No one has won.")
            :else (recur new-board (change-player curr-player) (disj unused-fields field))
          )
        )
      )       
    )
  )
)

(def tic_tac_toe_3 (create-tic-tac-toe 3))
(tic_tac_toe_3)