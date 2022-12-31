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
import kotlinx.android.synthetic.main.activity_hard_model_multi.*
import java.util.*
import kotlin.collections.ArrayList

class HardModelMulti : AppCompatActivity() {
    val business = Business()
    val rd = Random()
    var randomSayilar = ArrayList<Int>()
    var cards = ArrayList<Cards>()

    private var indexOfSingleSelectedKart: Int? = null

    var ifAllMatch2 = false
    var playPerson = 1
    var point1 = 0
    var point2 = 0
    var totalTime = 61 * 1000
    var remainTime = 0

    lateinit var buttons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard_model_multi)

        //Time
        object : CountDownTimer(totalTime.toLong(),1000){
            override fun onTick(millisUntilFinished: Long) {
                remainTime = (millisUntilFinished/1000).toInt()
                textViewTime1.text = remainTime.toString()
                textViewPoint4.text = point1.toString()
                textViewPoint6.text = point2.toString()
                textViewperson2.text = "User${playPerson}"
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
        val images = mutableListOf<String>()
        for (i in randomSayilar){
            images.add( AllCards.allCards.get(i).image)
        }
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
        buttons = listOf(imageButton101, imageButton102, imageButton103, imageButton104,imageButton105,imageButton106,imageButton107,imageButton108,imageButton109,imageButton110,imageButton111,imageButton112,imageButton113,imageButton114,imageButton115,imageButton116,imageButton117,imageButton118,imageButton119,imageButton120,imageButton121,imageButton122,imageButton123,imageButton124,imageButton125,imageButton126,imageButton127,imageButton128,imageButton129,imageButton130,imageButton131,imageButton132,imageButton133,imageButton134,imageButton135,imageButton136,imageButton137,imageButton138,imageButton139,imageButton140)

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
                        buttons[caunt].setImageBitmap(business.donustur(images[caunt],150))
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
        var a = 2
        var b = 0
        while (true) {
            var firstCard = rd.nextInt(a)+b
            b+=2
            randomSayilar.add(firstCard)
            if(randomSayilar.size == 20){
                break
            }
        }
    }


}