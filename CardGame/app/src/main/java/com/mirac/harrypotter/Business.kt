package com.mirac.harrypotter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.mirac.harrypotter.entities.Cards

class Business {

    fun puanHesaplaWithTime(card1: Cards, card2: Cards, kalanSure:Int, toplamSure:Int, match:Boolean):Int{
        var puan = 0
        var evkatsayi1 = 1
        var evkatsayi2 = 1
        var sure = 1

        if(card1.home == "Gryffindor" || card1.home=="Slytherin"){
            evkatsayi1++
        }
        if(card2.home == "Gryffindor" || card2.home=="Slytherin"){
            evkatsayi2++
        }

        if(match){
            // puan eklenecek
            if(kalanSure/10 != 0){
                sure = kalanSure/10
            }
            puan = card1.puan*2*evkatsayi1*sure
        }else{
            if((toplamSure-kalanSure)/10 != 0){
                sure = (toplamSure-kalanSure)/10
            }
            // puan azalacak
            if(card1.home == card2.home){
                // evler aynı ise
                puan = (((card1.puan+card2.puan)/evkatsayi1)*sure)*-1
            }else{
                // evler farklı ise
                puan = ((((card1.puan + card2.puan)/2)*evkatsayi1*evkatsayi2)*sure)*-1
            }
        }

        return puan
    }

    fun puanHesapla(card1: Cards, card2: Cards, match:Boolean):Int{
        var puan = 0
        var evkatsayi1 = 1
        var evkatsayi2 = 1

        if(card1.home == "Gryffindor" || card1.home=="Slytherin"){
            evkatsayi1++
        }
        if(card2.home == "Gryffindor" || card2.home=="Slytherin"){
            evkatsayi2++
        }

        if(match){
            // puan eklenecek
            puan = card1.puan*2*evkatsayi1
        }else{
            // puan azalacak
            if(card1.home == card2.home){
                // evler aynı ise
                puan = ((card1.puan+card2.puan)/evkatsayi1)*-1
            }else{
                // evler farklı ise
                puan = (((card1.puan + card2.puan)/2)*evkatsayi1*evkatsayi2)*-1
            }
        }

        return puan
    }

    fun donustur(resim : String,boyut:Int) : Bitmap {
        var imageBytes = Base64.decode(resim, 0)
        var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        var bgimage = Bitmap.createScaledBitmap(image, boyut, boyut, false);
        return bgimage
    }

}