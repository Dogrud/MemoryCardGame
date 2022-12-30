package com.mirac.harrypotter.gameScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirac.harrypotter.R
import com.mirac.harrypotter.entities.Cards
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Base64
import android.widget.ImageButton
import android.widget.Toast
import com.mirac.harrypotter.AllCards
import com.mirac.harrypotter.Business
import kotlinx.android.synthetic.main.activity_medium_mode_multi.*


class MediumModeMulti : AppCompatActivity() {
    val business = Business()
    val rd = Random()
    var randomSayilar = ArrayList<Int>()
    var cards = ArrayList<Cards>()

    private var indexOfSingleSelectedKart: Int? = null

    var playPerson = 1
    var point1 = 0
    var point2 = 0
    var totalTime = 61 * 1000
    var remainTime = 0
    var ifAllMatch2 = false

    lateinit var buttons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medium_mode_multi)

        // time
        object : CountDownTimer(totalTime.toLong(),1000){
            override fun onTick(millisUntilFinished: Long) {
                remainTime = (millisUntilFinished/1000).toInt()
                textViewTime.text = remainTime.toString()
                textPoint3.text = point1.toString()
                textPoint4.text = point2.toString()
                textPerson.text = "User${playPerson}"
            }
            override fun onFinish() {
                if(!ifAllMatch2){
                    var intent = Intent(applicationContext, GameOverActivity::class.java)
                    intent.putExtra("point1",point1)
                    intent.putExtra("point2",point2)
                    intent.putExtra("mode","multi")
                    intent.putExtra("ifAllMatch2",ifAllMatch2)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()

        // Random Sayilar
        kontrol()

        //image listesi
        val images = mutableListOf(AllCards.allCards.get(randomSayilar.get(0)).image, AllCards.allCards.get(randomSayilar.get(1)).image,AllCards.allCards.get(randomSayilar.get(2)).image,AllCards.allCards.get(randomSayilar.get(3)).image,AllCards.allCards.get(randomSayilar.get(4)).image,AllCards.allCards.get(randomSayilar.get(5)).image,AllCards.allCards.get(randomSayilar.get(6)).image,AllCards.allCards.get(randomSayilar.get(7)).image,)
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

        // button list
        buttons = listOf(imageButton1, imageButton2, imageButton3, imageButton4,imageButton5,imageButton6,imageButton7,imageButton8,imageButton9,imageButton10,imageButton11,imageButton12,imageButton13,imageButton14,imageButton15,imageButton16)


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
                        buttons[caunt].setImageBitmap(business.donustur(images[caunt],250))
                    } else {
                        buttons[caunt].setImageResource(R.drawable.img)
                    }
                    caunt++
                    if(!card.isMatched){
                        isAllMatch = false
                    }
                }

                if(isAllMatch){
                    var intent = Intent(applicationContext, GameOverActivity::class.java)
                    ifAllMatch2 = true
                    intent.putExtra("point1",point1)
                    intent.putExtra("point2",point2)
                    intent.putExtra("mode","multi")
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
            if(playPerson ==1){
                point1 += business.puanHesapla(cards.get(position1),cards.get(position2),true)
            }else if(playPerson ==2){
                point2 += business.puanHesapla(cards.get(position1),cards.get(position2),true)
            }
            cards.get(position1).isMatched = true
            cards.get(position2).isMatched = true
            var timeover = MediaPlayer.create(applicationContext,R.raw.ismatch)
            timeover.start()
            timeover.setVolume(10F,10F)
        }else{
            if(playPerson ==1){
                point1 += business.puanHesapla(cards.get(position1),cards.get(position2),false)
                playPerson=2
            }else if(playPerson ==2){
                point2 += business.puanHesapla(cards.get(position1),cards.get(position2),false)
                playPerson=1
            }
        }

    }


    fun kontrol(){
        var a = 11
        var b = 1
        while (true) {
            var firstCard = rd.nextInt(a)+b
            var secondCard = rd.nextInt(a)+b
            while (true) {
                if (firstCard != secondCard) {
                    randomSayilar.add(firstCard)
                    randomSayilar.add(secondCard)
                    if(randomSayilar.size == 8){
                        return
                    }
                    b+=11
                    break
                } else {
                    secondCard = rd.nextInt(a) + b
                }
            }
        }
    }

}
