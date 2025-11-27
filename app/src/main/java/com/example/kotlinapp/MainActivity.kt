package com.example.kotlinapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttons: Array<Button>
    private lateinit var tvStatus: TextView
    private lateinit var btnReset: Button

    private var currentPlayer = "X"
    private var gameActive = true
    private var filledPos = IntArray(9) { -1 } // -1: empty, 0: X, 1: O

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnReset = findViewById(R.id.btnReset)

        buttons = Array(9) { i ->
            val btnID = resources.getIdentifier("btn$i", "id", packageName)
            findViewById(btnID)
        }

        for (btn in buttons) {
            btn.setOnClickListener(this)
        }

        btnReset.setOnClickListener {
            resetGame()
        }
    }

    override fun onClick(v: View?) {
        if (!gameActive) return

        val btnClicked = v as Button
        var clickedTag = -1

        for (i in buttons.indices) {
            if (buttons[i] == btnClicked) {
                clickedTag = i
                break
            }
        }

        if (clickedTag == -1 || filledPos[clickedTag] != -1) return

        filledPos[clickedTag] = if (currentPlayer == "X") 0 else 1
        btnClicked.text = currentPlayer
        
        val color = if (currentPlayer == "X") R.color.colorBlue else R.color.colorRed
        btnClicked.setTextColor(ContextCompat.getColor(this, color))

        checkForWin()
        
        if (gameActive) {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            updateStatusText()
        }
    }

    private fun checkForWin() {
        val winningPositions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Cols
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // Diagonals
        )

        for (pos in winningPositions) {
            val val0 = filledPos[pos[0]]
            val val1 = filledPos[pos[1]]
            val val2 = filledPos[pos[2]]

            if (val0 != -1 && val0 == val1 && val1 == val2) {
                gameActive = false
                showWinner(currentPlayer)
                return
            }
        }

        if (filledPos.none { it == -1 }) {
            gameActive = false
            tvStatus.text = getString(R.string.draw)
        }
    }

    private fun showWinner(winner: String) {
        if (winner == "X") {
            tvStatus.text = getString(R.string.win_x)
        } else {
            tvStatus.text = getString(R.string.win_o)
        }
    }

    private fun updateStatusText() {
        if (currentPlayer == "X") {
            tvStatus.text = getString(R.string.turn_x)
        } else {
            tvStatus.text = getString(R.string.turn_o)
        }
    }

    private fun resetGame() {
        currentPlayer = "X"
        gameActive = true
        filledPos = IntArray(9) { -1 }
        updateStatusText()

        for (btn in buttons) {
            btn.text = ""
        }
    }
}