package com.example.midterm112012129

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.Serializable
import java.util.Calendar

class RecordingActivity : AppCompatActivity() {

    private val recorder = MediaRecorder()
    private val player = MediaPlayer()
    private lateinit var folder: File
    private var fileName = ""

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && requestCode == 0) {

            val result = grantResults[0]
            if (result == PackageManager.PERMISSION_DENIED)
                finish()
            else {
                setFolder()
                setListener()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        val player1Character = intent.getSerializableExtra("player1Character") as Character
        val player2Character = intent.getSerializableExtra("player2Character") as Character


        val btn_record_next = findViewById<Button>(R.id.button_recordingNext)
        btn_record_next.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("player1Character", player1Character as Serializable)
            intent.putExtra("player2Character", player2Character as Serializable)
            startActivity(intent)
        }


        val permission = android.Manifest.permission.RECORD_AUDIO
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(permission), 0)
        } else {
            setFolder()
            setListener()
        }
    }
    override fun onDestroy() {
        recorder.release()
        player.release()
        super.onDestroy()
    }
    private fun setFolder() {
        folder = File(filesDir.absolutePath+"/record")
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    private fun setListener() {

        val btn_record = findViewById<Button>(R.id.btn_record)
        val btn_stop_record = findViewById<Button>(R.id.btn_stop_record)
        val btn_play = findViewById<Button>(R.id.btn_play)
        val btn_stop_play = findViewById<Button>(R.id.btn_stop_play)
        val textView = findViewById<TextView>(R.id.txt_top)

        btn_record.setOnClickListener {
            fileName = "${Calendar.getInstance().time.time}"
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(File(folder, fileName).absolutePath)
            recorder.prepare()
            recorder.start()
            textView.text = "Recording..."
            btn_record.isEnabled = false
            btn_stop_record.isEnabled = true
            btn_play.isEnabled = false
            btn_stop_play.isEnabled = false
        }
        btn_stop_record.setOnClickListener {
            try {
                val file = File(folder, fileName)
                recorder.stop()
                textView.text = "Stored to ${file.absolutePath}"
                btn_record.isEnabled = true
                btn_stop_record.isEnabled = false
                btn_play.isEnabled = true
                btn_stop_play.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
                recorder.reset()
                textView.text = "Record fail"
                btn_record.isEnabled = true
                btn_stop_record.isEnabled = false
                btn_play.isEnabled = false
                btn_stop_play.isEnabled = false
            }
        }
        btn_play.setOnClickListener {
            val file = File(folder, fileName)
            player.setDataSource(applicationContext, Uri.fromFile(file))
            player.setVolume(3f, 3f)
            player.prepare()
            player.start()
            textView.text = "Playback..."

            btn_record.isEnabled = false
            btn_stop_record.isEnabled = false
            btn_play.isEnabled = false
            btn_stop_play.isEnabled = true
        }
        btn_stop_play.setOnClickListener {
            player.stop()
            player.reset()
            textView.text = "Playback end"

            btn_record.isEnabled = true
            btn_stop_record.isEnabled = false
            btn_play.isEnabled = true
            btn_stop_play.isEnabled = false
        }
        player.setOnCompletionListener {
            it.reset()
            textView.text = "Playback end"
            btn_record.isEnabled = true
            btn_stop_record.isEnabled = false
            btn_play.isEnabled = true
            btn_stop_play.isEnabled = false
        }
    }

}