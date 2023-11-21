(ns main
  (:require [clojure.string :as string]))

(defn change-player [player]
  (println "PLAYER IS CURRENTLY:")
  (println player)
  (cond
    (= player "X") "O"
    :else "X"))

(defn generate-random-move [] (rand-int 3))

(defn get-player-input [dim]
  (println (format "Please enter your %s (0-2)!" dim))
  (try
    (let [input (-> (read-line) string/trim string/lower-case)]
      (cond
        (= input "exit") (System/exit 0)
        (and (integer? (Integer/parseInt input)) (<= 0 (Integer/parseInt input) 2)) (Integer/parseInt input)
        :else (do
                (println "Your number was not between 0 and 2!")
                (get-player-input dim)
              )
      )
    )
    (catch Exception e (do
                         (println "Your input was wrong!")
                         (get-player-input dim)
                        )
    )
  )
)

(defn execute-move [player s-start board col row]
  (println (format "%s placing %s in col %d and row %d" s-start player col row))
  (update-in board [row col] (constantly player))
)

(defn print-board [board]
  (doseq [row (interpose "---------" (map #(string/join " | " %) board))]
    (println row)
  )
)

(defn play-round [board player curr-player]
  (if (= player curr-player) 
    (do
      (println "Your turn!")
      (let [col (get-player-input "col") row (get-player-input "row")]
        (execute-move curr-player "You are" board col row)
      )
    )
    (do
      (println "Enemys turn!")
      (let [col (generate-random-move) row (generate-random-move)]
        (execute-move curr-player "Enemy is" board col row)
      )
    )
  )
)

(defn game-over? [board]
  (every? (fn [row] (every? (fn [value] (or (= value "X") (= value "O"))) row)) board)
)

(defn game-won? [board]
  ;TODO
)

(defn play-tic-tac-toe []
  (let [player (if (= (Math/round (rand)) 0) "X" "O")]
    (println "Let's play a game of tic tac toe!")
    (println "You can exit this game if you type exit!")
    (println (format "You are player %s!" player))
    
    (loop [board (vec (repeat 3 (vec (repeat 3 " ")))) curr-player "X"]
      (let [new-board (play-round board player curr-player)]
        (print-board new-board)
        (cond
          (game-won? new-board) (println "The game is over! Player %s (%s) has won." curr-player (if (= player curr-player) "you" "enemy"))
          (game-over? new-board) (println "The game is over! No one has won.")
          :else (recur new-board (change-player curr-player))
        )
      )
    )       
  )
)

(play-tic-tac-toe)