package com.example.kotlinapp

//INMUTABLE
data class GameState(
    val board: List<String> = List(9) { "" }, // "" = vacío, "X" o "O"
    val currentPlayer: String = "X",
    val gameStatus: GameStatus = GameStatus.PLAYING,
    val statusMessage: String = "Player X Turn"
)


enum class GameStatus {
    PLAYING,
    X_WON,
    O_WON,
    DRAW
}
