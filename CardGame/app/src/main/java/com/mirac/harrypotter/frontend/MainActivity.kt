package com.mirac.harrypotter.frontend

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirac.harrypotter.R
import com.mirac.harrypotter.entities.Users
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth : FirebaseAuth
    var users = ArrayList<Users>()
    var musicButton = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        fireBaseDataPull()

        var mediaPlayer = MediaPlayer.create(applicationContext,R.raw.prologue)
        mediaPlayer.start()
        mediaPlayer.setVolume(35F,35F)

        imageButtonMusic.setOnClickListener(View.OnClickListener {
            musicButton = !musicButton
            if(!musicButton){
                mediaPlayer.pause()
                imageButtonMusic.setImageResource(android.R.drawable.ic_lock_silent_mode)
            }else{
                imageButtonMusic.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
                mediaPlayer.start()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        textMessage.text = ""
    }

    fun Login(view: View){
        val name = textName.text.toString()
        val password = textPassword.text.toString()
        var kontrol:Boolean = false

        for (kullanici in users){
            if (kullanici.userName.equals(name) && kullanici.password.equals(password)){
                kontrol =true
            }
        }

        if(kontrol){
            var intent = Intent(applicationContext, MainScreen::class.java)
            Toast.makeText(applicationContext, "Welcome "+name, Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }else{
            textMessage.text = "Username or password is incorrect"
        }
    }


    fun GoToSignUpClass(view: View){
        var intent = Intent(applicationContext, SignUp::class.java)
        startActivity(intent)
    }


    fun fireBaseDataPull(){
        db.collection("Users").addSnapshotListener { snapshot, error ->
            if(error != null){
                Toast.makeText(this,error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents
                        users.clear()
                        for (doc in documents){
                            var name = doc.get("userName").toString()
                            var password = doc.get("password").toString()
                            var email = doc.get("email").toString()
                            var user = Users(name,password,email)
                            users.add(user)
                        }
                    }
                }
            }
        }
    }


}


