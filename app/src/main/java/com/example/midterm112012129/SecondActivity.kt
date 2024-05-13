package com.example.midterm112012129

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.view.View
import com.bumptech.glide.Glide
import android.media.MediaPlayer

class SecondActivity : AppCompatActivity() {

    private var progressPlayer1 = 0
    private var progressPlayer2 = 0

    private lateinit var btn_startGame: Button
    private lateinit var sb_Player1: SeekBar
    private lateinit var sb_Player2: SeekBar
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Retrieve selected characters' data from Intent extras
        val player1Character = intent.getSerializableExtra("player1Character") as Character
        val player2Character = intent.getSerializableExtra("player2Character") as Character

        // Display selected characters' data in TextViews
        findViewById<TextView>(R.id.textView_Player1).text = " ${player1Character.name}"
        findViewById<ImageView>(R.id.imageView_Player1).setImageResource(player1Character.image)
        findViewById<TextView>(R.id.textView_Player2).text = " ${player2Character.name}"
        findViewById<ImageView>(R.id.imageView_Player2).setImageResource(player2Character.image)

        btn_startGame = findViewById(R.id.button_StartGame)
        sb_Player1 = findViewById(R.id.seekBar1)
        sb_Player2 = findViewById(R.id.seekBar2)
        val btnBack = findViewById<Button>(R.id.button_Back)


        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btn_startGame.setOnClickListener {
            playSound()
            initGif()
            btn_startGame.isEnabled = false
            progressPlayer1 = 0
            progressPlayer2 = 0
            sb_Player1.progress = 0
            sb_Player2.progress = 0
            runPlayer1(player1Character)
            runPlayer2(player2Character)
        }
    }

    private fun playSound(){
        // Release any existing media player resources
        mediaPlayer?.release()

        // Create a new MediaPlayer instance
        mediaPlayer = MediaPlayer.create(this, R.raw.neongogogo)

        // Start playing the sound
        mediaPlayer?.start()
    }

    private fun initGif(){
        val image = findViewById<ImageView>(R.id.imageView_Gif)
        val gifResource = R.drawable.valogif2

        Glide.with(this)
            .asGif()
            .load(gifResource)
            .into(image)
        image.visibility = View.VISIBLE
    }

    private fun changeGif(){
        val image = findViewById<ImageView>(R.id.imageView_Gif)
        val gifResource = R.drawable.valogif

        Glide.with(this)
            .asGif()
            .load(gifResource)
            .into(image)
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun runPlayer1(player1: Character) {
        GlobalScope.launch(Dispatchers.Main) {
            while (progressPlayer1 < 100 && progressPlayer2 < 100) {
                delay(100)
                progressPlayer1 += 1
                sb_Player1.progress = progressPlayer1
                if (progressPlayer1 >= 100 && progressPlayer2 < 100) {
                    showToast("${player1.name} Win")
                    changeGif()
                    btn_startGame.isEnabled = true
                }
            }
        }
    }

    private fun runPlayer2(player2: Character) {
        GlobalScope.launch(Dispatchers.Main) {
            while (progressPlayer2 < 100 && progressPlayer1 < 100) {
                delay(Random.nextLong(1, 650))
                progressPlayer2 += 3
                sb_Player2.progress = progressPlayer2
                if (progressPlayer2 >= 100 && progressPlayer1 < 100) {
                    showToast("${player2.name} Win")
                    changeGif()
                    btn_startGame.isEnabled = true
                }
            }
        }
    }

}