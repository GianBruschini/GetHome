package com.gian.gethome.Activities.animaldetalle.interfaces

import android.graphics.drawable.Drawable
import com.gian.gethome.Clases.UserInfo


interface AnimalDetalleView {
    fun databaseError()
    fun setFavButton()
    fun setNombreYfoto(userInfo: UserInfo)
    fun loadImages(animalImages:MutableList<String>)
    fun setSexo(sexo:Int)
    fun setTransitoUrgente(transito:String)
    fun onDestroy()
}