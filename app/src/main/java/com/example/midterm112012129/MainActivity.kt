package com.example.midterm112012129

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    // Variables to track current player and their selected character
    var currentPlayer = 1
    var player1Character: Character? = null
    var player2Character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridView = findViewById<GridView>(R.id.gridView)
        val count = ArrayList<String>()
        val item = ArrayList<Character>()
        val array = resources.obtainTypedArray(R.array.image_list)
        val nameList = resources.getStringArray(R.array.Name_list)
        for (i in 0 until array.length()) {
            val photo = array.getResourceId(i, 0)
            val name = nameList[i]
            count.add("${i + 1} pieces")
            item.add(Character(name, photo))
        }
        array.recycle()
        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, item, R.layout.adapter_vertical)

        gridView.setOnItemClickListener { parent, view, position, id ->
            val selectedCharacter = item[position]

            // Check if the selected character is already selected by the other player
            if (currentPlayer == 1 && player2Character != null && player2Character == selectedCharacter) {
                // Display toast if the selected character is already chosen by player 2
                Toast.makeText(this, "Player 2 already selected this character", Toast.LENGTH_SHORT).show()
            } else if (currentPlayer == 2 && player1Character != null && player1Character == selectedCharacter) {
                // Display toast if the selected character is already chosen by player 1
                Toast.makeText(this, "Player 1 already selected this character", Toast.LENGTH_SHORT).show()
            } else {
                // Toggle between players and update selected characters
                if (currentPlayer == 1) {
                    player1Character = selectedCharacter
                    currentPlayer = 2
                    // Update player1 TextView
                    findViewById<TextView>(R.id.player1).text = selectedCharacter.name
                    findViewById<ImageView>(R.id.imageView_Player1).setImageResource(selectedCharacter.image)
                } else {
                    player2Character = selectedCharacter
                    currentPlayer = 1
                    // Update player2 TextView
                    findViewById<TextView>(R.id.player2).text = selectedCharacter.name
                    findViewById<ImageView>(R.id.imageView_Player2).setImageResource(selectedCharacter.image)
                }
            }
        }


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Check if both players have selected characters
            if (player1Character != null && player2Character != null) {
                // Create Intent to start SecondActivity
                val intent = Intent(this, RecordingActivity::class.java)
                // Pass selected characters' data as extras
                intent.putExtra("player1Character", player1Character as Serializable)
                intent.putExtra("player2Character", player2Character as Serializable)
                // Start SecondActivity
                startActivity(intent)
            } else {
                // Display a toast indicating that both players must be filled
                Toast.makeText(this, "Need to pick 2 players", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

data class Character(
    val name: String,
    val image: Int
): Serializable