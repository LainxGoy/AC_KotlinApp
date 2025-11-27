package com.example.kotlinapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

  //P I U
    fun processIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.CellClicked -> handleCellClick(intent.position)
            is GameIntent.ResetGame -> resetGame()
        }
    }

 //CLICK
    private fun handleCellClick(position: Int) {
        val currentState = _state.value

        if (currentState.gameStatus != GameStatus.PLAYING) return
        if (currentState.board[position].isNotEmpty()) return
        val newBoard = currentState.board.toMutableList()
        newBoard[position] = currentState.currentPlayer

        val winner = checkWinner(newBoard)
        
        when {
            winner != null -> {
                val newStatus = if (winner == "X") GameStatus.X_WON else GameStatus.O_WON
                val message = if (winner == "X") "X Wins!" else "O Wins!"
                _state.value = currentState.copy(
                    board = newBoard,
                    gameStatus = newStatus,
                    statusMessage = message
                )
            }
            newBoard.none { it.isEmpty() } -> {
                _state.value = currentState.copy(
                    board = newBoard,
                    gameStatus = GameStatus.DRAW,
                    statusMessage = "Draw!"
                )
            }
            else -> {
                val nextPlayer = if (currentState.currentPlayer == "X") "O" else "X"
                _state.value = currentState.copy(
                    board = newBoard,
                    currentPlayer = nextPlayer,
                    statusMessage = "Player $nextPlayer Turn"
                )
            }
        }
    }

  //MAGISTRAL
    private fun checkWinner(board: List<String>): String? {
        val winningPositions = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (positions in winningPositions) {
            val (a, b, c) = positions
            if (board[a].isNotEmpty() && 
                board[a] == board[b] && 
                board[b] == board[c]) {
                return board[a]
            }
        }
        return null
    }

    // ><
    private fun resetGame() {
        _state.value = GameState()
    }
}
