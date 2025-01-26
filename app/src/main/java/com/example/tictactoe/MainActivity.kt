package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var currentPlayer = "X"
    private var scoreX = 0
    private var scoreO = 0
    private val board = Array(3) { arrayOfNulls<String>(3) }
    private var gameActive = true
    private val maxGames = 10
    private var totalGames = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restartButton = findViewById<Button>(R.id.restartButton)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)

        val buttons = arrayOf(
            arrayOf(
                findViewById<Button>(R.id.button1),
                findViewById<Button>(R.id.button2),
                findViewById<Button>(R.id.button3)
            ),
            arrayOf(
                findViewById<Button>(R.id.button4),
                findViewById<Button>(R.id.button5),
                findViewById<Button>(R.id.button6)
            ),
            arrayOf(
                findViewById<Button>(R.id.button7),
                findViewById<Button>(R.id.button8),
                findViewById<Button>(R.id.button9)
            )
        )

        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                buttons[i][j].setOnClickListener {
                    if (gameActive && buttons[i][j].text.isEmpty()) {
                        buttons[i][j].text = currentPlayer
                        board[i][j] = currentPlayer

                        if (checkWin()) { // If a player wins
                            totalGames++
                            updateScoreAndShowWinner(scoreTextView, restartButton)
                            gameActive = false
                        } else if (isBoardFull()) { // If it's a tie
                            totalGames++
                            handleTie(scoreTextView, restartButton)
                            gameActive = false
                        } else {
                            currentPlayer = if (currentPlayer == "X") "O" else "X" // Switch player
                        }
                    }
                }
            }
        }

        restartButton.setOnClickListener {
            resetGame(buttons, scoreTextView, restartButton) // Reset the game board
        }
    }

    private fun checkWin(): Boolean {
        // Check rows, columns, and diagonals
        for (i in 0..2) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) return true
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) return true
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) return true
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == null) return false
            }
        }
        return true
    }

    private fun updateScoreAndShowWinner(scoreTextView: TextView, restartButton: Button) {
        // Update the score if there's a winner
        if (currentPlayer == "X") {
            scoreX++
        } else {
            scoreO++
        }

        scoreTextView.text = "Player X: $scoreX | Player O: $scoreO"

            // Overall winner
        if (scoreX == 3 || scoreO == 3) {
            val winnerName = if (scoreX == 3) "Player X" else "Player O"
            showWinnerScreen(winnerName)
        } else {
            // Regular win
            scoreTextView.text = "${if (currentPlayer == "X") "Player X" else "Player O"} wins! Play again."
            restartButton.text = "Play Again"
            restartButton.visibility = View.VISIBLE
        }
    }

    private fun handleTie(scoreTextView: TextView, restartButton: Button) {
        // Handle a tie
        scoreTextView.text = "It's a Tie! Play Again."
        restartButton.text = "Play Again"
        restartButton.visibility = View.VISIBLE
    }

    private fun resetGame(buttons: Array<Array<Button>>, scoreTextView: TextView, restartButton: Button) {
        // Reset all buttons on the board
        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                buttons[i][j].text = ""
                board[i][j] = null
            }
        }

        currentPlayer = "X"
        gameActive = true
        scoreTextView.text = "Player X: $scoreX | Player O: $scoreO"
        restartButton.visibility = View.GONE
    }

    private fun showWinnerScreen(winnerName: String) {
        val intent = Intent(this, WinnerActivity::class.java)
        intent.putExtra("WINNER_NAME", winnerName)
        startActivity(intent)
        finish() // End the current activity
    }
}
