package com.mirac.harrypotter.gameScreens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.widget.ImageButton
import android.widget.Toast
import com.mirac.harrypotter.AllCards
import com.mirac.harrypotter.Business
import com.mirac.harrypotter.R
import com.mirac.harrypotter.entities.Cards
import kotlinx.android.synthetic.main.activity_easy_mode_single.*
import java.io.File
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

class EasyModeSingle : AppCompatActivity() {
    val business = Business()
    val rd = Random()
    var firstCard = 1
    var secondCard = 2
    var cards = ArrayList<Cards>()
    private var indexOfSingleSelectedKart: Int? = null

    var point = 0
    var totalTime = 46 * 1000
    var remainTime = 0
    var ifAllMatch2 = false

    lateinit var buttons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy_mode_single)
        object : CountDownTimer(totalTime.toLong(),1000){
            override fun onTick(millisUntilFinished: Long) {
                remainTime = (millisUntilFinished/1000).toInt()
                textTime.text = remainTime.toString()
                textPoint1.text = point.toString()
            }
            override fun onFinish() {
                if (!ifAllMatch2){
                    var intent = Intent(applicationContext, GameOverActivity::class.java)
                    intent.putExtra("point1",point)
                    intent.putExtra("mode","single")
                    intent.putExtra("ifAllMatch2",ifAllMatch2)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()

        kontrol()
        val images = mutableListOf(AllCards.allCards.get(firstCard).image, AllCards.allCards.get(secondCard).image)
        images.addAll(images)
        images.shuffle()

        // image listesi ile dogru kartların eşleştrilip Arraylist e atanmasi
        var num = 0
        for (image in images){
            for (card in AllCards.allCards){
                if(image == card.image){
                    var kart = Cards(card.name,card.image,card.home,card.puan,card.isFaceUp,card.isMatched)
                    cards.add(kart)
                    println(""+ num + " --> "+kart.name)
                    num+=1
                    break
                }
            }
        }


        buttons = listOf(imageButton1001, imageButton1002, imageButton1003, imageButton1004)

        // button listesinden herhangi bir butona tıklanma
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateCard(index)

                //updateViews()
                var caunt = 0
                var isAllMatch = true
                for (card in cards){
                    if(card.isMatched){
                        buttons[caunt].alpha = 0.2f
                    }
                    if (card.isFaceUp) {
                        buttons[caunt].setImageBitmap(business.donustur(images[caunt],450))
                    } else {
                        buttons[caunt].setImageResource(R.drawable.img2)
                    }
                    caunt++
                    if(!card.isMatched){
                        isAllMatch = false
                    }
                }
                if(isAllMatch){
                    var intent = Intent(applicationContext, GameOverActivity::class.java)
                    ifAllMatch2 = true
                    intent.putExtra("point1",point)
                    intent.putExtra("mode","single")
                    intent.putExtra("ifAllMatch2",ifAllMatch2)
                    startActivity(intent)
                    finish()
                }


            }

        }

    }

    private fun updateCard(index:Int) {
        // error check
        if(cards.get(index).isFaceUp){
            Toast.makeText(applicationContext, "Gecersiz hareket", Toast.LENGTH_SHORT).show()
            return
        }

        if(indexOfSingleSelectedKart == null){
            // 0 veya 2 kart seçilmiştir
            //restore in cards
            for (kart in cards){
                if(!kart.isMatched){
                    kart.isFaceUp = false
                }
            }

            indexOfSingleSelectedKart = index
        }else{
            // 1 kart seçilmişse
            checkForMatch(indexOfSingleSelectedKart!!,index)
            indexOfSingleSelectedKart = null
        }
        cards.get(index).isFaceUp = !cards.get(index).isFaceUp
    }

    private fun checkForMatch(position1:Int,position2:Int) {
        if(cards.get(position1).image == cards.get(position2).image){
            point += business.puanHesaplaWithTime(cards.get(position1),cards.get(position2),remainTime,totalTime/1000,true)
            cards.get(position1).isMatched = true
            cards.get(position2).isMatched = true
        }else{
            point += business.puanHesaplaWithTime(cards.get(position1),cards.get(position2),remainTime,totalTime/1000,false)
        }
    }


    fun kontrol(){
        firstCard = rd.nextInt(23)
        secondCard = rd.nextInt(11)+22
        while (true){
            if(firstCard != secondCard){
                break
            }else{
                secondCard = rd.nextInt(11)+22
            }
        }
    }



}