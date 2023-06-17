package com.mirac.harrypotter.frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirac.harrypotter.AllCards
import com.mirac.harrypotter.R
import com.mirac.harrypotter.entities.Cards
import com.mirac.harrypotter.gameScreens.*
import kotlinx.android.synthetic.main.activity_game_screen.*


class MainScreen : AppCompatActivity() {
    val db = Firebase.firestore
    var musicButton = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        fireBaseVerieriAl()

        imageButtonMusic1.setOnClickListener(View.OnClickListener {
            musicButton = !musicButton
            if(!musicButton){
                // silent mode
                imageButtonMusic1.setImageResource(android.R.drawable.ic_lock_silent_mode)
                //audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT

            }else{
                imageButtonMusic1.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
                //audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        })
    }

    fun Start(view: View){
        mesaj.text = ""

        var rdGroupMod = findViewById<RadioGroup>(R.id.radioGroupMod)
        var rdGroupCesid = findViewById<RadioGroup>(R.id.radioGroupCesid)

        var selectBtnMod:Int = rdGroupMod!!.checkedRadioButtonId
        var selectBtnCesid:Int = rdGroupCesid!!.checkedRadioButtonId

        if(selectBtnMod == -1 && selectBtnCesid== -1){
            mesaj.text = "Please select Game mode and type of game"
            return
        }else if(selectBtnMod == -1){
            mesaj.text = "Please select Game mode"
            return
        }else if(selectBtnCesid == -1){
            mesaj.text = "Please select type of game"
            return
        }

        var buttonMod = findViewById<RadioButton>(selectBtnMod)
        var buttonCesid = findViewById<RadioButton>(selectBtnCesid)

        if (buttonMod.text == "Easy"){
            if(buttonCesid.text == "Single Player"){
                var intent = Intent(applicationContext, EasyModeSingle::class.java)
                startActivity(intent)
            }else if(buttonCesid.text == "Multiplayer"){
                var intent = Intent(applicationContext, EasyModeMulti::class.java)
                startActivity(intent)
            }
        }else if (buttonMod.text == "Middle"){
            if(buttonCesid.text == "Single Player"){
                var intent = Intent(applicationContext, MediumModeSingle::class.java)
                startActivity(intent)
            }else if(buttonCesid.text == "Multiplayer"){
                var intent = Intent(applicationContext, MediumModeMulti::class.java)
                startActivity(intent)
            }
        }else if(buttonMod.text == "Hard"){
            if(buttonCesid.text == "Single Player"){
                var intent = Intent(applicationContext, HardModelSingle::class.java)
                startActivity(intent)
            }else if(buttonCesid.text == "Multiplayer"){
                var intent = Intent(applicationContext, HardModelMulti::class.java)
                startActivity(intent)
            }
        }else{
            mesaj.text = "Please select Game mode"
        }

    }


    fun fireBaseVerieriAl() {
        db.collection("Cards").orderBy("home").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        AllCards.allCards.clear()
                        for (doc in documents) {
                            val home = doc.get("home").toString()
                            val image = doc.get("image").toString()
                            val name = doc.get("name").toString()
                            val puan = doc.get("puan").toString()
                            val card = Cards(name,image,home,Integer.parseInt(puan),false,false)
                            AllCards.allCards.add(card)
                        }
                    }
                }
            }
        }

    }


}