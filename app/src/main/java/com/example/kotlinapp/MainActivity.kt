package com.example.kotlinapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()
    
    private lateinit var buttons: Array<Button>
    private lateinit var tvStatus: TextView
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        observeState()
        setupClickListeners()
    }
    private fun initViews() {
        tvStatus = findViewById(R.id.tvStatus)
        btnReset = findViewById(R.id.btnReset)

        buttons = Array(9) { i ->
            val btnID = resources.getIdentifier("btn$i", "id", packageName)
            findViewById(btnID)
        }
    }
    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUI(state)
                }
            }
        }
    }
    private fun setupClickListeners() {
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.processIntent(GameIntent.CellClicked(index))
            }
        }

        btnReset.setOnClickListener {
            viewModel.processIntent(GameIntent.ResetGame)
        }
    }

    private fun updateUI(state: GameState) {
// A M E
        tvStatus.text = state.statusMessage

//A T
        state.board.forEachIndexed { index, value ->
            buttons[index].text = value

            if (value.isNotEmpty()) {
                val color = if (value == "X") R.color.colorBlue else R.color.colorRed
                buttons[index].setTextColor(ContextCompat.getColor(this, color))
            } else {
                buttons[index].setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }
}