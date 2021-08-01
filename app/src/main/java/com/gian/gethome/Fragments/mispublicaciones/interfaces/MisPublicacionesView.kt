package com.gian.gethome.Fragments.mispublicaciones.interfaces

import com.gian.gethome.Clases.Animal

interface MisPublicacionesView {
    fun fillRecyclerViewWith(animal: Animal, imagenNotNull: String)
    fun notifyDataChanged()
    fun setTextViewVisible()
    fun showDataBaseError()
    fun notifyPubDeleted(position:Int)


}