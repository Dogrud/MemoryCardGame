package com.mirac.harrypotter.frontend

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_kaydol.*

class Kaydol : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth : FirebaseAuth
    var users = ArrayList<Users>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kaydol)
        auth = Firebase.auth
        fireBaseVerieriAl()
    }


    fun KayitYap(view: View){
        message.text = ""
        var userName = TextName.text.toString()
        var password = TextPassword.text.toString()
        var email = TextEmailAddress.text.toString()

        if(password.length == 0 || TextPasswordRepait.text.toString().length == 0 ||userName.length == 0 || email.length == 0){
            message.text = "Boş bırakılan alanlar mevcut"
            return
        }

        if(!TextPassword.text.toString().equals(TextPasswordRepait.text.toString())) {
            message.text = "Girdiğiniz parolalar eşleşmemektedir"
            return
        }

        var user = Users(userName,password,email)
        if(checkName(user)){
            addUser(user)

            Toast.makeText(applicationContext, "Başarıyla kayıt olundu", Toast.LENGTH_SHORT).show()
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            message.text = "Girdiğiniz kullanıcı adı mevcut"
        }

    }

    fun checkName(user: Users):Boolean{
        var kontrol:Boolean = true
        for (kullanici in users){
            if(user.userName.equals(kullanici.userName)){
                kontrol = false
                break
            }
        }
        return kontrol
    }

    fun addUser(user:Users){
        val userMap = hashMapOf<String,Any>()
        userMap.put("userName",user.userName)
        userMap.put("password",user.password)
        userMap.put("email",user.email)

        db.collection("Users").add(user)

    }


    fun fireBaseVerieriAl(){
        db.collection("Users").addSnapshotListener { snapshot, error ->
            if(error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
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