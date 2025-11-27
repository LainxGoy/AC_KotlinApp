package com.example.kotlinapp


sealed class GameIntent {

    data class CellClicked(val position: Int) : GameIntent()

    object ResetGame : GameIntent()
}
