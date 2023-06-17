package com.mirac.harrypotter.gameScreens

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mirac.harrypotter.R
import com.mirac.harrypotter.frontend.MainScreen
import kotlinx.android.synthetic.main.activity_game_over.*

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        var playMode = intent.getStringExtra("mode")
        var point1 = getIntent().getIntExtra("point1",0)
        var ifAllMatch2 = intent.getBooleanExtra("ifAllMatch2",false)

        var mediaPlayer = MediaPlayer.create(applicationContext,R.raw.congratulations)

        if(ifAllMatch2){
            textViewGameOver.text = "Congratulations"
            mediaPlayer.start()
            mediaPlayer.setVolume(35F,35F)
        }else{
            textViewGameOver.text = "Game Over"
            var timeover = MediaPlayer.create(applicationContext,R.raw.over)
            timeover.start()
            timeover.setVolume(35F,35F)
        }

        if (playMode == "single"){
            textViewSummary.text= "Score = ${point1}"
        }else if(playMode == "multi"){
            var point2 = getIntent().getIntExtra("point2",0)
            textViewSummary.text= "Score User1 = ${point1} \nScore User2 = ${point2}"
        }

        buttonMenu.setOnClickListener(View.OnClickListener {
            var intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intent)
            mediaPlayer.pause()
            finish()
        })
    }

    fun GoToAnaEkran(view: View){
        var intent = Intent(applicationContext, MainScreen::class.java)
        startActivity(intent)
        finish()
    }



}